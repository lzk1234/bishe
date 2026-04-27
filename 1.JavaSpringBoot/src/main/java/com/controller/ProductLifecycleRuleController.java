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
import com.entity.ProductLifecycleRuleEntity;
import com.entity.ShangpinxinxiEntity;
import com.service.LifecycleDecisionService;
import com.service.ProductLifecycleRuleService;
import com.service.ShangpinxinxiService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/productLifecycleRule")
public class ProductLifecycleRuleController {

    @Autowired
    private ProductLifecycleRuleService productLifecycleRuleService;

    @Autowired
    private LifecycleDecisionService lifecycleDecisionService;

    @Autowired
    private ShangpinxinxiService shangpinxinxiService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ProductLifecycleRuleEntity rule, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        if (isEnterprise(request)) rule.setEnterpriseaccount(currentUsername(request));
        EntityWrapper<ProductLifecycleRuleEntity> wrapper = new EntityWrapper<ProductLifecycleRuleEntity>();
        PageUtils page = productLifecycleRuleService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, rule), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        ProductLifecycleRuleEntity rule = productLifecycleRuleService.selectById(id);
        if (rule != null && !canAccessEnterpriseRecord(request, rule.getEnterpriseaccount())) return forbidden();
        return R.ok().put("data", rule);
    }

    @RequestMapping("/save")
    public R save(@RequestBody ProductLifecycleRuleEntity rule, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        rule.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(rule, request);
        R invalid = normalizeLifecycleProduct(rule);
        if (invalid != null) return invalid;
        productLifecycleRuleService.insert(rule);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody ProductLifecycleRuleEntity rule, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        fillEnterprise(rule, request);
        R invalid = normalizeLifecycleProduct(rule);
        if (invalid != null) return invalid;
        productLifecycleRuleService.updateById(rule);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        productLifecycleRuleService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/todayStatus")
    public R todayStatus(HttpServletRequest request) {
        if (!canAccess(request)) return forbidden();
        return R.ok().put("data", lifecycleDecisionService.buildTodayStatus(getEnterpriseAccount(request)));
    }

    private void fillEnterprise(ProductLifecycleRuleEntity rule, HttpServletRequest request) { if (isEnterprise(request)) rule.setEnterpriseaccount(currentUsername(request)); }
    private R normalizeLifecycleProduct(ProductLifecycleRuleEntity rule) {
        if (rule.getProductid() == null) return R.error("productid required");
        ShangpinxinxiEntity product = shangpinxinxiService.selectById(rule.getProductid());
        if (product == null) return R.error("product not found");
        if (rule.getEnterpriseaccount() != null && product.getZhanghao() != null && !Objects.equals(rule.getEnterpriseaccount(), product.getZhanghao())) {
            return R.error("product enterprise mismatch");
        }
        rule.setEnterpriseaccount(product.getZhanghao());
        rule.setProductname(product.getShangpinmingcheng());
        rule.setTeatype(product.getShangpinfenlei());
        return null;
    }
    private boolean canAccess(HttpServletRequest request) { return isAdministrator(request) || isEnterprise(request); }
    private boolean canAccessEnterpriseRecord(HttpServletRequest request, String enterpriseAccount) { return isAdministrator(request) || (isEnterprise(request) && Objects.equals(currentUsername(request), enterpriseAccount)); }
    private boolean isAdministrator(HttpServletRequest request) { return "users".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private boolean isEnterprise(HttpServletRequest request) { return "shangjia".equals(String.valueOf(request.getSession().getAttribute("tableName"))); }
    private String currentUsername(HttpServletRequest request) { Object username = request.getSession().getAttribute("username"); return username == null ? null : String.valueOf(username); }
    private String getEnterpriseAccount(HttpServletRequest request) { return isEnterprise(request) ? currentUsername(request) : null; }
    private R forbidden() { return R.error(403, "无权访问"); }
}
