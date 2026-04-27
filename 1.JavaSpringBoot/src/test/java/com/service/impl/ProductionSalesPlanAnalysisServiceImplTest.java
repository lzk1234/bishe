package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductionSalesPlanAnalysisServiceImplTest {

    private ProductionSalesPlanAnalysisServiceImpl service;
    private List<Map<String, Object>> planRows;

    @BeforeEach
    public void setUp() {
        service = new ProductionSalesPlanAnalysisServiceImpl();
        planRows = new ArrayList<Map<String, Object>>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public List<Map<String, Object>> queryForList(String sql, Object... args) {
                return planRows;
            }
        };
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void annualBoardMarksGapWhenPlannedSalesExceedsOutputPlusCurrentInventory() {
        planRows.add(buildPlanRow(4, "green-tea", "120", "180", "20", "40", "16800"));

        Map<String, Object> board = service.buildAnnualBoard("account8", Integer.valueOf(2026));

        Map<?, ?> summary = (Map<?, ?>) board.get("summary");
        List<?> gapList = (List<?>) board.get("gapList");
        assertEquals(new BigDecimal("120"), summary.get("plannedOutput"));
        assertEquals(new BigDecimal("180"), summary.get("plannedSales"));
        assertEquals(1L, summary.get("gapCount"));
        assertEquals(1, gapList.size());
        assertTrue(((Map<?, ?>) gapList.get(0)).get("gapAmount").toString().startsWith("20"));
    }

    private Map<String, Object> buildPlanRow(
        int month,
        String teaType,
        String output,
        String sales,
        String targetInventory,
        String currentInventory,
        String revenue
    ) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("planMonth", month);
        row.put("teatype", teaType);
        row.put("plannedOutput", new BigDecimal(output));
        row.put("plannedSales", new BigDecimal(sales));
        row.put("targetInventory", new BigDecimal(targetInventory));
        row.put("currentInventory", new BigDecimal(currentInventory));
        row.put("plannedRevenue", new BigDecimal(revenue));
        return row;
    }
}
