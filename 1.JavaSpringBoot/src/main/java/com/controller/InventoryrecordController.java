package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.InventoryrecordEntity;
import com.entity.TeabatchEntity;
import com.entity.WarehouseInfoEntity;
import com.service.InventoryrecordService;
import com.service.TeabatchService;
import com.service.WarehouseInfoService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/inventoryrecord")
public class InventoryrecordController {

    @Autowired
    private InventoryrecordService inventoryrecordService;

    @Autowired
    private TeabatchService teabatchService;

    @Autowired
    private WarehouseInfoService warehouseInfoService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            inventoryrecord.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<InventoryrecordEntity> wrapper = new EntityWrapper<InventoryrecordEntity>();
        PageUtils page = inventoryrecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, inventoryrecord), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            inventoryrecord.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<InventoryrecordEntity> wrapper = new EntityWrapper<InventoryrecordEntity>();
        PageUtils page = inventoryrecordService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, inventoryrecord), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        InventoryrecordEntity inventoryrecord = inventoryrecordService.selectById(id);
        if (inventoryrecord != null && !canAccessEnterpriseRecord(request, inventoryrecord.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", inventoryrecord);
    }

    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        InventoryrecordEntity inventoryrecord = inventoryrecordService.selectById(id);
        if (inventoryrecord != null && !canAccessEnterpriseRecord(request, inventoryrecord.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", inventoryrecord);
    }

    @RequestMapping("/save")
    public R save(@RequestBody InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        inventoryrecord.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(inventoryrecord, request);
        R invalid = normalizeInventoryRecord(inventoryrecord, null);
        if (invalid != null) {
            return invalid;
        }
        inventoryrecordService.insert(inventoryrecord);
        return R.ok();
    }

    @RequestMapping("/add")
    public R add(@RequestBody InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        inventoryrecord.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(inventoryrecord, request);
        R invalid = normalizeInventoryRecord(inventoryrecord, null);
        if (invalid != null) {
            return invalid;
        }
        inventoryrecordService.insert(inventoryrecord);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (inventoryrecord.getId() != null) {
            InventoryrecordEntity stored = inventoryrecordService.selectById(inventoryrecord.getId());
            if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                return forbidden();
            }
        }
        fillEnterprise(inventoryrecord, request);
        R invalid = normalizeInventoryRecord(inventoryrecord, inventoryrecord.getId());
        if (invalid != null) {
            return invalid;
        }
        inventoryrecordService.updateById(inventoryrecord);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            for (Long id : ids) {
                InventoryrecordEntity stored = inventoryrecordService.selectById(id);
                if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                    return forbidden();
                }
            }
        }
        inventoryrecordService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    private void fillEnterprise(InventoryrecordEntity inventoryrecord, HttpServletRequest request) {
        if (isEnterprise(request)) {
            inventoryrecord.setEnterpriseaccount(currentUsername(request));
        }
    }

    private R normalizeInventoryRecord(InventoryrecordEntity inventoryrecord, Long currentId) {
        if (inventoryrecord.getBatchcode() == null || inventoryrecord.getBatchcode().trim().length() == 0) {
            return R.error("batchcode required");
        }
        if (inventoryrecord.getWarehousecode() == null || inventoryrecord.getWarehousecode().trim().length() == 0) {
            return R.error("warehousecode required");
        }

        TeabatchEntity batch = teabatchService.selectOne(new EntityWrapper<TeabatchEntity>()
                .eq("batchcode", inventoryrecord.getBatchcode()));
        if (batch == null) {
            return R.error("batch not found");
        }
        if (inventoryrecord.getEnterpriseaccount() != null
                && batch.getEnterpriseaccount() != null
                && !Objects.equals(inventoryrecord.getEnterpriseaccount(), batch.getEnterpriseaccount())) {
            return R.error("batch enterprise mismatch");
        }
        inventoryrecord.setEnterpriseaccount(batch.getEnterpriseaccount());
        inventoryrecord.setProductname(batch.getProductname());

        WarehouseInfoEntity warehouse = warehouseInfoService.selectOne(new EntityWrapper<WarehouseInfoEntity>()
                .eq("warehousecode", inventoryrecord.getWarehousecode()));
        if (warehouse == null) {
            return R.error("warehouse not found");
        }
        if (inventoryrecord.getEnterpriseaccount() != null
                && warehouse.getEnterpriseaccount() != null
                && !Objects.equals(inventoryrecord.getEnterpriseaccount(), warehouse.getEnterpriseaccount())) {
            return R.error("warehouse enterprise mismatch");
        }
        inventoryrecord.setWarehousename(warehouse.getWarehousename());

        BigDecimal baseStock = BigDecimal.ZERO;
        List<InventoryrecordEntity> existing = inventoryrecordService.selectList(new EntityWrapper<InventoryrecordEntity>()
                .eq("batchcode", inventoryrecord.getBatchcode())
                .eq("warehousecode", inventoryrecord.getWarehousecode())
                .eq("enterpriseaccount", inventoryrecord.getEnterpriseaccount())
                .orderBy("recordtime", false)
                .orderBy("id", false));
        if (existing != null) {
            for (InventoryrecordEntity item : existing) {
                if (currentId != null && Objects.equals(currentId, item.getId())) {
                    continue;
                }
                baseStock = item.getCurrentstock() == null ? BigDecimal.ZERO : item.getCurrentstock();
                break;
            }
        }

        BigDecimal changeStock = inventoryrecord.getChangestock() == null ? BigDecimal.ZERO : inventoryrecord.getChangestock();
        BigDecimal currentStock = baseStock.add(changeStock);
        if (currentStock.compareTo(BigDecimal.ZERO) < 0) {
            return R.error("current stock cannot be negative");
        }
        inventoryrecord.setCurrentstock(currentStock);
        if (inventoryrecord.getRecordtime() == null) {
            inventoryrecord.setRecordtime(new Date());
        }
        return null;
    }

    private boolean canAccessSupplyChain(HttpServletRequest request) {
        return isAdministrator(request) || isEnterprise(request);
    }

    private boolean canAccessEnterpriseRecord(HttpServletRequest request, String enterpriseAccount) {
        if (isAdministrator(request)) {
            return true;
        }
        return isEnterprise(request) && Objects.equals(currentUsername(request), enterpriseAccount);
    }

    private boolean isAdministrator(HttpServletRequest request) {
        return "users".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }

    private boolean isEnterprise(HttpServletRequest request) {
        return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName")));
    }

    private String currentUsername(HttpServletRequest request) {
        Object username = request.getSession().getAttribute("username");
        return username == null ? null : String.valueOf(username);
    }

    private R forbidden() {
        return R.error(403, "\u65e0\u6743\u8bbf\u95ee");
    }
}
