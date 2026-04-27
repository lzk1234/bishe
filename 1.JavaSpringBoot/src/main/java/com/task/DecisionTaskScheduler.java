package com.task;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.service.DecisionTaskService;

@Component
public class DecisionTaskScheduler {

    @Resource
    private DecisionTaskService decisionTaskService;

    @Scheduled(cron = "0 0 * * * ?")
    public void refreshDecisionTasks() {
        System.out.println("开始刷新经营任务: " + new Date());
        try {
            decisionTaskService.generateTasksForAll();
            decisionTaskService.refreshExpiredTasks();
            System.out.println("经营任务刷新完成: " + new Date());
        } catch (Exception e) {
            System.out.println("经营任务刷新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
