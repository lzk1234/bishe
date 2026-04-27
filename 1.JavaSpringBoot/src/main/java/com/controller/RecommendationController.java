package com.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.RecommendationEntity;
import com.entity.ShangpinxinxiEntity;
import com.entity.TokenEntity;
import com.entity.view.RecommendationView;
import com.interceptor.AuthorizationInterceptor;
import com.service.RecommendationService;
import com.service.ShangpinxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.Query;
import com.utils.R;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ShangpinxinxiService shangpinxinxiService;

    @Autowired
    private TokenService tokenService;

    @IgnoreAuth
    @RequestMapping("/today")
    public R getTodayRecommendations(@RequestParam(required = false) Long userid,
                                     @RequestParam(defaultValue = "8") int limit,
                                     HttpServletRequest request) {
        int safeLimit = normalizeLimit(limit);
        Long tokenUserid = resolveTokenUserId(request);
        Long targetUserid = tokenUserid != null ? tokenUserid : userid;

        List<RecommendationView> data = targetUserid != null
            ? buildRecommendationViews(recommendationService.getUserRecommendations(targetUserid, safeLimit))
            : buildHotRecommendationViews(safeLimit);

        return R.ok().put("data", data);
    }

    @RequestMapping("/user/list")
    public R userList(@RequestParam(defaultValue = "20") int limit, HttpServletRequest request) {
        if (!isUser(request)) {
            return R.error(403, "仅用户可查看个性化推荐");
        }

        Long userId = getSessionUserId(request);
        if (userId == null) {
            return R.error(400, "缺少用户信息");
        }

        List<RecommendationView> data = buildRecommendationViews(
            recommendationService.getUserRecommendations(userId, normalizeLimit(limit))
        );
        return R.ok().put("data", data);
    }

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return R.error(403, "仅管理员可查看推荐列表");
        }

        EntityWrapper<RecommendationEntity> wrapper = new EntityWrapper<RecommendationEntity>();
        Object userid = params.get("userid");
        if (userid != null && StringUtils.isNotBlank(String.valueOf(userid))) {
            wrapper.eq("userid", String.valueOf(userid));
        }
        wrapper.orderBy("create_time", false).orderBy("score", false);

        Page<RecommendationEntity> page = recommendationService.selectPage(
            new Query<RecommendationEntity>(params).getPage(),
            wrapper
        );

        PageUtils pageUtils = new PageUtils(
            buildRecommendationViews(page.getRecords()),
            (int) page.getTotal(),
            (int) page.getSize(),
            (int) page.getCurrent()
        );
        return R.ok().put("data", pageUtils);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return R.error(403, "仅管理员可查看推荐详情");
        }

        RecommendationEntity entity = recommendationService.selectById(id);
        if (entity == null) {
            return R.error("推荐记录不存在");
        }

        RecommendationView view = buildRecommendationView(
            entity,
            getGoodsMap(Collections.singletonList(entity.getGoodid()))
        );
        return R.ok().put("data", view);
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return R.error(403, "仅管理员可删除推荐记录");
        }
        recommendationService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/refresh")
    public R refreshRecommendations(@RequestParam(required = false) Long userid, HttpServletRequest request) {
        Long targetUserid = null;
        if (isUser(request)) {
            targetUserid = getSessionUserId(request);
        } else if (isAdmin(request) && userid != null) {
            targetUserid = userid;
        }

        if (targetUserid == null) {
            return R.error(400, "缺少用户信息");
        }

        try {
            recommendationService.refreshUserRecommendations(targetUserid);
            return R.ok("刷新成功");
        } catch (Exception e) {
            return R.error("刷新失败: " + e.getMessage());
        }
    }

    @RequestMapping("/refresh/all")
    public R refreshAllRecommendations(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return R.error(403, "仅管理员可刷新全部推荐");
        }

        try {
            recommendationService.generateRecommendationsForAll();
            return R.ok("刷新成功");
        } catch (Exception e) {
            return R.error("刷新失败: " + e.getMessage());
        }
    }

    private List<RecommendationView> buildRecommendationViews(List<RecommendationEntity> recommendationList) {
        if (recommendationList == null || recommendationList.isEmpty()) {
            return new ArrayList<RecommendationView>();
        }

        Map<Long, ShangpinxinxiEntity> goodsMap = getGoodsMap(
            recommendationList.stream().map(RecommendationEntity::getGoodid).collect(Collectors.toList())
        );

        List<RecommendationView> views = new ArrayList<RecommendationView>();
        for (RecommendationEntity entity : recommendationList) {
            RecommendationView view = buildRecommendationView(entity, goodsMap);
            if (view != null) {
                views.add(view);
            }
        }
        return views;
    }

    private List<RecommendationView> buildHotRecommendationViews(int limit) {
        Map<Long, Double> hotScores = recommendationService.hotRecommendation();
        if (hotScores == null || hotScores.isEmpty()) {
            return new ArrayList<RecommendationView>();
        }

        List<Map.Entry<Long, Double>> sorted = hotScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());

        Map<Long, ShangpinxinxiEntity> goodsMap = getGoodsMap(
            sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList())
        );

        List<RecommendationView> views = new ArrayList<RecommendationView>();
        for (Map.Entry<Long, Double> entry : sorted) {
            ShangpinxinxiEntity goods = goodsMap.get(entry.getKey());
            if (goods == null) {
                continue;
            }

            RecommendationView view = new RecommendationView();
            view.setGoodid(entry.getKey());
            view.setScore(BigDecimal.valueOf(entry.getValue()));
            view.setReason("热门推荐");
            view.setAlgorithmType("hot");
            fillGoodsFields(view, goods);
            views.add(view);
        }
        return views;
    }

    private RecommendationView buildRecommendationView(RecommendationEntity entity,
                                                       Map<Long, ShangpinxinxiEntity> goodsMap) {
        if (entity == null) {
            return null;
        }

        ShangpinxinxiEntity goods = goodsMap.get(entity.getGoodid());
        if (goods == null) {
            return null;
        }

        RecommendationView view = new RecommendationView(entity);
        fillGoodsFields(view, goods);
        return view;
    }

    private void fillGoodsFields(RecommendationView view, ShangpinxinxiEntity goods) {
        view.setShangpinmingcheng(goods.getShangpinmingcheng());
        view.setShangpinfenlei(goods.getShangpinfenlei());
        view.setTupian(goods.getTupian());
        view.setPinpai(goods.getPinpai());
        view.setPrice(goods.getPrice());
        view.setVipprice(goods.getVipprice());
    }

    private Map<Long, ShangpinxinxiEntity> getGoodsMap(List<Long> goodIds) {
        if (goodIds == null || goodIds.isEmpty()) {
            return new LinkedHashMap<Long, ShangpinxinxiEntity>();
        }

        List<ShangpinxinxiEntity> goodsList = shangpinxinxiService.selectBatchIds(goodIds);
        Map<Long, ShangpinxinxiEntity> goodsMap = new LinkedHashMap<Long, ShangpinxinxiEntity>();
        for (ShangpinxinxiEntity goods : goodsList) {
            goodsMap.put(goods.getId(), goods);
        }
        return goodsMap;
    }

    private Long resolveTokenUserId(HttpServletRequest request) {
        String token = request.getHeader(AuthorizationInterceptor.LOGIN_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            return null;
        }

        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null || !"yonghu".equals(tokenEntity.getTablename())) {
            return null;
        }
        return tokenEntity.getUserid();
    }

    private int normalizeLimit(int limit) {
        if (limit <= 0) {
            return 8;
        }
        return Math.min(limit, 50);
    }

    private Long getSessionUserId(HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        return userId == null ? null : Long.valueOf(userId.toString());
    }

    private boolean isAdmin(HttpServletRequest request) {
        return "users".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }

    private boolean isUser(HttpServletRequest request) {
        return "yonghu".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }
}
