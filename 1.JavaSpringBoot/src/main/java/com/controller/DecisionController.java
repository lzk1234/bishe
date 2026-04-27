package com.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.DecisionTaskEntity;
import com.entity.view.DecisionDashboardSnapshotView;
import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;
import com.service.DecisionRuleService;
import com.service.DecisionTaskService;
import com.service.InventoryRiskService;
import com.service.LifecycleDecisionService;
import com.service.ProductionSalesPlanAnalysisService;
import com.service.RestockSuggestionService;
import com.service.SalesForecastService;
import com.service.WarehouseCoordinationService;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/decision")
public class DecisionController {

    private static final List<String> VALID_ORDER_STATUSES = Arrays.asList(
        "\u5df2\u652f\u4ed8",
        "\u5df2\u53d1\u8d27",
        "\u5df2\u5b8c\u6210"
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DecisionRuleService decisionRuleService;

    @Autowired
    private SalesForecastService salesForecastService;

    @Autowired
    private InventoryRiskService inventoryRiskService;

    @Autowired
    private RestockSuggestionService restockSuggestionService;

    @Autowired
    private DecisionTaskService decisionTaskService;

    @Autowired(required = false)
    private ProductionSalesPlanAnalysisService productionSalesPlanAnalysisService;

    @Autowired(required = false)
    private WarehouseCoordinationService warehouseCoordinationService;

    @Autowired(required = false)
    private LifecycleDecisionService lifecycleDecisionService;

    @RequestMapping("/dashboard")
    public R dashboard(HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        String enterpriseAccount = getEnterpriseAccount(request);
        Map<String, Object> stats = buildStats(enterpriseAccount);
        List<Map<String, Object>> categoryShare = buildCategoryShare(enterpriseAccount);
        List<ForecastItemView> forecastList = salesForecastService.listForecasts(enterpriseAccount);
        List<InventoryRiskItemView> inventoryRiskList = inventoryRiskService.listInventoryRisks(enterpriseAccount);
        List<RestockPlanItemView> restockPlanList = restockSuggestionService.listRestockPlans(enterpriseAccount);
        Map<String, Object> forecastSummary = buildForecastSummary(forecastList);
        long restockPendingCount = countRestockPending(restockPlanList);
        long highRiskCount = countHighRisk(inventoryRiskList);
        stats.put("forecast7DaysTotal", safeLong(forecastSummary.get("totalForecast7Days")));
        stats.put("highRiskProductCount", highRiskCount);
        stats.put("restockPendingCount", restockPendingCount);
        DecisionDashboardSnapshotView snapshot = buildSnapshot(
            stats,
            categoryShare,
            enterpriseAccount,
            forecastSummary,
            highRiskCount,
            restockPendingCount
        );

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("stats", stats);
        data.put("salesTrend", buildSalesTrend(enterpriseAccount));
        data.put("categoryShare", categoryShare);
        data.put("topProducts", buildTopProducts(enterpriseAccount));
        data.put("topEnterprises", buildTopEnterprises(enterpriseAccount));
        data.put("lowStockList", buildLowStockList(enterpriseAccount));
        data.put("forecastList", forecastList);
        data.put("inventoryRiskList", inventoryRiskList);
        data.put("restockPlanList", restockPlanList);
        data.put("forecastSummary", forecastSummary);
        data.put("restockPendingCount", restockPendingCount);
        data.put("adviceList", decisionRuleService.generateAdvice(snapshot));
        data.put("taskStats", decisionTaskService.buildTaskStats(enterpriseAccount));
        data.put("highlightedTasks", decisionTaskService.listHighlightedTasks(enterpriseAccount, Integer.valueOf(6)));
        data.put("taskPage", decisionTaskService.queryTaskPage(defaultTaskParams(), enterpriseAccount));
        data.put("weeklyReview", decisionTaskService.buildWeeklyReview(enterpriseAccount));
        data.put("baseCapacitySummary", buildBaseCapacitySummary(enterpriseAccount));
        data.put("baseRegionStats", buildBaseRegionStats(enterpriseAccount));
        data.put("annualPlanSummary", buildAnnualPlanSummary(enterpriseAccount));
        data.put("annualPlanTrend", buildAnnualPlanTrend(enterpriseAccount));
        data.put("planGapList", buildPlanGapList(enterpriseAccount));
        data.put("warehouseInventorySummary", buildWarehouseInventorySummary(enterpriseAccount));
        data.put("transferSuggestionList", buildTransferSuggestionList(enterpriseAccount));
        data.put("crossWarehouseRiskCount", Long.valueOf(buildTransferSuggestionList(enterpriseAccount).size()));
        Map<String, Object> lifecycleStatus = buildLifecycleStatus(enterpriseAccount);
        data.put("lifecycleStageSummary", lifecycleStatus.get("lifecycleStageSummary"));
        data.put("bestSaleWindowList", lifecycleStatus.get("bestSaleWindowList"));
        data.put("lifecycleRiskList", lifecycleStatus.get("lifecycleRiskList"));
        return R.ok().put("data", data);
    }

    @RequestMapping("/advice")
    public R advice(HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        String enterpriseAccount = getEnterpriseAccount(request);
        Map<String, Object> stats = buildStats(enterpriseAccount);
        List<Map<String, Object>> categoryShare = buildCategoryShare(enterpriseAccount);
        Map<String, Object> forecastSummary = buildForecastSummary(salesForecastService.listForecasts(enterpriseAccount));
        DecisionDashboardSnapshotView snapshot = buildSnapshot(
            stats,
            categoryShare,
            enterpriseAccount,
            forecastSummary,
            countHighRisk(inventoryRiskService.listInventoryRisks(enterpriseAccount)),
            countRestockPending(restockSuggestionService.listRestockPlans(enterpriseAccount))
        );
        return R.ok().put("data", decisionRuleService.generateAdvice(snapshot));
    }

    @RequestMapping("/forecast")
    public R forecast(HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        return R.ok().put("data", salesForecastService.listForecasts(getEnterpriseAccount(request)));
    }

    @RequestMapping("/inventory-risk")
    public R inventoryRisk(HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        return R.ok().put("data", inventoryRiskService.listInventoryRisks(getEnterpriseAccount(request)));
    }

    @RequestMapping("/restock-plan")
    public R restockPlan(HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        return R.ok().put("data", restockSuggestionService.listRestockPlans(getEnterpriseAccount(request)));
    }

    @RequestMapping("/tasks")
    public R tasks(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        PageUtils page = decisionTaskService.queryTaskPage(params, getEnterpriseAccount(request));
        return R.ok().put("data", page);
    }

    @RequestMapping("/tasks/{id}")
    public R taskDetail(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        try {
            DecisionTaskEntity task = decisionTaskService.getTaskDetail(id, getEnterpriseAccount(request), isAdministrator(request));
            return R.ok().put("data", task);
        } catch (IllegalArgumentException e) {
            return R.error(400, e.getMessage());
        }
    }

    @RequestMapping("/tasks/{id}/start")
    public R startTask(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        try {
            DecisionTaskEntity task = decisionTaskService.startTask(id, getEnterpriseAccount(request), isAdministrator(request));
            return R.ok().put("data", task);
        } catch (IllegalArgumentException e) {
            return R.error(400, e.getMessage());
        }
    }

    @RequestMapping("/tasks/{id}/complete")
    public R completeTask(@PathVariable("id") Long id,
                          @RequestBody(required = false) Map<String, Object> body,
                          HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        try {
            String resultNote = body == null || body.get("resultNote") == null ? null : String.valueOf(body.get("resultNote"));
            DecisionTaskEntity task = decisionTaskService.completeTask(
                id,
                getEnterpriseAccount(request),
                isAdministrator(request),
                resultNote
            );
            return R.ok().put("data", task);
        } catch (IllegalArgumentException e) {
            return R.error(400, e.getMessage());
        }
    }

    @RequestMapping("/tasks/{id}/ignore")
    public R ignoreTask(@PathVariable("id") Long id,
                        @RequestBody(required = false) Map<String, Object> body,
                        HttpServletRequest request) {
        if (!canAccessDecisionCenter(request)) {
            return forbidden();
        }
        try {
            String ignoreReason = body == null || body.get("ignoreReason") == null ? null : String.valueOf(body.get("ignoreReason"));
            DecisionTaskEntity task = decisionTaskService.ignoreTask(
                id,
                getEnterpriseAccount(request),
                isAdministrator(request),
                ignoreReason
            );
            return R.ok().put("data", task);
        } catch (IllegalArgumentException e) {
            return R.error(400, e.getMessage());
        }
    }

    private Map<String, Object> buildStats(String enterpriseAccount) {
        Map<String, Object> stats = new LinkedHashMap<String, Object>();
        LocalDate today = LocalDate.now();
        Timestamp currentMonthStart = Timestamp.valueOf(today.withDayOfMonth(1).atStartOfDay());
        Timestamp nextMonthStart = Timestamp.valueOf(today.withDayOfMonth(1).plusMonths(1).atStartOfDay());
        Timestamp lastMonthStart = Timestamp.valueOf(today.withDayOfMonth(1).minusMonths(1).atStartOfDay());
        Timestamp slowMovingLimit = Timestamp.valueOf(today.minusDays(30).atStartOfDay());

        stats.put("enterpriseCount", countEnterprises(enterpriseAccount));
        stats.put("baseCount", queryLong(buildEnterpriseQuery("select count(*) from teabase", "enterpriseaccount", enterpriseAccount)));
        stats.put("productCount", queryLong(buildEnterpriseQuery("select count(*) from shangpinxinxi", "zhanghao", enterpriseAccount)));
        stats.put("orderCount", queryLong(buildEnterpriseQuery("select count(*) from orders", "zhanghao", enterpriseAccount)));
        stats.put("currentMonthSales", queryLong(buildOrderAggregateQuery(
            "select coalesce(sum(buynumber),0) from orders where",
            enterpriseAccount,
            currentMonthStart,
            nextMonthStart
        )));
        stats.put("currentMonthRevenue", queryDecimal(buildOrderRevenueQuery(enterpriseAccount, currentMonthStart, nextMonthStart)));
        stats.put("lastMonthSales", queryLong(buildOrderAggregateQuery(
            "select coalesce(sum(buynumber),0) from orders where",
            enterpriseAccount,
            lastMonthStart,
            currentMonthStart
        )));
        stats.put("lastMonthRevenue", queryDecimal(buildOrderRevenueQuery(enterpriseAccount, lastMonthStart, currentMonthStart)));
        stats.put("inventoryWarningCount", queryLong(buildInventoryWarningQuery(enterpriseAccount, true)));
        stats.put("slowMovingProductCount", queryLong(buildSlowMovingQuery(enterpriseAccount, slowMovingLimit)));
        stats.put("abnormalEnterpriseCount", countAbnormalEnterprises(enterpriseAccount, lastMonthStart, currentMonthStart, nextMonthStart));
        return stats;
    }

    private DecisionDashboardSnapshotView buildSnapshot(
        Map<String, Object> stats,
        List<Map<String, Object>> categoryShare,
        String enterpriseAccount,
        Map<String, Object> forecastSummary,
        long highRiskCount,
        long restockPendingCount
    ) {
        DecisionDashboardSnapshotView snapshot = new DecisionDashboardSnapshotView();
        snapshot.setInventoryWarningCount(((Long) stats.get("inventoryWarningCount")).intValue());
        snapshot.setSlowMovingProductCount(((Long) stats.get("slowMovingProductCount")).intValue());
        snapshot.setCurrentMonthSales(((Long) stats.get("currentMonthSales")).intValue());
        snapshot.setLastMonthSales(((Long) stats.get("lastMonthSales")).intValue());
        snapshot.setCurrentMonthRevenue((BigDecimal) stats.get("currentMonthRevenue"));
        snapshot.setLastMonthRevenue((BigDecimal) stats.get("lastMonthRevenue"));
        snapshot.setAbnormalEnterpriseCount(((Long) stats.get("abnormalEnterpriseCount")).intValue());
        snapshot.setTopCategoryName(categoryShare.isEmpty()
            ? (enterpriseAccount == null ? "\u9ad8\u5c71\u8336" : "\u5f53\u524d\u8336\u54c1")
            : String.valueOf(categoryShare.get(0).get("name")));
        snapshot.setForecastTotal7Days((int) safeLong(forecastSummary.get("totalForecast7Days")));
        snapshot.setHighRiskProductCount((int) highRiskCount);
        snapshot.setRestockPendingCount((int) restockPendingCount);
        snapshot.setTopForecastProductName(String.valueOf(forecastSummary.get("topProductName")));
        return snapshot;
    }

    private Map<String, Object> buildForecastSummary(List<ForecastItemView> forecastList) {
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        long totalForecast7Days = 0L;
        String topProductName = "暂无预测";
        long topForecast7Days = 0L;
        for (ForecastItemView item : forecastList) {
            int currentForecast = item.getForecast7Days() == null ? 0 : item.getForecast7Days().intValue();
            totalForecast7Days += currentForecast;
            if (currentForecast > topForecast7Days) {
                topForecast7Days = currentForecast;
                topProductName = item.getProductName();
            }
        }
        summary.put("totalForecast7Days", totalForecast7Days);
        summary.put("topProductName", topProductName);
        summary.put("topForecast7Days", topForecast7Days);
        return summary;
    }

    private Map<String, Object> buildBaseCapacitySummary(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select count(*) as baseCount, coalesce(sum(area),0) as totalArea, coalesce(sum(annualcapacity),0) as annualCapacity, " +
            "coalesce(sum(case when altitude >= 800 then 1 else 0 end),0) as highAltitudeBaseCount from teabase"
        );
        List<Map<String, Object>> rows;
        if (enterpriseAccount == null) {
            rows = queryList(sql.toString());
        } else {
            rows = queryList(sql.append(" where enterpriseaccount = ?").toString(), enterpriseAccount);
        }
        return rows.isEmpty() ? new LinkedHashMap<String, Object>() : rows.get(0);
    }

    private List<Map<String, Object>> buildBaseRegionStats(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select coalesce(regiontag, location, '未分区') as regionName, count(*) as baseCount, coalesce(sum(annualcapacity),0) as annualCapacity " +
            "from teabase where 1=1"
        );
        if (enterpriseAccount == null) {
            return queryList(sql.append(" group by coalesce(regiontag, location, '未分区') order by annualCapacity desc limit 8").toString());
        }
        return queryList(sql.append(" and enterpriseaccount = ? group by coalesce(regiontag, location, '未分区') order by annualCapacity desc limit 8").toString(), enterpriseAccount);
    }

    private Map<String, Object> buildAnnualPlanBoard(String enterpriseAccount) {
        if (productionSalesPlanAnalysisService == null) {
            Map<String, Object> fallback = new LinkedHashMap<String, Object>();
            fallback.put("summary", new LinkedHashMap<String, Object>());
            fallback.put("trend", new ArrayList<Map<String, Object>>());
            fallback.put("gapList", new ArrayList<Map<String, Object>>());
            return fallback;
        }
        return productionSalesPlanAnalysisService.buildAnnualBoard(enterpriseAccount, Integer.valueOf(LocalDate.now().getYear()));
    }

    private Map<String, Object> buildAnnualPlanSummary(String enterpriseAccount) {
        Object value = buildAnnualPlanBoard(enterpriseAccount).get("summary");
        return value instanceof Map ? (Map<String, Object>) value : new LinkedHashMap<String, Object>();
    }

    private Object buildAnnualPlanTrend(String enterpriseAccount) {
        return buildAnnualPlanBoard(enterpriseAccount).get("trend");
    }

    private Object buildPlanGapList(String enterpriseAccount) {
        return buildAnnualPlanBoard(enterpriseAccount).get("gapList");
    }

    private List<Map<String, Object>> buildWarehouseInventorySummary(String enterpriseAccount) {
        return warehouseCoordinationService == null ? new ArrayList<Map<String, Object>>() : warehouseCoordinationService.listWarehouseInventorySummary(enterpriseAccount);
    }

    private List<Map<String, Object>> buildTransferSuggestionList(String enterpriseAccount) {
        return warehouseCoordinationService == null ? new ArrayList<Map<String, Object>>() : warehouseCoordinationService.generateTransferSuggestions(enterpriseAccount);
    }

    private Map<String, Object> buildLifecycleStatus(String enterpriseAccount) {
        if (lifecycleDecisionService == null) {
            Map<String, Object> fallback = new LinkedHashMap<String, Object>();
            fallback.put("lifecycleStageSummary", new LinkedHashMap<String, Object>());
            fallback.put("bestSaleWindowList", new ArrayList<Map<String, Object>>());
            fallback.put("lifecycleRiskList", new ArrayList<Map<String, Object>>());
            return fallback;
        }
        return lifecycleDecisionService.buildTodayStatus(enterpriseAccount);
    }

    private long countHighRisk(List<InventoryRiskItemView> inventoryRiskList) {
        long count = 0L;
        for (InventoryRiskItemView item : inventoryRiskList) {
            if ("urgent".equals(item.getRiskLevel()) || "warning".equals(item.getRiskLevel())) {
                count++;
            }
        }
        return count;
    }

    private long countRestockPending(List<RestockPlanItemView> restockPlanList) {
        long count = 0L;
        for (RestockPlanItemView item : restockPlanList) {
            if (item.getSuggestedRestock() != null && item.getSuggestedRestock().compareTo(BigDecimal.ZERO) > 0) {
                count++;
            }
        }
        return count;
    }

    private List<Map<String, Object>> buildSalesTrend(String enterpriseAccount) {
        LocalDate startMonth = LocalDate.now().withDayOfMonth(1).minusMonths(5);
        Timestamp startTime = Timestamp.valueOf(startMonth.atStartOfDay());
        QuerySpec spec = buildSalesTrendQuery(enterpriseAccount, startTime);
        List<Map<String, Object>> raw = queryList(spec);

        Map<String, Map<String, Object>> rawMap = new LinkedHashMap<String, Map<String, Object>>();
        for (Map<String, Object> item : raw) {
            rawMap.put(String.valueOf(item.get("month_key")), item);
        }

        DateTimeFormatter keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("M\u6708");
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 6; i++) {
            LocalDate month = startMonth.plusMonths(i);
            String monthKey = month.format(keyFormatter);
            Map<String, Object> item = rawMap.get(monthKey);
            Map<String, Object> trend = new LinkedHashMap<String, Object>();
            trend.put("monthKey", monthKey);
            trend.put("label", month.format(labelFormatter));
            trend.put("sales", item == null ? 0L : safeLong(item.get("sales")));
            trend.put("revenue", item == null ? BigDecimal.ZERO : safeDecimal(item.get("revenue")));
            result.add(trend);
        }
        return result;
    }

    private List<Map<String, Object>> buildCategoryShare(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select coalesce(s.shangpinfenlei,'\u672a\u5206\u7c7b') as name, coalesce(sum(o.buynumber),0) as value " +
            "from orders o left join shangpinxinxi s on o.goodid = s.id where o.status"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(buildStatusInSql());
        if (enterpriseAccount != null) {
            sql.append(" and o.zhanghao = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" group by s.shangpinfenlei order by value desc limit 6");
        return queryList(sql.toString(), args.toArray());
    }

    private List<Map<String, Object>> buildTopProducts(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select coalesce(o.goodname,'\u672a\u547d\u540d\u8336\u54c1') as name, coalesce(sum(o.buynumber),0) as value " +
            "from orders o where o.status"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(buildStatusInSql());
        if (enterpriseAccount != null) {
            sql.append(" and o.zhanghao = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" group by o.goodname order by value desc limit 5");
        return queryList(sql.toString(), args.toArray());
    }

    private List<Map<String, Object>> buildTopEnterprises(String enterpriseAccount) {
        if (enterpriseAccount != null) {
            List<Map<String, Object>> onlySelf = new ArrayList<Map<String, Object>>();
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("name", enterpriseAccount);
            item.put("value", queryLong("select count(*) from orders where zhanghao = ?", enterpriseAccount));
            onlySelf.add(item);
            return onlySelf;
        }
        return queryList(
            "select coalesce(zhanghao,'\u672a\u77e5\u4f01\u4e1a') as name, count(*) as value from orders group by zhanghao order by value desc limit 5"
        );
    }

    private List<Map<String, Object>> buildLowStockList(String enterpriseAccount) {
        return queryList(buildInventoryWarningQuery(enterpriseAccount, false));
    }

    private QuerySpec buildInventoryWarningQuery(String enterpriseAccount, boolean countOnly) {
        StringBuilder sql = new StringBuilder();
        List<Object> args = new ArrayList<Object>();
        if (countOnly) {
            sql.append("select count(*)");
        } else {
            sql.append(
                "select i.batchcode as batchCode, i.productname as productName, i.currentstock as currentStock, " +
                "i.warningstock as warningStock, i.enterpriseaccount as enterpriseAccount"
            );
        }
        sql.append(
            " from inventoryrecord i inner join (" +
            "select batchcode, max(addtime) as max_addtime from inventoryrecord group by batchcode" +
            ") latest on i.batchcode = latest.batchcode and i.addtime = latest.max_addtime " +
            "where i.currentstock <= i.warningstock"
        );
        if (enterpriseAccount != null) {
            sql.append(" and i.enterpriseaccount = ?");
            args.add(enterpriseAccount);
        }
        if (!countOnly) {
            sql.append(" order by i.currentstock asc limit 6");
        }
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private QuerySpec buildSlowMovingQuery(String enterpriseAccount, Timestamp slowMovingLimit) {
        StringBuilder sql = new StringBuilder(
            "select count(*) from shangpinxinxi s left join (" +
            "select goodid, max(addtime) as last_order_time from orders where status"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(buildStatusInSql());
        sql.append(" group by goodid) recent on s.id = recent.goodid ");
        sql.append("where (recent.last_order_time is null or recent.last_order_time < ?)");
        args.add(slowMovingLimit);
        if (enterpriseAccount != null) {
            sql.append(" and s.zhanghao = ?");
            args.add(enterpriseAccount);
        }
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private long countAbnormalEnterprises(
        String enterpriseAccount,
        Timestamp lastMonthStart,
        Timestamp currentMonthStart,
        Timestamp nextMonthStart
    ) {
        if (enterpriseAccount != null) {
            long currentCount = queryLong(buildOrderCountQuery(enterpriseAccount, currentMonthStart, nextMonthStart));
            long lastCount = queryLong(buildOrderCountQuery(enterpriseAccount, lastMonthStart, currentMonthStart));
            return isAbnormal(currentCount, lastCount) ? 1L : 0L;
        }

        List<Map<String, Object>> currentList = queryList(buildEnterpriseOrderCountGroupQuery(currentMonthStart, nextMonthStart));
        List<Map<String, Object>> lastList = queryList(buildEnterpriseOrderCountGroupQuery(lastMonthStart, currentMonthStart));

        Map<String, Long> lastMap = new LinkedHashMap<String, Long>();
        for (Map<String, Object> item : lastList) {
            lastMap.put(String.valueOf(item.get("enterpriseAccount")), safeLong(item.get("totalCount")));
        }

        long abnormalCount = 0L;
        for (Map<String, Object> item : currentList) {
            String key = String.valueOf(item.get("enterpriseAccount"));
            long currentCount = safeLong(item.get("totalCount"));
            long lastCount = lastMap.containsKey(key) ? lastMap.get(key) : 0L;
            if (isAbnormal(currentCount, lastCount)) {
                abnormalCount++;
            }
        }
        return abnormalCount;
    }

    private boolean isAbnormal(long currentCount, long lastCount) {
        if (currentCount < 3) {
            return false;
        }
        if (lastCount <= 0) {
            return currentCount >= 5;
        }
        return currentCount >= Math.ceil(lastCount * 1.5D);
    }

    private long countEnterprises(String enterpriseAccount) {
        if (enterpriseAccount != null) {
            return queryLong("select count(*) from shangjia where zhanghao = ?", enterpriseAccount);
        }
        return queryLong("select count(*) from shangjia");
    }

    private QuerySpec buildEnterpriseQuery(String baseSql, String fieldName, String enterpriseAccount, Object... extraArgs) {
        StringBuilder sql = new StringBuilder(baseSql);
        List<Object> args = new ArrayList<Object>();
        appendEnterpriseCondition(sql, args, fieldName, enterpriseAccount);
        addArgs(args, extraArgs);
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private QuerySpec buildOrderAggregateQuery(
        String prefixSql,
        String enterpriseAccount,
        Timestamp startTime,
        Timestamp endTime
    ) {
        StringBuilder sql = new StringBuilder(prefixSql);
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(" status").append(buildStatusInSql());
        if (enterpriseAccount != null) {
            sql.append(" and zhanghao = ?");
            args.add(enterpriseAccount);
        }
        if (startTime != null) {
            sql.append(" and addtime >= ?");
            args.add(startTime);
        }
        if (endTime != null) {
            sql.append(" and addtime < ?");
            args.add(endTime);
        }
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private QuerySpec buildOrderRevenueQuery(String enterpriseAccount, Timestamp startTime, Timestamp endTime) {
        return buildOrderAggregateQuery(
            "select coalesce(sum(total),0) from orders where",
            enterpriseAccount,
            startTime,
            endTime
        );
    }

    private QuerySpec buildSalesTrendQuery(String enterpriseAccount, Timestamp startTime) {
        StringBuilder sql = new StringBuilder(
            "select DATE_FORMAT(addtime,'%Y-%m') as month_key, coalesce(sum(buynumber),0) as sales, " +
            "coalesce(sum(total),0) as revenue from orders where status"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(buildStatusInSql());
        if (enterpriseAccount != null) {
            sql.append(" and zhanghao = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" and addtime >= ?");
        args.add(startTime);
        sql.append(" group by DATE_FORMAT(addtime,'%Y-%m') order by month_key asc");
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private QuerySpec buildOrderCountQuery(String enterpriseAccount, Timestamp startTime, Timestamp endTime) {
        return buildOrderAggregateQuery("select count(*) from orders where", enterpriseAccount, startTime, endTime);
    }

    private QuerySpec buildEnterpriseOrderCountGroupQuery(Timestamp startTime, Timestamp endTime) {
        StringBuilder sql = new StringBuilder(
            "select zhanghao as enterpriseAccount, count(*) as totalCount from orders where status"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        sql.append(buildStatusInSql());
        sql.append(" and addtime >= ? and addtime < ? group by zhanghao");
        args.add(startTime);
        args.add(endTime);
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private void appendEnterpriseCondition(StringBuilder sql, List<Object> args, String fieldName, String enterpriseAccount) {
        if (enterpriseAccount == null) {
            return;
        }
        sql.append(hasWhereClause(sql) ? " and " : " where ");
        sql.append(fieldName).append(" = ?");
        args.add(enterpriseAccount);
    }

    private boolean hasWhereClause(StringBuilder sql) {
        return sql.toString().toLowerCase().contains(" where ");
    }

    private void addArgs(List<Object> args, Object... extraArgs) {
        if (extraArgs == null) {
            return;
        }
        args.addAll(Arrays.asList(extraArgs));
    }

    private String buildStatusInSql() {
        return " in (?,?,?)";
    }

    private long queryLong(QuerySpec spec) {
        return queryLong(spec.sql, spec.args);
    }

    private BigDecimal queryDecimal(QuerySpec spec) {
        return queryDecimal(spec.sql, spec.args);
    }

    private List<Map<String, Object>> queryList(QuerySpec spec) {
        return queryList(spec.sql, spec.args);
    }

    private long queryLong(String sql, Object... args) {
        Number result = jdbcTemplate.queryForObject(sql, args, Number.class);
        return result == null ? 0L : result.longValue();
    }

    private BigDecimal queryDecimal(String sql, Object... args) {
        Object result = jdbcTemplate.queryForObject(sql, args, Object.class);
        return safeDecimal(result);
    }

    private List<Map<String, Object>> queryList(String sql, Object... args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    private long safeLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return value == null ? 0L : Long.parseLong(value.toString());
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

    private boolean canAccessDecisionCenter(HttpServletRequest request) {
        return isAdministrator(request) || isEnterprise(request);
    }

    private boolean isAdministrator(HttpServletRequest request) {
        return "users".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }

    private boolean isEnterprise(HttpServletRequest request) {
        return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }

    private R forbidden() {
        return R.error(403, "\u65e0\u6743\u8bbf\u95ee");
    }

    private String getEnterpriseAccount(HttpServletRequest request) {
        if (isEnterprise(request)) {
            Object username = request.getSession().getAttribute("username");
            return username == null ? null : String.valueOf(username);
        }
        return null;
    }

    private Map<String, Object> defaultTaskParams() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("page", Integer.valueOf(1));
        params.put("limit", Integer.valueOf(10));
        return params;
    }

    private static class QuerySpec {
        private final String sql;
        private final Object[] args;

        private QuerySpec(String sql, Object[] args) {
            this.sql = sql;
            this.args = args;
        }
    }
}
