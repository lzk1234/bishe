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
import org.springframework.stereotype.Service;

import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;
import com.service.InventoryRiskService;
import com.service.RestockSuggestionService;
import com.service.SalesForecastService;

@Service("restockSuggestionService")
public class RestockSuggestionServiceImpl implements RestockSuggestionService {

    @Autowired
    private SalesForecastService salesForecastService;

    @Autowired
    private InventoryRiskService inventoryRiskService;

    @Override
    public List<RestockPlanItemView> listRestockPlans(String enterpriseAccount) {
        Map<String, ForecastItemView> forecastMap = buildForecastMap(salesForecastService.listForecasts(enterpriseAccount));
        Map<String, RestockAggregate> aggregateMap = new LinkedHashMap<String, RestockAggregate>();

        for (InventoryRiskItemView batchRisk : inventoryRiskService.listBatchInventoryRisks(enterpriseAccount)) {
            ForecastItemView forecast = forecastMap.get(buildProductKey(batchRisk.getEnterpriseAccount(), batchRisk.getProductName()));
            BigDecimal dailyAverage = forecast == null ? safeDecimal(batchRisk.getDailyAverage()) : safeDecimal(forecast.getDailyAverage());
            int forecast7Days = forecast == null ? safeInt(batchRisk.getForecast7Days()) : safeInt(forecast.getForecast7Days());
            BigDecimal currentStock = safeDecimal(batchRisk.getCurrentStock());
            BigDecimal warningStock = safeDecimal(batchRisk.getWarningStock());
            BigDecimal safetyStock = warningStock.max(roundUpToInteger(dailyAverage.multiply(new BigDecimal("3"))));
            BigDecimal suggestedRestock = calculateSuggestedRestock(forecast7Days, safetyStock, currentStock);

            mergePlan(
                aggregateMap,
                batchRisk.getEnterpriseAccount(),
                batchRisk.getProductName(),
                batchRisk.getCategory(),
                currentStock,
                safetyStock,
                forecast7Days,
                suggestedRestock,
                batchRisk.getRiskLevel()
            );
        }

        for (ForecastItemView forecast : forecastMap.values()) {
            String productKey = buildProductKey(forecast.getEnterpriseAccount(), forecast.getProductName());
            if (aggregateMap.containsKey(productKey)) {
                continue;
            }

            BigDecimal dailyAverage = safeDecimal(forecast.getDailyAverage());
            BigDecimal safetyStock = roundUpToInteger(dailyAverage.multiply(new BigDecimal("3")));
            BigDecimal suggestedRestock = calculateSuggestedRestock(safeInt(forecast.getForecast7Days()), safetyStock, BigDecimal.ZERO);
            mergePlan(
                aggregateMap,
                forecast.getEnterpriseAccount(),
                forecast.getProductName(),
                forecast.getCategory(),
                BigDecimal.ZERO,
                safetyStock,
                safeInt(forecast.getForecast7Days()),
                suggestedRestock,
                "urgent"
            );
        }

        List<RestockPlanItemView> result = new ArrayList<RestockPlanItemView>();
        for (RestockAggregate aggregate : aggregateMap.values()) {
            aggregate.item.setReason(buildReason(aggregate.item, aggregate.riskLevel));
            result.add(aggregate.item);
        }

        Collections.sort(result, (left, right) -> {
            int compare = resolvePriorityWeight(right.getPriority()) - resolvePriorityWeight(left.getPriority());
            if (compare != 0) {
                return compare;
            }
            return right.getSuggestedRestock().compareTo(left.getSuggestedRestock());
        });
        return result;
    }

    private void mergePlan(
        Map<String, RestockAggregate> aggregateMap,
        String enterpriseAccount,
        String productName,
        String category,
        BigDecimal currentStock,
        BigDecimal safetyStock,
        int forecast7Days,
        BigDecimal suggestedRestock,
        String riskLevel
    ) {
        String productKey = buildProductKey(enterpriseAccount, productName);
        RestockAggregate aggregate = aggregateMap.get(productKey);
        if (aggregate == null) {
            aggregate = new RestockAggregate();
            aggregate.item.setEnterpriseAccount(enterpriseAccount);
            aggregate.item.setProductName(productName);
            aggregate.item.setCategory(category);
            aggregate.item.setCurrentStock(BigDecimal.ZERO);
            aggregate.item.setSafetyStock(BigDecimal.ZERO);
            aggregate.item.setForecast7Days(forecast7Days);
            aggregate.item.setSuggestedRestock(BigDecimal.ZERO);
            aggregate.item.setSuggestedDays(0);
            aggregate.item.setPriority("none");
            aggregate.riskLevel = "safe";
            aggregateMap.put(productKey, aggregate);
        }

        aggregate.item.setCategory(firstNonBlank(aggregate.item.getCategory(), category));
        aggregate.item.setCurrentStock(normalizeDisplayNumber(aggregate.item.getCurrentStock().add(currentStock)));
        aggregate.item.setSafetyStock(normalizeDisplayNumber(aggregate.item.getSafetyStock().add(safetyStock)));
        aggregate.item.setForecast7Days(Math.max(safeInt(aggregate.item.getForecast7Days()), forecast7Days));
        aggregate.item.setSuggestedRestock(normalizeDisplayNumber(aggregate.item.getSuggestedRestock().add(suggestedRestock)));

        String priority = resolvePriority(riskLevel, suggestedRestock);
        int priorityWeight = resolvePriorityWeight(priority);
        int suggestedDays = resolveSuggestedDays(riskLevel, suggestedRestock);
        if (priorityWeight > aggregate.priorityWeight) {
            aggregate.priorityWeight = priorityWeight;
            aggregate.item.setPriority(priority);
            aggregate.item.setSuggestedDays(suggestedDays);
            aggregate.riskLevel = riskLevel;
        } else if (priorityWeight == aggregate.priorityWeight && suggestedRestock.compareTo(BigDecimal.ZERO) > 0) {
            aggregate.item.setSuggestedDays(Math.min(aggregate.item.getSuggestedDays(), suggestedDays));
        }
    }

    private Map<String, ForecastItemView> buildForecastMap(List<ForecastItemView> forecastList) {
        Map<String, ForecastItemView> forecastMap = new LinkedHashMap<String, ForecastItemView>();
        for (ForecastItemView item : forecastList) {
            forecastMap.put(buildProductKey(item.getEnterpriseAccount(), item.getProductName()), item);
        }
        return forecastMap;
    }

    private BigDecimal calculateSuggestedRestock(int forecast7Days, BigDecimal safetyStock, BigDecimal currentStock) {
        BigDecimal requiredStock = BigDecimal.valueOf(forecast7Days).add(safetyStock);
        BigDecimal suggestedRestock = requiredStock.subtract(currentStock);
        if (suggestedRestock.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return suggestedRestock.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal roundUpToInteger(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(0, RoundingMode.UNNECESSARY);
        }
        return value.setScale(0, RoundingMode.CEILING);
    }

    private int resolveSuggestedDays(String riskLevel, BigDecimal suggestedRestock) {
        if (suggestedRestock.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        if ("urgent".equals(riskLevel)) {
            return 1;
        }
        if ("warning".equals(riskLevel)) {
            return 3;
        }
        if ("attention".equals(riskLevel)) {
            return 5;
        }
        return 7;
    }

    private String resolvePriority(String riskLevel, BigDecimal suggestedRestock) {
        if (suggestedRestock.compareTo(BigDecimal.ZERO) <= 0) {
            return "none";
        }
        if ("urgent".equals(riskLevel)) {
            return "urgent";
        }
        if ("warning".equals(riskLevel)) {
            return "high";
        }
        if ("attention".equals(riskLevel)) {
            return "medium";
        }
        return "low";
    }

    private int resolvePriorityWeight(String priority) {
        if ("urgent".equals(priority)) {
            return 5;
        }
        if ("high".equals(priority)) {
            return 4;
        }
        if ("medium".equals(priority)) {
            return 3;
        }
        if ("low".equals(priority)) {
            return 2;
        }
        return 1;
    }

    private String buildReason(RestockPlanItemView item, String riskLevel) {
        if (item.getSuggestedRestock().compareTo(BigDecimal.ZERO) <= 0) {
            return "现有库存可覆盖未来 7 天需求，暂不需要补货。";
        }
        return String.format(
            Locale.ROOT,
            "未来 7 天预计销量 %d，安全库存 %s，当前风险等级 %s，建议在 %d 天内补货。",
            item.getForecast7Days(),
            item.getSafetyStock().toPlainString(),
            riskLevel,
            item.getSuggestedDays()
        );
    }

    private BigDecimal normalizeDisplayNumber(BigDecimal value) {
        BigDecimal normalized = value.stripTrailingZeros();
        if (normalized.scale() < 0) {
            return normalized.setScale(0, RoundingMode.UNNECESSARY);
        }
        return normalized;
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

    private static class RestockAggregate {
        private final RestockPlanItemView item = new RestockPlanItemView();
        private String riskLevel;
        private int priorityWeight;
    }
}
