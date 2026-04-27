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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.entity.OrdersEntity;
import com.service.OrdersService;
import com.service.UserBehaviorService;

@ExtendWith(MockitoExtension.class)
public class OrdersControllerShipTest {

    @Mock
    private OrdersService ordersService;

    @Mock
    private UserBehaviorService userBehaviorService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        OrdersController controller = new OrdersController();
        ReflectionTestUtils.setField(controller, "ordersService", ordersService);
        ReflectionTestUtils.setField(controller, "userBehaviorService", userBehaviorService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void merchantShipsPaidOrderWithLogistics() throws Exception {
        OrdersEntity order = createOrder(5L, "merchantA", "已支付");
        when(ordersService.selectById(5L)).thenReturn(order);
        when(ordersService.updateById(any(OrdersEntity.class))).thenReturn(true);

        mockMvc.perform(post("/orders/ship")
                .sessionAttr("tableName", "shangjia")
                .sessionAttr("username", "merchantA")
                .contentType("application/json;charset=UTF-8")
                .content("{\"id\":5,\"logistics\":\"SF123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0));

        ArgumentCaptor<OrdersEntity> captor = ArgumentCaptor.forClass(OrdersEntity.class);
        verify(ordersService).updateById(captor.capture());
        OrdersEntity shippedOrder = captor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals("已发货", shippedOrder.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals("SF123456", shippedOrder.getLogistics());
    }

    @Test
    public void merchantCannotShipForeignOrder() throws Exception {
        OrdersEntity order = createOrder(6L, "merchantB", "已支付");
        when(ordersService.selectById(6L)).thenReturn(order);

        mockMvc.perform(post("/orders/ship")
                .sessionAttr("tableName", "shangjia")
                .sessionAttr("username", "merchantA")
                .contentType("application/json;charset=UTF-8")
                .content("{\"id\":6,\"logistics\":\"SF123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    private OrdersEntity createOrder(Long id, String zhanghao, String status) {
        OrdersEntity order = new OrdersEntity();
        order.setId(id);
        order.setZhanghao(zhanghao);
        order.setStatus(status);
        return order;
    }
}
