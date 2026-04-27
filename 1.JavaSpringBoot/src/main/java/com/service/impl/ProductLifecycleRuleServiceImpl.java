package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ProductLifecycleRuleDao;
import com.entity.ProductLifecycleRuleEntity;
import com.service.ProductLifecycleRuleService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("productLifecycleRuleService")
public class ProductLifecycleRuleServiceImpl extends ServiceImpl<ProductLifecycleRuleDao, ProductLifecycleRuleEntity> implements ProductLifecycleRuleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ProductLifecycleRuleEntity> page = this.selectPage(new Query<ProductLifecycleRuleEntity>(params).getPage(), new EntityWrapper<ProductLifecycleRuleEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ProductLifecycleRuleEntity> wrapper) {
        Page<ProductLifecycleRuleEntity> page = this.selectPage(new Query<ProductLifecycleRuleEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
