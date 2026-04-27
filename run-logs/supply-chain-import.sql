ALTER TABLE `shangpinxinxi`
  ADD COLUMN IF NOT EXISTS `chandi` varchar(200) DEFAULT NULL COMMENT '产地',
  ADD COLUMN IF NOT EXISTS `haiba` int DEFAULT NULL COMMENT '海拔',
  ADD COLUMN IF NOT EXISTS `shengchanpici` varchar(100) DEFAULT NULL COMMENT '生产批次',
  ADD COLUMN IF NOT EXISTS `shiyongchangjing` varchar(200) DEFAULT NULL COMMENT '适饮场景';

UPDATE `shangpinxinxi`
SET `chandi` = '武夷山高山茶园',
    `haiba` = 860,
    `shengchanpici` = 'PC202604-A01',
    `shiyongchangjing` = '商务接待、礼赠收藏'
WHERE `id` = 42;

UPDATE `shangpinxinxi`
SET `chandi` = '黄山生态茶园',
    `haiba` = 720,
    `shengchanpici` = 'PC202604-A02',
    `shiyongchangjing` = '日常品饮、办公室冲泡'
WHERE `id` = 43;

UPDATE `shangpinxinxi`
SET `chandi` = '高山示范茶园',
    `haiba` = 680,
    `shengchanpici` = 'PC202604-A03',
    `shiyongchangjing` = '新客体验、门店试饮'
WHERE `chandi` IS NULL;

DROP TABLE IF EXISTS `teabase`;
CREATE TABLE `teabase` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `basecode` varchar(64) NOT NULL COMMENT '基地编号',
  `basename` varchar(200) NOT NULL COMMENT '基地名称',
  `location` varchar(255) NOT NULL COMMENT '基地位置',
  `altitude` int DEFAULT NULL COMMENT '海拔',
  `area` decimal(10,2) DEFAULT NULL COMMENT '面积(亩)',
  `principal` varchar(100) DEFAULT NULL COMMENT '负责人',
  `contactphone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `teatype` varchar(100) DEFAULT NULL COMMENT '主栽茶类',
  `certification` varchar(255) DEFAULT NULL COMMENT '认证信息',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teabase_code` (`basecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='茶园基地';

INSERT INTO `teabase` (`id`,`basecode`,`basename`,`location`,`altitude`,`area`,`principal`,`contactphone`,`teatype`,`certification`,`enterpriseaccount`,`remark`)
VALUES
  (202604220001,'TB-001','云岭一号高山茶园','福建省武夷山市星村镇',860,128.50,'陈建国','13800000001','高山绿茶','绿色食品认证','1','重点展示基地'),
  (202604220002,'TB-002','清风生态茶园','安徽省黄山市歙县',720,96.00,'李春明','13800000002','高山红茶','有机转换认证','账号4','适合作为产地展示样板');

DROP TABLE IF EXISTS `teabatch`;
CREATE TABLE `teabatch` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `batchcode` varchar(64) NOT NULL COMMENT '批次编号',
  `basename` varchar(200) NOT NULL COMMENT '基地名称',
  `productname` varchar(200) NOT NULL COMMENT '茶品名称',
  `teatype` varchar(100) DEFAULT NULL COMMENT '茶类',
  `pickingdate` date DEFAULT NULL COMMENT '采摘日期',
  `freshweight` decimal(10,2) DEFAULT NULL COMMENT '鲜叶重量(kg)',
  `processmethod` varchar(200) DEFAULT NULL COMMENT '加工方式',
  `finishedweight` decimal(10,2) DEFAULT NULL COMMENT '成品重量(kg)',
  `batchstatus` varchar(100) DEFAULT NULL COMMENT '批次状态',
  `altitude` int DEFAULT NULL COMMENT '海拔',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teabatch_code` (`batchcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产批次';

INSERT INTO `teabatch` (`id`,`batchcode`,`basename`,`productname`,`teatype`,`pickingdate`,`freshweight`,`processmethod`,`finishedweight`,`batchstatus`,`altitude`,`enterpriseaccount`,`remark`)
VALUES
  (202604220101,'PC202604-A01','云岭一号高山茶园','高山云雾绿茶','绿茶','2026-04-10',520.00,'鲜叶摊青+杀青+烘焙',118.00,'已入库',860,'1','用于春茶主推展示'),
  (202604220102,'PC202604-A02','清风生态茶园','高山蜜香红茶','红茶','2026-04-12',460.00,'萎凋+揉捻+发酵+烘干',105.00,'加工完成',720,'账号4','适合在客户端展示批次信息');

DROP TABLE IF EXISTS `inventoryrecord`;
CREATE TABLE `inventoryrecord` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `batchcode` varchar(64) NOT NULL COMMENT '批次编号',
  `productname` varchar(200) NOT NULL COMMENT '茶品名称',
  `recordtype` varchar(100) NOT NULL COMMENT '记录类型',
  `changestock` decimal(10,2) DEFAULT NULL COMMENT '变动数量(kg)',
  `currentstock` decimal(10,2) DEFAULT NULL COMMENT '当前库存(kg)',
  `warningstock` decimal(10,2) DEFAULT NULL COMMENT '预警库存(kg)',
  `recordtime` datetime DEFAULT NULL COMMENT '记录时间',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `relatedorderid` varchar(100) DEFAULT NULL COMMENT '关联订单号',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存台账';

INSERT INTO `inventoryrecord` (`id`,`batchcode`,`productname`,`recordtype`,`changestock`,`currentstock`,`warningstock`,`recordtime`,`enterpriseaccount`,`relatedorderid`,`remark`)
VALUES
  (202604220201,'PC202604-A01','高山云雾绿茶','入库',118.00,118.00,40.00,'2026-04-15 09:00:00','1',NULL,'春茶批次首次入库'),
  (202604220202,'PC202604-A01','高山云雾绿茶','出库',-82.00,36.00,40.00,'2026-04-20 15:30:00','1','20260422001','库存已接近预警线'),
  (202604220203,'PC202604-A02','高山蜜香红茶','入库',105.00,105.00,35.00,'2026-04-16 10:00:00','账号4',NULL,'红茶批次入库');
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

