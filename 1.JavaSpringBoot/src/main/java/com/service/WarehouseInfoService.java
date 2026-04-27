package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.WarehouseInfoEntity;
import com.utils.PageUtils;

public interface WarehouseInfoService extends IService<WarehouseInfoEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<WarehouseInfoEntity> wrapper);
}
