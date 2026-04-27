-- 推荐系统数据库表

-- 用户行为记录表
DROP TABLE IF EXISTS `user_behavior`;
CREATE TABLE `user_behavior` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `goodid` bigint(20) NOT NULL COMMENT '茶叶ID',
  `behavior_type` varchar(20) NOT NULL COMMENT '行为类型：view-浏览,favorite-收藏,buy-购买',
  `addtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '行为时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_good` (`userid`, `goodid`),
  KEY `idx_addtime` (`addtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为记录表';

-- 推荐结果表
DROP TABLE IF EXISTS `recommendation`;
CREATE TABLE `recommendation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL COMMENT '用户ID',
  `goodid` bigint(20) NOT NULL COMMENT '茶叶ID',
  `score` decimal(10,4) NOT NULL COMMENT '推荐分数',
  `reason` varchar(100) DEFAULT NULL COMMENT '推荐理由',
  `algorithm_type` varchar(50) NOT NULL COMMENT '算法类型：collaborative-协同过滤,content-内容推荐,hot-热门',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_score` (`userid`, `score` DESC),
  KEY `idx_goodid` (`goodid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐结果表';
