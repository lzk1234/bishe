package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ProductionSalesPlanEntity;
import com.service.ProductionSalesPlanAnalysisService;
import com.service.ProductionSalesPlanService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/productionSalesPlan")
public class ProductionSalesPlanController {

    @Autowired
    private ProductionSalesPlanService productionSalesPlanService;

    @Autowired
    private ProductionSalesPlanAnalysisService productionSalesPlanAnalysisService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ProductionSalesPlanEntity plan, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (isEnterprise(request)) plan.setEnterpriseaccount(currentUsername(request));
        EntityWrapper<ProductionSalesPlanEntity> wrapper = new EntityWrapper<ProductionSalesPlanEntity>();
        PageUtils page = productionSalesPlanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, plan), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        ProductionSalesPlanEntity plan = productionSalesPlanService.selectById(id);
        if (plan != null && !canAccessEnterpriseRecord(request, plan.getEnterpriseaccount())) return forbidden();
        return R.ok().put("data", plan);
    }

    @RequestMapping("/save")
    public R save(@RequestBody ProductionSalesPlanEntity plan, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        plan.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(plan, request);
        productionSalesPlanService.insert(plan);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody ProductionSalesPlanEntity plan, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (plan.getId() != null) {
            ProductionSalesPlanEntity stored = productionSalesPlanService.selectById(plan.getId());
            if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) return forbidden();
        }
        fillEnterprise(plan, request);
        productionSalesPlanService.updateById(plan);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (isEnterprise(request)) {
            for (Long id : ids) {
                ProductionSalesPlanEntity stored = productionSalesPlanService.selectById(id);
                if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) return forbidden();
            }
        }
        productionSalesPlanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/annualBoard")
    public R annualBoard(@RequestParam(required = false) Integer year, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        return R.ok().put("data", productionSalesPlanAnalysisService.buildAnnualBoard(getEnterpriseAccount(request), year));
    }

    @RequestMapping("/stockSummary")
    public R stockSummary(@RequestParam String productname, @RequestParam(required = false) String enterpriseaccount, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        String account = isEnterprise(request) ? currentUsername(request) : enterpriseaccount;
        StringBuilder sql = new StringBuilder("select coalesce(sum(currentstock),0) from inventoryrecord where productname = ?");
        BigDecimal stock;
        if (account == null || account.trim().length() == 0) {
            stock = jdbcTemplate.queryForObject(sql.toString(), new Object[] { productname }, BigDecimal.class);
        } else {
            stock = jdbcTemplate.queryForObject(sql.append(" and enterpriseaccount = ?").toString(), new Object[] { productname, account }, BigDecimal.class);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productname", productname);
        data.put("enterpriseaccount", account);
        data.put("currentStock", stock == null ? BigDecimal.ZERO : stock);
        return R.ok().put("data", data);
    }

    private void fillEnterprise(ProductionSalesPlanEntity plan, HttpServletRequest request) {
        if (isEnterprise(request)) plan.setEnterpriseaccount(currentUsername(request));
    }
    private boolean canAccess(HttpServletRequest request) { return isAdministrator(request) || isEnterprise(request); }
    private boolean canAccessEnterpriseRecord(HttpServletRequest request, String enterpriseAccount) { return isAdministrator(request) || (isEnterprise(request) && Objects.equals(currentUsername(request), enterpriseAccount)); }
    private boolean isAdministrator(HttpServletRequest request) { return "users".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private boolean isEnterprise(HttpServletRequest request) { return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private String currentUsername(HttpServletRequest request) { Object username = request.getSession().getAttribute("username"); return username == null ? null : String.valueOf(username); }
    private String getEnterpriseAccount(HttpServletRequest request) { return isEnterprise(request) ? currentUsername(request) : null; }
    private R forbidden() { return R.error(403, "无权访问"); }
}
