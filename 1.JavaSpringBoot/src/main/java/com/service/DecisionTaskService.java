package com.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.entity.DecisionTaskEntity;
import com.utils.PageUtils;

public interface DecisionTaskService extends IService<DecisionTaskEntity> {

    PageUtils queryTaskPage(Map<String, Object> params, String merchantAccount);

    List<DecisionTaskEntity> listHighlightedTasks(String merchantAccount, Integer limit);

    Map<String, Object> buildTaskStats(String merchantAccount);

    Map<String, Object> buildWeeklyReview(String merchantAccount);

    DecisionTaskEntity getTaskDetail(Long id, String merchantAccount, boolean admin);

    DecisionTaskEntity startTask(Long id, String merchantAccount, boolean admin);

    DecisionTaskEntity completeTask(Long id, String merchantAccount, boolean admin, String resultNote);

    DecisionTaskEntity ignoreTask(Long id, String merchantAccount, boolean admin, String ignoreReason);

    void generateTasksForMerchant(String merchantAccount);

    void generateTasksForAll();

    void refreshExpiredTasks();
}
