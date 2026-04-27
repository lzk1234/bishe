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

@TableName("inventoryrecord")
public class InventoryrecordEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public InventoryrecordEntity() {
    }

    public InventoryrecordEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String batchcode;
    private String productname;
    private String recordtype;
    private BigDecimal changestock;
    private BigDecimal currentstock;
    private BigDecimal warningstock;
    private String warehousecode;
    private String warehousename;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date recordtime;

    private String enterpriseaccount;
    private String relatedorderid;
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

    public String getBatchcode() {
        return batchcode;
    }

    public void setBatchcode(String batchcode) {
        this.batchcode = batchcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype;
    }

    public BigDecimal getChangestock() {
        return changestock;
    }

    public void setChangestock(BigDecimal changestock) {
        this.changestock = changestock;
    }

    public BigDecimal getCurrentstock() {
        return currentstock;
    }

    public void setCurrentstock(BigDecimal currentstock) {
        this.currentstock = currentstock;
    }

    public BigDecimal getWarningstock() {
        return warningstock;
    }

    public void setWarningstock(BigDecimal warningstock) {
        this.warningstock = warningstock;
    }

    public String getWarehousecode() {
        return warehousecode;
    }

    public void setWarehousecode(String warehousecode) {
        this.warehousecode = warehousecode;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }

    public Date getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }

    public String getEnterpriseaccount() {
        return enterpriseaccount;
    }

    public void setEnterpriseaccount(String enterpriseaccount) {
        this.enterpriseaccount = enterpriseaccount;
    }

    public String getRelatedorderid() {
        return relatedorderid;
    }

    public void setRelatedorderid(String relatedorderid) {
        this.relatedorderid = relatedorderid;
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
