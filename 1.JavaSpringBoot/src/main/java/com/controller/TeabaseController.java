package com.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.entity.TeabaseEntity;
import com.service.TeabaseService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/teabase")
public class TeabaseController {

    @Autowired
    private TeabaseService teabaseService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TeabaseEntity teabase, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            teabase.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<TeabaseEntity> wrapper = new EntityWrapper<TeabaseEntity>();
        PageUtils page = teabaseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, teabase), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, TeabaseEntity teabase, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            teabase.setEnterpriseaccount(currentUsername(request));
        }
        EntityWrapper<TeabaseEntity> wrapper = new EntityWrapper<TeabaseEntity>();
        PageUtils page = teabaseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(wrapper, teabase), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        TeabaseEntity teabase = teabaseService.selectById(id);
        if (teabase != null && !canAccessEnterpriseRecord(request, teabase.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", teabase);
    }

    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        TeabaseEntity teabase = teabaseService.selectById(id);
        if (teabase != null && !canAccessEnterpriseRecord(request, teabase.getEnterpriseaccount())) {
            return forbidden();
        }
        return R.ok().put("data", teabase);
    }

    @RequestMapping("/save")
    public R save(@RequestBody TeabaseEntity teabase, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        teabase.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(teabase, request);
        fillDecisionFields(teabase);
        teabaseService.insert(teabase);
        return R.ok();
    }

    @RequestMapping("/add")
    public R add(@RequestBody TeabaseEntity teabase, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        teabase.setId(new Date().getTime() + (long) Math.floor(Math.random() * 1000));
        fillEnterprise(teabase, request);
        fillDecisionFields(teabase);
        teabaseService.insert(teabase);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody TeabaseEntity teabase, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (teabase.getId() != null) {
            TeabaseEntity stored = teabaseService.selectById(teabase.getId());
            if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                return forbidden();
            }
        }
        fillEnterprise(teabase, request);
        fillDecisionFields(teabase);
        teabaseService.updateById(teabase);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        if (isEnterprise(request)) {
            for (Long id : ids) {
                TeabaseEntity stored = teabaseService.selectById(id);
                if (stored != null && !canAccessEnterpriseRecord(request, stored.getEnterpriseaccount())) {
                    return forbidden();
                }
            }
        }
        teabaseService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/regionStats")
    public R regionStats(HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        StringBuilder sql = new StringBuilder(
            "select coalesce(regiontag, location, '未分区') as regionName, count(*) as baseCount, " +
            "coalesce(sum(area),0) as totalArea, coalesce(sum(annualcapacity),0) as annualCapacity, " +
            "coalesce(avg(altitude),0) as avgAltitude from teabase where 1=1"
        );
        if (isEnterprise(request)) {
            return R.ok().put("data", jdbcTemplate.queryForList(sql.append(" and enterpriseaccount = ? group by coalesce(regiontag, location, '未分区') order by annualCapacity desc").toString(), currentUsername(request)));
        }
        return R.ok().put("data", jdbcTemplate.queryForList(sql.append(" group by coalesce(regiontag, location, '未分区') order by annualCapacity desc").toString()));
    }

    @RequestMapping("/capacitySummary")
    public R capacitySummary(HttpServletRequest request) {
        if (!canAccessSupplyChain(request)) {
            return forbidden();
        }
        String sql = "select count(*) as baseCount, coalesce(sum(area),0) as totalArea, coalesce(sum(annualcapacity),0) as annualCapacity, " +
            "sum(case when altitude >= 800 then 1 else 0 end) as highAltitudeBaseCount from teabase";
        if (isEnterprise(request)) {
            return R.ok().put("data", jdbcTemplate.queryForMap(sql + " where enterpriseaccount = ?", currentUsername(request)));
        }
        return R.ok().put("data", jdbcTemplate.queryForMap(sql));
    }

    private void fillEnterprise(TeabaseEntity teabase, HttpServletRequest request) {
        if (isEnterprise(request)) {
            teabase.setEnterpriseaccount(currentUsername(request));
        }
    }

    private void fillDecisionFields(TeabaseEntity teabase) {
        if (isBlank(teabase.getRegiontag())) {
            teabase.setRegiontag(teabase.getLocation());
        }
        if (teabase.getAnnualcapacity() == null) {
            teabase.setAnnualcapacity(BigDecimal.ZERO);
        }
        if (isBlank(teabase.getMainvariety())) {
            teabase.setMainvariety(teabase.getTeatype());
        }
        if (teabase.getPlantingyear() == null) {
            teabase.setPlantingyear(Calendar.getInstance().get(Calendar.YEAR));
        }
        if (isBlank(teabase.getBasestatus())) {
            teabase.setBasestatus("正常");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
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
