package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.UserBehaviorDao;
import com.entity.UserBehaviorEntity;
import com.service.UserBehaviorService;
import com.entity.vo.UserBehaviorVO;
import com.entity.view.UserBehaviorView;

import java.util.Date;

@Service("userBehaviorService")
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorDao, UserBehaviorEntity> implements UserBehaviorService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UserBehaviorEntity> page = this.selectPage(
                new Query<UserBehaviorEntity>(params).getPage(),
                new EntityWrapper<UserBehaviorEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<UserBehaviorEntity> wrapper) {
		  Page<UserBehaviorView> page =new Query<UserBehaviorView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<UserBehaviorVO> selectListVO(Wrapper<UserBehaviorEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public UserBehaviorVO selectVO(Wrapper<UserBehaviorEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<UserBehaviorView> selectListView(Wrapper<UserBehaviorEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public UserBehaviorView selectView(Wrapper<UserBehaviorEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public void recordBehavior(Long userid, Long goodid, String behaviorType) {
		UserBehaviorEntity behavior = new UserBehaviorEntity();
		behavior.setUserid(userid);
		behavior.setGoodid(goodid);
		behavior.setBehaviorType(behaviorType);
		behavior.setAddtime(new Date());
		this.insert(behavior);
	}
	
	@Override
	public List<Map<String, Object>> getUserBehaviorStats(Long userid) {
		return baseMapper.getUserBehaviorStats(userid);
	}

}
