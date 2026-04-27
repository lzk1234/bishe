package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("product_lifecycle_rule")
public class ProductLifecycleRuleEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProductLifecycleRuleEntity() {
    }

    public ProductLifecycleRuleEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String enterpriseaccount;
    private Long productid;
    private String productname;
    private String teatype;
    private Integer newperioddays;
    private Integer mainsaleperioddays;
    private Integer promotionperioddays;
    private Integer warningperioddays;
    private String strategynote;
    private Integer enabled;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnterpriseaccount() { return enterpriseaccount; }
    public void setEnterpriseaccount(String enterpriseaccount) { this.enterpriseaccount = enterpriseaccount; }
    public Long getProductid() { return productid; }
    public void setProductid(Long productid) { this.productid = productid; }
    public String getProductname() { return productname; }
    public void setProductname(String productname) { this.productname = productname; }
    public String getTeatype() { return teatype; }
    public void setTeatype(String teatype) { this.teatype = teatype; }
    public Integer getNewperioddays() { return newperioddays; }
    public void setNewperioddays(Integer newperioddays) { this.newperioddays = newperioddays; }
    public Integer getMainsaleperioddays() { return mainsaleperioddays; }
    public void setMainsaleperioddays(Integer mainsaleperioddays) { this.mainsaleperioddays = mainsaleperioddays; }
    public Integer getPromotionperioddays() { return promotionperioddays; }
    public void setPromotionperioddays(Integer promotionperioddays) { this.promotionperioddays = promotionperioddays; }
    public Integer getWarningperioddays() { return warningperioddays; }
    public void setWarningperioddays(Integer warningperioddays) { this.warningperioddays = warningperioddays; }
    public String getStrategynote() { return strategynote; }
    public void setStrategynote(String strategynote) { this.strategynote = strategynote; }
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
