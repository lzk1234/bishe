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

@TableName("production_sales_plan")
public class ProductionSalesPlanEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProductionSalesPlanEntity() {
    }

    public ProductionSalesPlanEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId
    private Long id;
    private Integer planyear;
    private Integer planmonth;
    private String enterpriseaccount;
    private Long productid;
    private String productname;
    private String teatype;
    private BigDecimal plannedoutput;
    private BigDecimal plannedsales;
    private BigDecimal targetinventory;
    private BigDecimal plannedrevenue;
    private String risklevel;
    private String remark;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getPlanyear() { return planyear; }
    public void setPlanyear(Integer planyear) { this.planyear = planyear; }
    public Integer getPlanmonth() { return planmonth; }
    public void setPlanmonth(Integer planmonth) { this.planmonth = planmonth; }
    public String getEnterpriseaccount() { return enterpriseaccount; }
    public void setEnterpriseaccount(String enterpriseaccount) { this.enterpriseaccount = enterpriseaccount; }
    public Long getProductid() { return productid; }
    public void setProductid(Long productid) { this.productid = productid; }
    public String getProductname() { return productname; }
    public void setProductname(String productname) { this.productname = productname; }
    public String getTeatype() { return teatype; }
    public void setTeatype(String teatype) { this.teatype = teatype; }
    public BigDecimal getPlannedoutput() { return plannedoutput; }
    public void setPlannedoutput(BigDecimal plannedoutput) { this.plannedoutput = plannedoutput; }
    public BigDecimal getPlannedsales() { return plannedsales; }
    public void setPlannedsales(BigDecimal plannedsales) { this.plannedsales = plannedsales; }
    public BigDecimal getTargetinventory() { return targetinventory; }
    public void setTargetinventory(BigDecimal targetinventory) { this.targetinventory = targetinventory; }
    public BigDecimal getPlannedrevenue() { return plannedrevenue; }
    public void setPlannedrevenue(BigDecimal plannedrevenue) { this.plannedrevenue = plannedrevenue; }
    public String getRisklevel() { return risklevel; }
    public void setRisklevel(String risklevel) { this.risklevel = risklevel; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
