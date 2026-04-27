package com.controller;

import java.util.Arrays;
import java.util.Date;
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
import com.entity.WarehouseInfoEntity;
import com.service.WarehouseInfoService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseInfoService warehouseInfoService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, WarehouseInfoEntity warehouse, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (isEnterprise(request)) warehouse.setEnterpriseaccount(currentUsername(request));
        EntityWrapper<WarehouseInfoEntity> wrapper = new EntityWrapper<WarehouseInfoEntity>();
        PageUtils page = warehouseInfoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, warehouse), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        WarehouseInfoEntity warehouse = warehouseInfoService.selectById(id);
        if (warehouse != null && !canAccessEnterpriseRecord(request, warehouse.getEnterpriseaccount())) return forbidden();
        return R.ok().put("data", warehouse);
    }

    @RequestMapping("/save")
    public R save(@RequestBody WarehouseInfoEntity warehouse, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        warehouse.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(warehouse, request);
        warehouseInfoService.insert(warehouse);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody WarehouseInfoEntity warehouse, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (warehouse.getId() != null) {
            WarehouseInfoEntity stored = warehouseInfoService.selectById(warehouse.getId());
            if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) return forbidden();
        }
        fillEnterprise(warehouse, request);
        warehouseInfoService.updateById(warehouse);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        warehouseInfoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    private void fillEnterprise(WarehouseInfoEntity warehouse, HttpServletRequest request) { if (isEnterprise(request)) warehouse.setEnterpriseaccount(currentUsername(request)); }
    private boolean canAccess(HttpServletRequest request) { return isAdministrator(request) || isEnterprise(request); }
    private boolean canAccessEnterpriseRecord(HttpServletRequest request, String enterpriseAccount) { return isAdministrator(request) || (isEnterprise(request) && Objects.equals(currentUsername(request), enterpriseAccount)); }
    private boolean isAdministrator(HttpServletRequest request) { return "users".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private boolean isEnterprise(HttpServletRequest request) { return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private String currentUsername(HttpServletRequest request) { Object username = request.getSession().getAttribute("username"); return username == null ? null : String.valueOf(username); }
    private R forbidden() { return R.error(403, "无权访问"); }
}
