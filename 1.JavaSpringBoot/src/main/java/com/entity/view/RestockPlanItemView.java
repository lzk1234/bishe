package com.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class RestockPlanItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private String category;
    private String enterpriseAccount;
    private BigDecimal currentStock;
    private BigDecimal safetyStock;
    private Integer forecast7Days;
    private BigDecimal suggestedRestock;
    private Integer suggestedDays;
    private String priority;
    private String reason;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEnterpriseAccount() {
        return enterpriseAccount;
    }

    public void setEnterpriseAccount(String enterpriseAccount) {
        this.enterpriseAccount = enterpriseAccount;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public BigDecimal getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(BigDecimal safetyStock) {
        this.safetyStock = safetyStock;
    }

    public Integer getForecast7Days() {
        return forecast7Days;
    }

    public void setForecast7Days(Integer forecast7Days) {
        this.forecast7Days = forecast7Days;
    }

    public BigDecimal getSuggestedRestock() {
        return suggestedRestock;
    }

    public void setSuggestedRestock(BigDecimal suggestedRestock) {
        this.suggestedRestock = suggestedRestock;
    }

    public Integer getSuggestedDays() {
        return suggestedDays;
    }

    public void setSuggestedDays(Integer suggestedDays) {
        this.suggestedDays = suggestedDays;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
