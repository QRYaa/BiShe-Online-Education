-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: tgonline
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bs_app_log`
--

DROP TABLE IF EXISTS `bs_app_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_app_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `exception_info` varchar(255) DEFAULT NULL,
  `result` int DEFAULT NULL,
  `track_id` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_app_log`
--

LOCK TABLES `bs_app_log` WRITE;
/*!40000 ALTER TABLE `bs_app_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_app_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_app_setting`
--

DROP TABLE IF EXISTS `bs_app_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_app_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_id` bigint DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_app_setting`
--

LOCK TABLES `bs_app_setting` WRITE;
/*!40000 ALTER TABLE `bs_app_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_app_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_application`
--

DROP TABLE IF EXISTS `bs_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_application`
--

LOCK TABLES `bs_application` WRITE;
/*!40000 ALTER TABLE `bs_application` DISABLE KEYS */;
INSERT INTO `bs_application` VALUES (1,'ONLINE_EDU',1,'','在线学习',1,'http://localhost:3003/','online'),(2,'WXCP',1,NULL,'企微',2,NULL,'wxcp');
/*!40000 ALTER TABLE `bs_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_department`
--

DROP TABLE IF EXISTS `bs_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_department` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enable` int DEFAULT NULL,
  `head_id` bigint DEFAULT NULL,
  `level` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_department`
--

LOCK TABLES `bs_department` WRITE;
/*!40000 ALTER TABLE `bs_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_dict`
--

DROP TABLE IF EXISTS `bs_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_dict`
--

LOCK TABLES `bs_dict` WRITE;
/*!40000 ALTER TABLE `bs_dict` DISABLE KEYS */;
INSERT INTO `bs_dict` VALUES (12,'STATUS','','状态'),(13,'GENDER','','性别'),(15,'TEACHER_TYPE','','老师类型'),(16,'MEMBER_TYPE','','学员类型'),(17,'CARD_TYPE','','卡片类型'),(18,'CARD_STATUS','','卡项状态'),(19,'CARD_VALID_TYPE','','卡项有效期类型'),(20,'CUSTOMER_SOURCE','','客户来源'),(21,'TEACHING_FORM','','授课形式'),(22,'STAGE_TYPE','','阶段类型'),(23,'FILE_TYPE','','文件类型'),(24,'YES_NO','','是否'),(25,'COURSE_STATUS','','课程状态'),(26,'PAYMENT_STATUS','','支付状态'),(27,'PAYMENT_METHOD','','支付方式'),(28,'WATCHED_STATUS','','观看状态'),(29,'ORDER_ITEM_TYPE','','订单项类型'),(30,'MSG_TYPE','','消息类型'),(31,'MEMBER_STATUS','','员工状态'),(32,'JOIN_SCENE','','加群方式'),(33,'CUSTOMER_TYPE','','客户类型'),(34,'ADD_WAY','','添加客户方式'),(35,'TASK_RESULT','','任务执行结果'),(36,'TASK_STATUS','','任务状态'),(37,'APP_TYPE','','应用类型'),(38,'UALOG_RESULT','','操作结果');
/*!40000 ALTER TABLE `bs_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_dict_item`
--

DROP TABLE IF EXISTS `bs_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `dict_id` bigint DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `item_key` varchar(255) DEFAULT NULL,
  `item_value` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_dict_item`
--

LOCK TABLES `bs_dict_item` WRITE;
/*!40000 ALTER TABLE `bs_dict_item` DISABLE KEYS */;
INSERT INTO `bs_dict_item` VALUES (9,NULL,12,NULL,'1','可用',1),(10,NULL,12,NULL,'0','不可用',0),(11,NULL,13,NULL,'1','男',10),(12,NULL,13,NULL,'2','女',0),(13,NULL,13,NULL,'0','未知',100),(14,NULL,15,NULL,'1','全职',10),(15,NULL,15,NULL,'2','兼职',2),(18,NULL,17,NULL,'1','次卡',10),(19,NULL,17,NULL,'2','时卡',5),(20,NULL,18,NULL,'1','已发布',10),(21,NULL,18,NULL,'2','未发布',5),(22,NULL,19,NULL,'1','永久有效',10),(23,NULL,19,NULL,'2','天数',5),(24,NULL,19,NULL,'3','指定日期',3),(25,NULL,20,NULL,'1','熟人介绍',0),(26,NULL,20,NULL,'2','网络',0),(27,NULL,21,NULL,'1','面授',1),(28,NULL,21,NULL,'2','录播',2),(29,NULL,22,NULL,'1','全职',1),(30,NULL,22,NULL,'2','特聘',2),(31,NULL,22,NULL,'3','试课',3),(32,NULL,23,NULL,'1','图文',1),(33,NULL,23,NULL,'2','音频',2),(34,NULL,23,NULL,'3','视频',3),(35,NULL,24,NULL,'0','否',0),(36,NULL,24,NULL,'1','是',1),(37,NULL,25,NULL,'1','未完结',1),(38,NULL,25,NULL,'2','完结',2),(39,NULL,26,NULL,'3','已取消',3),(40,NULL,26,NULL,'1','待支付',1),(41,NULL,26,NULL,'2','已支付',2),(42,NULL,27,NULL,'1','微信',1),(43,NULL,27,NULL,'2','支付宝',2),(44,NULL,28,NULL,'1','未观看',1),(45,NULL,28,NULL,'2','观看中',2),(46,NULL,28,NULL,'3','观看完',3),(47,NULL,29,NULL,'1','线上课程',1),(48,NULL,29,NULL,'2','书本',2),(49,NULL,30,NULL,'1','文本',1),(50,NULL,30,NULL,'2','图片',2),(51,NULL,31,NULL,'1','激活',1),(52,NULL,31,NULL,'2','禁用',2),(53,NULL,31,NULL,'4','未激活',4),(54,NULL,31,NULL,'5','退出企业',5),(55,NULL,32,NULL,'1','直邀',1),(56,NULL,32,NULL,'2','邀请链接',2),(57,NULL,32,NULL,'3','扫描二维码',3),(58,NULL,33,NULL,'1','微信用户',1),(59,NULL,33,NULL,'2','企微用户',2),(60,NULL,34,NULL,'6','微信联系人',6),(61,NULL,35,NULL,'1','成功',1),(62,NULL,35,NULL,'2','失败',2),(63,NULL,35,NULL,'3','中止',3),(64,NULL,36,NULL,'1','运行中',1),(65,NULL,36,NULL,'2','等待运行',2),(66,NULL,36,NULL,'0','暂停',0),(67,NULL,37,NULL,'1','内部',1),(68,NULL,37,NULL,'2','外部',2),(69,NULL,38,NULL,'1','失败',1),(70,NULL,38,NULL,'2','成功',2),(71,NULL,16,NULL,'1','微信注册',1),(72,NULL,16,NULL,'2','系统添加',2);
/*!40000 ALTER TABLE `bs_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_permission`
--

DROP TABLE IF EXISTS `bs_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `paths` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_permission`
--

LOCK TABLES `bs_permission` WRITE;
/*!40000 ALTER TABLE `bs_permission` DISABLE KEYS */;
INSERT INTO `bs_permission` VALUES (1,'PERMISSION_MGT','权限管理',-1,'/admin/bsPermission',0),(5,'DICT_MGT','字典管理',-1,'/admin/bsDict/save,/admin/bsDict/update,/admin/bsDict/delete,/admin/bsDict/item/save,/admin/bsDict/item/update,/admin/bsDict/item/delete',100),(8,'SETTING_MGT','设置管理',-1,'/admin/bsSetting',90),(9,'ROLE','角色',-1,'/admin/bsRole',60),(10,'DEPARTMENT','组织架构',-1,'/admin/bsDepartment',70),(11,'DEPARTMENT_MGT','组织架构管理',10,'/admin/bsDepartment/save,/admin/bsDepartment/update,/admin/bsDepartment/delete',0),(12,'ROLE_MGT','角色管理',9,'/admin/bsRole/save,/admin/bsRole/update,/admin/bsRole/delete,/admin/bsRole/assignRolePermission',0),(13,'USER','用户',-1,'/admin/bsUser',80),(14,'USER_MGT','用户管理',13,'/admin/bsUser/save,/admin/bsUser/update,/admin/bsUser/delete,/admin/bsUser/assignRole,/admin/bsUser/changePassword',0);
/*!40000 ALTER TABLE `bs_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_role`
--

DROP TABLE IF EXISTS `bs_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_role`
--

LOCK TABLES `bs_role` WRITE;
/*!40000 ALTER TABLE `bs_role` DISABLE KEYS */;
INSERT INTO `bs_role` VALUES (1,'ADMIN','管理员',1,'管理员'),(3,'STAFF','普通员工',1,'普通员工'),(4,'EDITOR','',1,'编辑');
/*!40000 ALTER TABLE `bs_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_role_permission_map`
--

DROP TABLE IF EXISTS `bs_role_permission_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_role_permission_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pid_idx` (`permission_id`),
  KEY `fk_rid_idx` (`role_id`),
  CONSTRAINT `fk_rp_pid` FOREIGN KEY (`permission_id`) REFERENCES `bs_permission` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rp_rid` FOREIGN KEY (`role_id`) REFERENCES `bs_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_role_permission_map`
--

LOCK TABLES `bs_role_permission_map` WRITE;
/*!40000 ALTER TABLE `bs_role_permission_map` DISABLE KEYS */;
INSERT INTO `bs_role_permission_map` VALUES (55,5,1),(56,8,1),(57,13,1),(58,14,1),(59,10,1),(60,11,1),(61,9,1),(62,12,1),(63,1,1),(64,13,3),(65,10,3),(66,9,3),(68,13,4),(69,14,4);
/*!40000 ALTER TABLE `bs_role_permission_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_setting`
--

DROP TABLE IF EXISTS `bs_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_setting`
--

LOCK TABLES `bs_setting` WRITE;
/*!40000 ALTER TABLE `bs_setting` DISABLE KEYS */;
INSERT INTO `bs_setting` VALUES (6,'dingding.appId','xxxxxxaawwddd','','钉钉AppId',NULL,NULL,1,'2024-06-24 16:55:42.426000'),(7,'dingding.appSecret','fssddd','','钉钉AppSecret',NULL,NULL,1,'2024-06-24 16:55:08.522000'),(13,'FKW_AK','tgxyatgz','','凡科AK',1,'2024-06-27 16:32:15.556000',1,'2024-06-27 16:32:15.556000'),(15,'sso.allow.paths','','如果为空，则允许所有地址','允许的单点登录回调地址',1,'2024-10-30 15:25:25.091000',1,'2024-10-30 15:25:25.091000');
/*!40000 ALTER TABLE `bs_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_task`
--

DROP TABLE IF EXISTS `bs_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cron` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `last_result` int DEFAULT NULL,
  `last_time` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `param` varchar(255) DEFAULT NULL,
  `handler_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_task`
--

LOCK TABLES `bs_task` WRITE;
/*!40000 ALTER TABLE `bs_task` DISABLE KEYS */;
INSERT INTO `bs_task` VALUES (2,'0/10 * 8-20 ? * MON,TUE,WED,THU,FRI *',0,2,'2024-12-31 15:05:40.000000','企微拉取信息',0,NULL,'fetchCpMsg'),(4,'5 * 8-20 ? * MON,TUE,WED,THU,FRI *',0,2,'2024-12-11 15:40:05.000000','企微更新客户',0,NULL,'fetchCpCustomer'),(5,'15 * 8-20 ? * MON,TUE,WED,THU,FRI *',0,1,'2024-12-11 15:40:00.000000','企微更新内部群',0,NULL,'fetchCpInternalGroup'),(6,'25 * 8-20 ? * MON,TUE,WED,THU,FRI *',0,2,'2024-12-11 15:40:10.000000','企微更新外部群',0,NULL,'fetchCpExternalGroup');
/*!40000 ALTER TABLE `bs_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_task_log`
--

DROP TABLE IF EXISTS `bs_task_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_task_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exception_info` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `result` int DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `stop_time` datetime(6) DEFAULT NULL,
  `task_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_task_log`
--

LOCK TABLES `bs_task_log` WRITE;
/*!40000 ALTER TABLE `bs_task_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_task_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_user`
--

DROP TABLE IF EXISTS `bs_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  `department_id` bigint DEFAULT NULL,
  `is_super_admin` bit(1) DEFAULT NULL,
  `job_no` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_user`
--

LOCK TABLES `bs_user` WRITE;
/*!40000 ALTER TABLE `bs_user` DISABLE KEYS */;
INSERT INTO `bs_user` VALUES (1,'','noside@126.com',1,0,'$2a$10$OsB/wLTsPDyC/ysBROGTGuPo.fDsSSrQOQQv.E3oQ2kjOQQM2pSOy','admin',1,_binary '\0','000','13480274602','管理员',NULL,NULL,1,'2024-07-30 14:31:45.865000',NULL),(6,'',NULL,1,1,'$2a$10$MEWngukmYw9.WyidoPgSXe6fHDx7GIhMla/jnCf1ESiJ5wlGw1oqa','noside',2,_binary '\0','0001','13800138000','普通员工',NULL,NULL,1,'2024-12-05 17:36:10.064000',NULL),(31,NULL,NULL,1,NULL,'$2a$10$PAU1ttOo7OtC03727Pr/8uqiYIlQRJRn6i4NADP6gh336vIU/rfXe','qry',2,NULL,NULL,'15992976643','邱如意',1,'2024-12-06 09:51:53.254000',1,'2024-12-06 09:51:53.254000',NULL),(32,'',NULL,1,1,NULL,'QiuRuYiCeShi',2,_binary '\0',NULL,'13118850623','邱如意测试',NULL,NULL,1,'2024-12-09 14:17:26.780000',NULL);
/*!40000 ALTER TABLE `bs_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_user_app_log`
--

DROP TABLE IF EXISTS `bs_user_app_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_user_app_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `app_id` bigint DEFAULT NULL,
  `creat_time` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `result` int DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `exception_info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_user_app_log`
--

LOCK TABLES `bs_user_app_log` WRITE;
/*!40000 ALTER TABLE `bs_user_app_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_user_app_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_user_app_map`
--

DROP TABLE IF EXISTS `bs_user_app_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_user_app_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_uam_user_user_id_id_idx` (`user_id`),
  KEY `fk_uam_application_app_id_id_idx` (`app_id`),
  CONSTRAINT `fk_uam_application_app_id_id` FOREIGN KEY (`app_id`) REFERENCES `bs_application` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_uam_user_user_id_id` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_user_app_map`
--

LOCK TABLES `bs_user_app_map` WRITE;
/*!40000 ALTER TABLE `bs_user_app_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `bs_user_app_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_user_department_map`
--

DROP TABLE IF EXISTS `bs_user_department_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_user_department_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department_id` bigint DEFAULT NULL,
  `major` bit(1) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_user_department_map`
--

LOCK TABLES `bs_user_department_map` WRITE;
/*!40000 ALTER TABLE `bs_user_department_map` DISABLE KEYS */;
INSERT INTO `bs_user_department_map` VALUES (33,1,_binary '',1),(34,2,_binary '',6),(35,2,_binary '',31),(36,2,_binary '',32),(38,4,_binary '\0',32),(39,5,_binary '\0',32),(40,6,_binary '\0',32);
/*!40000 ALTER TABLE `bs_user_department_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bs_user_role_map`
--

DROP TABLE IF EXISTS `bs_user_role_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bs_user_role_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rid_idx` (`role_id`),
  KEY `fk_uid_idx` (`user_id`),
  CONSTRAINT `fk_ur_rid` FOREIGN KEY (`role_id`) REFERENCES `bs_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_uid` FOREIGN KEY (`user_id`) REFERENCES `bs_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bs_user_role_map`
--

LOCK TABLES `bs_user_role_map` WRITE;
/*!40000 ALTER TABLE `bs_user_role_map` DISABLE KEYS */;
INSERT INTO `bs_user_role_map` VALUES (1,1,1),(6,3,6),(9,3,1),(11,4,1);
/*!40000 ALTER TABLE `bs_user_role_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oe_banner`
--

DROP TABLE IF EXISTS `oe_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_banner` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enable` int DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oe_banner`
--

LOCK TABLES `oe_banner` WRITE;
/*!40000 ALTER TABLE `oe_banner` DISABLE KEYS */;
/*!40000 ALTER TABLE `oe_banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oe_chapter`
--

DROP TABLE IF EXISTS `oe_chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_chapter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_course`
--

DROP TABLE IF EXISTS `oe_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `course_type_id` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discounted_price` decimal(38,2) DEFAULT NULL,
  `free` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `original_price` decimal(38,2) DEFAULT NULL,
  `published` bit(1) DEFAULT NULL,
  `pupular` bit(1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `paid` bit(1) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `content` longtext,
  `horizontal_image` varchar(255) DEFAULT NULL,
  `square_image` varchar(255) DEFAULT NULL,
  `vertical_image` varchar(255) DEFAULT NULL,
  `actual_learning_count` int DEFAULT NULL,
  `learning_count` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `course_type_id` (`course_type_id`) /*!80000 INVISIBLE */,
  KEY `name` (`name`) /*!80000 INVISIBLE */,
  KEY `published` (`published`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_course_type`
--

DROP TABLE IF EXISTS `oe_course_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_course_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_lesson`
--

DROP TABLE IF EXISTS `oe_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_lesson` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chapter_id` bigint DEFAULT NULL,
  `content` longtext,
  `enable` int DEFAULT NULL,
  `media_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `media_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_member`
--

DROP TABLE IF EXISTS `oe_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `birthday` datetime(6) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `enable` int DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `register_date` datetime(6) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `weixin_open_id` varchar(255) DEFAULT NULL,
  `add_way` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `area_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) /*!80000 INVISIBLE */,
  UNIQUE KEY `tel` (`tel`) /*!80000 INVISIBLE */,
  KEY `enable` (`enable`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
LOCK TABLES `oe_member` WRITE;

INSERT INTO `oe_member` (`id`, `avatar`, `birthday`, `code`, `enable`, `gender`, `name`, `password`, `register_date`, `remark`, `tel`, `weixin_open_id`, `add_way`, `type`, `area_id`) VALUES ('1','/img/2025/3/5/64791a1b-497b-43e4-9fb5-359f572c87cf.png',null,'202503190029','1',null,'默认用户0303',null,'2025-03-04 00:00:00.000000',null,'15992976645','o0Hls6xF6DDuPiBMRGXvXB4pHBko',null,null,null);
UNLOCK TABLES;


--
-- Table structure for table `oe_member_auth_token`
--

DROP TABLE IF EXISTS `oe_member_auth_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_member_auth_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expire_date` datetime(6) DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_member_course`
--

DROP TABLE IF EXISTS `oe_member_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_member_course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completion_percentage` int DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `lesson_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `watched_status` int DEFAULT NULL,
  `last_view_time` datetime(6) DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`) /*!80000 INVISIBLE */,
  KEY `course_id` (`course_id`) /*!80000 INVISIBLE */,
  KEY `completion_percentage` (`completion_percentage`) /*!80000 INVISIBLE */,
  KEY `watch_status` (`watched_status`) /*!80000 INVISIBLE */,
  KEY `last_view_time` (`last_view_time`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_member_lesson`
--

DROP TABLE IF EXISTS `oe_member_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_member_lesson` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_viewed_time` datetime(6) DEFAULT NULL,
  `lesson_id` bigint DEFAULT NULL,
  `progress` int DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `watched_status` int DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `member_course_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_news`
--

DROP TABLE IF EXISTS `oe_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_news` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` bigint DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `content` longtext,
  `digest` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `pinned` bit(1) DEFAULT NULL,
  `published` bit(1) DEFAULT NULL,
  `published_time` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `view_count` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_order`
--

DROP TABLE IF EXISTS `oe_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `after_sale_status` int DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `discount` decimal(38,2) DEFAULT NULL,
  `final_price` decimal(38,2) DEFAULT NULL,
  `member_id` bigint DEFAULT NULL,
  `original_price` decimal(38,2) DEFAULT NULL,
  `payment_method` int DEFAULT NULL,
  `payment_status` int DEFAULT NULL,
  `payment_time` datetime(6) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `member_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) /*!80000 INVISIBLE */,
  KEY `transaction_id` (`transaction_id`) /*!80000 INVISIBLE */,
  KEY `created_time` (`created_time`),
  KEY `payment_status` (`payment_status`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `oe_order_item`
--

DROP TABLE IF EXISTS `oe_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `oe_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `reference_id` bigint DEFAULT NULL,
  `type` int DEFAULT NULL,
  `discount` decimal(38,2) DEFAULT NULL,
  `order_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;