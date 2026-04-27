package com.dao;

import com.entity.RecommendationEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.RecommendationVO;
import com.entity.view.RecommendationView;


/**
 * 推荐结果表
 * 
 * @author 
 * @email 
 * @date 2024-01-01
 */
public interface RecommendationDao extends BaseMapper<RecommendationEntity> {
	
	List<RecommendationVO> selectListVO(@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	RecommendationVO selectVO(@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	List<RecommendationView> selectListView(@Param("ew") Wrapper<RecommendationEntity> wrapper);

	List<RecommendationView> selectListView(Pagination page,@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	RecommendationView selectView(@Param("ew") Wrapper<RecommendationEntity> wrapper);
	
	
	// 自定义查询方法
	int deleteByUserid(@Param("userid") Long userid);
	
	int deleteOldRecommendations(@Param("days") int days);

}
