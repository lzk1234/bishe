package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.TeabaseDao;
import com.entity.TeabaseEntity;
import com.service.TeabaseService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("teabaseService")
public class TeabaseServiceImpl extends ServiceImpl<TeabaseDao, TeabaseEntity> implements TeabaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TeabaseEntity> page = this.selectPage(new Query<TeabaseEntity>(params).getPage(), new EntityWrapper<TeabaseEntity>());
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<TeabaseEntity> wrapper) {
        Page<TeabaseEntity> page = this.selectPage(new Query<TeabaseEntity>(params).getPage(), wrapper);
        return new PageUtils(page);
    }
}
