package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 用户行为记录表
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2024-01-01
 */
@TableName("user_behavior")
public class UserBehaviorEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public UserBehaviorEntity() {
		
	}
	
	public UserBehaviorEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId
	private Long id;
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


	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
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

}
