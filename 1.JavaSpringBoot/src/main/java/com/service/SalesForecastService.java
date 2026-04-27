package com.service;

import java.util.List;

import com.entity.view.ForecastItemView;

public interface SalesForecastService {

    List<ForecastItemView> listForecasts(String enterpriseAccount);
}
