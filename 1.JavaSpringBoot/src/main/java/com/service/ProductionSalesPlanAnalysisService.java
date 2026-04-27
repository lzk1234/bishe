package com.service;

import java.util.Map;

public interface ProductionSalesPlanAnalysisService {
    Map<String, Object> buildAnnualBoard(String enterpriseAccount, Integer year);
}
