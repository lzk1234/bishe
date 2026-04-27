package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("teabatch")
public class TeabatchEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public TeabatchEntity() {
    }

    public TeabatchEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String batchcode;
    private String basename;
    private String productname;
    private String teatype;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat
    private Date pickingdate;

    private BigDecimal freshweight;
    private String processmethod;
    private BigDecimal finishedweight;
    private String batchstatus;
    private Integer altitude;
    private String enterpriseaccount;
    private String remark;

    @TableField(exist = false)
    private Long baseid;

    @TableField(exist = false)
    private Long productid;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchcode() {
        return batchcode;
    }

    public void setBatchcode(String batchcode) {
        this.batchcode = batchcode;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getTeatype() {
        return teatype;
    }

    public void setTeatype(String teatype) {
        this.teatype = teatype;
    }

    public Date getPickingdate() {
        return pickingdate;
    }

    public void setPickingdate(Date pickingdate) {
        this.pickingdate = pickingdate;
    }

    public BigDecimal getFreshweight() {
        return freshweight;
    }

    public void setFreshweight(BigDecimal freshweight) {
        this.freshweight = freshweight;
    }

    public String getProcessmethod() {
        return processmethod;
    }

    public void setProcessmethod(String processmethod) {
        this.processmethod = processmethod;
    }

    public BigDecimal getFinishedweight() {
        return finishedweight;
    }

    public void setFinishedweight(BigDecimal finishedweight) {
        this.finishedweight = finishedweight;
    }

    public String getBatchstatus() {
        return batchstatus;
    }

    public void setBatchstatus(String batchstatus) {
        this.batchstatus = batchstatus;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public String getEnterpriseaccount() {
        return enterpriseaccount;
    }

    public void setEnterpriseaccount(String enterpriseaccount) {
        this.enterpriseaccount = enterpriseaccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getBaseid() {
        return baseid;
    }

    public void setBaseid(Long baseid) {
        this.baseid = baseid;
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
