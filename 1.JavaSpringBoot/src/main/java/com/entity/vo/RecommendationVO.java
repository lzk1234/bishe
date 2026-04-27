package com.entity.vo;

import com.entity.RecommendationEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;


/**
 * 推荐结果表
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 * @date 2024-01-01
 */
public class RecommendationVO  implements Serializable {
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
				
	
	/**
	 * 设置：生成时间
	 */
	 
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 获取：生成时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
			
}
