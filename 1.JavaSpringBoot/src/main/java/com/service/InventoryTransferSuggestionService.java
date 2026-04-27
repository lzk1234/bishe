package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.InventoryTransferSuggestionEntity;
import com.utils.PageUtils;

public interface InventoryTransferSuggestionService extends IService<InventoryTransferSuggestionEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<InventoryTransferSuggestionEntity> wrapper);
}
