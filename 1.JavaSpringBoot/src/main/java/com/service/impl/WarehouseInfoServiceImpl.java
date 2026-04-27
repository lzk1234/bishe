package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.WarehouseInfoDao;
import com.entity.WarehouseInfoEntity;
import com.service.WarehouseInfoService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("warehouseInfoService")
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoDao, WarehouseInfoEntity> implements WarehouseInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<WarehouseInfoEntity> page = this.selectPage(new Query<WarehouseInfoEntity>(params).getPage(), new EntityWrapper<WarehouseInfoEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<WarehouseInfoEntity> wrapper) {
        Page<WarehouseInfoEntity> page = this.selectPage(new Query<WarehouseInfoEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
