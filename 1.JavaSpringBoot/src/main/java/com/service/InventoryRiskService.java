package com.service;

import java.util.List;

import com.entity.view.InventoryRiskItemView;

public interface InventoryRiskService {

    List<InventoryRiskItemView> listBatchInventoryRisks(String enterpriseAccount);

    List<InventoryRiskItemView> listInventoryRisks(String enterpriseAccount);
}
