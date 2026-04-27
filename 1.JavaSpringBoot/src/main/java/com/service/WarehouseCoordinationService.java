package com.service;

import java.util.List;
import java.util.Map;

public interface WarehouseCoordinationService {
    List<Map<String, Object>> listWarehouseInventorySummary(String enterpriseAccount);

    List<Map<String, Object>> generateTransferSuggestions(String enterpriseAccount);

    List<Map<String, Object>> listTransferSuggestions(String enterpriseAccount);
}
