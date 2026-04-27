package com.entity.view;

import com.entity.RecommendationEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;


/**
 * 推荐结果表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-01-01
 */
@TableName("recommendation")
public class RecommendationView  extends RecommendationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String shangpinmingcheng;

	private String shangpinfenlei;

	private String tupian;

	private String pinpai;

	private Float price;

	private Float vipprice;

	public RecommendationView(){
	}
 
 	public RecommendationView(RecommendationEntity recommendationEntity){
 	try {
			BeanUtils.copyProperties(this, recommendationEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}

	public String getShangpinmingcheng() {
		return shangpinmingcheng;
	}

	public void setShangpinmingcheng(String shangpinmingcheng) {
		this.shangpinmingcheng = shangpinmingcheng;
	}

	public String getShangpinfenlei() {
		return shangpinfenlei;
	}

	public void setShangpinfenlei(String shangpinfenlei) {
		this.shangpinfenlei = shangpinfenlei;
	}

	public String getTupian() {
		return tupian;
	}

	public void setTupian(String tupian) {
		this.tupian = tupian;
	}

	public String getPinpai() {
		return pinpai;
	}

	public void setPinpai(String pinpai) {
		this.pinpai = pinpai;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getVipprice() {
		return vipprice;
	}

	public void setVipprice(Float vipprice) {
		this.vipprice = vipprice;
	}
}
