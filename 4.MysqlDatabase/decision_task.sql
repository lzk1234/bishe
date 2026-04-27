SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `decision_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_type` varchar(64) NOT NULL COMMENT '任务类型',
  `source_type` varchar(64) NOT NULL COMMENT '来源类型',
  `source_key` varchar(255) NOT NULL COMMENT '来源唯一键',
  `merchant_account` varchar(64) NOT NULL COMMENT '商家账号',
  `title` varchar(255) NOT NULL COMMENT '任务标题',
  `summary` varchar(1000) DEFAULT NULL COMMENT '问题说明',
  `action_suggestion` varchar(1000) DEFAULT NULL COMMENT '建议动作',
  `priority` varchar(32) NOT NULL DEFAULT 'medium' COMMENT '优先级',
  `status` varchar(32) NOT NULL DEFAULT 'todo' COMMENT '任务状态',
  `evidence_snapshot` text COMMENT '证据快照',
  `result_note` varchar(1000) DEFAULT NULL COMMENT '处理说明',
  `ignore_reason` varchar(1000) DEFAULT NULL COMMENT '忽略原因',
  `generated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `due_at` datetime DEFAULT NULL COMMENT '截止时间',
  `handled_at` datetime DEFAULT NULL COMMENT '处理时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_decision_task_merchant_status` (`merchant_account`, `status`),
  KEY `idx_decision_task_source` (`source_type`, `source_key`, `merchant_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='决策任务表';
