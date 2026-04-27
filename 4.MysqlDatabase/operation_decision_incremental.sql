SET NAMES utf8mb4;

ALTER TABLE `teabase`
  ADD COLUMN `regiontag` varchar(100) DEFAULT NULL COMMENT '产区标签',
  ADD COLUMN `annualcapacity` decimal(12,2) DEFAULT NULL COMMENT '预计年产能(kg)',
  ADD COLUMN `mainvariety` varchar(100) DEFAULT NULL COMMENT '主栽品种',
  ADD COLUMN `plantingyear` int DEFAULT NULL COMMENT '建园年份',
  ADD COLUMN `basestatus` varchar(50) DEFAULT NULL COMMENT '基地状态';

ALTER TABLE `inventoryrecord`
  ADD COLUMN `warehousecode` varchar(64) DEFAULT NULL COMMENT '仓库编号',
  ADD COLUMN `warehousename` varchar(200) DEFAULT NULL COMMENT '仓库名称';

UPDATE `teabase`
SET `regiontag` = '武夷高山产区',
    `annualcapacity` = 5200.00,
    `mainvariety` = '福鼎大白茶',
    `plantingyear` = 2014,
    `basestatus` = '正常'
WHERE `basecode` = 'TB-001';

UPDATE `teabase`
SET `regiontag` = '黄山云雾产区',
    `annualcapacity` = 3800.00,
    `mainvariety` = '祁门槠叶种',
    `plantingyear` = 2017,
    `basestatus` = '正常'
WHERE `basecode` = 'TB-002';

UPDATE `inventoryrecord`
SET `warehousecode` = 'WH-A8-01',
    `warehousename` = '账号8总仓'
WHERE `id` = 202604220201;

UPDATE `inventoryrecord`
SET `warehousecode` = 'WH-A8-02',
    `warehousename` = '账号8电商仓'
WHERE `id` = 202604220202;

UPDATE `inventoryrecord`
SET `warehousecode` = 'WH-A4-01',
    `warehousename` = '账号4总仓'
WHERE `id` = 202604220203;

INSERT INTO `inventoryrecord` (`id`,`batchcode`,`productname`,`recordtype`,`changestock`,`currentstock`,`warningstock`,`warehousecode`,`warehousename`,`recordtime`,`enterpriseaccount`,`relatedorderid`,`remark`)
SELECT 202604220204,'PC202604-A01','茶叶名称2','盘点',0.00,126.00,40.00,'WH-A8-01','账号8总仓','2026-04-21 10:00:00','账号8',NULL,'总仓库存充足可调拨'
WHERE NOT EXISTS (SELECT 1 FROM `inventoryrecord` WHERE `id` = 202604220204);

UPDATE `teabase`
SET
  `regiontag` = COALESCE(`regiontag`, `location`),
  `annualcapacity` = COALESCE(`annualcapacity`, 0),
  `mainvariety` = COALESCE(`mainvariety`, `teatype`),
  `plantingyear` = COALESCE(`plantingyear`, YEAR(CURDATE())),
  `basestatus` = COALESCE(`basestatus`, '正常')
WHERE `regiontag` IS NULL
   OR `annualcapacity` IS NULL
   OR `mainvariety` IS NULL
   OR `plantingyear` IS NULL
   OR `basestatus` IS NULL;

CREATE TABLE IF NOT EXISTS `production_sales_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `planyear` int NOT NULL COMMENT '计划年份',
  `planmonth` int NOT NULL COMMENT '计划月份',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `productid` bigint DEFAULT NULL COMMENT '茶品ID',
  `productname` varchar(200) NOT NULL COMMENT '茶品名称',
  `teatype` varchar(100) DEFAULT NULL COMMENT '茶类',
  `plannedoutput` decimal(12,2) DEFAULT NULL COMMENT '计划产量(kg)',
  `plannedsales` decimal(12,2) DEFAULT NULL COMMENT '计划销量(kg)',
  `targetinventory` decimal(12,2) DEFAULT NULL COMMENT '目标库存(kg)',
  `plannedrevenue` decimal(12,2) DEFAULT NULL COMMENT '计划收入',
  `risklevel` varchar(50) DEFAULT NULL COMMENT '风险等级',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年度产销计划';

INSERT INTO `production_sales_plan` (`id`,`planyear`,`planmonth`,`enterpriseaccount`,`productid`,`productname`,`teatype`,`plannedoutput`,`plannedsales`,`targetinventory`,`plannedrevenue`,`risklevel`,`remark`)
VALUES
  (202604260101,2026,4,'账号8',42,'茶叶名称2','高山绿茶',120.00,180.00,20.00,16800.00,'warning','春茶主销月，存在供需缺口'),
  (202604260102,2026,5,'账号8',42,'茶叶名称2','高山绿茶',180.00,150.00,50.00,14500.00,'normal','供给充足'),
  (202604260103,2026,4,'账号4',44,'茶叶名称4','高山红茶',105.00,80.00,30.00,8200.00,'normal','红茶稳定销售')
ON DUPLICATE KEY UPDATE `risklevel` = VALUES(`risklevel`), `remark` = VALUES(`remark`);

CREATE TABLE IF NOT EXISTS `warehouse_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `warehousecode` varchar(64) NOT NULL COMMENT '仓库编号',
  `warehousename` varchar(200) NOT NULL COMMENT '仓库名称',
  `warehousetype` varchar(100) DEFAULT NULL COMMENT '仓库类型',
  `location` varchar(255) DEFAULT NULL COMMENT '仓库位置',
  `principal` varchar(100) DEFAULT NULL COMMENT '负责人',
  `contactphone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_warehouse_code` (`warehousecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库主数据';

INSERT INTO `warehouse_info` (`id`,`warehousecode`,`warehousename`,`warehousetype`,`location`,`principal`,`contactphone`,`enterpriseaccount`,`status`,`remark`)
VALUES
  (202604260201,'WH-A8-01','账号8总仓','总仓','福建省武夷山市','陈建国','13800000001','账号8','启用','承接基地入库'),
  (202604260202,'WH-A8-02','账号8电商仓','电商仓','福建省福州市','王晓','13800000003','账号8','启用','承接线上订单'),
  (202604260203,'WH-A4-01','账号4总仓','总仓','安徽省黄山市','李春明','13800000002','账号4','启用','红茶库存仓')
ON DUPLICATE KEY UPDATE `warehousename` = VALUES(`warehousename`), `status` = VALUES(`status`);

CREATE TABLE IF NOT EXISTS `inventory_transfer_suggestion` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `productname` varchar(200) NOT NULL COMMENT '茶品名称',
  `sourcewarehousecode` varchar(64) DEFAULT NULL COMMENT '来源仓编号',
  `sourcewarehousename` varchar(200) DEFAULT NULL COMMENT '来源仓名称',
  `targetwarehousecode` varchar(64) DEFAULT NULL COMMENT '目标仓编号',
  `targetwarehousename` varchar(200) DEFAULT NULL COMMENT '目标仓名称',
  `suggestedquantity` decimal(12,2) DEFAULT NULL COMMENT '建议调拨量(kg)',
  `reason` varchar(500) DEFAULT NULL COMMENT '建议原因',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存调拨建议';

INSERT INTO `inventory_transfer_suggestion` (`id`,`enterpriseaccount`,`productname`,`sourcewarehousecode`,`sourcewarehousename`,`targetwarehousecode`,`targetwarehousename`,`suggestedquantity`,`reason`,`status`,`remark`)
VALUES
  (202604260301,'账号8','茶叶名称2','WH-A8-01','账号8总仓','WH-A8-02','账号8电商仓',4.00,'电商仓低于预警库存，总仓库存充足','待处理','演示调拨建议')
ON DUPLICATE KEY UPDATE `status` = VALUES(`status`), `reason` = VALUES(`reason`);

CREATE TABLE IF NOT EXISTS `product_lifecycle_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `productid` bigint DEFAULT NULL COMMENT '茶品ID',
  `productname` varchar(200) DEFAULT NULL COMMENT '茶品名称',
  `teatype` varchar(100) DEFAULT NULL COMMENT '茶类',
  `newperioddays` int DEFAULT '15' COMMENT '新茶期天数',
  `mainsaleperioddays` int DEFAULT '45' COMMENT '主销期天数',
  `promotionperioddays` int DEFAULT '90' COMMENT '促销期天数',
  `warningperioddays` int DEFAULT '120' COMMENT '风险期天数',
  `strategynote` varchar(500) DEFAULT NULL COMMENT '策略说明',
  `enabled` int DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='茶品生命周期规则';

INSERT INTO `product_lifecycle_rule` (`id`,`enterpriseaccount`,`productid`,`productname`,`teatype`,`newperioddays`,`mainsaleperioddays`,`promotionperioddays`,`warningperioddays`,`strategynote`,`enabled`)
VALUES
  (202604260401,'账号8',42,'茶叶名称2','高山绿茶',15,45,90,120,'春茶优先曝光，过主销期后转礼盒组合',1),
  (202604260402,'账号4',44,'茶叶名称4','高山红茶',20,60,120,180,'红茶适合中长期销售，库存偏高时转企业采购',1)
ON DUPLICATE KEY UPDATE `strategynote` = VALUES(`strategynote`), `enabled` = VALUES(`enabled`);
