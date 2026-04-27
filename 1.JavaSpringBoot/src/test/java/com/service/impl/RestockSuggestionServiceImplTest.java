package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;
import com.service.InventoryRiskService;
import com.service.SalesForecastService;

@ExtendWith(MockitoExtension.class)
public class RestockSuggestionServiceImplTest {

    @Mock
    private SalesForecastService salesForecastService;

    @Mock
    private InventoryRiskService inventoryRiskService;

    private RestockSuggestionServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new RestockSuggestionServiceImpl();
        ReflectionTestUtils.setField(service, "salesForecastService", salesForecastService);
        ReflectionTestUtils.setField(service, "inventoryRiskService", inventoryRiskService);
    }

    @Test
    public void listRestockPlansNeverReturnsNegativeSuggestedRestock() {
        when(salesForecastService.listForecasts("merchantA")).thenReturn(buildForecastRows());
        when(inventoryRiskService.listBatchInventoryRisks("merchantA")).thenReturn(buildRiskRows());

        List<RestockPlanItemView> result = service.listRestockPlans("merchantA");

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0"), result.get(0).getSuggestedRestock());
    }

    @Test
    public void listRestockPlansIncludesForecastedProductWithoutInventoryRecord() {
        when(salesForecastService.listForecasts("merchantA")).thenReturn(buildForecastRows());
        when(inventoryRiskService.listBatchInventoryRisks("merchantA")).thenReturn(new ArrayList<InventoryRiskItemView>());

        List<RestockPlanItemView> result = service.listRestockPlans("merchantA");

        assertEquals(1, result.size());
        assertEquals("凤凰单丛", result.get(0).getProductName());
        assertEquals(new BigDecimal("20"), result.get(0).getSuggestedRestock());
        assertEquals("urgent", result.get(0).getPriority());
    }

    @Test
    public void listRestockPlansSumsShortageAcrossBatchesInsteadOfNettingAgainstSurplus() {
        when(salesForecastService.listForecasts("merchantA")).thenReturn(buildForecastRows());
        when(inventoryRiskService.listBatchInventoryRisks("merchantA")).thenReturn(buildBatchRiskRows());

        List<RestockPlanItemView> result = service.listRestockPlans("merchantA");

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("20"), result.get(0).getSuggestedRestock());
    }

    private List<ForecastItemView> buildForecastRows() {
        List<ForecastItemView> rows = new ArrayList<ForecastItemView>();
        ForecastItemView item = new ForecastItemView();
        item.setProductName("凤凰单丛");
        item.setCategory("乌龙茶");
        item.setEnterpriseAccount("merchantA");
        item.setDailyAverage(new BigDecimal("2.00"));
        item.setForecast7Days(14);
        rows.add(item);
        return rows;
    }

    private List<InventoryRiskItemView> buildRiskRows() {
        List<InventoryRiskItemView> rows = new ArrayList<InventoryRiskItemView>();
        InventoryRiskItemView item = new InventoryRiskItemView();
        item.setProductName("凤凰单丛");
        item.setCategory("乌龙茶");
        item.setEnterpriseAccount("merchantA");
        item.setCurrentStock(new BigDecimal("50"));
        item.setWarningStock(new BigDecimal("10"));
        item.setRiskLevel("safe");
        rows.add(item);
        return rows;
    }

    private List<InventoryRiskItemView> buildBatchRiskRows() {
        List<InventoryRiskItemView> rows = new ArrayList<InventoryRiskItemView>();

        InventoryRiskItemView urgentBatch = new InventoryRiskItemView();
        urgentBatch.setProductName("凤凰单丛");
        urgentBatch.setCategory("乌龙茶");
        urgentBatch.setEnterpriseAccount("merchantA");
        urgentBatch.setCurrentStock(new BigDecimal("0"));
        urgentBatch.setWarningStock(new BigDecimal("5"));
        urgentBatch.setRiskLevel("urgent");
        rows.add(urgentBatch);

        InventoryRiskItemView safeBatch = new InventoryRiskItemView();
        safeBatch.setProductName("凤凰单丛");
        safeBatch.setCategory("乌龙茶");
        safeBatch.setEnterpriseAccount("merchantA");
        safeBatch.setCurrentStock(new BigDecimal("50"));
        safeBatch.setWarningStock(new BigDecimal("5"));
        safeBatch.setRiskLevel("safe");
        rows.add(safeBatch);

        return rows;
    }
}
