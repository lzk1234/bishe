package com.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class InventoryRiskItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String batchCode;
    private String productName;
    private String category;
    private String enterpriseAccount;
    private BigDecimal currentStock;
    private BigDecimal warningStock;
    private BigDecimal dailyAverage;
    private Integer forecast7Days;
    private BigDecimal availableDays;
    private String riskLevel;
    private BigDecimal riskScore;
    private String warningReason;
    private Integer batchCount;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

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

    public BigDecimal getWarningStock() {
        return warningStock;
    }

    public void setWarningStock(BigDecimal warningStock) {
        this.warningStock = warningStock;
    }

    public BigDecimal getDailyAverage() {
        return dailyAverage;
    }

    public void setDailyAverage(BigDecimal dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    public Integer getForecast7Days() {
        return forecast7Days;
    }

    public void setForecast7Days(Integer forecast7Days) {
        this.forecast7Days = forecast7Days;
    }

    public BigDecimal getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(BigDecimal availableDays) {
        this.availableDays = availableDays;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(BigDecimal riskScore) {
        this.riskScore = riskScore;
    }

    public String getWarningReason() {
        return warningReason;
    }

    public void setWarningReason(String warningReason) {
        this.warningReason = warningReason;
    }

    public Integer getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(Integer batchCount) {
        this.batchCount = batchCount;
    }
}
