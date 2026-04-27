package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.TeabaseEntity;
import com.utils.PageUtils;

public interface TeabaseService extends IService<TeabaseEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<TeabaseEntity> wrapper);
}
