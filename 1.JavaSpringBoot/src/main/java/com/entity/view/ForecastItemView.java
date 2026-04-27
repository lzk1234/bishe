package com.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class ForecastItemView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName;
    private String category;
    private String brand;
    private String enterpriseAccount;
    private Integer recentSales;
    private BigDecimal dailyAverage;
    private Integer forecast3Days;
    private Integer forecast7Days;
    private String trend;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEnterpriseAccount() {
        return enterpriseAccount;
    }

    public void setEnterpriseAccount(String enterpriseAccount) {
        this.enterpriseAccount = enterpriseAccount;
    }

    public Integer getRecentSales() {
        return recentSales;
    }

    public void setRecentSales(Integer recentSales) {
        this.recentSales = recentSales;
    }

    public BigDecimal getDailyAverage() {
        return dailyAverage;
    }

    public void setDailyAverage(BigDecimal dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    public Integer getForecast3Days() {
        return forecast3Days;
    }

    public void setForecast3Days(Integer forecast3Days) {
        this.forecast3Days = forecast3Days;
    }

    public Integer getForecast7Days() {
        return forecast7Days;
    }

    public void setForecast7Days(Integer forecast7Days) {
        this.forecast7Days = forecast7Days;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
}
