package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.InventoryrecordEntity;
import com.utils.PageUtils;

public interface InventoryrecordService extends IService<InventoryrecordEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<InventoryrecordEntity> wrapper);
}
