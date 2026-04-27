package com.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Date;

import com.service.RecommendationService;
import com.dao.RecommendationDao;

/**
 * 推荐系统定时任务
 * 每天凌晨刷新推荐结果
 * @author 
 * @date 2024-01-01
 */
@Component
public class RecommendationTask {
    
    @Resource
    private RecommendationService recommendationService;
    
    @Resource
    private RecommendationDao recommendationDao;
    
    /**
     * 每天凌晨2点刷新所有用户的推荐结果
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshAllRecommendations() {
        System.out.println("开始刷新推荐结果: " + new Date());
        
        try {
            // 删除7天前的旧推荐结果
            recommendationDao.deleteOldRecommendations(7);
            
            // 为所有活跃用户重新生成推荐
            recommendationService.generateRecommendationsForAll();
            
            System.out.println("推荐结果刷新完成: " + new Date());
        } catch (Exception e) {
            System.out.println("推荐结果刷新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 每天凌晨3点清理30天前的推荐结果
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanOldRecommendations() {
        System.out.println("开始清理旧推荐结果: " + new Date());
        
        try {
            // 删除30天前的推荐结果
            recommendationDao.deleteOldRecommendations(30);
            
            System.out.println("旧推荐结果清理完成: " + new Date());
        } catch (Exception e) {
            System.out.println("旧推荐结果清理失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
