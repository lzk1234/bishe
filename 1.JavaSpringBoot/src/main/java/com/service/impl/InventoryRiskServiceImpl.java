package com.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.service.InventoryRiskService;
import com.service.SalesForecastService;

@Service("inventoryRiskService")
public class InventoryRiskServiceImpl implements InventoryRiskService {

    private static final BigDecimal NO_SALES_AVAILABLE_DAYS = new BigDecimal("999.00");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SalesForecastService salesForecastService;

    @Override
    public List<InventoryRiskItemView> listBatchInventoryRisks(String enterpriseAccount) {
        List<Map<String, Object>> latestInventoryRows = deduplicateInventoryRows(queryLatestInventoryRows(enterpriseAccount));
        Map<String, ForecastItemView> forecastMap = buildForecastMap(salesForecastService.listForecasts(enterpriseAccount));
        List<InventoryRiskItemView> result = new ArrayList<InventoryRiskItemView>();

        for (Map<String, Object> row : latestInventoryRows) {
            String productName = stringValue(row.get("productName"));
            String category = stringValue(row.get("category"));
            String account = stringValue(row.get("enterpriseAccount"));
            BigDecimal currentStock = safeDecimal(row.get("currentStock"));
            BigDecimal warningStock = safeDecimal(row.get("warningStock"));
            ForecastItemView forecast = forecastMap.get(buildProductKey(account, productName));
            BigDecimal dailyAverage = forecast == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : safeDecimal(forecast.getDailyAverage());
            Integer forecast7Days = forecast == null ? Integer.valueOf(0) : Integer.valueOf(safeInt(forecast.getForecast7Days()));
            BigDecimal availableDays = calculateAvailableDays(currentStock, dailyAverage);
            String riskLevel = resolveRiskLevel(currentStock, warningStock, availableDays);
            BigDecimal riskScore = resolveRiskScore(riskLevel, currentStock, warningStock, availableDays);

            InventoryRiskItemView item = new InventoryRiskItemView();
            item.setBatchCode(stringValue(row.get("batchCode")));
            item.setProductName(productName);
            item.setCategory(category);
            item.setEnterpriseAccount(account);
            item.setCurrentStock(currentStock);
            item.setWarningStock(warningStock);
            item.setDailyAverage(dailyAverage);
            item.setForecast7Days(forecast7Days);
            item.setAvailableDays(availableDays);
            item.setRiskLevel(riskLevel);
            item.setRiskScore(riskScore);
            item.setWarningReason(buildWarningReason(riskLevel, availableDays, currentStock, warningStock));
            item.setBatchCount(1);
            result.add(item);
        }

        sortByRisk(result);
        return result;
    }

    @Override
    public List<InventoryRiskItemView> listInventoryRisks(String enterpriseAccount) {
        Map<String, InventoryRiskItemView> aggregatedMap = new LinkedHashMap<String, InventoryRiskItemView>();

        for (InventoryRiskItemView batchItem : listBatchInventoryRisks(enterpriseAccount)) {
            String productKey = buildProductKey(batchItem.getEnterpriseAccount(), batchItem.getProductName());
            InventoryRiskItemView item = aggregatedMap.get(productKey);
            if (item == null) {
                item = new InventoryRiskItemView();
                item.setProductName(batchItem.getProductName());
                item.setCategory(batchItem.getCategory());
                item.setEnterpriseAccount(batchItem.getEnterpriseAccount());
                item.setCurrentStock(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                item.setWarningStock(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                item.setDailyAverage(safeDecimal(batchItem.getDailyAverage()));
                item.setForecast7Days(safeInt(batchItem.getForecast7Days()));
                item.setAvailableDays(NO_SALES_AVAILABLE_DAYS);
                item.setRiskLevel("safe");
                item.setRiskScore(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                item.setWarningReason("");
                item.setBatchCount(0);
                aggregatedMap.put(productKey, item);
            }

            item.setCategory(firstNonBlank(item.getCategory(), batchItem.getCategory()));
            item.setCurrentStock(item.getCurrentStock().add(safeDecimal(batchItem.getCurrentStock())));
            item.setWarningStock(item.getWarningStock().add(safeDecimal(batchItem.getWarningStock())));
            item.setDailyAverage(safeDecimal(batchItem.getDailyAverage()));
            item.setForecast7Days(safeInt(batchItem.getForecast7Days()));
            item.setBatchCount(item.getBatchCount() + safeInt(batchItem.getBatchCount()));

            if (compareRiskLevel(batchItem.getRiskLevel(), item.getRiskLevel()) > 0) {
                item.setRiskLevel(batchItem.getRiskLevel());
                item.setRiskScore(safeDecimal(batchItem.getRiskScore()));
                item.setWarningReason(batchItem.getWarningReason());
            } else if (safeDecimal(batchItem.getRiskScore()).compareTo(safeDecimal(item.getRiskScore())) > 0) {
                item.setRiskScore(safeDecimal(batchItem.getRiskScore()));
            }

            if (safeDecimal(batchItem.getAvailableDays()).compareTo(safeDecimal(item.getAvailableDays())) < 0) {
                item.setAvailableDays(safeDecimal(batchItem.getAvailableDays()));
            }
        }

        List<InventoryRiskItemView> result = new ArrayList<InventoryRiskItemView>(aggregatedMap.values());
        sortByRisk(result);
        return result;
    }

    private List<Map<String, Object>> queryLatestInventoryRows(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select i.batchcode as batchCode, i.productname as productName, "
                + "coalesce(s.shangpinfenlei, '未分类') as category, "
                + "i.enterpriseaccount as enterpriseAccount, "
                + "coalesce(i.currentstock, 0) as currentStock, "
                + "coalesce(i.warningstock, 0) as warningStock "
                + "from inventoryrecord i "
                + "inner join ("
                + "select batchcode, max(addtime) as maxAddTime from inventoryrecord group by batchcode"
                + ") latest on i.batchcode = latest.batchcode and i.addtime = latest.maxAddTime "
                + "left join ("
                + "select zhanghao, shangpinmingcheng, max(shangpinfenlei) as shangpinfenlei "
                + "from shangpinxinxi group by zhanghao, shangpinmingcheng"
                + ") s on s.shangpinmingcheng = i.productname and s.zhanghao = i.enterpriseaccount "
                + "where 1 = 1"
        );
        List<Object> args = new ArrayList<Object>();
        if (enterpriseAccount != null) {
            sql.append(" and i.enterpriseaccount = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" order by i.currentstock asc, i.batchcode asc");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    private List<Map<String, Object>> deduplicateInventoryRows(List<Map<String, Object>> inventoryRows) {
        Map<String, Map<String, Object>> uniqueRows = new LinkedHashMap<String, Map<String, Object>>();
        for (Map<String, Object> row : inventoryRows) {
            String uniqueKey = buildBatchKey(
                stringValue(row.get("enterpriseAccount")),
                stringValue(row.get("productName")),
                stringValue(row.get("batchCode"))
            );
            if (!uniqueRows.containsKey(uniqueKey)) {
                uniqueRows.put(uniqueKey, row);
            }
        }
        return new ArrayList<Map<String, Object>>(uniqueRows.values());
    }

    private Map<String, ForecastItemView> buildForecastMap(List<ForecastItemView> forecasts) {
        Map<String, ForecastItemView> map = new LinkedHashMap<String, ForecastItemView>();
        for (ForecastItemView item : forecasts) {
            map.put(buildProductKey(item.getEnterpriseAccount(), item.getProductName()), item);
        }
        return map;
    }

    private void sortByRisk(List<InventoryRiskItemView> items) {
        Collections.sort(items, (left, right) -> {
            int compare = compareRiskLevel(right.getRiskLevel(), left.getRiskLevel());
            if (compare != 0) {
                return compare;
            }
            return safeDecimal(right.getRiskScore()).compareTo(safeDecimal(left.getRiskScore()));
        });
    }

    private BigDecimal calculateAvailableDays(BigDecimal currentStock, BigDecimal dailyAverage) {
        if (dailyAverage == null || dailyAverage.compareTo(BigDecimal.ZERO) <= 0) {
            return NO_SALES_AVAILABLE_DAYS;
        }
        return currentStock.divide(dailyAverage, 2, RoundingMode.HALF_UP);
    }

    private String resolveRiskLevel(BigDecimal currentStock, BigDecimal warningStock, BigDecimal availableDays) {
        if (currentStock.compareTo(BigDecimal.ZERO) <= 0) {
            return "urgent";
        }
        if (currentStock.compareTo(warningStock) <= 0 || availableDays.compareTo(new BigDecimal("2.00")) <= 0) {
            return "urgent";
        }
        if (availableDays.compareTo(new BigDecimal("4.00")) <= 0) {
            return "warning";
        }
        if (availableDays.compareTo(new BigDecimal("7.00")) <= 0
            || currentStock.compareTo(warningStock.multiply(new BigDecimal("1.50"))) <= 0) {
            return "attention";
        }
        return "safe";
    }

    private BigDecimal resolveRiskScore(String riskLevel, BigDecimal currentStock, BigDecimal warningStock, BigDecimal availableDays) {
        BigDecimal baseScore;
        if ("urgent".equals(riskLevel)) {
            baseScore = new BigDecimal("90");
        } else if ("warning".equals(riskLevel)) {
            baseScore = new BigDecimal("70");
        } else if ("attention".equals(riskLevel)) {
            baseScore = new BigDecimal("45");
        } else {
            baseScore = new BigDecimal("10");
        }
        if (warningStock.compareTo(BigDecimal.ZERO) > 0 && currentStock.compareTo(warningStock) < 0) {
            baseScore = baseScore.add(warningStock.subtract(currentStock));
        }
        if (availableDays.compareTo(NO_SALES_AVAILABLE_DAYS) < 0) {
            baseScore = baseScore.add(new BigDecimal("10").subtract(availableDays.min(new BigDecimal("10"))));
        }
        return baseScore.setScale(2, RoundingMode.HALF_UP);
    }

    private String buildWarningReason(String riskLevel, BigDecimal availableDays, BigDecimal currentStock, BigDecimal warningStock) {
        if ("urgent".equals(riskLevel)) {
            return "当前库存已经低于预警线，建议立即安排补货。";
        }
        if ("warning".equals(riskLevel)) {
            return "按当前销量估算，可售天数不足 4 天。";
        }
        if ("attention".equals(riskLevel)) {
            return "库存处于关注区间，需要持续跟踪销量和补货节奏。";
        }
        if (availableDays.compareTo(NO_SALES_AVAILABLE_DAYS) >= 0) {
            return "近期暂无销量消耗，库存风险较低。";
        }
        return String.format(
            Locale.ROOT,
            "当前库存 %.2f，高于预警值 %.2f，可售天数约 %.2f 天。",
            currentStock,
            warningStock,
            availableDays
        );
    }

    private int compareRiskLevel(String left, String right) {
        return resolveSeverity(left) - resolveSeverity(right);
    }

    private int resolveSeverity(String riskLevel) {
        if ("urgent".equals(riskLevel)) {
            return 4;
        }
        if ("warning".equals(riskLevel)) {
            return 3;
        }
        if ("attention".equals(riskLevel)) {
            return 2;
        }
        return 1;
    }

    private String buildBatchKey(String enterpriseAccount, String productName, String batchCode) {
        return buildProductKey(enterpriseAccount, productName) + "::" + normalize(batchCode);
    }

    private String buildProductKey(String enterpriseAccount, String productName) {
        return normalize(enterpriseAccount) + "::" + normalize(productName);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String firstNonBlank(String currentValue, String fallbackValue) {
        if (currentValue != null && currentValue.trim().length() > 0) {
            return currentValue;
        }
        return fallbackValue;
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private BigDecimal safeDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }
        if (value == null || String.valueOf(value).trim().isEmpty()) {
            return new BigDecimal("0.00");
        }
        return new BigDecimal(String.valueOf(value)).setScale(2, RoundingMode.HALF_UP);
    }

    private int safeInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
