package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ProductionSalesPlanEntity;
import com.utils.PageUtils;

public interface ProductionSalesPlanService extends IService<ProductionSalesPlanEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ProductionSalesPlanEntity> wrapper);
}
