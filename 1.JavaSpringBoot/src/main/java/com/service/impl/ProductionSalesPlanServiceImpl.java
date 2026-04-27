package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ProductionSalesPlanDao;
import com.entity.ProductionSalesPlanEntity;
import com.service.ProductionSalesPlanService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("productionSalesPlanService")
public class ProductionSalesPlanServiceImpl extends ServiceImpl<ProductionSalesPlanDao, ProductionSalesPlanEntity> implements ProductionSalesPlanService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProductionSalesPlanEntity> page = this.selectPage(new Query<ProductionSalesPlanEntity>(params).getPage(), new EntityWrapper<ProductionSalesPlanEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ProductionSalesPlanEntity> wrapper) {
        Page<ProductionSalesPlanEntity> page = this.selectPage(new Query<ProductionSalesPlanEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
