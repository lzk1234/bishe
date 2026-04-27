package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 推荐结果表
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2024-01-01
 */
@TableName("recommendation")
public class RecommendationEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public RecommendationEntity() {
		
	}
	
	public RecommendationEntity(T t) {
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
	 * 推荐分数
	 */
					
	private BigDecimal score;
	
	/**
	 * 推荐理由
	 */
					
	private String reason;
	
	/**
	 * 算法类型：collaborative-协同过滤,content-内容推荐,hot-热门
	 */
					
	private String algorithmType;
	
	/**
	 * 生成时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat		
	private Date createTime;


	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	 * 设置：推荐分数
	 */
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	/**
	 * 获取：推荐分数
	 */
	public BigDecimal getScore() {
		return score;
	}
	/**
	 * 设置：推荐理由
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：推荐理由
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 设置：算法类型：collaborative-协同过滤,content-内容推荐,hot-热门
	 */
	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}
	/**
	 * 获取：算法类型：collaborative-协同过滤,content-内容推荐,hot-热门
	 */
	public String getAlgorithmType() {
		return algorithmType;
	}

}
