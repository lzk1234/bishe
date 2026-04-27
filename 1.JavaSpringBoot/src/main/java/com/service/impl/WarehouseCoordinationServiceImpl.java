package com.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.service.WarehouseCoordinationService;

@Service("warehouseCoordinationService")
public class WarehouseCoordinationServiceImpl implements WarehouseCoordinationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> listWarehouseInventorySummary(String enterpriseAccount) {
        return queryLatestInventory(enterpriseAccount);
    }

    @Override
    public List<Map<String, Object>> generateTransferSuggestions(String enterpriseAccount) {
        List<Map<String, Object>> rows = queryLatestInventory(enterpriseAccount);
        List<Map<String, Object>> suggestions = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> low : rows) {
            BigDecimal lowStock = safeDecimal(first(low, "currentStock", "currentstock"));
            BigDecimal warning = safeDecimal(first(low, "warningStock", "warningstock"));
            if (lowStock.compareTo(warning) >= 0) {
                continue;
            }
            String productName = String.valueOf(first(low, "productName", "productname"));
            for (Map<String, Object> source : rows) {
                if (!productName.equals(String.valueOf(first(source, "productName", "productname")))) {
                    continue;
                }
                if (String.valueOf(first(source, "warehouseCode", "warehousecode")).equals(String.valueOf(first(low, "warehouseCode", "warehousecode")))) {
                    continue;
                }
                BigDecimal sourceStock = safeDecimal(first(source, "currentStock", "currentstock"));
                BigDecimal sourceWarning = safeDecimal(first(source, "warningStock", "warningstock"));
                BigDecimal surplus = sourceStock.subtract(sourceWarning.multiply(new BigDecimal("2")));
                if (surplus.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal shortage = warning.subtract(lowStock);
                BigDecimal suggested = shortage.min(surplus).setScale(2, BigDecimal.ROUND_HALF_UP);
                Map<String, Object> item = new LinkedHashMap<String, Object>();
                item.put("enterpriseAccount", first(low, "enterpriseAccount", "enterpriseaccount"));
                item.put("productName", productName);
                item.put("sourceWarehouseCode", first(source, "warehouseCode", "warehousecode"));
                item.put("sourceWarehouseName", first(source, "warehouseName", "warehousename"));
                item.put("targetWarehouseCode", first(low, "warehouseCode", "warehousecode"));
                item.put("targetWarehouseName", first(low, "warehouseName", "warehousename"));
                item.put("suggestedQuantity", suggested);
                item.put("reason", "目标仓低于预警库存，来源仓库存充足");
                item.put("status", "待处理");
                suggestions.add(item);
                break;
            }
        }
        return suggestions;
    }

    @Override
    public List<Map<String, Object>> listTransferSuggestions(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select productname as productName, sourcewarehousecode as sourceWarehouseCode, sourcewarehousename as sourceWarehouseName, " +
            "targetwarehousecode as targetWarehouseCode, targetwarehousename as targetWarehouseName, suggestedquantity as suggestedQuantity, " +
            "reason, status from inventory_transfer_suggestion where 1=1"
        );
        if (enterpriseAccount == null) {
            return jdbcTemplate.queryForList(sql.append(" order by addtime desc limit 8").toString());
        }
        return jdbcTemplate.queryForList(sql.append(" and enterpriseaccount = ? order by addtime desc limit 8").toString(), enterpriseAccount);
    }

    private List<Map<String, Object>> queryLatestInventory(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select i.warehousecode as warehouseCode, i.warehousename as warehouseName, i.productname as productName, " +
            "i.currentstock as currentStock, i.warningstock as warningStock, i.enterpriseaccount as enterpriseAccount " +
            "from inventoryrecord i inner join (" +
            "select productname, coalesce(warehousecode,'DEFAULT') as warehousecode, max(addtime) as max_addtime from inventoryrecord group by productname, coalesce(warehousecode,'DEFAULT')" +
            ") latest on i.productname = latest.productname and coalesce(i.warehousecode,'DEFAULT') = latest.warehousecode and i.addtime = latest.max_addtime where 1=1"
        );
        if (enterpriseAccount == null) {
            return jdbcTemplate.queryForList(sql.toString());
        }
        return jdbcTemplate.queryForList(sql.append(" and i.enterpriseaccount = ?").toString(), enterpriseAccount);
    }

    private Object first(Map<String, Object> row, String... keys) {
        for (String key : keys) {
            if (row.containsKey(key)) {
                return row.get(key);
            }
        }
        return null;
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
}
