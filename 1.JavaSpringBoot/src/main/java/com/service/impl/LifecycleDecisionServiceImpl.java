package com.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.service.LifecycleDecisionService;

@Service("lifecycleDecisionService")
public class LifecycleDecisionServiceImpl implements LifecycleDecisionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> buildTodayStatus(String enterpriseAccount) {
        List<Map<String, Object>> rows = queryLifecycleRows(enterpriseAccount);
        Map<String, Long> stageSummary = new LinkedHashMap<String, Long>();
        List<Map<String, Object>> bestSaleWindowList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> riskList = new ArrayList<Map<String, Object>>();
        LocalDate today = LocalDate.now();

        for (Map<String, Object> row : rows) {
            LocalDate saleDate = toLocalDate(first(row, "saleDate", "saledate"));
            long ageDays = saleDate == null ? 0L : ChronoUnit.DAYS.between(saleDate, today);
            int newDays = safeInt(first(row, "newPeriodDays", "newperioddays"), 15);
            int mainDays = safeInt(first(row, "mainSalePeriodDays", "mainsaleperioddays"), 45);
            int promotionDays = safeInt(first(row, "promotionPeriodDays", "promotionperioddays"), 90);
            int warningDays = safeInt(first(row, "warningPeriodDays", "warningperioddays"), 120);
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            String stageCode;
            String actionLabel;
            if (ageDays <= newDays) {
                stageCode = "new";
                actionLabel = "建议作为新茶主推";
            } else if (ageDays <= mainDays) {
                stageCode = "main";
                actionLabel = "维持主销节奏";
            } else if (ageDays <= promotionDays) {
                stageCode = "promotion";
                actionLabel = "建议组合促销或礼盒渠道去化";
            } else if (ageDays <= warningDays) {
                stageCode = "warning";
                actionLabel = "建议调仓或控制补货";
            } else {
                stageCode = "risk";
                actionLabel = "建议清货并停止补货";
            }
            item.put("productId", first(row, "productId", "productid"));
            item.put("productName", first(row, "productName", "productname"));
            item.put("teatype", first(row, "teatype"));
            item.put("ageDays", Long.valueOf(ageDays));
            item.put("stageCode", stageCode);
            item.put("actionLabel", actionLabel);
            item.put("currentStock", safeDecimal(first(row, "currentStock", "currentstock")));
            item.put("strategyNote", first(row, "strategyNote", "strategynote"));
            stageSummary.put(stageCode, Long.valueOf(stageSummary.containsKey(stageCode) ? stageSummary.get(stageCode).longValue() + 1L : 1L));
            bestSaleWindowList.add(item);
            if ("warning".equals(stageCode) || "risk".equals(stageCode)) {
                riskList.add(item);
            }
        }

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("lifecycleStageSummary", stageSummary);
        result.put("bestSaleWindowList", bestSaleWindowList);
        result.put("lifecycleRiskList", riskList);
        return result;
    }

    private List<Map<String, Object>> queryLifecycleRows(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select r.productid as productId, coalesce(r.productname, s.shangpinmingcheng) as productName, coalesce(r.teatype, s.shangpinfenlei) as teatype, " +
            "s.shangjiariqi as saleDate, r.newperioddays as newPeriodDays, r.mainsaleperioddays as mainSalePeriodDays, " +
            "r.promotionperioddays as promotionPeriodDays, r.warningperioddays as warningPeriodDays, r.strategynote as strategyNote, " +
            "coalesce(s.alllimittimes,0) as currentStock from product_lifecycle_rule r left join shangpinxinxi s on r.productid = s.id where coalesce(r.enabled,1) = 1"
        );
        if (enterpriseAccount == null) {
            return jdbcTemplate.queryForList(sql.toString());
        }
        return jdbcTemplate.queryForList(sql.append(" and r.enterpriseaccount = ?").toString(), enterpriseAccount);
    }

    private Object first(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            if (row.containsKey(key)) {
                return row.get(key);
            }
        }
        return null;
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof Date) {
            return ((Date) value).toLocalDate();
        }
        if (value instanceof java.util.Date) {
            return new Date(((java.util.Date) value).getTime()).toLocalDate();
        }
        return value == null ? null : LocalDate.parse(value.toString().substring(0, 10));
    }

    private int safeInt(Object value, int defaultValue) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return value == null ? defaultValue : Integer.parseInt(value.toString());
    }

    private BigDecimal safeDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }
}
