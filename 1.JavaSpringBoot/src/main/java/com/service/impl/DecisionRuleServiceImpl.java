package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.entity.view.DecisionAdviceItemView;
import com.entity.view.DecisionDashboardSnapshotView;
import com.service.DecisionRuleService;

@Service("decisionRuleService")
public class DecisionRuleServiceImpl implements DecisionRuleService {

    @Override
    public List<DecisionAdviceItemView> generateAdvice(DecisionDashboardSnapshotView snapshot) {
        List<DecisionAdviceItemView> adviceList = new ArrayList<DecisionAdviceItemView>();
        if (snapshot == null) {
            adviceList.add(createAdvice("overview", "info", "经营平稳", "当前暂无明显风险，可继续保持现有产供销节奏。", "持续监测"));
            return adviceList;
        }

        int inventoryWarningCount = safeInt(snapshot.getInventoryWarningCount());
        int slowMovingProductCount = safeInt(snapshot.getSlowMovingProductCount());
        int currentMonthSales = safeInt(snapshot.getCurrentMonthSales());
        int lastMonthSales = safeInt(snapshot.getLastMonthSales());
        int abnormalEnterpriseCount = safeInt(snapshot.getAbnormalEnterpriseCount());

        BigDecimal currentMonthRevenue = safeDecimal(snapshot.getCurrentMonthRevenue());
        BigDecimal lastMonthRevenue = safeDecimal(snapshot.getLastMonthRevenue());
        String topCategoryName = snapshot.getTopCategoryName() == null || snapshot.getTopCategoryName().trim().isEmpty()
            ? "高山茶"
            : snapshot.getTopCategoryName().trim();

        if (inventoryWarningCount > 0) {
            adviceList.add(createAdvice(
                "inventory",
                "warning",
                "库存预警",
                String.format("当前共有 %d 个批次库存低于预警线，建议优先安排补货或调整销售节奏。", inventoryWarningCount),
                "查看库存"
            ));
        }

        if (slowMovingProductCount > 0) {
            adviceList.add(createAdvice(
                "sales",
                "warning",
                "滞销提醒",
                String.format("近 30 天共有 %d 个茶品销售表现偏弱，建议结合促销活动或渠道调整进行去库存。", slowMovingProductCount),
                "查看滞销"
            ));
        }

        if (currentMonthSales > lastMonthSales && growthRate(lastMonthSales, currentMonthSales) >= 0.15D) {
            adviceList.add(createAdvice(
                "growth",
                "success",
                "主推建议",
                String.format("%s 本月销售增速明显，建议加大首页曝光与重点渠道投放。", topCategoryName),
                "查看趋势"
            ));
        } else if (currentMonthRevenue.compareTo(lastMonthRevenue) < 0
            && revenueDropRate(lastMonthRevenue, currentMonthRevenue) >= 0.10D) {
            adviceList.add(createAdvice(
                "growth",
                "danger",
                "销售回落",
                "本月销售额较上月出现回落，建议复盘价格策略、渠道结构与库存周转节奏。",
                "查看经营"
            ));
        }

        if (abnormalEnterpriseCount > 0) {
            adviceList.add(createAdvice(
                "enterprise",
                "info",
                "经营关注",
                String.format("发现 %d 家企业订单波动较大，建议重点核查库存准备、履约能力与渠道变化。", abnormalEnterpriseCount),
                "查看企业"
            ));
        }

        if (adviceList.isEmpty()) {
            adviceList.add(createAdvice(
                "overview",
                "info",
                "经营平稳",
                "当前产供销数据整体平稳，建议继续关注库存结构与月度销量变化。",
                "持续监测"
            ));
        }

        return adviceList;
    }

    private DecisionAdviceItemView createAdvice(String type, String level, String title, String content, String actionLabel) {
        DecisionAdviceItemView item = new DecisionAdviceItemView();
        item.setType(type);
        item.setLevel(level);
        item.setTitle(title);
        item.setContent(content);
        item.setActionLabel(actionLabel);
        return item;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value.intValue();
    }

    private BigDecimal safeDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private double growthRate(int base, int current) {
        if (base <= 0) {
            return current > 0 ? 1D : 0D;
        }
        return (current - base) / (double) base;
    }

    private double revenueDropRate(BigDecimal base, BigDecimal current) {
        if (base.compareTo(BigDecimal.ZERO) <= 0) {
            return 0D;
        }
        return base.subtract(current).divide(base, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
