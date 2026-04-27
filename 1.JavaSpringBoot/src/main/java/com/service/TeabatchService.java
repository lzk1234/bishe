package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.TeabatchEntity;
import com.utils.PageUtils;

public interface TeabatchService extends IService<TeabatchEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Wrapper<TeabatchEntity> wrapper);
}
