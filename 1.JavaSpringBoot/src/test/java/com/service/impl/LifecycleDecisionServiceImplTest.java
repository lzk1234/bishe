package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class LifecycleDecisionServiceImplTest {

    private LifecycleDecisionServiceImpl service;
    private List<Map<String, Object>> rows;

    @BeforeEach
    public void setUp() {
        service = new LifecycleDecisionServiceImpl();
        rows = new ArrayList<Map<String, Object>>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public List<Map<String, Object>> queryForList(String sql, Object... args) {
                return rows;
            }
        };
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void todayStatusMarksProductAsPromotionWhenBeyondMainSaleWindow() {
        rows.add(buildLifecycleRow(LocalDate.now().minusDays(55)));

        Map<String, Object> status = service.buildTodayStatus("账号8");

        List<?> list = (List<?>) status.get("bestSaleWindowList");
        Map<?, ?> item = (Map<?, ?>) list.get(0);
        assertEquals("promotion", item.get("stageCode"));
        assertEquals("建议组合促销或礼盒渠道去化", item.get("actionLabel"));
    }

    private Map<String, Object> buildLifecycleRow(LocalDate saleDate) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("productId", Long.valueOf(42L));
        row.put("productName", "云岭春茶");
        row.put("teatype", "高山绿茶");
        row.put("saleDate", Date.valueOf(saleDate));
        row.put("newPeriodDays", Integer.valueOf(15));
        row.put("mainSalePeriodDays", Integer.valueOf(45));
        row.put("promotionPeriodDays", Integer.valueOf(90));
        row.put("warningPeriodDays", Integer.valueOf(120));
        row.put("currentStock", new BigDecimal("80.00"));
        row.put("strategyNote", "春茶优先");
        return row;
    }
}
