package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("teabase")
public class TeabaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public TeabaseEntity() {
    }

    public TeabaseEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String basecode;
    private String basename;
    private String location;
    private Integer altitude;
    private BigDecimal area;
    private String principal;
    private String contactphone;
    private String teatype;
    private String certification;
    private String enterpriseaccount;
    private String regiontag;
    private BigDecimal annualcapacity;
    private String mainvariety;
    private Integer plantingyear;
    private String basestatus;
    private String remark;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBasecode() {
        return basecode;
    }

    public void setBasecode(String basecode) {
        this.basecode = basecode;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getTeatype() {
        return teatype;
    }

    public void setTeatype(String teatype) {
        this.teatype = teatype;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getEnterpriseaccount() {
        return enterpriseaccount;
    }

    public void setEnterpriseaccount(String enterpriseaccount) {
        this.enterpriseaccount = enterpriseaccount;
    }

    public String getRegiontag() {
        return regiontag;
    }

    public void setRegiontag(String regiontag) {
        this.regiontag = regiontag;
    }

    public BigDecimal getAnnualcapacity() {
        return annualcapacity;
    }

    public void setAnnualcapacity(BigDecimal annualcapacity) {
        this.annualcapacity = annualcapacity;
    }

    public String getMainvariety() {
        return mainvariety;
    }

    public void setMainvariety(String mainvariety) {
        this.mainvariety = mainvariety;
    }

    public Integer getPlantingyear() {
        return plantingyear;
    }

    public void setPlantingyear(Integer plantingyear) {
        this.plantingyear = plantingyear;
    }

    public String getBasestatus() {
        return basestatus;
    }

    public void setBasestatus(String basestatus) {
        this.basestatus = basestatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
