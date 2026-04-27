package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.InventoryrecordEntity;
import com.entity.OrdersEntity;
import com.entity.ShangpinxinxiEntity;
import com.entity.TeabatchEntity;
import com.service.InventoryrecordService;
import com.service.ShangpinxinxiService;
import com.service.TeabatchService;

@ExtendWith(MockitoExtension.class)
public class OrderInventorySyncServiceImplTest {
    private static final String STATUS_PENDING = "\u5f85\u652f\u4ed8";
    private static final String STATUS_PAID = "\u5df2\u652f\u4ed8";
    private static final String RECORD_TYPE_OUTBOUND = "\u51fa\u5e93";
    private static final String RECORD_TYPE_SKIPPED = "\u51fa\u5e93\u5f02\u5e38";

    @Mock
    private InventoryrecordService inventoryrecordService;

    @Mock
    private ShangpinxinxiService shangpinxinxiService;

    @Mock
    private TeabatchService teabatchService;

    private OrderInventorySyncServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new OrderInventorySyncServiceImpl();
        ReflectionTestUtils.setField(service, "inventoryrecordService", inventoryrecordService);
        ReflectionTestUtils.setField(service, "shangpinxinxiService", shangpinxinxiService);
        ReflectionTestUtils.setField(service, "teabatchService", teabatchService);
    }

    @Test
    public void writesOutboundRecordAndUpdatesSnapshotWhenOrderFirstBecomesPaid() {
        OrdersEntity previousOrder = createOrder(8L, "ORD-8", STATUS_PENDING);
        OrdersEntity currentOrder = createOrder(8L, "ORD-8", STATUS_PAID);
        ShangpinxinxiEntity product = createProduct("BATCH-8", Integer.valueOf(10));
        TeabatchEntity batch = createBatch("BATCH-8");
        when(inventoryrecordService.selectCount(any())).thenReturn(Integer.valueOf(0));
        when(shangpinxinxiService.selectById(88L)).thenReturn(product);
        when(teabatchService.selectOne(any())).thenReturn(batch);

        service.syncOnOrderEffectiveStatusChange(previousOrder, currentOrder);

        ArgumentCaptor<ShangpinxinxiEntity> productCaptor = ArgumentCaptor.forClass(ShangpinxinxiEntity.class);
        verify(shangpinxinxiService).updateById(productCaptor.capture());
        assertEquals(Integer.valueOf(8), productCaptor.getValue().getAlllimittimes());

        ArgumentCaptor<InventoryrecordEntity> recordCaptor = ArgumentCaptor.forClass(InventoryrecordEntity.class);
        verify(inventoryrecordService).insert(recordCaptor.capture());
        InventoryrecordEntity record = recordCaptor.getValue();
        assertEquals(RECORD_TYPE_OUTBOUND, record.getRecordtype());
        assertEquals(new BigDecimal("-2"), record.getChangestock());
        assertEquals(new BigDecimal("8"), record.getCurrentstock());
        assertEquals("BATCH-8", record.getBatchcode());
        assertEquals("ORD-8", record.getRelatedorderid());
    }

    @Test
    public void duplicateEffectiveUpdateDoesNotDeductInventoryTwice() {
        OrdersEntity previousOrder = createOrder(9L, "ORD-9", STATUS_PENDING);
        OrdersEntity currentOrder = createOrder(9L, "ORD-9", STATUS_PAID);
        when(inventoryrecordService.selectCount(any())).thenReturn(Integer.valueOf(1));

        service.syncOnOrderEffectiveStatusChange(previousOrder, currentOrder);

        verify(shangpinxinxiService, never()).updateById(any(ShangpinxinxiEntity.class));
        verify(inventoryrecordService, never()).insert(any(InventoryrecordEntity.class));
    }

    @Test
    public void missingBatchDoesNotBlockOrderFlowAndWritesTraceRecord() {
        OrdersEntity previousOrder = createOrder(10L, "ORD-10", STATUS_PENDING);
        OrdersEntity currentOrder = createOrder(10L, "ORD-10", STATUS_PAID);
        ShangpinxinxiEntity product = createProduct(null, Integer.valueOf(10));
        when(inventoryrecordService.selectCount(any())).thenReturn(Integer.valueOf(0));
        when(shangpinxinxiService.selectById(88L)).thenReturn(product);

        assertDoesNotThrow(() -> service.syncOnOrderEffectiveStatusChange(previousOrder, currentOrder));

        verify(shangpinxinxiService, never()).updateById(any(ShangpinxinxiEntity.class));
        ArgumentCaptor<InventoryrecordEntity> recordCaptor = ArgumentCaptor.forClass(InventoryrecordEntity.class);
        verify(inventoryrecordService).insert(recordCaptor.capture());
        InventoryrecordEntity record = recordCaptor.getValue();
        assertEquals(RECORD_TYPE_SKIPPED, record.getRecordtype());
        assertEquals(BigDecimal.ZERO, record.getChangestock());
        assertEquals("ORD-10", record.getRelatedorderid());
    }

    @Test
    public void missingInventoryBaselineDoesNotBlockOrderFlowAndWritesTraceRecord() {
        OrdersEntity previousOrder = createOrder(12L, "ORD-12", STATUS_PENDING);
        OrdersEntity currentOrder = createOrder(12L, "ORD-12", STATUS_PAID);
        ShangpinxinxiEntity product = createProduct("BATCH-12", null);
        TeabatchEntity batch = createBatch("BATCH-12");
        when(inventoryrecordService.selectCount(any())).thenReturn(Integer.valueOf(0));
        when(shangpinxinxiService.selectById(88L)).thenReturn(product);
        when(teabatchService.selectOne(any())).thenReturn(batch);

        assertDoesNotThrow(() -> service.syncOnOrderEffectiveStatusChange(previousOrder, currentOrder));

        verify(shangpinxinxiService, never()).updateById(any(ShangpinxinxiEntity.class));
        ArgumentCaptor<InventoryrecordEntity> recordCaptor = ArgumentCaptor.forClass(InventoryrecordEntity.class);
        verify(inventoryrecordService).insert(recordCaptor.capture());
        InventoryrecordEntity record = recordCaptor.getValue();
        assertEquals(RECORD_TYPE_SKIPPED, record.getRecordtype());
        assertEquals(BigDecimal.ZERO, record.getChangestock());
        assertEquals("ORD-12", record.getRelatedorderid());
    }

    private OrdersEntity createOrder(Long id, String orderId, String status) {
        OrdersEntity order = new OrdersEntity();
        order.setId(id);
        order.setOrderid(orderId);
        order.setTablename("shangpinxinxi");
        order.setGoodid(88L);
        order.setGoodname("Longjing");
        order.setBuynumber(Integer.valueOf(2));
        order.setStatus(status);
        order.setZhanghao("merchantA");
        return order;
    }

    private ShangpinxinxiEntity createProduct(String batchCode, Integer stockSnapshot) {
        ShangpinxinxiEntity product = new ShangpinxinxiEntity();
        product.setId(88L);
        product.setShangpinmingcheng("Longjing");
        product.setShengchanpici(batchCode);
        product.setAlllimittimes(stockSnapshot);
        product.setZhanghao("merchantA");
        return product;
    }

    private TeabatchEntity createBatch(String batchCode) {
        TeabatchEntity batch = new TeabatchEntity();
        batch.setBatchcode(batchCode);
        batch.setProductname("Longjing");
        batch.setEnterpriseaccount("merchantA");
        return batch;
    }
}
