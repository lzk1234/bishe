package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.InventoryrecordDao;
import com.entity.InventoryrecordEntity;
import com.service.InventoryrecordService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("inventoryrecordService")
public class InventoryrecordServiceImpl extends ServiceImpl<InventoryrecordDao, InventoryrecordEntity> implements InventoryrecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InventoryrecordEntity> page = this.selectPage(new Query<InventoryrecordEntity>(params).getPage(), new EntityWrapper<InventoryrecordEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<InventoryrecordEntity> wrapper) {
        Page<InventoryrecordEntity> page = this.selectPage(new Query<InventoryrecordEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
