package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.entity.view.DecisionDashboardSnapshotView;
import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;

public class DecisionTaskRuleEngineTest {

    private final DecisionTaskRuleEngine engine = new DecisionTaskRuleEngine();

    @Test
    public void buildTasksCreatesOperationalTasksForTriggeredSignals() {
        DecisionDashboardSnapshotView snapshot = new DecisionDashboardSnapshotView();
        snapshot.setInventoryWarningCount(2);
        snapshot.setSlowMovingProductCount(3);
        snapshot.setCurrentMonthSales(160);
        snapshot.setLastMonthSales(100);
        snapshot.setCurrentMonthRevenue(new BigDecimal("32000.00"));
        snapshot.setLastMonthRevenue(new BigDecimal("26000.00"));
        snapshot.setAbnormalEnterpriseCount(1);
        snapshot.setTopCategoryName("高山绿茶");
        snapshot.setTopForecastProductName("明前龙井");

        InventoryRiskItemView inventoryRisk = new InventoryRiskItemView();
        inventoryRisk.setBatchCode("BATCH-001");
        inventoryRisk.setProductName("明前龙井");
        inventoryRisk.setCategory("绿茶");
        inventoryRisk.setRiskLevel("urgent");
        inventoryRisk.setCurrentStock(new BigDecimal("8"));
        inventoryRisk.setWarningStock(new BigDecimal("20"));
        inventoryRisk.setAvailableDays(new BigDecimal("1.2"));
        inventoryRisk.setWarningReason("库存低于预警线");

        RestockPlanItemView restockPlan = new RestockPlanItemView();
        restockPlan.setProductName("明前龙井");
        restockPlan.setCategory("绿茶");
        restockPlan.setPriority("urgent");
        restockPlan.setSuggestedRestock(new BigDecimal("60"));
        restockPlan.setForecast7Days(72);
        restockPlan.setCurrentStock(new BigDecimal("8"));
        restockPlan.setSafetyStock(new BigDecimal("30"));
        restockPlan.setReason("预计 7 天销量高于当前库存");

        ForecastItemView forecast = new ForecastItemView();
        forecast.setProductName("明前龙井");
        forecast.setForecast7Days(72);
        forecast.setTrend("up");

        List<DecisionTaskDraft> tasks = engine.buildTasks(
            "merchantA",
            snapshot,
            Arrays.asList(inventoryRisk),
            Arrays.asList(restockPlan),
            Arrays.asList(forecast)
        );

        List<String> taskTypes = tasks.stream().map(DecisionTaskDraft::getTaskType).collect(Collectors.toList());

        assertEquals(5, tasks.size());
        assertTrue(taskTypes.contains("inventory_alert"));
        assertTrue(taskTypes.contains("restock_followup"));
        assertTrue(taskTypes.contains("slow_moving"));
        assertTrue(taskTypes.contains("growth_followup"));
        assertTrue(taskTypes.contains("enterprise_abnormal"));
    }
}
