package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.UserBehaviorEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.UserBehaviorVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.UserBehaviorView;


/**
 * 用户行为记录表
 *
 * @author 
 * @email 
 * @date 2024-01-01
 */
public interface UserBehaviorService extends IService<UserBehaviorEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<UserBehaviorVO> selectListVO(Wrapper<UserBehaviorEntity> wrapper);
	
	UserBehaviorVO selectVO(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	List<UserBehaviorView> selectListView(Wrapper<UserBehaviorEntity> wrapper);
	
	UserBehaviorView selectView(@Param("ew") Wrapper<UserBehaviorEntity> wrapper);
	
	PageUtils queryPage(Map<String, Object> params,Wrapper<UserBehaviorEntity> wrapper);
	
	// 用户行为相关方法
	void recordBehavior(Long userid, Long goodid, String behaviorType);
	
	List<Map<String, Object>> getUserBehaviorStats(Long userid);

}
