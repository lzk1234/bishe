package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.RecommendationDao;
import com.dao.UserBehaviorDao;
import com.dao.StoreupDao;
import com.dao.OrdersDao;
import com.dao.ShangpinxinxiDao;
import com.entity.RecommendationEntity;
import com.entity.UserBehaviorEntity;
import com.entity.StoreupEntity;
import com.entity.OrdersEntity;
import com.entity.ShangpinxinxiEntity;
import com.service.RecommendationService;
import com.entity.vo.RecommendationVO;
import com.entity.view.RecommendationView;

import javax.annotation.Resource;
import java.util.Date;

@Service("recommendationService")
public class RecommendationServiceImpl extends ServiceImpl<RecommendationDao, RecommendationEntity> implements RecommendationService {
	
	@Resource
	private UserBehaviorDao userBehaviorDao;
	
	@Resource
	private StoreupDao storeupDao;
	
	@Resource
	private OrdersDao ordersDao;
	
	@Resource
	private ShangpinxinxiDao shangpinxinxiDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RecommendationEntity> page = this.selectPage(
                new Query<RecommendationEntity>(params).getPage(),
                new EntityWrapper<RecommendationEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<RecommendationEntity> wrapper) {
		  Page<RecommendationView> page =new Query<RecommendationView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<RecommendationVO> selectListVO(Wrapper<RecommendationEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public RecommendationVO selectVO(Wrapper<RecommendationEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<RecommendationView> selectListView(Wrapper<RecommendationEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public RecommendationView selectView(Wrapper<RecommendationEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public void generateRecommendations(Long userid) {
		// 清除该用户的旧推荐
		baseMapper.deleteByUserid(userid);
		
		// 获取各种推荐分数
		Map<Long, Double> itemBasedScores = collaborativeFilteringByItem(userid);
		Map<Long, Double> userBasedScores = collaborativeFilteringByUser(userid);
		Map<Long, Double> contentScores = contentBasedRecommendation(userid);
		Map<Long, Double> hotScores = hotRecommendation();
		
		// 合并推荐分数（加权融合）
		Map<Long, Double> finalScores = new HashMap<>();
		Set<Long> allGoods = new HashSet<>();
		allGoods.addAll(itemBasedScores.keySet());
		allGoods.addAll(userBasedScores.keySet());
		allGoods.addAll(contentScores.keySet());
		allGoods.addAll(hotScores.keySet());
		
		for (Long goodid : allGoods) {
			double itemScore = itemBasedScores.getOrDefault(goodid, 0.0);
			double userScore = userBasedScores.getOrDefault(goodid, 0.0);
			double contentScore = contentScores.getOrDefault(goodid, 0.0);
			double hotScore = hotScores.getOrDefault(goodid, 0.0);
			
			// 加权融合：协同过滤(物品) 0.3 + 协同过滤(用户) 0.3 + 内容推荐 0.3 + 热门 0.1
			double finalScore = 0.3 * itemScore + 0.3 * userScore + 0.3 * contentScore + 0.1 * hotScore;
			finalScores.put(goodid, finalScore);
		}
		
		// 按分数排序并保存推荐结果
		List<Map.Entry<Long, Double>> sortedScores = finalScores.entrySet().stream()
			.sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
			.collect(Collectors.toList());
		
		int rank = 1;
		for (Map.Entry<Long, Double> entry : sortedScores) {
			if (rank > 20) break;
			RecommendationEntity recommendation = new RecommendationEntity();
			recommendation.setUserid(userid);
			recommendation.setGoodid(entry.getKey());
			recommendation.setScore(BigDecimal.valueOf(entry.getValue()));
			recommendation.setReason("\u7efc\u5408\u63a8\u8350\u7b2c" + rank + "\u4f4d");
			recommendation.setAlgorithmType("hybrid");
			recommendation.setCreateTime(new Date());
			this.insert(recommendation);
			rank++;
		}
	}
	
	@Override
	public void generateRecommendationsForAll() {
		List<UserBehaviorEntity> behaviors = userBehaviorDao.selectList(new EntityWrapper<UserBehaviorEntity>());
		Set<Long> userIds = new LinkedHashSet<Long>();
		for (UserBehaviorEntity behavior : behaviors) {
			if(behavior.getUserid() != null) {
				userIds.add(behavior.getUserid());
			}
		}
		for (Long userId : userIds) {
			generateRecommendations(userId);
		}
	}

	
	@Override
	public Map<Long, Double> collaborativeFilteringByItem(Long userid) {
		Map<Long, Double> scores = new HashMap<>();
		
		// 获取用户购买/收藏过的茶叶
		EntityWrapper<UserBehaviorEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("userid", userid);
		wrapper.in("behavior_type", Arrays.asList("buy", "favorite"));
		List<UserBehaviorEntity> userBehaviors = userBehaviorDao.selectList(wrapper);
		
		if (userBehaviors.isEmpty()) {
			return scores;
		}
		
		Set<Long> userGoods = userBehaviors.stream()
			.map(UserBehaviorEntity::getGoodid)
			.collect(Collectors.toSet());
		
		// 计算茶叶相似度
		for (Long userGood : userGoods) {
			List<Map<String, Object>> similarities = userBehaviorDao.getItemSimilarity(userGood);
			for (Map<String, Object> sim : similarities) {
				Long similarGood = ((Number) sim.get("goodid")).longValue();
				Double similarity = ((Number) sim.get("similarity")).doubleValue();
				
				if (!userGoods.contains(similarGood)) {
					scores.put(similarGood, scores.getOrDefault(similarGood, 0.0) + similarity);
				}
			}
		}
		
		return scores;
	}
	
	@Override
	public Map<Long, Double> collaborativeFilteringByUser(Long userid) {
		Map<Long, Double> scores = new HashMap<>();
		
		// 获取用户购买/收藏过的茶叶
		EntityWrapper<UserBehaviorEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("userid", userid);
		wrapper.in("behavior_type", Arrays.asList("buy", "favorite"));
		List<UserBehaviorEntity> userBehaviors = userBehaviorDao.selectList(wrapper);
		
		if (userBehaviors.isEmpty()) {
			return scores;
		}
		
		Set<Long> userGoods = userBehaviors.stream()
			.map(UserBehaviorEntity::getGoodid)
			.collect(Collectors.toSet());
		
		// 找相似用户
		List<Map<String, Object>> similarUsers = userBehaviorDao.getUserSimilarity(userid);
		
		for (Map<String, Object> simUser : similarUsers) {
			Long similarUserId = ((Number) simUser.get("userid")).longValue();
			Double similarity = ((Number) simUser.get("similarity")).doubleValue();
			
			// 获取相似用户购买/收藏的茶叶
			EntityWrapper<UserBehaviorEntity> simWrapper = new EntityWrapper<>();
			simWrapper.eq("userid", similarUserId);
			simWrapper.in("behavior_type", Arrays.asList("buy", "favorite"));
			List<UserBehaviorEntity> simUserBehaviors = userBehaviorDao.selectList(simWrapper);
			
			for (UserBehaviorEntity behavior : simUserBehaviors) {
				Long goodid = behavior.getGoodid();
				if (!userGoods.contains(goodid)) {
					scores.put(goodid, scores.getOrDefault(goodid, 0.0) + similarity);
				}
			}
		}
		
		return scores;
	}
	
	@Override
	public Map<Long, Double> contentBasedRecommendation(Long userid) {
		Map<Long, Double> scores = new HashMap<>();
		
		// 获取用户购买/收藏过的茶叶
		EntityWrapper<UserBehaviorEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("userid", userid);
		wrapper.in("behavior_type", Arrays.asList("buy", "favorite"));
		List<UserBehaviorEntity> userBehaviors = userBehaviorDao.selectList(wrapper);
		
		if (userBehaviors.isEmpty()) {
			return scores;
		}
		
		// 分析用户偏好
		Map<String, Integer> categoryPrefs = new HashMap<>();
		Map<String, Integer> brandPrefs = new HashMap<>();
		Double avgPrice = 0.0;
		
		for (UserBehaviorEntity behavior : userBehaviors) {
			ShangpinxinxiEntity good = shangpinxinxiDao.selectById(behavior.getGoodid());
			if (good != null) {
				if (good.getShangpinfenlei() != null) {
					categoryPrefs.put(good.getShangpinfenlei(), 
						categoryPrefs.getOrDefault(good.getShangpinfenlei(), 0) + 1);
				}
				if (good.getPinpai() != null) {
					brandPrefs.put(good.getPinpai(), 
						brandPrefs.getOrDefault(good.getPinpai(), 0) + 1);
				}
				if (good.getPrice() != null) {
					avgPrice += good.getPrice();
				}
			}
		}
		
		if (!userBehaviors.isEmpty()) {
			avgPrice = avgPrice / userBehaviors.size();
		}
		
		// 获取所有茶叶进行匹配
		List<ShangpinxinxiEntity> allGoods = shangpinxinxiDao.selectList(null);
		
		for (ShangpinxinxiEntity good : allGoods) {
			double score = 0.0;
			
			// 分类匹配
			if (good.getShangpinfenlei() != null && categoryPrefs.containsKey(good.getShangpinfenlei())) {
				score += categoryPrefs.get(good.getShangpinfenlei()) * 1.0;
			}
			
			// 品牌匹配
			if (good.getPinpai() != null && brandPrefs.containsKey(good.getPinpai())) {
				score += brandPrefs.get(good.getPinpai()) * 1.0;
			}
			
			// 价格匹配（价格相近得分高）
			if (good.getPrice() != null && avgPrice > 0) {
				double priceDiff = Math.abs(good.getPrice() - avgPrice) / avgPrice;
				if (priceDiff < 0.2) {
					score += 0.5;
				} else if (priceDiff < 0.5) {
					score += 0.3;
				}
			}
			
			if (score > 0) {
				scores.put(good.getId(), score);
			}
		}
		
		return scores;
	}
	
	@Override
	public Map<Long, Double> hotRecommendation() {
		Map<Long, Double> scores = new HashMap<>();
		
		// 统计茶叶的热度（基于浏览次数）
		EntityWrapper<UserBehaviorEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("behavior_type", "view");
		wrapper.orderBy("addtime", false);
		List<UserBehaviorEntity> viewBehaviors = userBehaviorDao.selectList(wrapper);
		
		Map<Long, Integer> viewCounts = new HashMap<>();
		for (UserBehaviorEntity behavior : viewBehaviors) {
			viewCounts.put(behavior.getGoodid(), 
				viewCounts.getOrDefault(behavior.getGoodid(), 0) + 1);
		}
		
		// 归一化热度分数
		int maxCount = viewCounts.values().stream().max(Integer::compare).orElse(1);
		for (Map.Entry<Long, Integer> entry : viewCounts.entrySet()) {
			scores.put(entry.getKey(), (double) entry.getValue() / maxCount);
		}
		
		return scores;
	}
	
	@Override
	public List<RecommendationEntity> getUserRecommendations(Long userid, int limit) {
		EntityWrapper<RecommendationEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("userid", userid);
		wrapper.orderBy("score", false);
		wrapper.last("LIMIT " + limit);
		return this.selectList(wrapper);
	}
	
	@Override
	public void refreshUserRecommendations(Long userid) {
		generateRecommendations(userid);
	}

}
