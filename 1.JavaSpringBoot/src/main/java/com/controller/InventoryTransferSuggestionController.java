package com.controller;

import java.util.Arrays;
import java.util.Date;
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
import com.entity.InventoryTransferSuggestionEntity;
import com.service.InventoryTransferSuggestionService;
import com.service.WarehouseCoordinationService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/inventoryTransferSuggestion")
public class InventoryTransferSuggestionController {

    @Autowired
    private InventoryTransferSuggestionService inventoryTransferSuggestionService;

    @Autowired
    private WarehouseCoordinationService warehouseCoordinationService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, InventoryTransferSuggestionEntity suggestion, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (isEnterprise(request)) suggestion.setEnterpriseaccount(currentUsername(request));
        EntityWrapper<InventoryTransferSuggestionEntity> wrapper = new EntityWrapper<InventoryTransferSuggestionEntity>();
        PageUtils page = inventoryTransferSuggestionService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, suggestion), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        InventoryTransferSuggestionEntity item = inventoryTransferSuggestionService.selectById(id);
        if (item != null && !canAccessEnterpriseRecord(request, item.getEnterpriseaccount())) return forbidden();
        return R.ok().put("data", item);
    }

    @RequestMapping("/save")
    public R save(@RequestBody InventoryTransferSuggestionEntity item, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        item.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(item, request);
        inventoryTransferSuggestionService.insert(item);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody InventoryTransferSuggestionEntity item, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        fillEnterprise(item, request);
        inventoryTransferSuggestionService.updateById(item);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        inventoryTransferSuggestionService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/generate")
    public R generate(HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        List<Map<String, Object>> suggestions = warehouseCoordinationService.generateTransferSuggestions(getEnterpriseAccount(request));
        return R.ok().put("data", suggestions);
    }

    @RequestMapping("/confirm/{id}")
    public R confirm(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        InventoryTransferSuggestionEntity item = inventoryTransferSuggestionService.selectById(id);
        if (item == null) return R.error("调拨建议不存在");
        if (!canAccessEnterpriseRecord(request, item.getEnterpriseaccount())) return forbidden();
        item.setStatus("已确认");
        inventoryTransferSuggestionService.updateById(item);
        return R.ok();
    }

    private void fillEnterprise(InventoryTransferSuggestionEntity item, HttpServletRequest request) { if (isEnterprise(request)) item.setEnterpriseaccount(currentUsername(request)); }
    private boolean canAccess(HttpServletRequest request) { return isAdministrator(request) || isEnterprise(request); }
    private boolean canAccessEnterpriseRecord(HttpServletRequest request, String enterpriseAccount) { return isAdministrator(request) || (isEnterprise(request) && Objects.equals(currentUsername(request), enterpriseAccount)); }
    private boolean isAdministrator(HttpServletRequest request) { return "users".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private boolean isEnterprise(HttpServletRequest request) { return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private String currentUsername(HttpServletRequest request) { Object username = request.getSession().getAttribute("username"); return username == null ? null : String.valueOf(username); }
    private String getEnterpriseAccount(HttpServletRequest request) { return isEnterprise(request) ? currentUsername(request) : null; }
    private R forbidden() { return R.error(403, "无权访问"); }
}
