package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.service.SalesForecastService;

@ExtendWith(MockitoExtension.class)
public class InventoryRiskServiceImplTest {

    @Mock
    private SalesForecastService salesForecastService;

    private InventoryRiskServiceImpl service;
    private List<Map<String, Object>> inventoryRows;

    @BeforeEach
    public void setUp() {
        service = new InventoryRiskServiceImpl();
        inventoryRows = new ArrayList<Map<String, Object>>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public List<Map<String, Object>> queryForList(String sql, Object... args) {
                return inventoryRows;
            }
        };
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(service, "salesForecastService", salesForecastService);
    }

    @Test
    public void listInventoryRisksMarksUrgentWhenCurrentStockDropsBelowWarningStock() {
        inventoryRows = buildInventoryRows();
        when(salesForecastService.listForecasts("merchantA")).thenReturn(buildForecastRows());

        List<InventoryRiskItemView> result = service.listInventoryRisks("merchantA");

        assertEquals(1, result.size());
        assertEquals("urgent", result.get(0).getRiskLevel());
        assertTrue(result.get(0).getAvailableDays().compareTo(new BigDecimal("3.00")) < 0);
        assertTrue(result.get(0).getRiskScore().compareTo(new BigDecimal("0.00")) > 0);
    }

    @Test
    public void listInventoryRisksDeduplicatesDuplicateBatchRows() {
        inventoryRows = buildDuplicatedInventoryRows();
        when(salesForecastService.listForecasts("merchantA")).thenReturn(buildForecastRows());

        List<InventoryRiskItemView> result = service.listInventoryRisks("merchantA");

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("8.00"), result.get(0).getCurrentStock());
        assertEquals(Integer.valueOf(1), result.get(0).getBatchCount());
    }

    private List<Map<String, Object>> buildInventoryRows() {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        rows.add(buildInventoryRow("BATCH-001", "凤凰单丛", "乌龙茶", "merchantA", "8", "10"));
        return rows;
    }

    private List<Map<String, Object>> buildDuplicatedInventoryRows() {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        rows.add(buildInventoryRow("BATCH-001", "凤凰单丛", "乌龙茶", "merchantA", "8", "10"));
        rows.add(buildInventoryRow("BATCH-001", "凤凰单丛", "乌龙茶", "merchantA", "8", "10"));
        return rows;
    }

    private Map<String, Object> buildInventoryRow(
        String batchCode,
        String productName,
        String category,
        String enterpriseAccount,
        String currentStock,
        String warningStock
    ) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("batchCode", batchCode);
        row.put("productName", productName);
        row.put("category", category);
        row.put("enterpriseAccount", enterpriseAccount);
        row.put("currentStock", new BigDecimal(currentStock));
        row.put("warningStock", new BigDecimal(warningStock));
        return row;
    }

    private List<ForecastItemView> buildForecastRows() {
        List<ForecastItemView> rows = new ArrayList<ForecastItemView>();
        ForecastItemView item = new ForecastItemView();
        item.setProductName("凤凰单丛");
        item.setCategory("乌龙茶");
        item.setEnterpriseAccount("merchantA");
        item.setDailyAverage(new BigDecimal("4.00"));
        item.setForecast7Days(28);
        rows.add(item);
        return rows;
    }
}
