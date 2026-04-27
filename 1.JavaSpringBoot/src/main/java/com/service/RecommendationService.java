package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.RecommendationEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.RecommendationVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.RecommendationView;


/**
 * 推荐结果表
 *
 * @author 
 * @email 
 * @date 2024-01-01
 */
public interface RecommendationService extends IService<RecommendationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<RecommendationVO> selectListVO(Wrapper<RecommendationEntity> wrapper);
	
	RecommendationVO selectVO(@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	List<RecommendationView> selectListView(Wrapper<RecommendationEntity> wrapper);
	
	RecommendationView selectView(@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	PageUtils queryPage(Map<String, Object> params,Wrapper<RecommendationEntity> wrapper);
	
	// 推荐算法相关方法
	void generateRecommendations(Long userid);
	
	void generateRecommendationsForAll();
	
	// 基于物品的协同过滤
	Map<Long, Double> collaborativeFilteringByItem(Long userid);
	
	// 基于用户的协同过滤
	Map<Long, Double> collaborativeFilteringByUser(Long userid);
	
	// 内容推荐
	Map<Long, Double> contentBasedRecommendation(Long userid);
	
	// 热门推荐
	Map<Long, Double> hotRecommendation();
	
	// 获取用户推荐列表
	List<RecommendationEntity> getUserRecommendations(Long userid, int limit);
	
	// 刷新用户推荐
	void refreshUserRecommendations(Long userid);

}
