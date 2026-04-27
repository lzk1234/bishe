package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class WarehouseCoordinationServiceImplTest {

    private WarehouseCoordinationServiceImpl service;
    private List<Map<String, Object>> inventoryRows;

    @BeforeEach
    public void setUp() {
        service = new WarehouseCoordinationServiceImpl();
        inventoryRows = new ArrayList<Map<String, Object>>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public List<Map<String, Object>> queryForList(String sql, Object... args) {
                return inventoryRows;
            }
        };
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void generateSuggestionsMovesStockFromSurplusWarehouseToLowWarehouse() {
        inventoryRows.add(buildInventoryRow("WH-A", "总仓", "云岭春茶", "12", "40"));
        inventoryRows.add(buildInventoryRow("WH-B", "电商仓", "云岭春茶", "110", "40"));

        List<Map<String, Object>> suggestions = service.generateTransferSuggestions("账号8");

        assertEquals(1, suggestions.size());
        assertEquals("WH-B", suggestions.get(0).get("sourceWarehouseCode"));
        assertEquals("WH-A", suggestions.get(0).get("targetWarehouseCode"));
        assertEquals(new BigDecimal("28.00"), suggestions.get(0).get("suggestedQuantity"));
    }

    private Map<String, Object> buildInventoryRow(
        String warehouseCode,
        String warehouseName,
        String productName,
        String currentStock,
        String warningStock
    ) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("warehouseCode", warehouseCode);
        row.put("warehouseName", warehouseName);
        row.put("productName", productName);
        row.put("currentStock", new BigDecimal(currentStock));
        row.put("warningStock", new BigDecimal(warningStock));
        row.put("enterpriseAccount", "账号8");
        return row;
    }
}
