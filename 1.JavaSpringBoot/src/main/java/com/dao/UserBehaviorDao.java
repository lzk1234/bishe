package com.dao;

import com.entity.UserBehaviorEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.UserBehaviorVO;
import com.entity.view.UserBehaviorView;


/**
 * 用户行为记录表
 * 
 * @author 
 * @email 
 * @date 2024-01-01
 */
public interface UserBehaviorDao extends BaseMapper<UserBehaviorEntity> {
	
	List<UserBehaviorVO> selectListVO(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	UserBehaviorVO selectVO(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	List<UserBehaviorView> selectListView(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);

	List<UserBehaviorView> selectListView(Pagination page,@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	UserBehaviorView selectView(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	
	// 自定义查询方法
	List<Map<String, Object>> getUserBehaviorStats(@Param("userid") Long userid);
	
	List<Map<String, Object>> getItemSimilarity(@Param("goodid") Long goodid);
	
	List<Map<String, Object>> getUserSimilarity(@Param("userid") Long userid);

}
