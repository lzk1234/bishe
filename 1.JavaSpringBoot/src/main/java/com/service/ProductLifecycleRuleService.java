package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ProductLifecycleRuleEntity;
import com.utils.PageUtils;

public interface ProductLifecycleRuleService extends IService<ProductLifecycleRuleEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ProductLifecycleRuleEntity> wrapper);
}
