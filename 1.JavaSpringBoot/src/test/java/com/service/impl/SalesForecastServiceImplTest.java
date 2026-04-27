package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.view.ForecastItemView;

public class SalesForecastServiceImplTest {

    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private SalesForecastServiceImpl service;
    private List<Map<String, Object>> productRows;
    private List<Map<String, Object>> salesRows;

    @BeforeEach
    public void setUp() {
        service = new SalesForecastServiceImpl();
        productRows = new ArrayList<Map<String, Object>>();
        salesRows = new ArrayList<Map<String, Object>>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public List<Map<String, Object>> queryForList(String sql, Object... args) {
                if (sql.startsWith("select shangpinmingcheng as productName")) {
                    return productRows;
                }
                if (sql.startsWith("select goodname as productName")) {
                    return salesRows;
                }
                return new ArrayList<Map<String, Object>>();
            }
        };
        ReflectionTestUtils.setField(service, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    public void listForecastsReturnsZeroWhenRecentSevenDaysHaveNoSales() {
        productRows = buildProductRows();
        salesRows = new ArrayList<Map<String, Object>>();

        List<ForecastItemView> result = service.listForecasts("merchantA");

        assertEquals(1, result.size());
        assertEquals("spring-tea", result.get(0).getProductName());
        assertEquals(0, result.get(0).getRecentSales().intValue());
        assertEquals(new BigDecimal("0.00"), result.get(0).getDailyAverage());
        assertEquals(0, result.get(0).getForecast7Days().intValue());
        assertEquals("flat", result.get(0).getTrend());
    }

    @Test
    public void listForecastsMarksTrendUpWhenRecentThreeDaysOutperformPreviousFourDays() {
        productRows = buildProductRows();
        salesRows = buildSalesRows();

        List<ForecastItemView> result = service.listForecasts("merchantA");

        assertEquals(1, result.size());
        assertEquals("up", result.get(0).getTrend());
        assertTrue(result.get(0).getForecast7Days().intValue() > result.get(0).getRecentSales().intValue());
        assertTrue(result.get(0).getDailyAverage().compareTo(new BigDecimal("0.00")) > 0);
    }

    private List<Map<String, Object>> buildProductRows() {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("productName", "spring-tea");
        row.put("category", "green-tea");
        row.put("brand", "mountain-tea");
        row.put("enterpriseAccount", "merchantA");
        rows.add(row);
        return rows;
    }

    private List<Map<String, Object>> buildSalesRows() {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        LocalDate startDay = LocalDate.now().minusDays(6);
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(0).format(DAY_FORMATTER), 1));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(1).format(DAY_FORMATTER), 1));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(2).format(DAY_FORMATTER), 1));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(3).format(DAY_FORMATTER), 1));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(4).format(DAY_FORMATTER), 5));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(5).format(DAY_FORMATTER), 5));
        rows.add(buildSalesRow("spring-tea", "merchantA", startDay.plusDays(6).format(DAY_FORMATTER), 5));
        return rows;
    }

    private Map<String, Object> buildSalesRow(String productName, String enterpriseAccount, String dayKey, int sales) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("productName", productName);
        row.put("enterpriseAccount", enterpriseAccount);
        row.put("dayKey", dayKey);
        row.put("sales", sales);
        return row;
    }
}
