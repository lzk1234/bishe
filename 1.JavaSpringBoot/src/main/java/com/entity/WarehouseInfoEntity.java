package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("warehouse_info")
public class WarehouseInfoEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public WarehouseInfoEntity() {
    }

    public WarehouseInfoEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private String warehousecode;
    private String warehousename;
    private String warehousetype;
    private String location;
    private String principal;
    private String contactphone;
    private String enterpriseaccount;
    private String status;
    private String remark;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWarehousecode() { return warehousecode; }
    public void setWarehousecode(String warehousecode) { this.warehousecode = warehousecode; }
    public String getWarehousename() { return warehousename; }
    public void setWarehousename(String warehousename) { this.warehousename = warehousename; }
    public String getWarehousetype() { return warehousetype; }
    public void setWarehousetype(String warehousetype) { this.warehousetype = warehousetype; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getPrincipal() { return principal; }
    public void setPrincipal(String principal) { this.principal = principal; }
    public String getContactphone() { return contactphone; }
    public void setContactphone(String contactphone) { this.contactphone = contactphone; }
    public String getEnterpriseaccount() { return enterpriseaccount; }
    public void setEnterpriseaccount(String enterpriseaccount) { this.enterpriseaccount = enterpriseaccount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
