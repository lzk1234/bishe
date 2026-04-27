SET NAMES utf8mb4;

-- TeaMall incremental demo seed data
-- Idempotent import only. Does not overwrite springbooty2rp6.sql.

DROP TEMPORARY TABLE IF EXISTS `seed_demo_users`;
CREATE TEMPORARY TABLE `seed_demo_users` (
  `seq_no` int NOT NULL,
  `userid` bigint NOT NULL,
  `yonghuming` varchar(200) NOT NULL,
  `xingming` varchar(200) NOT NULL,
  `xingbie` varchar(20) NOT NULL,
  `youxiang` varchar(200) NOT NULL,
  `shouji` varchar(30) NOT NULL,
  `money` decimal(10,2) NOT NULL,
  `vip` varchar(20) NOT NULL,
  `address_id` bigint NOT NULL,
  `address_detail` varchar(255) NOT NULL,
  PRIMARY KEY (`seq_no`),
  UNIQUE KEY `uk_seed_demo_users_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `seed_demo_users`
(`seq_no`,`userid`,`yonghuming`,`xingming`,`xingbie`,`youxiang`,`shouji`,`money`,`vip`,`address_id`,`address_detail`)
VALUES
  (1,20260425001,'demo_user_01','Demo User 01','F','demo_user_01@teamall.local','13900010001',1888.00,CONVERT(0xE698AF USING utf8mb4),20260425101,'No.88 Yincheng Middle Rd, Pudong, Shanghai'),
  (2,20260425002,'demo_user_02','Demo User 02','M','demo_user_02@teamall.local','13900010002',1666.00,CONVERT(0xE590A6 USING utf8mb4),20260425102,'No.99 Wensan Rd, Xihu, Hangzhou'),
  (3,20260425003,'demo_user_03','Demo User 03','F','demo_user_03@teamall.local','13900010003',1999.00,CONVERT(0xE698AF USING utf8mb4),20260425103,'No.168 Huandao East Rd, Siming, Xiamen'),
  (4,20260425004,'demo_user_04','Demo User 04','M','demo_user_04@teamall.local','13900010004',1450.00,CONVERT(0xE590A6 USING utf8mb4),20260425104,'No.66 Xinghai St, SIP, Suzhou'),
  (5,20260425005,'demo_user_05','Demo User 05','F','demo_user_05@teamall.local','13900010005',2100.00,CONVERT(0xE698AF USING utf8mb4),20260425105,'No.588 Tianfu Ave, Chengdu');

DROP TEMPORARY TABLE IF EXISTS `seed_demo_products`;
CREATE TEMPORARY TABLE `seed_demo_products` (
  `seq_no` int NOT NULL,
  `goodid` bigint NOT NULL,
  `batchcode` varchar(64) NOT NULL,
  `basename` varchar(200) NOT NULL,
  `teatype` varchar(100) NOT NULL,
  `pickingdate` date NOT NULL,
  `freshweight` decimal(10,2) NOT NULL,
  `finishedweight` decimal(10,2) NOT NULL,
  `processmethod` varchar(200) NOT NULL,
  `batchstatus` varchar(100) NOT NULL,
  `altitude` int NOT NULL,
  `warningstock` decimal(10,2) NOT NULL,
  `remark` varchar(255) NOT NULL,
  PRIMARY KEY (`seq_no`),
  UNIQUE KEY `uk_seed_demo_products_goodid` (`goodid`),
  UNIQUE KEY `uk_seed_demo_products_batchcode` (`batchcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `seed_demo_products`
(`seq_no`,`goodid`,`batchcode`,`basename`,`teatype`,`pickingdate`,`freshweight`,`finishedweight`,`processmethod`,`batchstatus`,`altitude`,`warningstock`,`remark`)
VALUES
  (1,42,'PC202604-A01','Yunling Highland Base 1','green','2026-04-10',520.00,118.00,'pan-fry+bake','in_stock',860,40.00,'existing spring batch'),
  (2,43,'PC202604-B03','Yunling Highland Base 1','green','2026-04-13',480.00,102.00,'pan-fry+rebake','in_stock',860,34.00,'supplemental batch for good 43'),
  (3,44,'PC202604-A02','Qingfeng Eco Base','black','2026-04-12',460.00,105.00,'wither+roll+ferment+dry','processed',720,35.00,'existing spring batch'),
  (4,45,'PC202604-B05','Gaoshan Plot 5','oolong','2026-04-09',430.00,96.00,'shake+roast','in_stock',680,30.00,'supplemental batch for good 45'),
  (5,46,'PC202604-B06','Gaoshan Plot 6','white','2026-04-08',410.00,90.00,'sun-wither+dry','in_stock',690,28.00,'supplemental batch for good 46'),
  (6,47,'PC202604-B07','Gaoshan Plot 7','dark','2026-04-07',455.00,98.00,'pile-ferment+roast','in_stock',700,32.00,'supplemental batch for good 47'),
  (7,48,'PC202604-B08','Yunling Eco Base 2','green','2026-04-11',500.00,108.00,'shape+aroma-finish','in_stock',840,36.00,'supplemental batch for good 48');

INSERT INTO `yonghu`
(`id`,`addtime`,`yonghuming`,`mima`,`xingming`,`touxiang`,`xingbie`,`youxiang`,`shouji`,`money`,`vip`)
SELECT
  u.`userid`,
  '2026-04-25 09:00:00',
  u.`yonghuming`,
  '123456',
  u.`xingming`,
  CONCAT('upload/demo_user_', LPAD(u.`seq_no`, 2, '0'), '.jpg'),
  u.`xingbie`,
  u.`youxiang`,
  u.`shouji`,
  u.`money`,
  u.`vip`
FROM `seed_demo_users` u
WHERE NOT EXISTS (
  SELECT 1
  FROM `yonghu` y
  WHERE y.`yonghuming` = u.`yonghuming`
);

UPDATE `yonghu` y
JOIN `seed_demo_users` u ON u.`userid` = y.`id`
SET y.`mima` = '123456'
WHERE y.`mima` <> '123456';

INSERT INTO `address`
(`id`,`addtime`,`userid`,`address`,`name`,`phone`,`isdefault`)
SELECT
  u.`address_id`,
  '2026-04-25 09:05:00',
  u.`userid`,
  u.`address_detail`,
  u.`xingming`,
  u.`shouji`,
  CONVERT(0xE698AF USING utf8mb4)
FROM `seed_demo_users` u
WHERE NOT EXISTS (
  SELECT 1
  FROM `address` a
  WHERE a.`userid` = u.`userid`
    AND a.`address` = u.`address_detail`
);

UPDATE `shangpinxinxi` s
JOIN `seed_demo_products` p ON p.`goodid` = s.`id`
SET s.`shengchanpici` = p.`batchcode`
WHERE s.`shengchanpici` IS NULL
   OR s.`shengchanpici` = '';

INSERT INTO `teabatch`
(`id`,`batchcode`,`basename`,`productname`,`teatype`,`pickingdate`,`freshweight`,`processmethod`,`finishedweight`,`batchstatus`,`altitude`,`enterpriseaccount`,`remark`)
SELECT
  20260425200 + p.`seq_no`,
  p.`batchcode`,
  p.`basename`,
  s.`shangpinmingcheng`,
  p.`teatype`,
  p.`pickingdate`,
  p.`freshweight`,
  p.`processmethod`,
  p.`finishedweight`,
  p.`batchstatus`,
  p.`altitude`,
  s.`zhanghao`,
  p.`remark`
FROM `seed_demo_products` p
JOIN `shangpinxinxi` s ON s.`id` = p.`goodid`
WHERE NOT EXISTS (
  SELECT 1
  FROM `teabatch` t
  WHERE t.`batchcode` = p.`batchcode`
);

INSERT INTO `inventoryrecord`
(`id`,`batchcode`,`productname`,`recordtype`,`changestock`,`currentstock`,`warningstock`,`recordtime`,`enterpriseaccount`,`relatedorderid`,`remark`)
SELECT
  v.`id`,
  v.`batchcode`,
  s.`shangpinmingcheng`,
  v.`recordtype`,
  v.`changestock`,
  v.`currentstock`,
  p.`warningstock`,
  v.`recordtime`,
  s.`zhanghao`,
  v.`relatedorderid`,
  v.`remark`
FROM (
  SELECT 20260425301 AS `id`,'PC202604-A01' AS `batchcode`,42 AS `goodid`,'adjust' AS `recordtype`,8.00 AS `changestock`,44.00 AS `currentstock`,'2026-03-28 09:10:00' AS `recordtime`,NULL AS `relatedorderid`,'recent stock adjustment' AS `remark`
  UNION ALL SELECT 20260425302,'PC202604-A01',42,'outbound',-6.00,38.00,'2026-04-02 14:20:00','DEMOORD20260425041','recent outbound sample'
  UNION ALL SELECT 20260425303,'PC202604-A01',42,'inbound',18.00,56.00,'2026-04-18 10:30:00',NULL,'restock sample'
  UNION ALL SELECT 20260425304,'PC202604-B03',43,'inbound',102.00,102.00,'2026-03-25 09:00:00',NULL,'new batch inbound'
  UNION ALL SELECT 20260425305,'PC202604-B03',43,'outbound',-11.00,91.00,'2026-04-05 15:00:00','DEMOORD20260425045','trial and sales outbound'
  UNION ALL SELECT 20260425306,'PC202604-B03',43,'outbound',-9.00,82.00,'2026-04-22 11:30:00','DEMOORD20260425077','repeat purchase outbound'
  UNION ALL SELECT 20260425307,'PC202604-A02',44,'adjust',6.00,111.00,'2026-03-27 10:20:00',NULL,'offline stock sync'
  UNION ALL SELECT 20260425308,'PC202604-A02',44,'outbound',-12.00,99.00,'2026-04-06 13:10:00','DEMOORD20260425046','recent outbound sample'
  UNION ALL SELECT 20260425309,'PC202604-A02',44,'outbound',-8.00,91.00,'2026-04-20 16:40:00','DEMOORD20260425078','latest week outbound'
  UNION ALL SELECT 20260425310,'PC202604-B05',45,'inbound',96.00,96.00,'2026-03-24 08:50:00',NULL,'new batch inbound'
  UNION ALL SELECT 20260425311,'PC202604-B05',45,'outbound',-10.00,86.00,'2026-04-07 17:20:00','DEMOORD20260425047','recent outbound sample'
  UNION ALL SELECT 20260425312,'PC202604-B05',45,'adjust',4.00,90.00,'2026-04-23 09:40:00',NULL,'stocktake correction'
  UNION ALL SELECT 20260425313,'PC202604-B06',46,'inbound',90.00,90.00,'2026-03-23 09:15:00',NULL,'new batch inbound'
  UNION ALL SELECT 20260425314,'PC202604-B06',46,'outbound',-13.00,77.00,'2026-04-08 15:35:00','DEMOORD20260425048','recent outbound sample'
  UNION ALL SELECT 20260425315,'PC202604-B06',46,'outbound',-7.00,70.00,'2026-04-24 18:15:00','DEMOORD20260425080','latest 24h outbound'
  UNION ALL SELECT 20260425316,'PC202604-B07',47,'inbound',98.00,98.00,'2026-03-22 08:40:00',NULL,'new batch inbound'
  UNION ALL SELECT 20260425317,'PC202604-B07',47,'outbound',-9.00,89.00,'2026-04-09 12:45:00','DEMOORD20260425049','recent outbound sample'
  UNION ALL SELECT 20260425318,'PC202604-B07',47,'adjust',3.00,92.00,'2026-04-21 10:05:00',NULL,'stocktake correction'
  UNION ALL SELECT 20260425319,'PC202604-B08',48,'inbound',108.00,108.00,'2026-03-26 09:25:00',NULL,'new batch inbound'
  UNION ALL SELECT 20260425320,'PC202604-B08',48,'outbound',-14.00,94.00,'2026-04-11 14:55:00','DEMOORD20260425050','recent outbound sample'
  UNION ALL SELECT 20260425321,'PC202604-B08',48,'outbound',-10.00,84.00,'2026-04-24 11:10:00','DEMOORD20260425079','latest 24h outbound'
) v
JOIN `seed_demo_products` p ON p.`batchcode` = v.`batchcode`
JOIN `shangpinxinxi` s ON s.`id` = v.`goodid`
WHERE NOT EXISTS (
  SELECT 1
  FROM `inventoryrecord` ir
  WHERE ir.`id` = v.`id`
);

DROP TEMPORARY TABLE IF EXISTS `seed_order_plan`;
CREATE TEMPORARY TABLE `seed_order_plan` (
  `seq_no` int NOT NULL,
  `order_time` datetime NOT NULL,
  PRIMARY KEY (`seq_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `seed_order_plan` (`seq_no`,`order_time`)
WITH RECURSIVE `seq` AS (
  SELECT 1 AS `n`
  UNION ALL
  SELECT `n` + 1
  FROM `seq`
  WHERE `n` < 80
)
SELECT
  s.`n`,
  CASE
    WHEN s.`n` <= 40 THEN DATE_ADD('2025-11-12 10:15:00', INTERVAL (s.`n` - 1) * 3 DAY)
    ELSE DATE_ADD('2026-03-18 09:20:00', INTERVAL (s.`n` - 41) DAY)
  END + INTERVAL (s.`n` MOD 5) HOUR + INTERVAL ((s.`n` * 7) MOD 50) MINUTE
FROM `seq` s;

INSERT INTO `orders`
(`id`,`addtime`,`orderid`,`tablename`,`userid`,`goodid`,`goodname`,`picture`,`buynumber`,`price`,`discountprice`,`total`,`discounttotal`,`type`,`status`,`address`,`tel`,`consignee`,`remark`,`logistics`,`zhanghao`,`goodtype`)
SELECT
  202604254000 + o.`seq_no`,
  o.`order_time`,
  CONCAT('DEMOORD20260425', LPAD(o.`seq_no`, 3, '0')),
  'shangpinxinxi',
  u.`userid`,
  s.`id`,
  s.`shangpinmingcheng`,
  s.`tupian`,
  (o.`seq_no` MOD 3) + 1,
  s.`price`,
  s.`price`,
  ROUND(((o.`seq_no` MOD 3) + 1) * s.`price`, 2),
  ROUND(((o.`seq_no` MOD 3) + 1) * s.`price`, 2),
  1,
  CASE
    WHEN o.`seq_no` MOD 10 IN (1,2,3,4,5,6) THEN CONVERT(0xE5B7B2E5AE8CE68890 USING utf8mb4)
    WHEN o.`seq_no` MOD 10 IN (7,8) THEN CONVERT(0xE5B7B2E694AFE4BB98 USING utf8mb4)
    WHEN o.`seq_no` MOD 10 = 9 THEN CONVERT(0xE5B7B2E98080E6ACBE USING utf8mb4)
    ELSE CONVERT(0xE5B7B2E58F96E6B688 USING utf8mb4)
  END,
  u.`address_detail`,
  u.`shouji`,
  u.`xingming`,
  CONCAT('demo-seed-', DATE_FORMAT(o.`order_time`, '%Y%m')),
  CASE
    WHEN o.`seq_no` MOD 10 IN (1,2,3,4,5,6,7,8) THEN CONCAT('YTDEMO', LPAD(o.`seq_no`, 6, '0'))
    ELSE NULL
  END,
  s.`zhanghao`,
  s.`shangpinfenlei`
FROM `seed_order_plan` o
JOIN `seed_demo_users` u ON u.`seq_no` = ((o.`seq_no` - 1) MOD 5) + 1
JOIN `seed_demo_products` p ON p.`seq_no` = ((o.`seq_no` - 1) MOD 7) + 1
JOIN `shangpinxinxi` s ON s.`id` = p.`goodid`
WHERE NOT EXISTS (
  SELECT 1
  FROM `orders` od
  WHERE od.`orderid` = CONCAT('DEMOORD20260425', LPAD(o.`seq_no`, 3, '0'))
);

INSERT INTO `storeup`
(`id`,`addtime`,`userid`,`refid`,`tablename`,`name`,`picture`,`type`,`inteltype`,`remark`)
SELECT
  202604255000 + e.`seq_no`,
  DATE_ADD('2026-04-10 08:00:00', INTERVAL e.`seq_no` DAY),
  u.`userid`,
  s.`id`,
  'shangpinxinxi',
  s.`shangpinmingcheng`,
  s.`tupian`,
  '1',
  s.`shangpinfenlei`,
  'demo favorite sample'
FROM (
  SELECT 1 AS `seq_no` UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
  UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
  UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
) e
JOIN `seed_demo_users` u ON u.`seq_no` = ((e.`seq_no` - 1) MOD 5) + 1
JOIN `seed_demo_products` p ON p.`seq_no` = ((e.`seq_no` + 1) MOD 7) + 1
JOIN `shangpinxinxi` s ON s.`id` = p.`goodid`
WHERE NOT EXISTS (
  SELECT 1
  FROM `storeup` st
  WHERE st.`userid` = u.`userid`
    AND st.`refid` = s.`id`
    AND st.`tablename` = 'shangpinxinxi'
);

INSERT INTO `user_behavior`
(`userid`,`goodid`,`behavior_type`,`addtime`)
SELECT
  od.`userid`,
  od.`goodid`,
  'buy',
  od.`addtime`
FROM `orders` od
WHERE od.`orderid` LIKE 'DEMOORD20260425%'
  AND od.`status` IN (
    CONVERT(0xE5B7B2E694AFE4BB98 USING utf8mb4),
    CONVERT(0xE5B7B2E5AE8CE68890 USING utf8mb4)
  )
  AND NOT EXISTS (
    SELECT 1
    FROM `user_behavior` ub
    WHERE ub.`userid` = od.`userid`
      AND ub.`goodid` = od.`goodid`
      AND ub.`behavior_type` = 'buy'
      AND ub.`addtime` = od.`addtime`
  );

INSERT INTO `user_behavior`
(`userid`,`goodid`,`behavior_type`,`addtime`)
SELECT
  u.`userid`,
  s.`id`,
  CASE WHEN e.`seq_no` MOD 2 = 1 THEN 'view' ELSE 'favorite' END,
  DATE_ADD('2026-04-01 09:00:00', INTERVAL e.`seq_no` DAY)
FROM (
  SELECT 1 AS `seq_no` UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
  UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10
  UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15
  UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20
) e
JOIN `seed_demo_users` u ON u.`seq_no` = ((e.`seq_no` - 1) MOD 5) + 1
JOIN `seed_demo_products` p ON p.`seq_no` = ((e.`seq_no` + 2) MOD 7) + 1
JOIN `shangpinxinxi` s ON s.`id` = p.`goodid`
WHERE NOT EXISTS (
  SELECT 1
  FROM `user_behavior` ub
  WHERE ub.`userid` = u.`userid`
    AND ub.`goodid` = s.`id`
    AND ub.`behavior_type` = CASE WHEN e.`seq_no` MOD 2 = 1 THEN 'view' ELSE 'favorite' END
    AND ub.`addtime` = DATE_ADD('2026-04-01 09:00:00', INTERVAL e.`seq_no` DAY)
);

INSERT INTO `recommendation`
(`id`,`userid`,`goodid`,`score`,`reason`,`algorithm_type`,`create_time`)
SELECT
  202604256000 + ((u.`seq_no` - 1) * 5) + p.`seq_no`,
  u.`userid`,
  p.`goodid`,
  ROUND(1.4500 - ((p.`seq_no` - 1) * 0.1800) + ((u.`seq_no` - 1) * 0.0200), 4),
  CASE
    WHEN p.`seq_no` IN (1,2) THEN 'recent 30d preference boost'
    WHEN p.`seq_no` IN (3,4) THEN 'recent 6m cross-category similarity'
    ELSE 'hot batch + favorite overlap'
  END,
  'hybrid',
  '2026-04-25 12:00:00'
FROM `seed_demo_users` u
JOIN `seed_demo_products` p ON p.`seq_no` <= 5
WHERE NOT EXISTS (
  SELECT 1
  FROM `recommendation` r
  WHERE r.`id` = 202604256000 + ((u.`seq_no` - 1) * 5) + p.`seq_no`
);

DROP TEMPORARY TABLE IF EXISTS `seed_order_plan`;
DROP TEMPORARY TABLE IF EXISTS `seed_demo_products`;
DROP TEMPORARY TABLE IF EXISTS `seed_demo_users`;
