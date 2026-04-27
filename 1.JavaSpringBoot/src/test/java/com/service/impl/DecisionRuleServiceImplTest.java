package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.entity.view.DecisionAdviceItemView;
import com.entity.view.DecisionDashboardSnapshotView;

public class DecisionRuleServiceImplTest {

    private final DecisionRuleServiceImpl service = new DecisionRuleServiceImpl();

    @Test
    public void generateAdviceIncludesInventorySlowMovingAndGrowthSignals() {
        DecisionDashboardSnapshotView snapshot = new DecisionDashboardSnapshotView();
        snapshot.setInventoryWarningCount(3);
        snapshot.setSlowMovingProductCount(2);
        snapshot.setCurrentMonthSales(128);
        snapshot.setLastMonthSales(82);
        snapshot.setCurrentMonthRevenue(new BigDecimal("26800.00"));
        snapshot.setLastMonthRevenue(new BigDecimal("19800.00"));
        snapshot.setTopCategoryName("高山绿茶");
        snapshot.setAbnormalEnterpriseCount(1);

        List<DecisionAdviceItemView> adviceList = service.generateAdvice(snapshot);
        List<String> titles = adviceList.stream().map(DecisionAdviceItemView::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("库存预警"));
        assertTrue(titles.contains("滞销提醒"));
        assertTrue(titles.contains("主推建议"));
        assertTrue(titles.contains("经营关注"));
    }

    @Test
    public void generateAdviceReturnsFallbackWhenNoRuleTriggered() {
        DecisionDashboardSnapshotView snapshot = new DecisionDashboardSnapshotView();
        snapshot.setInventoryWarningCount(0);
        snapshot.setSlowMovingProductCount(0);
        snapshot.setCurrentMonthSales(20);
        snapshot.setLastMonthSales(20);
        snapshot.setCurrentMonthRevenue(new BigDecimal("6000.00"));
        snapshot.setLastMonthRevenue(new BigDecimal("6000.00"));
        snapshot.setTopCategoryName("高山红茶");
        snapshot.setAbnormalEnterpriseCount(0);

        List<DecisionAdviceItemView> adviceList = service.generateAdvice(snapshot);

        assertEquals(1, adviceList.size());
        assertEquals("经营平稳", adviceList.get(0).getTitle());
    }
}
