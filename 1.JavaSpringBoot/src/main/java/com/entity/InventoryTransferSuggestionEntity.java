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

@TableName("inventory_transfer_suggestion")
public class InventoryTransferSuggestionEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public InventoryTransferSuggestionEntity() {
    }

    public InventoryTransferSuggestionEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String enterpriseaccount;
    private String productname;
    private String sourcewarehousecode;
    private String sourcewarehousename;
    private String targetwarehousecode;
    private String targetwarehousename;
    private BigDecimal suggestedquantity;
    private String reason;
    private String status;
    private String remark;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEnterpriseaccount() { return enterpriseaccount; }
    public void setEnterpriseaccount(String enterpriseaccount) { this.enterpriseaccount = enterpriseaccount; }
    public String getProductname() { return productname; }
    public void setProductname(String productname) { this.productname = productname; }
    public String getSourcewarehousecode() { return sourcewarehousecode; }
    public void setSourcewarehousecode(String sourcewarehousecode) { this.sourcewarehousecode = sourcewarehousecode; }
    public String getSourcewarehousename() { return sourcewarehousename; }
    public void setSourcewarehousename(String sourcewarehousename) { this.sourcewarehousename = sourcewarehousename; }
    public String getTargetwarehousecode() { return targetwarehousecode; }
    public void setTargetwarehousecode(String targetwarehousecode) { this.targetwarehousecode = targetwarehousecode; }
    public String getTargetwarehousename() { return targetwarehousename; }
    public void setTargetwarehousename(String targetwarehousename) { this.targetwarehousename = targetwarehousename; }
    public BigDecimal getSuggestedquantity() { return suggestedquantity; }
    public void setSuggestedquantity(BigDecimal suggestedquantity) { this.suggestedquantity = suggestedquantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
