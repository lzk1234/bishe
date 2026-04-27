package com.entity.view;

import com.entity.UserBehaviorEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;


/**
 * 用户行为记录表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-01-01
 */
@TableName("user_behavior")
public class UserBehaviorView  extends UserBehaviorEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public UserBehaviorView(){
	}
 
 	public UserBehaviorView(UserBehaviorEntity userBehaviorEntity){
 	try {
			BeanUtils.copyProperties(this, userBehaviorEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
