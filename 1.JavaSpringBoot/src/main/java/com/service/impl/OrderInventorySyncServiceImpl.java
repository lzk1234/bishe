package com.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.InventoryrecordEntity;
import com.entity.OrdersEntity;
import com.entity.ShangpinxinxiEntity;
import com.entity.TeabatchEntity;
import com.service.InventoryrecordService;
import com.service.OrderInventorySyncService;
import com.service.ShangpinxinxiService;
import com.service.TeabatchService;

@Service("orderInventorySyncService")
public class OrderInventorySyncServiceImpl implements OrderInventorySyncService {
    static final String ORDER_TABLE_NAME = "shangpinxinxi";
    static final String STATUS_PAID = "\u5df2\u652f\u4ed8";
    static final String STATUS_SHIPPED = "\u5df2\u53d1\u8d27";
    static final String STATUS_FINISHED = "\u5df2\u5b8c\u6210";
    static final String RECORD_TYPE_OUTBOUND = "\u51fa\u5e93";
    static final String RECORD_TYPE_SKIPPED = "\u51fa\u5e93\u5f02\u5e38";
    static final String REMARK_AUTO_OUTBOUND = "\u8ba2\u5355\u8fdb\u5165\u6709\u6548\u5c65\u7ea6\u72b6\u6001\u540e\u81ea\u52a8\u6263\u51cf\u5e93\u5b58";
    static final String REMARK_MISSING_BATCH = "\u5546\u54c1\u672a\u914d\u7f6e\u53ef\u7528\u6279\u6b21\uff0c\u5df2\u8df3\u8fc7\u5e93\u5b58\u6263\u51cf";
    static final String REMARK_MISSING_BASELINE = "\u5546\u54c1\u7f3a\u5c11\u5e93\u5b58\u57fa\u7ebf\uff0c\u5df2\u8df3\u8fc7\u5e93\u5b58\u6263\u51cf";
    static final String REMARK_MISSING_PRODUCT = "\u672a\u627e\u5230\u8ba2\u5355\u5bf9\u5e94\u5546\u54c1\uff0c\u5df2\u8df3\u8fc7\u5e93\u5b58\u6263\u51cf";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInventorySyncServiceImpl.class);

    @Autowired
    private InventoryrecordService inventoryrecordService;

    @Autowired
    private ShangpinxinxiService shangpinxinxiService;

    @Autowired
    private TeabatchService teabatchService;

    @Override
    public void syncOnOrderEffectiveStatusChange(OrdersEntity previousOrder, OrdersEntity currentOrder) {
        if (!shouldSync(previousOrder, currentOrder)) {
            return;
        }

        try {
            String relatedOrderId = resolveRelatedOrderId(currentOrder);
            if (hasExistingOutboundRecord(relatedOrderId)) {
                return;
            }

            ShangpinxinxiEntity product = shangpinxinxiService.selectById(currentOrder.getGoodid());
            if (product == null) {
                insertTraceRecord(currentOrder, null, null, BigDecimal.ZERO, REMARK_MISSING_PRODUCT);
                return;
            }

            if (StringUtils.isBlank(product.getShengchanpici())) {
                insertTraceRecord(currentOrder, product, null, BigDecimal.ZERO, REMARK_MISSING_BATCH);
                return;
            }

            TeabatchEntity batch = teabatchService.selectOne(new EntityWrapper<TeabatchEntity>()
                .eq("batchcode", product.getShengchanpici()));
            if (batch == null) {
                insertTraceRecord(currentOrder, product, null, BigDecimal.ZERO, REMARK_MISSING_BATCH);
                return;
            }

            Integer snapshot = product.getAlllimittimes();
            if (snapshot == null) {
                insertTraceRecord(currentOrder, product, batch, BigDecimal.ZERO, REMARK_MISSING_BASELINE);
                return;
            }

            BigDecimal quantity = BigDecimal.valueOf(resolveBuyNumber(currentOrder));
            BigDecimal nextStock = BigDecimal.valueOf(snapshot.longValue()).subtract(quantity);
            product.setAlllimittimes(Integer.valueOf(nextStock.intValue()));
            shangpinxinxiService.updateById(product);

            InventoryrecordEntity record = buildBaseRecord(currentOrder, product, batch);
            record.setRecordtype(RECORD_TYPE_OUTBOUND);
            record.setChangestock(quantity.negate());
            record.setCurrentstock(nextStock);
            record.setRemark(REMARK_AUTO_OUTBOUND);
            inventoryrecordService.insert(record);
        } catch (Exception ex) {
            LOGGER.warn("Skip inventory sync for order {} because inventory linkage failed", currentOrder.getId(), ex);
        }
    }

    private boolean shouldSync(OrdersEntity previousOrder, OrdersEntity currentOrder) {
        if (currentOrder == null) {
            return false;
        }
        if (!ORDER_TABLE_NAME.equals(currentOrder.getTablename())) {
            return false;
        }
        if (!isEffectiveStatus(currentOrder.getStatus())) {
            return false;
        }
        if (currentOrder.getGoodid() == null) {
            return false;
        }
        return previousOrder == null || !isEffectiveStatus(previousOrder.getStatus());
    }

    private boolean isEffectiveStatus(String status) {
        return STATUS_PAID.equals(status) || STATUS_SHIPPED.equals(status) || STATUS_FINISHED.equals(status);
    }

    private boolean hasExistingOutboundRecord(String relatedOrderId) {
        Integer count = inventoryrecordService.selectCount(new EntityWrapper<InventoryrecordEntity>()
            .eq("relatedorderid", relatedOrderId)
            .eq("recordtype", RECORD_TYPE_OUTBOUND));
        return count != null && count.intValue() > 0;
    }

    private void insertTraceRecord(
        OrdersEntity order,
        ShangpinxinxiEntity product,
        TeabatchEntity batch,
        BigDecimal currentStock,
        String remark
    ) {
        InventoryrecordEntity record = buildBaseRecord(order, product, batch);
        record.setRecordtype(RECORD_TYPE_SKIPPED);
        record.setChangestock(BigDecimal.ZERO);
        record.setCurrentstock(currentStock);
        record.setRemark(remark);
        inventoryrecordService.insert(record);
    }

    private InventoryrecordEntity buildBaseRecord(OrdersEntity order, ShangpinxinxiEntity product, TeabatchEntity batch) {
        InventoryrecordEntity record = new InventoryrecordEntity();
        Date now = new Date();
        record.setId(now.getTime() + ThreadLocalRandom.current().nextInt(1000));
        record.setBatchcode(batch == null ? null : batch.getBatchcode());
        record.setProductname(resolveProductName(order, product, batch));
        record.setWarningstock(null);
        record.setRecordtime(now);
        record.setAddtime(now);
        record.setEnterpriseaccount(resolveEnterpriseAccount(order, product, batch));
        record.setRelatedorderid(resolveRelatedOrderId(order));
        return record;
    }

    private String resolveProductName(OrdersEntity order, ShangpinxinxiEntity product, TeabatchEntity batch) {
        if (product != null && StringUtils.isNotBlank(product.getShangpinmingcheng())) {
            return product.getShangpinmingcheng();
        }
        if (batch != null && StringUtils.isNotBlank(batch.getProductname())) {
            return batch.getProductname();
        }
        return order.getGoodname();
    }

    private String resolveEnterpriseAccount(OrdersEntity order, ShangpinxinxiEntity product, TeabatchEntity batch) {
        if (product != null && StringUtils.isNotBlank(product.getZhanghao())) {
            return product.getZhanghao();
        }
        if (batch != null && StringUtils.isNotBlank(batch.getEnterpriseaccount())) {
            return batch.getEnterpriseaccount();
        }
        return order.getZhanghao();
    }

    private String resolveRelatedOrderId(OrdersEntity order) {
        if (StringUtils.isNotBlank(order.getOrderid())) {
            return order.getOrderid();
        }
        return String.valueOf(order.getId());
    }

    private long resolveBuyNumber(OrdersEntity order) {
        if (order.getBuynumber() == null || order.getBuynumber().intValue() <= 0) {
            return 0L;
        }
        return order.getBuynumber().longValue();
    }
}
