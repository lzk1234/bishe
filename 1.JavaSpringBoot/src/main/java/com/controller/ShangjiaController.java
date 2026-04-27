package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ShangjiaEntity;
import com.entity.view.ShangjiaView;

import com.service.ShangjiaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 鍟嗗
 * 鍚庣鎺ュ彛
 * @author
 * @email
 * @date 2023-03-08 09:39:22
 */
@RestController
@RequestMapping("/shangjia")
public class ShangjiaController {
    @Autowired
    private ShangjiaService shangjiaService;

    @Autowired
    private TokenService tokenService;

    /**
     * 鐧诲綍
     */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", username));
        if (u == null || !u.getMima().equals(password)) {
            return R.error("璐﹀彿鎴栧瘑鐮佷笉姝ｇ‘");
        }
        String token = tokenService.generateToken(u.getId(), username, "shangjia", "鍟嗗");
        return R.ok().put("token", token);
    }

    /**
     * 娉ㄥ唽
     */
    @IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody ShangjiaEntity shangjia) {
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
        if (u != null) {
            return R.error("娉ㄥ唽鐢ㄦ埛宸插瓨鍦?");
        }
        Long uId = new Date().getTime();
        shangjia.setId(uId);
        shangjiaService.insert(shangjia);
        return R.ok();
    }

    /**
     * 閫€鍑?
     */
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("閫€鍑烘垚鍔?");
    }

    /**
     * 鑾峰彇鐢ㄦ埛鐨剆ession鐢ㄦ埛淇℃伅
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        ShangjiaEntity u = shangjiaService.selectById(id);
        return R.ok().put("data", u);
    }

    /**
     * 瀵嗙爜閲嶇疆
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", username));
        if (u == null) {
            return R.error("璐﹀彿涓嶅瓨鍦?");
        }
        u.setMima("123456");
        shangjiaService.updateById(u);
        return R.ok("瀵嗙爜宸查噸缃负锛?23456");
    }

    /**
     * 鍚庣鍒楄〃
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ShangjiaEntity shangjia, HttpServletRequest request) {
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
        PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 鍓嶇鍒楄〃
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ShangjiaEntity shangjia, HttpServletRequest request) {
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
        PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 鍒楄〃
     */
    @RequestMapping("/lists")
    public R list(ShangjiaEntity shangjia) {
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangjia, "shangjia"));
        return R.ok().put("data", shangjiaService.selectListView(ew));
    }

    /**
     * 鏌ヨ
     */
    @RequestMapping("/query")
    public R query(ShangjiaEntity shangjia) {
        EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangjia, "shangjia"));
        ShangjiaView shangjiaView = shangjiaService.selectView(ew);
        return R.ok("鏌ヨ鍟嗗鎴愬姛").put("data", shangjiaView);
    }

    /**
     * 鍚庣璇︽儏
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
        return R.ok().put("data", shangjia);
    }

    /**
     * 鍓嶇璇︽儏
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ShangjiaEntity shangjia = shangjiaService.selectById(id);
        return R.ok().put("data", shangjia);
    }

    /**
     * 鍚庣淇濆瓨
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
        shangjia.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
        if (u != null) {
            return R.error("鐢ㄦ埛宸插瓨鍦?");
        }
        shangjia.setId(new Date().getTime());
        shangjiaService.insert(shangjia);
        return R.ok();
    }

    /**
     * 鍓嶇淇濆瓨
     */
    @RequestMapping("/add")
    public R add(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
        shangjia.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        ShangjiaEntity u = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
        if (u != null) {
            return R.error("鐢ㄦ埛宸插瓨鍦?");
        }
        shangjia.setId(new Date().getTime());
        shangjiaService.insert(shangjia);
        return R.ok();
    }

    /**
     * 淇敼
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
        shangjiaService.updateById(shangjia);
        return R.ok();
    }

    /**
     * 鍒犻櫎
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        shangjiaService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 鎻愰啋鎺ュ彛
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<ShangjiaEntity> wrapper = new EntityWrapper<ShangjiaEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }

        int count = shangjiaService.selectCount(wrapper);
        return R.ok().put("count", count);
    }
}
