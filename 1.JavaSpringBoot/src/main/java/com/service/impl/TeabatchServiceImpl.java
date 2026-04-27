package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.TeabatchDao;
import com.entity.TeabatchEntity;
import com.service.TeabatchService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("teabatchService")
public class TeabatchServiceImpl extends ServiceImpl<TeabatchDao, TeabatchEntity> implements TeabatchService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TeabatchEntity> page = this.selectPage(new Query<TeabatchEntity>(params).getPage(), new EntityWrapper<TeabatchEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<TeabatchEntity> wrapper) {
        Page<TeabatchEntity> page = this.selectPage(new Query<TeabatchEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
