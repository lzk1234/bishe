package com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockingDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.view.ForecastItemView;
import com.entity.view.InventoryRiskItemView;
import com.entity.view.RestockPlanItemView;
import com.service.DecisionTaskService;
import com.service.InventoryRiskService;
import com.service.DecisionRuleService;
import com.service.RestockSuggestionService;
import com.service.SalesForecastService;
import com.utils.R;

@ExtendWith(MockitoExtension.class)
public class DecisionControllerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DecisionRuleService decisionRuleService;

    @Mock
    private SalesForecastService salesForecastService;

    @Mock
    private InventoryRiskService inventoryRiskService;

    @Mock
    private RestockSuggestionService restockSuggestionService;

    @Mock
    private DecisionTaskService decisionTaskService;

    private DecisionController controller;

    @BeforeEach
    public void setUp() {
        controller = new DecisionController();
        ReflectionTestUtils.setField(controller, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(controller, "decisionRuleService", decisionRuleService);
        ReflectionTestUtils.setField(controller, "salesForecastService", salesForecastService);
        ReflectionTestUtils.setField(controller, "inventoryRiskService", inventoryRiskService);
        ReflectionTestUtils.setField(controller, "restockSuggestionService", restockSuggestionService);
        ReflectionTestUtils.setField(controller, "decisionTaskService", decisionTaskService);

        lenient().when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Number.class))).thenReturn(Long.valueOf(0L));
        lenient().when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Object.class))).thenReturn(BigDecimal.ZERO);
        lenient().doReturn(new ArrayList<Map<String, Object>>()).when(jdbcTemplate).queryForList(anyString(), any(Object[].class));
        lenient().when(decisionRuleService.generateAdvice(any())).thenReturn(new ArrayList<>());
        lenient().when(salesForecastService.listForecasts(any())).thenReturn(new ArrayList<ForecastItemView>());
        lenient().when(inventoryRiskService.listInventoryRisks(any())).thenReturn(new ArrayList<InventoryRiskItemView>());
        lenient().when(restockSuggestionService.listRestockPlans(any())).thenReturn(new ArrayList<RestockPlanItemView>());
        lenient().when(decisionTaskService.buildTaskStats(any())).thenReturn(new java.util.LinkedHashMap<String, Object>());
        lenient().when(decisionTaskService.listHighlightedTasks(any(), any(Integer.class))).thenReturn(new ArrayList<>());
        lenient().when(decisionTaskService.queryTaskPage(any(), any())).thenReturn(new com.utils.PageUtils(new ArrayList<>(), 0, 10, 1));
        lenient().when(decisionTaskService.buildWeeklyReview(any())).thenReturn(new java.util.LinkedHashMap<String, Object>());
    }

    @Test
    public void dashboardRejectsNormalUsers() {
        HttpServletRequest request = createRequest("yonghu", "tea-user");

        R result = controller.dashboard(request);

        assertEquals(403, result.get("code"));
    }

    @Test
    public void adviceRejectsNormalUsers() {
        HttpServletRequest request = createRequest("yonghu", "tea-user");

        R result = controller.advice(request);

        assertEquals(403, result.get("code"));
    }

    @Test
    public void forecastRejectsNormalUsers() {
        HttpServletRequest request = createRequest("yonghu", "tea-user");

        R result = controller.forecast(request);

        assertEquals(403, result.get("code"));
    }

    @Test
    public void inventoryRiskRejectsNormalUsers() {
        HttpServletRequest request = createRequest("yonghu", "tea-user");

        R result = controller.inventoryRisk(request);

        assertEquals(403, result.get("code"));
    }

    @Test
    public void restockPlanRejectsNormalUsers() {
        HttpServletRequest request = createRequest("yonghu", "tea-user");

        R result = controller.restockPlan(request);

        assertEquals(403, result.get("code"));
    }

    @Test
    public void merchantDashboardUsesParameterizedQueries() {
        HttpServletRequest request = createRequest("shangjia", "merchantA");

        controller.dashboard(request);

        List<String> executedSql = new ArrayList<String>();
        for (Invocation invocation : mockingDetails(jdbcTemplate).getInvocations()) {
            if (invocation.getMethod().getName().startsWith("queryFor")) {
                executedSql.add(String.valueOf(invocation.getArguments()[0]));
            }
        }

        for (String sql : executedSql) {
            assertFalse(sql.contains("merchantA"));
        }
    }

    @Test
    public void dashboardIncludesForecastAndRestockFields() {
        HttpServletRequest request = createRequest("users", "admin");

        R result = controller.dashboard(request);
        Object data = result.get("data");

        assertTrue(data instanceof Map);
        Map<?, ?> payload = (Map<?, ?>) data;
        assertTrue(payload.containsKey("forecastList"));
        assertTrue(payload.containsKey("inventoryRiskList"));
        assertTrue(payload.containsKey("restockPlanList"));
        assertTrue(payload.containsKey("forecastSummary"));
        assertTrue(payload.containsKey("restockPendingCount"));
        assertTrue(payload.containsKey("taskStats"));
        assertTrue(payload.containsKey("highlightedTasks"));
        assertTrue(payload.containsKey("taskPage"));
        assertTrue(payload.containsKey("weeklyReview"));
    }

    private HttpServletRequest createRequest(String tableName, String username) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("tableName", tableName);
        request.getSession().setAttribute("username", username);
        return request;
    }
}
