package com.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.entity.view.DecisionDashboardSnapshotView;
import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;

@Component
public class DecisionTaskRuleEngine {

    public List<DecisionTaskDraft> buildTasks(String merchantAccount,
                                              DecisionDashboardSnapshotView snapshot,
                                              List<InventoryRiskItemView> inventoryRiskList,
                                              List<RestockPlanItemView> restockPlanList,
                                              List<ForecastItemView> forecastList) {
        List<DecisionTaskDraft> tasks = new ArrayList<DecisionTaskDraft>();
        if (snapshot == null) {
            return tasks;
        }

        for (InventoryRiskItemView item : inventoryRiskList) {
            if (!"urgent".equals(item.getRiskLevel()) && !"warning".equals(item.getRiskLevel())) {
                continue;
            }
            Map<String, Object> evidence = new LinkedHashMap<String, Object>();
            evidence.put("productName", item.getProductName());
            evidence.put("batchCode", item.getBatchCode());
            evidence.put("riskLevel", item.getRiskLevel());
            evidence.put("currentStock", item.getCurrentStock());
            evidence.put("warningStock", item.getWarningStock());
            evidence.put("availableDays", item.getAvailableDays());
            evidence.put("warningReason", item.getWarningReason());
            tasks.add(buildDraft(
                merchantAccount,
                "inventory_alert",
                "inventory_risk",
                safeKey(merchantAccount, item.getBatchCode(), item.getProductName()),
                "库存预警任务",
                item.getProductName() + " 库存已达到风险区间，建议优先补货或调整销售节奏。",
                "核查当前库存批次并确认补货计划",
                normalizePriority(item.getRiskLevel()),
                evidence,
                1
            ));
        }

        for (RestockPlanItemView item : restockPlanList) {
            if (item.getSuggestedRestock() == null || item.getSuggestedRestock().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            Map<String, Object> evidence = new LinkedHashMap<String, Object>();
            evidence.put("productName", item.getProductName());
            evidence.put("suggestedRestock", item.getSuggestedRestock());
            evidence.put("forecast7Days", item.getForecast7Days());
            evidence.put("currentStock", item.getCurrentStock());
            evidence.put("safetyStock", item.getSafetyStock());
            evidence.put("reason", item.getReason());
            tasks.add(buildDraft(
                merchantAccount,
                "restock_followup",
                "restock_plan",
                safeKey(merchantAccount, item.getProductName()),
                "补货跟进任务",
                item.getProductName() + " 预计未来 7 天存在缺货风险，建议尽快确认补货执行。",
                "确认补货数量并推进入库安排",
                normalizePriority(item.getPriority()),
                evidence,
                2
            ));
        }

        if (safeInt(snapshot.getSlowMovingProductCount()) > 0) {
            Map<String, Object> evidence = new LinkedHashMap<String, Object>();
            evidence.put("slowMovingProductCount", snapshot.getSlowMovingProductCount());
            evidence.put("topForecastProductName", snapshot.getTopForecastProductName());
            tasks.add(buildDraft(
                merchantAccount,
                "slow_moving",
                "slow_moving",
                safeKey(merchantAccount, "slow-moving"),
                "滞销处理任务",
                "近 30 天存在滞销商品，建议复盘促销、定价和库存结构。",
                "筛查滞销商品并制定去库存动作",
                "medium",
                evidence,
                3
            ));
        }

        if (growthRate(snapshot.getLastMonthSales(), snapshot.getCurrentMonthSales()) >= 0.15D) {
            ForecastItemView topForecast = forecastList.isEmpty() ? null : forecastList.get(0);
            Map<String, Object> evidence = new LinkedHashMap<String, Object>();
            evidence.put("currentMonthSales", snapshot.getCurrentMonthSales());
            evidence.put("lastMonthSales", snapshot.getLastMonthSales());
            evidence.put("topCategoryName", snapshot.getTopCategoryName());
            evidence.put("topForecastProductName", topForecast == null ? snapshot.getTopForecastProductName() : topForecast.getProductName());
            tasks.add(buildDraft(
                merchantAccount,
                "growth_followup",
                "growth",
                safeKey(merchantAccount, "growth"),
                "增长跟进任务",
                "本月销量增速明显，建议抓住增长窗口加大主推资源和渠道曝光。",
                "确认增长商品的资源倾斜和活动计划",
                "medium",
                evidence,
                3
            ));
        }

        if (safeInt(snapshot.getAbnormalEnterpriseCount()) > 0) {
            Map<String, Object> evidence = new LinkedHashMap<String, Object>();
            evidence.put("abnormalEnterpriseCount", snapshot.getAbnormalEnterpriseCount());
            evidence.put("currentMonthRevenue", snapshot.getCurrentMonthRevenue());
            evidence.put("lastMonthRevenue", snapshot.getLastMonthRevenue());
            tasks.add(buildDraft(
                merchantAccount,
                "enterprise_abnormal",
                "enterprise_abnormal",
                safeKey(merchantAccount, "enterprise-abnormal"),
                "异常关注任务",
                "检测到经营波动异常，建议尽快核查订单、库存和履约情况。",
                "排查近阶段订单波动与库存准备情况",
                "high",
                evidence,
                2
            ));
        }

        return tasks;
    }

    private DecisionTaskDraft buildDraft(String merchantAccount,
                                         String taskType,
                                         String sourceType,
                                         String sourceKey,
                                         String title,
                                         String summary,
                                         String actionSuggestion,
                                         String priority,
                                         Map<String, Object> evidence,
                                         int dueDays) {
        DecisionTaskDraft draft = new DecisionTaskDraft();
        draft.setMerchantAccount(merchantAccount);
        draft.setTaskType(taskType);
        draft.setSourceType(sourceType);
        draft.setSourceKey(sourceKey);
        draft.setTitle(title);
        draft.setSummary(summary);
        draft.setActionSuggestion(actionSuggestion);
        draft.setPriority(priority);
        draft.setEvidenceSnapshot(JSON.toJSONString(evidence));
        draft.setDueAt(addDays(dueDays));
        return draft;
    }

    private java.util.Date addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private String safeKey(String merchantAccount, String... parts) {
        StringBuilder builder = new StringBuilder(merchantAccount == null ? "platform" : merchantAccount);
        for (String part : parts) {
            builder.append(':').append(part == null ? "unknown" : part.trim());
        }
        return builder.toString();
    }

    private String normalizePriority(String priority) {
        if ("urgent".equals(priority) || "high".equals(priority) || "medium".equals(priority) || "low".equals(priority)) {
            return priority;
        }
        if ("warning".equals(priority)) {
            return "high";
        }
        return "medium";
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value.intValue();
    }

    private double growthRate(Integer base, Integer current) {
        int safeBase = base == null ? 0 : base.intValue();
        int safeCurrent = current == null ? 0 : current.intValue();
        if (safeBase <= 0) {
            return safeCurrent > 0 ? 1D : 0D;
        }
        return (safeCurrent - safeBase) / (double) safeBase;
    }
}
