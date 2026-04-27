package com.entity.vo;

import com.entity.UserBehaviorEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;


/**
 * 用户行为记录表
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 * @date 2024-01-01
 */
public class UserBehaviorVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 用户ID
	 */
	
	private Long userid;
		
	/**
	 * 茶叶ID
	 */
	
	private Long goodid;
		
	/**
	 * 行为类型：view-浏览,favorite-收藏,buy-购买
	 */
	
	private String behaviorType;
		
	/**
	 * 行为时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat		
	private Date addtime;
		

	/**
	 * 设置：用户ID
	 */
	 
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	/**
	 * 获取：用户ID
	 */
	public Long getUserid() {
		return userid;
	}
				
	
	/**
	 * 设置：茶叶ID
	 */
	 
	public void setGoodid(Long goodid) {
		this.goodid = goodid;
	}
	
	/**
	 * 获取：茶叶ID
	 */
	public Long getGoodid() {
		return goodid;
	}
				
	
	/**
	 * 设置：行为类型：view-浏览,favorite-收藏,buy-购买
	 */
	 
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}
	
	/**
	 * 获取：行为类型：view-浏览,favorite-收藏,buy-购买
	 */
	public String getBehaviorType() {
		return behaviorType;
	}
				
	
	/**
	 * 设置：行为时间
	 */
	 
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	/**
	 * 获取：行为时间
	 */
	public Date getAddtime() {
		return addtime;
	}
			
}
