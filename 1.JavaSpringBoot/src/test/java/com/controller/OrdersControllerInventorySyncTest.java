package com.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.entity.OrdersEntity;
import com.service.OrderInventorySyncService;
import com.service.OrdersService;
import com.service.UserBehaviorService;

@ExtendWith(MockitoExtension.class)
public class OrdersControllerInventorySyncTest {
    private static final String STATUS_PENDING = "\u5f85\u652f\u4ed8";
    private static final String STATUS_PAID = "\u5df2\u652f\u4ed8";

    @Mock
    private OrdersService ordersService;

    @Mock
    private UserBehaviorService userBehaviorService;

    @Mock
    private OrderInventorySyncService orderInventorySyncService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        OrdersController controller = new OrdersController();
        ReflectionTestUtils.setField(controller, "ordersService", ordersService);
        ReflectionTestUtils.setField(controller, "userBehaviorService", userBehaviorService);
        ReflectionTestUtils.setField(controller, "orderInventorySyncService", orderInventorySyncService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void updateTriggersInventorySyncAfterOrderBecomesPaid() throws Exception {
        OrdersEntity previousOrder = createOrder(11L, "ORD-11", STATUS_PENDING);
        OrdersEntity currentOrder = createOrder(11L, "ORD-11", STATUS_PAID);
        when(ordersService.selectById(11L)).thenReturn(previousOrder, currentOrder);
        when(ordersService.updateById(any(OrdersEntity.class))).thenReturn(true);

        mockMvc.perform(post("/orders/update")
                .contentType("application/json;charset=UTF-8")
                .content("{\"id\":11,\"orderid\":\"ORD-11\",\"tablename\":\"shangpinxinxi\",\"status\":\"\\u5df2\\u652f\\u4ed8\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        verify(orderInventorySyncService).syncOnOrderEffectiveStatusChange(previousOrder, currentOrder);
    }

    private OrdersEntity createOrder(Long id, String orderId, String status) {
        OrdersEntity order = new OrdersEntity();
        order.setId(id);
        order.setOrderid(orderId);
        order.setTablename("shangpinxinxi");
        order.setStatus(status);
        return order;
    }
}
