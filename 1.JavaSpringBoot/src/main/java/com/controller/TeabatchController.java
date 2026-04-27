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
import com.entity.ShangpinxinxiEntity;
import com.entity.TeabaseEntity;
import com.entity.TeabatchEntity;
import com.service.ShangpinxinxiService;
import com.service.TeabaseService;
import com.service.TeabatchService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/teabatch")
public class TeabatchController {

    @Autowired
    private TeabatchService teabatchService;

    @Autowired
    private TeabaseService teabaseService;

    @Autowired
    private ShangpinxinxiService shangpinxinxiService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TeabatchEntity teabatch, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            teabatch.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<TeabatchEntity> wrapper = new EntityWrapper<TeabatchEntity>();
        PageUtils page = teabatchService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, teabatch), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, TeabatchEntity teabatch, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            teabatch.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<TeabatchEntity> wrapper = new EntityWrapper<TeabatchEntity>();
        PageUtils page = teabatchService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, teabatch), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        TeabatchEntity teabatch = teabatchService.selectById(id);
        if (teabatch != null && !canAccessEnterpriseRecord(request, teabatch.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", teabatch);
    }

    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        TeabatchEntity teabatch = teabatchService.selectById(id);
        if (teabatch != null && !canAccessEnterpriseRecord(request, teabatch.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", teabatch);
    }

    @RequestMapping("/save")
    public R save(@RequestBody TeabatchEntity teabatch, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        teabatch.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(teabatch, request);
        R invalid = normalizeTeabatch(teabatch);
        if (invalid != null) {
            return invalid;
        }
        teabatchService.insert(teabatch);
        return R.ok();
    }

    @RequestMapping("/add")
    public R add(@RequestBody TeabatchEntity teabatch, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        teabatch.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(teabatch, request);
        R invalid = normalizeTeabatch(teabatch);
        if (invalid != null) {
            return invalid;
        }
        teabatchService.insert(teabatch);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody TeabatchEntity teabatch, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (teabatch.getId() != null) {
            TeabatchEntity stored = teabatchService.selectById(teabatch.getId());
            if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                return forbidden();
            }
        }
        fillEnterprise(teabatch, request);
        R invalid = normalizeTeabatch(teabatch);
        if (invalid != null) {
            return invalid;
        }
        teabatchService.updateById(teabatch);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            for (Long id : ids) {
                TeabatchEntity stored = teabatchService.selectById(id);
                if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                    return forbidden();
                }
            }
        }
        teabatchService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    private void fillEnterprise(TeabatchEntity teabatch, HttpServletRequest request) {
        if (isEnterprise(request)) {
            teabatch.setEnterpriseaccount(currentUsername(request));
        }
    }

    private R normalizeTeabatch(TeabatchEntity teabatch) {
        if (teabatch.getEnterpriseaccount() == null || teabatch.getEnterpriseaccount().trim().length() == 0) {
            return R.error("enterpriseaccount required");
        }
        if (teabatch.getBaseid() == null) {
            return R.error("baseid required");
        }
        if (teabatch.getProductid() == null) {
            return R.error("productid required");
        }

        TeabaseEntity base = teabaseService.selectById(teabatch.getBaseid());
        if (base == null) {
            return R.error("base not found for enterprise");
        }
        if (!Objects.equals(teabatch.getEnterpriseaccount(), base.getEnterpriseaccount())) {
            return R.error("base enterprise mismatch");
        }

        ShangpinxinxiEntity product = shangpinxinxiService.selectById(teabatch.getProductid());
        if (product == null) {
            return R.error("product not found for enterprise");
        }
        if (!Objects.equals(teabatch.getEnterpriseaccount(), product.getZhanghao())) {
            return R.error("product enterprise mismatch");
        }

        teabatch.setBasename(base.getBasename());
        teabatch.setProductname(product.getShangpinmingcheng());
        teabatch.setAltitude(base.getAltitude());
        teabatch.setTeatype(product.getShangpinfenlei());
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
