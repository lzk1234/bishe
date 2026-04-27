
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `aboutus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aboutus` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '标题',
  `subtitle` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '副标题',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '内容',
  `picture1` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '图片1',
  `picture2` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '图片2',
  `picture3` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '图片3',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='关于我们';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `aboutus` WRITE;
/*!40000 ALTER TABLE `aboutus` DISABLE KEYS */;
INSERT INTO `aboutus` VALUES (1,'2024-03-07 02:38:03','你是最棒的','ABOUT US','<p>不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢? 你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。测试</p>','upload/aboutus_picture1.jpg','upload/aboutus_picture2.jpg','upload/aboutus_picture3.jpg');
/*!40000 ALTER TABLE `aboutus` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '用户id',
  `address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '地址',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '收货人',
  `phone` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '电话',
  `isdefault` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否默认地址[是/否]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1678240372218 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='地址';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'2024-03-07 02:38:03',11,'宇宙银河系金星1号','金某','13823888881','是'),(2,'2024-03-07 02:38:03',12,'宇宙银河系木星1号','木某','13823888882','是'),(3,'2024-03-07 02:38:03',13,'宇宙银河系水星1号','水某','13823888883','是'),(4,'2024-03-07 02:38:03',14,'宇宙银河系火星1号','火某','13823888884','是'),(5,'2024-03-07 02:38:03',15,'宇宙银河系土星1号','土某','13823888885','是'),(6,'2024-03-07 02:38:03',16,'宇宙银河系月球1号','月某','13823888886','是'),(7,'2024-03-07 02:38:03',17,'宇宙银河系黑洞1号','黑某','13823888887','是'),(8,'2024-03-07 02:38:03',18,'宇宙银河系地球1号','地某','13823888888','是'),(1678240372217,'2024-03-07 02:38:03',1678240351849,'xx地址','王丽','15214121411','是');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tablename` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'shangpinxinxi' COMMENT '茶叶表名',
  `userid` bigint NOT NULL COMMENT '用户id',
  `goodid` bigint NOT NULL COMMENT '茶叶id',
  `goodname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '茶叶名称',
  `picture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '图片',
  `buynumber` int NOT NULL COMMENT '购买数量',
  `price` float DEFAULT NULL COMMENT '单价',
  `discountprice` float DEFAULT NULL COMMENT '会员价',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商户名称',
  `goodtype` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '茶叶类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1709783520633 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1709782960585,'2024-03-07 03:42:40','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',1,99.9,0,'账号8','茶叶分类2'),(1709783520632,'2024-03-07 03:51:59','shangpinxinxi',1678240351849,43,'茶叶名称3','upload/shangpinxinxi_tupian3.jpg',1,99.9,0,'账号8','茶叶分类3');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='配置文件';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'picture1','upload/picture1.jpg'),(2,'picture2','upload/picture2.jpg'),(3,'picture3','upload/picture3.jpg');
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `discussshangpinxinxi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discussshangpinxinxi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1678240807027 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='茶叶信息评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `discussshangpinxinxi` WRITE;
/*!40000 ALTER TABLE `discussshangpinxinxi` DISABLE KEYS */;
INSERT INTO `discussshangpinxinxi` VALUES (1678240807026,'2024-03-07 02:38:03',42,1678240351849,'upload/1678240339215.jpg','2','6666','88888');
/*!40000 ALTER TABLE `discussshangpinxinxi` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '简介',
  `picture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商城资讯';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (101,'2024-03-07 02:38:03','有梦想，就要努力去实现','不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。','upload/news_picture1.jpg','<p>不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?</p><p>你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。</p><p>真正有行动力的人不需要别人告诉他如何做，因为他已经在做了。就算碰到问题，他也会自己想办法，自己动手去解决或者主动寻求可以帮助他的人，而不是等着别人为自己解决问题。</p><p>首先要学习独立思考。花一点时间想一下自己喜欢什么，梦想是什么，不要别人说想环游世界，你就说你的梦想是环游世界。</p><p>很多人说现实束缚了自己，其实在这个世界上，我们一直都可以有很多选择，生活的决定权也—直都在自己手上，只是我们缺乏行动力而已。</p><p>如果你觉得安于现状是你想要的，那选择安于现状就会让你幸福和满足;如果你不甘平庸，选择一条改变、进取和奋斗的道路，在这个追求的过程中，你也一样会感到快乐。所谓的成功，即是按照自己想要的生活方式生活。最糟糕的状态，莫过于当你想要选择一条不甘平庸、改变、进取和奋斗的道路时，却以一种安于现状的方式生活，最后抱怨自己没有得到想要的人生。</p><p>因为喜欢，你不是在苦苦坚持，也因为喜欢，你愿意投入时间、精力，长久以往，获得成功就是自然而然的事情。</p>'),(102,'2024-03-07 02:38:03','又是一年毕业季','又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。','upload/news_picture2.jpg','<p>又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。</p><p>过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。一直都是晚会的忠实参与者，无论是台前还是幕后，忽然间，角色转变，那种感觉确实难以用语言表达。</p><p>	过去的三年，总是默默地期盼着这个好雨时节，因为这时候，会有灿烂的阳光，会有满目的百花争艳，会有香甜的冰激凌，这是个毕业的季节，当时不经世事的我们会殷切地期待学校那一大堆的活动，期待穿上绚丽的演出服或者礼仪服，站在大礼堂镁光灯下尽情挥洒我们的澎拜的激情。</p><p>百感交集，隔岸观火与身临其境的感觉竟是如此不同。从来没想过一场晚会送走的是我们自己的时候会是怎样的感情，毕业就真的意味着结束吗?倔强的我们不愿意承认，谢谢学弟学妹们慷慨的将这次的主题定为“我们在这里”。我知道，这可能是他们对我们这些过来人的尊敬和施舍。</p><p>没有为这场晚会排练、奔波，没有为班级、学生会、文学院出点力，还真有点不习惯，百般无奈中，用“工作忙”个万能的借口来搪塞自己，欺骗别人。其实自己心里明白，那只是在逃避，只是不愿面对繁华落幕后的萧条和落寞。大四了，大家各奔东西，想凑齐班上的人真的是难上加难，敏燕从越南回来，刚落地就匆匆回了学校，那么恋家的人也启程回来了，睿睿学姐也是从家赶来跟我们团圆。大家—如既往的寒暄、打趣、调侃对方，似乎一切又回到了当初的单纯美好。</p><p>看着舞台上活泼可爱的学弟学妹们，如同一群机灵的小精灵，清澈的眼神，稚嫩的肢体，轻快地步伐，用他们那热情洋溢的舞姿渲染着在场的每一个人，我知道，我不应该羡慕嫉妒他们，不应该顾自怜惜逝去的青春，不应该感叹夕阳无限好，曾经，我们也拥有过，曾经，我们也年轻过，曾经，我们也灿烂过。我深深地告诉自己，人生的每个阶段都是美的，年轻有年轻的活力，成熟也有成熟的魅力。多—份稳重、淡然、优雅，也是漫漫时光掠影遗留下的.珍贵赏赐。</p>'),(103,'2024-03-07 02:38:03','挫折路上，坚持常在心间','回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。','upload/news_picture3.jpg','<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>	或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>'),(104,'2024-03-07 02:38:03','挫折是另一个生命的开端','当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。','upload/news_picture4.jpg','<p>当遇到挫折或失败，你是看见失败还是看见机会?</p><p>挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。</p><p>人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。</p><p>大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。</p><p>	“塞翁失马，焉知非福。”人生的道路，并不是每一步都迈向成功，这就是追求的意义。我们还要认识到一点，挫折作为一种情绪状态和一种个人体验，各人的耐受性是大不相同的，有的人经历了一次次挫折，就能够坚忍不拔，百折不挠;有的人稍遇挫折便意志消沉，一蹶不振。所以，挫折感是一种主观感受，因为人的目的和需要不同，成功标准不同，所以同一种活动对于不同的人可能会造成不同的挫折感受。</p><p>凡事皆以平常心来看待，对于生命顺逆不要太执著。能够“破我执”是很高层的人生境界。</p><p>人事的艰难就是一种考验。就像—支剑要有磨刀来磨，剑才会利:一块璞玉要有粗石来磨，才会发出耀眼的光芒。我们能够做到的，只是如何减少、避免那些由于自身的原因所造成的挫折，而在遇到痛苦和挫折之后，则力求化解痛苦，争取幸福。我们要知道，痛苦和挫折是双重性的，它既是我们人生中难以完全避免的，也是我们在争取成功时，不可缺少的一种动力。因为我认为，推动我们奋斗的力量，不仅仅是对成功的渴望，还有为摆脱痛苦和挫折而进行的奋斗。</p>'),(105,'2024-03-07 02:38:03','你要去相信，没有到不了的明天','有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。','upload/news_picture5.jpg','<p>有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。</p><p>不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。</p><p>	不管你现在是在图书馆里背着怎么也看不进去的英语单词，还是你现在迷茫地看不清未来的方向不知道要往哪走。</p><p>不管你现在是在努力着去实现梦想却没能拉近与梦想的距离，还是你已经慢慢地找不到自己的梦想了。</p><p>你都要去相信，没有到不了的明天。</p><p>	有的时候你的梦想太大，别人说你的梦想根本不可能实现;有的时候你的梦想又太小，又有人说你胸无大志;有的时候你对死党说着将来要去环游世界的梦想，却换来他的不屑一顾，于是你再也不提自己的梦想;有的时候你突然说起将来要开个小店的愿望，却发现你讲述的那个人，并没有听到你在说什么。</p><p>不过又能怎么样呢，未来始终是自己的，梦想始终是自己的，没有人会来帮你实现它。</p><p>也许很多时候我们只是需要朋友的一句鼓励，一句安慰，却也得不到。但是相信我，世界上还有很多人，只是想要和你说说话。</p><p>因为我们都一样。一样的被人说成固执，一样的在追逐他们眼里根本不在意的东西。</p><p>所以，又有什么关系呢，别人始终不是你、不能懂你的心情，你又何必多去解释呢。这个世界会来阻止你，困难也会接踵而至，其实真正关键的只有自己，有没有那个倔强。</p><p>这个世界上没有不带伤的人，真正能治愈自己的，只有自己。</p>'),(106,'2024-03-07 02:38:03','离开是一种痛苦，是一种勇气，但同样也是一个考验，是一个新的开端','无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。','upload/news_picture6.jpg','<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>'),(107,'2024-03-07 02:38:03','Leave未必是一种痛苦','无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。','upload/news_picture7.jpg','<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>'),(108,'2024-03-07 02:38:03','商城资讯','回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。','upload/news_picture8.jpg','<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。测试</p>');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单编号',
  `tablename` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'shangpinxinxi' COMMENT '茶叶表名',
  `userid` bigint NOT NULL COMMENT '用户id',
  `goodid` bigint NOT NULL COMMENT '茶叶id',
  `goodname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '茶叶名称',
  `picture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '茶叶图片',
  `buynumber` int NOT NULL COMMENT '购买数量',
  `price` float NOT NULL DEFAULT '0' COMMENT '价格',
  `discountprice` float DEFAULT '0' COMMENT '折扣价格',
  `total` float NOT NULL DEFAULT '0' COMMENT '总价格',
  `discounttotal` float DEFAULT '0' COMMENT '折扣总价格',
  `type` int DEFAULT '1' COMMENT '支付类型',
  `status` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '状态',
  `address` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '地址',
  `tel` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '电话',
  `consignee` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '收货人',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `logistics` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '物流',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商户名称',
  `goodtype` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '茶叶类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `orderid` (`orderid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1776791342710 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='订单';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1678240707351,'2024-03-07 02:38:03','20233895831362','shangpinxinxi',1678240351849,43,'茶叶名称3','upload/shangpinxinxi_tupian3.jpg',2,99.9,99.9,199.8,199.8,1,'已取消','xx地址','15214121411','王丽','1111',NULL,'账号8','茶叶分类3'),(1678240707680,'2024-03-07 02:38:03','20233895831358','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',1,99.9,99.9,99.9,99.9,1,'已完成','xx地址','15214121411','王丽','1111','SF202403070001','账号8','茶叶分类2'),(1678240707703,'2024-03-07 02:38:03','20233895831361','shangpinxinxi',1678240351849,48,'茶叶名称8','upload/shangpinxinxi_tupian8.jpg',2,99.9,99.9,199.8,199.8,1,'已取消','xx地址','15214121411','王丽','1111',NULL,'账号8','茶叶分类8'),(1678240708250,'2024-03-07 02:38:03','20233895831360','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',2,99.9,99.9,199.8,199.8,1,'未支付','xx地址','15214121411','王丽','1111',NULL,'账号8','茶叶分类2'),(1678240754093,'2024-03-07 02:38:03','20233895917931','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',2,99.9,99.9,199.8,199.8,1,'已支付','xx地址','15214121411','王丽','111',NULL,'账号8','茶叶分类2'),(1678240754533,'2024-03-07 02:38:03','20233895917937','shangpinxinxi',1678240351849,43,'茶叶名称3','upload/shangpinxinxi_tupian3.jpg',2,99.9,99.9,199.8,199.8,1,'已退款','xx地址','15214121411','王丽','111',NULL,'账号8','茶叶分类3'),(1678240754600,'2024-03-07 02:38:03','20233895917932','shangpinxinxi',1678240351849,48,'茶叶名称8','upload/shangpinxinxi_tupian8.jpg',2,99.9,99.9,199.8,199.8,1,'已退款','xx地址','15214121411','王丽','111',NULL,'账号8','茶叶分类8'),(1678240754782,'2024-03-07 02:38:03','20233895917930','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',1,99.9,99.9,99.9,99.9,1,'已完成','xx地址','15214121411','王丽','111',NULL,'账号8','茶叶分类2'),(1709782964239,'2024-03-07 03:42:43','202437114243390','shangpinxinxi',1678240351849,42,'茶叶名称2','upload/shangpinxinxi_tupian2.jpg',1,99.9,99.9,99.9,99.9,1,'已退款','xx地址','15214121411','王丽','',NULL,'账号8','茶叶分类2'),(1709783087063,'2024-03-07 03:44:46','202437114446139','shangpinxinxi',1678240351849,1709783037107,'ceshishangpin222','upload/1709783046843.png',1,1000,1000,1000,1000,1,'已支付','xx地址','15214121411','王丽','',NULL,'账号8','茶叶分类1'),(1776791342709,'2026-04-21 17:09:01','1776791341780','shangpinxinxi',1678240351849,44,'茶叶名称4','upload/shangpinxinxi_tupian4.jpg',1,99.9,99.9,99.9,99.9,1,'已完成','xx地址','13800000000','王丽','复购订单',NULL,'账号4','茶叶分类4');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `recommendation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` bigint NOT NULL COMMENT '用户ID',
  `goodid` bigint NOT NULL COMMENT '茶叶ID',
  `score` decimal(10,4) NOT NULL COMMENT '推荐分数',
  `reason` varchar(100) DEFAULT NULL COMMENT '推荐理由',
  `algorithm_type` varchar(50) NOT NULL COMMENT '算法类型：collaborative-协同过滤,content-内容推荐,hot-热门',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_score` (`userid`,`score` DESC),
  KEY `idx_goodid` (`goodid`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='推荐结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `recommendation` WRITE;
/*!40000 ALTER TABLE `recommendation` DISABLE KEYS */;
INSERT INTO `recommendation` VALUES (36,1678240351849,42,1.4500,'混合推荐1','hybrid','2026-04-22 01:08:01'),(37,1678240351849,43,1.1500,'混合推荐2','hybrid','2026-04-22 01:08:01'),(38,1678240351849,48,0.8500,'混合推荐3','hybrid','2026-04-22 01:08:01'),(39,1678240351849,44,0.1500,'混合推荐4','hybrid','2026-04-22 01:08:01'),(40,1678240351849,45,0.1500,'混合推荐5','hybrid','2026-04-22 01:08:01'),(41,1678240351849,46,0.1500,'混合推荐6','hybrid','2026-04-22 01:08:01'),(42,1678240351849,47,0.1500,'混合推荐7','hybrid','2026-04-22 01:08:01'),(43,18,48,1.4500,'混合推荐1','hybrid','2026-04-22 01:08:01'),(44,18,42,0.8500,'混合推荐2','hybrid','2026-04-22 01:08:01'),(45,18,43,0.8500,'混合推荐3','hybrid','2026-04-22 01:08:01'),(46,18,44,0.1500,'混合推荐4','hybrid','2026-04-22 01:08:01'),(47,18,45,0.1500,'混合推荐5','hybrid','2026-04-22 01:08:01'),(48,18,46,0.1500,'混合推荐6','hybrid','2026-04-22 01:08:01'),(49,18,47,0.1500,'混合推荐7','hybrid','2026-04-22 01:08:01');
/*!40000 ALTER TABLE `recommendation` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `shangjia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shangjia` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `shangjiaxingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商家姓名',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别',
  `touxiang` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '头像',
  `youxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `lianxidianhua` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系电话',
  `money` float DEFAULT '0' COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `zhanghao` (`zhanghao`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商家';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `shangjia` WRITE;
/*!40000 ALTER TABLE `shangjia` DISABLE KEYS */;
INSERT INTO `shangjia` VALUES (21,'2024-03-07 02:38:03','账号1','e10adc3949ba59abbe56e057f20f883e','商家姓名1','男','upload/shangjia_touxiang1.jpg','773890001@qq.com','13823888881',200),(22,'2024-03-07 02:38:03','账号2','e10adc3949ba59abbe56e057f20f883e','商家姓名2','男','upload/shangjia_touxiang2.jpg','773890002@qq.com','13823888882',200),(24,'2024-03-07 02:38:03','账号4','e10adc3949ba59abbe56e057f20f883e','商家姓名4','男','upload/shangjia_touxiang4.jpg','773890004@qq.com','13823888884',200),(25,'2024-03-07 02:38:03','账号5','e10adc3949ba59abbe56e057f20f883e','商家姓名5','男','upload/shangjia_touxiang5.jpg','773890005@qq.com','13823888885',200),(26,'2024-03-07 02:38:03','账号6','e10adc3949ba59abbe56e057f20f883e','商家姓名6','男','upload/shangjia_touxiang6.jpg','773890006@qq.com','13823888886',200),(27,'2024-03-07 02:38:03','账号7','e10adc3949ba59abbe56e057f20f883e','商家姓名7','男','upload/shangjia_touxiang7.jpg','773890007@qq.com','13823888887',200),(28,'2024-03-07 02:38:03','账号8','e10adc3949ba59abbe56e057f20f883e','商家姓名8','男','upload/shangjia_touxiang8.jpg','773890008@qq.com','13823888888',200);
/*!40000 ALTER TABLE `shangjia` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `shangpinfenlei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shangpinfenlei` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinfenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '茶叶分类',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `shangpinfenlei` (`shangpinfenlei`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='茶叶分类';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `shangpinfenlei` WRITE;
/*!40000 ALTER TABLE `shangpinfenlei` DISABLE KEYS */;
INSERT INTO `shangpinfenlei` VALUES (31,'2024-03-07 02:38:03','茶叶分类1'),(32,'2024-03-07 02:38:03','茶叶分类2'),(33,'2024-03-07 02:38:03','茶叶分类3'),(34,'2024-03-07 02:38:03','茶叶分类4'),(35,'2024-03-07 02:38:03','茶叶分类5'),(36,'2024-03-07 02:38:03','茶叶分类6');
/*!40000 ALTER TABLE `shangpinfenlei` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `shangpinxinxi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shangpinxinxi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinmingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '茶叶名称',
  `shangpinfenlei` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '茶叶分类',
  `tupian` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '图片',
  `pinpai` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '品牌',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
  `shangjiariqi` date DEFAULT NULL COMMENT '上架日期',
  `shangpinxiangqing` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '茶叶详情',
  `onelimittimes` int DEFAULT NULL COMMENT '单限',
  `alllimittimes` int DEFAULT NULL COMMENT '库存',
  `clicktime` datetime DEFAULT NULL COMMENT '最近点击时间',
  `price` float NOT NULL COMMENT '价格',
  `vipprice` float DEFAULT '-1' COMMENT '会员价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1709783037108 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='茶叶信息';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `shangpinxinxi` WRITE;
/*!40000 ALTER TABLE `shangpinxinxi` DISABLE KEYS */;
INSERT INTO `shangpinxinxi` VALUES (42,'2024-03-07 02:38:03','茶叶名称2','茶叶分类2','upload/shangpinxinxi_tupian2.jpg,upload/shangpinxinxi_tupian3.jpg,upload/shangpinxinxi_tupian4.jpg','品牌2','账号8','2024-03-07','<p>茶叶详情2</p>',2,95,'2026-04-22 01:09:02',99.9,2),(43,'2024-03-07 02:38:03','茶叶名称3','茶叶分类3','upload/shangpinxinxi_tupian3.jpg,upload/shangpinxinxi_tupian4.jpg,upload/shangpinxinxi_tupian5.jpg','品牌3','账号8','2024-03-07','<p>茶叶详情3</p>',3,97,'2024-03-07 11:51:52',99.9,3),(44,'2024-03-07 02:38:03','茶叶名称4','茶叶分类4','upload/shangpinxinxi_tupian4.jpg,upload/shangpinxinxi_tupian5.jpg,upload/shangpinxinxi_tupian6.jpg','品牌4','账号4','2024-03-07','茶叶详情4',4,99,'2023-03-08 09:39:40',99.9,4),(45,'2024-03-07 02:38:03','茶叶名称5','茶叶分类5','upload/shangpinxinxi_tupian5.jpg,upload/shangpinxinxi_tupian6.jpg,upload/shangpinxinxi_tupian7.jpg','品牌5','账号5','2024-03-07','茶叶详情5',5,99,'2023-03-08 09:39:40',99.9,5),(46,'2024-03-07 02:38:03','茶叶名称6','茶叶分类6','upload/shangpinxinxi_tupian6.jpg,upload/shangpinxinxi_tupian7.jpg,upload/shangpinxinxi_tupian8.jpg','品牌6','账号6','2024-03-07','茶叶详情6',6,99,'2023-03-08 09:39:40',99.9,6),(47,'2024-03-07 02:38:03','茶叶名称7','茶叶分类7','upload/shangpinxinxi_tupian7.jpg,upload/shangpinxinxi_tupian8.jpg,upload/shangpinxinxi_tupian9.jpg','品牌7','账号7','2024-03-07','茶叶详情7',7,99,'2023-03-08 09:39:40',99.9,7),(48,'2024-03-07 02:38:03','茶叶名称8','茶叶分类8','upload/shangpinxinxi_tupian8.jpg,upload/shangpinxinxi_tupian9.jpg,upload/shangpinxinxi_tupian10.jpg','品牌8','账号8','2024-03-07','茶叶详情8',8,99,'2023-03-08 09:59:27',99.9,8),(1709783037107,'2024-03-07 03:43:56','ceshishangpin222','茶叶分类1','upload/1709783046843.png','33','账号8','2024-03-07','',3,332,'2024-03-07 11:44:46',1000,-1);
/*!40000 ALTER TABLE `shangpinxinxi` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `storeup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storeup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '用户id',
  `refid` bigint DEFAULT NULL COMMENT '茶叶id',
  `tablename` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '图片',
  `type` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩,31:竞拍参与,41:关注)',
  `inteltype` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1776791342365 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `storeup` WRITE;
/*!40000 ALTER TABLE `storeup` DISABLE KEYS */;
INSERT INTO `storeup` VALUES (1678240651582,'2024-03-07 02:38:03',1678240351849,48,'shangpinxinxi','茶叶名称8','upload/shangpinxinxi_tupian8.jpg','1','茶叶分类8',NULL),(1678240673899,'2024-03-07 02:38:03',1678240351849,42,'shangpinxinxi','茶叶名称2','upload/shangpinxinxi_tupian2.jpg','1','茶叶分类2',NULL),(1776791342364,'2026-04-21 17:09:01',1678240351849,43,'shangpinxinxi','茶叶名称3','upload/shangpinxinxi_tupian3.jpg','1','茶叶分类3',NULL);
/*!40000 ALTER TABLE `storeup` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='token表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,1,'admin','users','管理员','apvc6bvw711ir5q3ctmvblmnl1ohkeo9','2024-03-07 02:38:03','2026-04-21 18:08:01'),(2,18,'1','yonghu','用户','0oogs1nvkvzdgmlnbtr1my0ri3mo5ruh','2024-03-07 02:38:03','2024-03-07 02:38:18'),(3,28,'账号8','shangjia','商家','f8jzytd9ot5h8j6xfa9z3oyar8goeddx','2024-03-07 02:38:03','2024-03-07 04:53:22'),(4,1678240351849,'2','yonghu','用户','75d0juaydtzo6znipmq6n8bd4reu799b','2024-03-07 02:38:03','2026-04-21 18:09:01');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `user_behavior`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_behavior` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userid` bigint NOT NULL COMMENT '用户ID',
  `goodid` bigint NOT NULL COMMENT '茶叶ID',
  `behavior_type` varchar(20) NOT NULL COMMENT '行为类型：view-浏览,favorite-收藏,buy-购买',
  `addtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '行为时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_good` (`userid`,`goodid`),
  KEY `idx_addtime` (`addtime`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `user_behavior` WRITE;
/*!40000 ALTER TABLE `user_behavior` DISABLE KEYS */;
INSERT INTO `user_behavior` VALUES (1,1678240351849,42,'view','2026-04-22 01:02:43'),(2,1678240351849,42,'favorite','2026-04-22 01:02:43'),(3,1678240351849,42,'buy','2026-04-22 01:02:43'),(4,1678240351849,43,'view','2026-04-22 01:02:43'),(5,1678240351849,48,'buy','2026-04-22 01:02:43'),(6,18,42,'buy','2026-04-22 01:02:43'),(7,18,43,'favorite','2026-04-22 01:02:43'),(8,18,48,'view','2026-04-22 01:02:43'),(9,1678240351849,42,'view','2026-04-22 01:09:02'),(10,1678240351849,43,'favorite','2026-04-22 01:09:02'),(11,1678240351849,43,'buy','2026-04-22 01:09:02');
/*!40000 ALTER TABLE `user_behavior` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','管理员','2024-03-07 02:38:03');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `yonghu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yonghu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `touxiang` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '头像',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '性别',
  `youxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
  `shouji` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机',
  `money` float DEFAULT '0' COMMENT '余额',
  `vip` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '否' COMMENT '是否会员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `yonghuming` (`yonghuming`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1678240351850 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `yonghu` WRITE;
/*!40000 ALTER TABLE `yonghu` DISABLE KEYS */;
INSERT INTO `yonghu` VALUES (1678240351849,'2024-03-07 02:38:03','2','e10adc3949ba59abbe56e057f20f883e','王丽','upload/1678240339215.jpg','女','12121@12.com','15214121411',209233,'');
/*!40000 ALTER TABLE `yonghu` ENABLE KEYS */;
UNLOCK TABLES;
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
    `shengchanpici` = NULL,
    `shiyongchangjing` = '日常品饮、办公室冲泡'
WHERE `id` = 43;

UPDATE `shangpinxinxi`
SET `chandi` = '黄山生态茶园',
    `haiba` = 720,
    `shengchanpici` = 'PC202604-A02',
    `shiyongchangjing` = '新客体验、门店试饮'
WHERE `id` = 44;

UPDATE `shangpinxinxi`
SET `chandi` = '高山示范茶园',
    `haiba` = 680,
    `shengchanpici` = NULL,
    `shiyongchangjing` = '新客体验、门店试饮'
WHERE `id` IN (45,46,47,48,1709783037107);

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
  `regiontag` varchar(100) DEFAULT NULL COMMENT '产区标签',
  `annualcapacity` decimal(12,2) DEFAULT NULL COMMENT '预计年产能(kg)',
  `mainvariety` varchar(100) DEFAULT NULL COMMENT '主栽品种',
  `plantingyear` int DEFAULT NULL COMMENT '建园年份',
  `basestatus` varchar(50) DEFAULT NULL COMMENT '基地状态',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teabase_code` (`basecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='茶园基地';

INSERT INTO `teabase` (`id`,`basecode`,`basename`,`location`,`altitude`,`area`,`principal`,`contactphone`,`teatype`,`certification`,`enterpriseaccount`,`regiontag`,`annualcapacity`,`mainvariety`,`plantingyear`,`basestatus`,`remark`)
VALUES
  (202604220001,'TB-001','云岭一号高山茶园','福建省武夷山市星村镇',860,128.50,'陈建国','13800000001','高山绿茶','绿色食品认证','账号8','武夷高山产区',5200.00,'福鼎大白茶',2014,'正常','重点展示基地'),
  (202604220002,'TB-002','清风生态茶园','安徽省黄山市歙县',720,96.00,'李春明','13800000002','高山红茶','有机转换认证','账号4','黄山云雾产区',3800.00,'祁门槠叶种',2017,'正常','适合作为产地展示样板');

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
  (202604220101,'PC202604-A01','云岭一号高山茶园','茶叶名称2','绿茶','2026-04-10',520.00,'鲜叶摊青+杀青+烘焙',118.00,'已入库',860,'账号8','用于春茶主推展示'),
  (202604220102,'PC202604-A02','清风生态茶园','茶叶名称4','红茶','2026-04-12',460.00,'萎凋+揉捻+发酵+烘干',105.00,'加工完成',720,'账号4','适合在客户端展示批次信息');

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
  `warehousecode` varchar(64) DEFAULT NULL COMMENT '仓库编号',
  `warehousename` varchar(200) DEFAULT NULL COMMENT '仓库名称',
  `recordtime` datetime DEFAULT NULL COMMENT '记录时间',
  `enterpriseaccount` varchar(200) DEFAULT NULL COMMENT '企业账号',
  `relatedorderid` varchar(100) DEFAULT NULL COMMENT '关联订单号',
  `remark` longtext COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存台账';

INSERT INTO `inventoryrecord` (`id`,`batchcode`,`productname`,`recordtype`,`changestock`,`currentstock`,`warningstock`,`warehousecode`,`warehousename`,`recordtime`,`enterpriseaccount`,`relatedorderid`,`remark`)
VALUES
  (202604220201,'PC202604-A01','茶叶名称2','入库',118.00,118.00,40.00,'WH-A8-01','账号8总仓','2026-04-15 09:00:00','账号8',NULL,'春茶批次首次入库'),
  (202604220202,'PC202604-A01','茶叶名称2','出库',-82.00,36.00,40.00,'WH-A8-02','账号8电商仓','2026-04-20 15:30:00','账号8','20260422001','库存已接近预警线'),
  (202604220204,'PC202604-A01','茶叶名称2','盘点',0.00,126.00,40.00,'WH-A8-01','账号8总仓','2026-04-21 10:00:00','账号8',NULL,'总仓库存充足可调拨'),
  (202604220203,'PC202604-A02','茶叶名称4','入库',105.00,105.00,35.00,'WH-A4-01','账号4总仓','2026-04-16 10:00:00','账号4',NULL,'红茶批次入库');

DROP TABLE IF EXISTS `production_sales_plan`;
CREATE TABLE `production_sales_plan` (
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
  (202604260103,2026,4,'账号4',44,'茶叶名称4','高山红茶',105.00,80.00,30.00,8200.00,'normal','红茶稳定销售');

DROP TABLE IF EXISTS `warehouse_info`;
CREATE TABLE `warehouse_info` (
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
  (202604260203,'WH-A4-01','账号4总仓','总仓','安徽省黄山市','李春明','13800000002','账号4','启用','红茶库存仓');

DROP TABLE IF EXISTS `inventory_transfer_suggestion`;
CREATE TABLE `inventory_transfer_suggestion` (
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
  (202604260301,'账号8','茶叶名称2','WH-A8-01','账号8总仓','WH-A8-02','账号8电商仓',4.00,'电商仓低于预警库存，总仓库存充足','待处理','演示调拨建议');

DROP TABLE IF EXISTS `product_lifecycle_rule`;
CREATE TABLE `product_lifecycle_rule` (
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
  (202604260402,'账号4',44,'茶叶名称4','高山红茶',20,60,120,180,'红茶适合中长期销售，库存偏高时转企业采购',1);
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

