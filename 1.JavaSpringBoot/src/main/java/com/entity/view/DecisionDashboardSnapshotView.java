package com.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class DecisionDashboardSnapshotView implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer inventoryWarningCount;
    private Integer slowMovingProductCount;
    private Integer currentMonthSales;
    private Integer lastMonthSales;
    private BigDecimal currentMonthRevenue;
    private BigDecimal lastMonthRevenue;
    private Integer abnormalEnterpriseCount;
    private String topCategoryName;
    private Integer forecastTotal7Days;
    private Integer highRiskProductCount;
    private Integer restockPendingCount;
    private String topForecastProductName;

    public Integer getInventoryWarningCount() {
        return inventoryWarningCount;
    }

    public void setInventoryWarningCount(Integer inventoryWarningCount) {
        this.inventoryWarningCount = inventoryWarningCount;
    }

    public Integer getSlowMovingProductCount() {
        return slowMovingProductCount;
    }

    public void setSlowMovingProductCount(Integer slowMovingProductCount) {
        this.slowMovingProductCount = slowMovingProductCount;
    }

    public Integer getCurrentMonthSales() {
        return currentMonthSales;
    }

    public void setCurrentMonthSales(Integer currentMonthSales) {
        this.currentMonthSales = currentMonthSales;
    }

    public Integer getLastMonthSales() {
        return lastMonthSales;
    }

    public void setLastMonthSales(Integer lastMonthSales) {
        this.lastMonthSales = lastMonthSales;
    }

    public BigDecimal getCurrentMonthRevenue() {
        return currentMonthRevenue;
    }

    public void setCurrentMonthRevenue(BigDecimal currentMonthRevenue) {
        this.currentMonthRevenue = currentMonthRevenue;
    }

    public BigDecimal getLastMonthRevenue() {
        return lastMonthRevenue;
    }

    public void setLastMonthRevenue(BigDecimal lastMonthRevenue) {
        this.lastMonthRevenue = lastMonthRevenue;
    }

    public Integer getAbnormalEnterpriseCount() {
        return abnormalEnterpriseCount;
    }

    public void setAbnormalEnterpriseCount(Integer abnormalEnterpriseCount) {
        this.abnormalEnterpriseCount = abnormalEnterpriseCount;
    }

    public String getTopCategoryName() {
        return topCategoryName;
    }

    public void setTopCategoryName(String topCategoryName) {
        this.topCategoryName = topCategoryName;
    }

    public Integer getForecastTotal7Days() {
        return forecastTotal7Days;
    }

    public void setForecastTotal7Days(Integer forecastTotal7Days) {
        this.forecastTotal7Days = forecastTotal7Days;
    }

    public Integer getHighRiskProductCount() {
        return highRiskProductCount;
    }

    public void setHighRiskProductCount(Integer highRiskProductCount) {
        this.highRiskProductCount = highRiskProductCount;
    }

    public Integer getRestockPendingCount() {
        return restockPendingCount;
    }

    public void setRestockPendingCount(Integer restockPendingCount) {
        this.restockPendingCount = restockPendingCount;
    }

    public String getTopForecastProductName() {
        return topForecastProductName;
    }

    public void setTopForecastProductName(String topForecastProductName) {
        this.topForecastProductName = topForecastProductName;
    }
}
