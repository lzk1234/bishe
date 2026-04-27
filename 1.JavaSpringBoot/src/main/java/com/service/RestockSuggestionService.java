package com.service;

import java.util.List;

import com.entity.view.RestockPlanItemView;

public interface RestockSuggestionService {

    List<RestockPlanItemView> listRestockPlans(String enterpriseAccount);
}
