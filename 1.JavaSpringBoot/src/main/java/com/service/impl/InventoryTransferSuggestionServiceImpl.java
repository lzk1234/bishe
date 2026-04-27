package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.InventoryTransferSuggestionDao;
import com.entity.InventoryTransferSuggestionEntity;
import com.service.InventoryTransferSuggestionService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("inventoryTransferSuggestionService")
public class InventoryTransferSuggestionServiceImpl extends ServiceImpl<InventoryTransferSuggestionDao, InventoryTransferSuggestionEntity> implements InventoryTransferSuggestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<InventoryTransferSuggestionEntity> page = this.selectPage(new Query<InventoryTransferSuggestionEntity>(params).getPage(), new EntityWrapper<InventoryTransferSuggestionEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<InventoryTransferSuggestionEntity> wrapper) {
        Page<InventoryTransferSuggestionEntity> page = this.selectPage(new Query<InventoryTransferSuggestionEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
