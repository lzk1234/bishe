package com.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.service.ProductionSalesPlanAnalysisService;

@Service("productionSalesPlanAnalysisService")
public class ProductionSalesPlanAnalysisServiceImpl implements ProductionSalesPlanAnalysisService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> buildAnnualBoard(String enterpriseAccount, Integer year) {
        int targetYear = year == null ? LocalDate.now().getYear() : year.intValue();
        List<Map<String, Object>> rows = queryPlanRows(enterpriseAccount, targetYear);
        List<Map<String, Object>> trend = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> gapList = new ArrayList<Map<String, Object>>();
        BigDecimal totalOutput = BigDecimal.ZERO;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalInventory = BigDecimal.ZERO;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Map<String, Object> row : rows) {
            BigDecimal output = safeDecimal(first(row, "plannedOutput", "plannedoutput", "planned_output"));
            BigDecimal sales = safeDecimal(first(row, "plannedSales", "plannedsales", "planned_sales"));
            BigDecimal targetInventory = safeDecimal(first(row, "targetInventory", "targetinventory", "target_inventory"));
            BigDecimal currentInventory = safeDecimal(first(row, "currentInventory", "currentinventory", "current_inventory"));
            BigDecimal revenue = safeDecimal(first(row, "plannedRevenue", "plannedrevenue", "planned_revenue"));
            BigDecimal available = output.add(currentInventory);
            BigDecimal gap = sales.subtract(available);
            totalOutput = totalOutput.add(output);
            totalSales = totalSales.add(sales);
            totalInventory = totalInventory.add(targetInventory);
            totalRevenue = totalRevenue.add(revenue);

            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("planMonth", safeInt(first(row, "planMonth", "planmonth", "plan_month")));
            item.put("teatype", first(row, "teatype", "teaType"));
            item.put("plannedOutput", output);
            item.put("plannedSales", sales);
            item.put("targetInventory", targetInventory);
            item.put("currentInventory", currentInventory);
            item.put("plannedRevenue", revenue);
            item.put("gapAmount", gap.compareTo(BigDecimal.ZERO) > 0 ? gap.setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            trend.add(item);
            if (gap.compareTo(BigDecimal.ZERO) > 0) {
                gapList.add(item);
            }
        }

        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("planYear", targetYear);
        summary.put("plannedOutput", totalOutput);
        summary.put("plannedSales", totalSales);
        summary.put("targetInventory", totalInventory);
        summary.put("plannedRevenue", totalRevenue);
        summary.put("gapCount", Long.valueOf(gapList.size()));

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("summary", summary);
        result.put("trend", trend);
        result.put("gapList", gapList);
        return result;
    }

    private List<Map<String, Object>> queryPlanRows(String enterpriseAccount, int year) {
        StringBuilder sql = new StringBuilder(
            "select p.planmonth as planMonth, p.teatype, p.plannedoutput as plannedOutput, p.plannedsales as plannedSales, " +
            "p.targetinventory as targetInventory, coalesce(s.currentInventory,0) as currentInventory, p.plannedrevenue as plannedRevenue " +
            "from production_sales_plan p left join (" +
            "select productname, enterpriseaccount, coalesce(sum(currentstock),0) as currentInventory from inventoryrecord group by productname, enterpriseaccount" +
            ") s on s.productname = p.productname and (s.enterpriseaccount = p.enterpriseaccount or (s.enterpriseaccount is null and p.enterpriseaccount is null)) where p.planyear = ?"
        );
        if (enterpriseAccount == null) {
            return jdbcTemplate.queryForList(sql.append(" order by p.planmonth asc").toString(), Integer.valueOf(year));
        }
        return jdbcTemplate.queryForList(sql.append(" and p.enterpriseaccount = ? order by p.planmonth asc").toString(), Integer.valueOf(year), enterpriseAccount);
    }

    private Object first(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            if (row.containsKey(key)) {
                return row.get(key);
            }
        }
        return null;
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

    private int safeInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return value == null ? 0 : Integer.parseInt(value.toString());
    }
}
