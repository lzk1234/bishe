package com.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.DecisionTaskDao;
import com.entity.DecisionTaskEntity;
import com.entity.ShangjiaEntity;
import com.entity.view.DecisionDashboardSnapshotView;
import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;
import com.service.DecisionTaskService;
import com.service.InventoryRiskService;
import com.service.RestockSuggestionService;
import com.service.SalesForecastService;
import com.service.ShangjiaService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("decisionTaskService")
public class DecisionTaskServiceImpl extends ServiceImpl<DecisionTaskDao, DecisionTaskEntity> implements DecisionTaskService {

    private static final List<String> VALID_ORDER_STATUSES = Arrays.asList("已支付", "已发货", "已完成");

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SalesForecastService salesForecastService;

    @Resource
    private InventoryRiskService inventoryRiskService;

    @Resource
    private RestockSuggestionService restockSuggestionService;

    @Resource
    private ShangjiaService shangjiaService;

    @Resource
    private DecisionTaskRuleEngine decisionTaskRuleEngine;

    @Override
    public PageUtils queryTaskPage(Map<String, Object> params, String merchantAccount) {
        refreshExpiredTasks();
        Page<DecisionTaskEntity> page = this.selectPage(new Query<DecisionTaskEntity>(params).getPage(), buildTaskWrapper(params, merchantAccount));
        return new PageUtils(page);
    }

    @Override
    public List<DecisionTaskEntity> listHighlightedTasks(String merchantAccount, Integer limit) {
        refreshExpiredTasks();
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        wrapper.in("priority", Arrays.asList("urgent", "high"));
        wrapper.in("status", Arrays.asList("todo", "processing", "expired"));
        wrapper.orderBy("updated_at", false);
        List<DecisionTaskEntity> list = this.selectList(wrapper);
        if (list == null) {
            return new ArrayList<DecisionTaskEntity>();
        }
        int safeLimit = limit == null || limit.intValue() <= 0 ? 6 : limit.intValue();
        return list.size() <= safeLimit ? list : new ArrayList<DecisionTaskEntity>(list.subList(0, safeLimit));
    }

    @Override
    public Map<String, Object> buildTaskStats(String merchantAccount) {
        refreshExpiredTasks();
        Map<String, Object> stats = new LinkedHashMap<String, Object>();
        stats.put("todoCount", countByStatus(merchantAccount, "todo"));
        stats.put("processingCount", countByStatus(merchantAccount, "processing"));
        stats.put("expiredCount", countByStatus(merchantAccount, "expired"));
        stats.put("done7DaysCount", countHandledWithinDays(merchantAccount, "done", 7));
        return stats;
    }

    @Override
    public Map<String, Object> buildWeeklyReview(String merchantAccount) {
        refreshExpiredTasks();
        Map<String, Object> review = new LinkedHashMap<String, Object>();
        review.put("newTaskCount", countGeneratedWithinDays(merchantAccount, 7));
        review.put("doneTaskCount", countHandledWithinDays(merchantAccount, "done", 7));
        review.put("ignoredTaskCount", countHandledWithinDays(merchantAccount, "ignored", 7));
        review.put("expiredTaskCount", countByStatus(merchantAccount, "expired"));
        review.put("highPriorityOpenCount", countOpenHighPriority(merchantAccount));
        return review;
    }

    @Override
    public DecisionTaskEntity getTaskDetail(Long id, String merchantAccount, boolean admin) {
        refreshExpiredTasks();
        return requireAccessibleTask(id, merchantAccount, admin);
    }

    @Override
    public DecisionTaskEntity startTask(Long id, String merchantAccount, boolean admin) {
        DecisionTaskEntity task = requireAccessibleTask(id, merchantAccount, admin);
        if ("done".equals(task.getStatus()) || "ignored".equals(task.getStatus())) {
            throw new IllegalArgumentException("当前任务已结束，不能再次开始处理");
        }
        task.setStatus("processing");
        task.setUpdatedAt(new Date());
        this.updateById(task);
        return task;
    }

    @Override
    public DecisionTaskEntity completeTask(Long id, String merchantAccount, boolean admin, String resultNote) {
        if (StringUtils.isBlank(resultNote)) {
            throw new IllegalArgumentException("处理说明不能为空");
        }
        DecisionTaskEntity task = requireAccessibleTask(id, merchantAccount, admin);
        task.setStatus("done");
        task.setResultNote(resultNote.trim());
        task.setIgnoreReason(null);
        task.setHandledAt(new Date());
        task.setUpdatedAt(new Date());
        this.updateById(task);
        return task;
    }

    @Override
    public DecisionTaskEntity ignoreTask(Long id, String merchantAccount, boolean admin, String ignoreReason) {
        if (StringUtils.isBlank(ignoreReason)) {
            throw new IllegalArgumentException("忽略原因不能为空");
        }
        DecisionTaskEntity task = requireAccessibleTask(id, merchantAccount, admin);
        task.setStatus("ignored");
        task.setIgnoreReason(ignoreReason.trim());
        task.setResultNote(null);
        task.setHandledAt(new Date());
        task.setUpdatedAt(new Date());
        this.updateById(task);
        return task;
    }

    @Override
    public void generateTasksForMerchant(String merchantAccount) {
        if (StringUtils.isBlank(merchantAccount)) {
            return;
        }
        refreshExpiredTasks();
        List<ForecastItemView> forecastList = salesForecastService.listForecasts(merchantAccount);
        List<InventoryRiskItemView> inventoryRiskList = inventoryRiskService.listInventoryRisks(merchantAccount);
        List<RestockPlanItemView> restockPlanList = restockSuggestionService.listRestockPlans(merchantAccount);
        DecisionDashboardSnapshotView snapshot = buildSnapshot(merchantAccount, forecastList, inventoryRiskList, restockPlanList);
        List<DecisionTaskDraft> drafts = decisionTaskRuleEngine.buildTasks(merchantAccount, snapshot, inventoryRiskList, restockPlanList, forecastList);
        for (DecisionTaskDraft draft : drafts) {
            upsertOpenTask(draft);
        }
    }

    @Override
    public void generateTasksForAll() {
        refreshExpiredTasks();
        List<ShangjiaEntity> merchantList = shangjiaService.selectList(new EntityWrapper<ShangjiaEntity>());
        for (ShangjiaEntity merchant : merchantList) {
            if (merchant != null && StringUtils.isNotBlank(merchant.getZhanghao())) {
                generateTasksForMerchant(merchant.getZhanghao());
            }
        }
    }

    @Override
    public void refreshExpiredTasks() {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        wrapper.in("status", Arrays.asList("todo", "processing"));
        List<DecisionTaskEntity> tasks = this.selectList(wrapper);
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        Date now = new Date();
        for (DecisionTaskEntity task : tasks) {
            if (task.getDueAt() != null && task.getDueAt().before(now)) {
                task.setStatus("expired");
                task.setUpdatedAt(now);
                this.updateById(task);
            }
        }
    }

    private EntityWrapper<DecisionTaskEntity> buildTaskWrapper(Map<String, Object> params, String merchantAccount) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        String status = stringParam(params, "status");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        String taskType = stringParam(params, "taskType");
        if (StringUtils.isNotBlank(taskType)) {
            wrapper.eq("task_type", taskType);
        }
        String priority = stringParam(params, "priority");
        if (StringUtils.isNotBlank(priority)) {
            wrapper.eq("priority", priority);
        }
        wrapper.orderBy("updated_at", false).orderBy("generated_at", false);
        return wrapper;
    }

    private long countByStatus(String merchantAccount, String status) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        wrapper.eq("status", status);
        return this.selectCount(wrapper);
    }

    private long countHandledWithinDays(String merchantAccount, String status, int days) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        wrapper.eq("status", status);
        wrapper.ge("handled_at", Timestamp.valueOf(LocalDate.now().minusDays(days).atStartOfDay()));
        return this.selectCount(wrapper);
    }

    private long countGeneratedWithinDays(String merchantAccount, int days) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        wrapper.ge("generated_at", Timestamp.valueOf(LocalDate.now().minusDays(days).atStartOfDay()));
        return this.selectCount(wrapper);
    }

    private long countOpenHighPriority(String merchantAccount) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        if (StringUtils.isNotBlank(merchantAccount)) {
            wrapper.eq("merchant_account", merchantAccount);
        }
        wrapper.in("priority", Arrays.asList("urgent", "high"));
        wrapper.in("status", Arrays.asList("todo", "processing", "expired"));
        return this.selectCount(wrapper);
    }

    private DecisionTaskEntity requireAccessibleTask(Long id, String merchantAccount, boolean admin) {
        refreshExpiredTasks();
        DecisionTaskEntity task = this.selectById(id);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        if (!admin && !StringUtils.equals(merchantAccount, task.getMerchantAccount())) {
            throw new IllegalArgumentException("无权操作该任务");
        }
        return task;
    }

    private void upsertOpenTask(DecisionTaskDraft draft) {
        EntityWrapper<DecisionTaskEntity> wrapper = new EntityWrapper<DecisionTaskEntity>();
        wrapper.eq("merchant_account", draft.getMerchantAccount());
        wrapper.eq("source_type", draft.getSourceType());
        wrapper.eq("source_key", draft.getSourceKey());
        wrapper.in("status", Arrays.asList("todo", "processing", "expired"));
        List<DecisionTaskEntity> openTasks = this.selectList(wrapper);
        Date now = new Date();
        if (openTasks == null) {
            openTasks = new ArrayList<DecisionTaskEntity>();
        }
        if (!openTasks.isEmpty()) {
            DecisionTaskEntity entity = openTasks.get(0);
            applyDraft(entity, draft, false, now);
            this.updateById(entity);
            return;
        }
        DecisionTaskEntity entity = new DecisionTaskEntity();
        applyDraft(entity, draft, true, now);
        this.insert(entity);
    }

    private void applyDraft(DecisionTaskEntity entity, DecisionTaskDraft draft, boolean created, Date now) {
        entity.setTaskType(draft.getTaskType());
        entity.setSourceType(draft.getSourceType());
        entity.setSourceKey(draft.getSourceKey());
        entity.setMerchantAccount(draft.getMerchantAccount());
        entity.setTitle(draft.getTitle());
        entity.setSummary(draft.getSummary());
        entity.setActionSuggestion(draft.getActionSuggestion());
        entity.setPriority(draft.getPriority());
        entity.setEvidenceSnapshot(draft.getEvidenceSnapshot());
        entity.setDueAt(draft.getDueAt());
        entity.setUpdatedAt(now);
        if (created) {
            entity.setStatus("todo");
            entity.setGeneratedAt(now);
        } else if ("expired".equals(entity.getStatus())) {
            entity.setStatus("todo");
        }
    }

    private DecisionDashboardSnapshotView buildSnapshot(String merchantAccount,
                                                        List<ForecastItemView> forecastList,
                                                        List<InventoryRiskItemView> inventoryRiskList,
                                                        List<RestockPlanItemView> restockPlanList) {
        Map<String, Object> forecastSummary = buildForecastSummary(forecastList);
        DecisionDashboardSnapshotView snapshot = new DecisionDashboardSnapshotView();
        snapshot.setInventoryWarningCount((int) countHighRisk(inventoryRiskList));
        snapshot.setSlowMovingProductCount((int) queryLong(buildSlowMovingQuery(merchantAccount, Timestamp.valueOf(LocalDate.now().minusDays(30).atStartOfDay()))));
        snapshot.setCurrentMonthSales((int) queryLong(buildOrderAggregateQuery(merchantAccount, 0)));
        snapshot.setLastMonthSales((int) queryLong(buildOrderAggregateQuery(merchantAccount, -1)));
        snapshot.setCurrentMonthRevenue(queryRevenue(merchantAccount, 0));
        snapshot.setLastMonthRevenue(queryRevenue(merchantAccount, -1));
        snapshot.setAbnormalEnterpriseCount((int) countAbnormalEnterprises(merchantAccount));
        snapshot.setTopCategoryName(queryTopCategoryName(merchantAccount));
        snapshot.setForecastTotal7Days((int) safeLong(forecastSummary.get("totalForecast7Days")));
        snapshot.setHighRiskProductCount((int) countHighRisk(inventoryRiskList));
        snapshot.setRestockPendingCount((int) countRestockPending(restockPlanList));
        snapshot.setTopForecastProductName(String.valueOf(forecastSummary.get("topProductName")));
        return snapshot;
    }

    private Map<String, Object> buildForecastSummary(List<ForecastItemView> forecastList) {
        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        long total = 0L;
        String topProduct = "暂无预测";
        long topForecast = 0L;
        for (ForecastItemView item : forecastList) {
            int current = item.getForecast7Days() == null ? 0 : item.getForecast7Days().intValue();
            total += current;
            if (current > topForecast) {
                topForecast = current;
                topProduct = item.getProductName();
            }
        }
        summary.put("totalForecast7Days", Long.valueOf(total));
        summary.put("topProductName", topProduct);
        return summary;
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

    private long queryLong(QuerySpec spec) {
        Number number = jdbcTemplate.queryForObject(spec.getSql(), spec.getArgs(), Number.class);
        return number == null ? 0L : number.longValue();
    }

    private BigDecimal queryRevenue(String merchantAccount, int monthOffset) {
        LocalDate start = LocalDate.now().withDayOfMonth(1).plusMonths(monthOffset);
        LocalDate end = start.plusMonths(1);
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        StringBuilder sql = new StringBuilder("select coalesce(sum(total),0) from orders where status");
        sql.append(buildStatusInSql());
        sql.append(" and addtime >= ? and addtime < ?");
        args.add(Timestamp.valueOf(start.atStartOfDay()));
        args.add(Timestamp.valueOf(end.atStartOfDay()));
        if (StringUtils.isNotBlank(merchantAccount)) {
            sql.append(" and zhanghao = ?");
            args.add(merchantAccount);
        }
        Object value = jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Object.class);
        return value instanceof BigDecimal ? (BigDecimal) value : BigDecimal.ZERO;
    }

    private QuerySpec buildOrderAggregateQuery(String merchantAccount, int monthOffset) {
        LocalDate start = LocalDate.now().withDayOfMonth(1).plusMonths(monthOffset);
        LocalDate end = start.plusMonths(1);
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        StringBuilder sql = new StringBuilder("select coalesce(sum(buynumber),0) from orders where status");
        sql.append(buildStatusInSql());
        sql.append(" and addtime >= ? and addtime < ?");
        args.add(Timestamp.valueOf(start.atStartOfDay()));
        args.add(Timestamp.valueOf(end.atStartOfDay()));
        if (StringUtils.isNotBlank(merchantAccount)) {
            sql.append(" and zhanghao = ?");
            args.add(merchantAccount);
        }
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private QuerySpec buildSlowMovingQuery(String merchantAccount, Timestamp limit) {
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from shangpinxinxi s left join (");
        sql.append("select goodid, max(addtime) as last_order_time from orders where status");
        sql.append(buildStatusInSql());
        sql.append(" group by goodid) recent on s.id = recent.goodid ");
        sql.append("where (recent.last_order_time is null or recent.last_order_time < ?)");
        args.add(limit);
        if (StringUtils.isNotBlank(merchantAccount)) {
            sql.append(" and s.zhanghao = ?");
            args.add(merchantAccount);
        }
        return new QuerySpec(sql.toString(), args.toArray());
    }

    private long countAbnormalEnterprises(String merchantAccount) {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        long current = monthOrderCount(merchantAccount, currentMonth, currentMonth.plusMonths(1));
        long last = monthOrderCount(merchantAccount, currentMonth.minusMonths(1), currentMonth);
        if (merchantAccount == null) {
            return current == 0L || last == 0L ? 1L : 0L;
        }
        return current == 0L || last == 0L ? 1L : 0L;
    }

    private long monthOrderCount(String merchantAccount, LocalDate start, LocalDate end) {
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        StringBuilder sql = new StringBuilder("select count(*) from orders where status");
        sql.append(buildStatusInSql());
        sql.append(" and addtime >= ? and addtime < ?");
        args.add(Timestamp.valueOf(start.atStartOfDay()));
        args.add(Timestamp.valueOf(end.atStartOfDay()));
        if (StringUtils.isNotBlank(merchantAccount)) {
            sql.append(" and zhanghao = ?");
            args.add(merchantAccount);
        }
        Number count = jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Number.class);
        return count == null ? 0L : count.longValue();
    }

    private String queryTopCategoryName(String merchantAccount) {
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        StringBuilder sql = new StringBuilder();
        sql.append("select coalesce(s.shangpinfenlei,'未分类') as name, coalesce(sum(o.buynumber),0) as value ");
        sql.append("from orders o left join shangpinxinxi s on o.goodid = s.id where o.status");
        sql.append(buildStatusInSql());
        if (StringUtils.isNotBlank(merchantAccount)) {
            sql.append(" and o.zhanghao = ?");
            args.add(merchantAccount);
        }
        sql.append(" group by s.shangpinfenlei order by value desc limit 1");
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), args.toArray());
        if (rows.isEmpty()) {
            return "当前茶品";
        }
        return String.valueOf(rows.get(0).get("name"));
    }

    private String buildStatusInSql() {
        return " in (?,?,?)";
    }

    private String stringParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        return value == null ? null : String.valueOf(value).trim();
    }

    private long safeLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    private static class QuerySpec {
        private final String sql;
        private final Object[] args;

        private QuerySpec(String sql, Object[] args) {
            this.sql = sql;
            this.args = args;
        }

        public String getSql() {
            return sql;
        }

        public Object[] getArgs() {
            return args;
        }
    }
}
