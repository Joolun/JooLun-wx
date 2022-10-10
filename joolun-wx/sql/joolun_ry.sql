/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.27 : Database - joolun_ry
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`joolun_ry` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `joolun_ry`;

/*Table structure for table `gen_table` */

DROP TABLE IF EXISTS `gen_table`;

CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';

/*Data for the table `gen_table` */

insert  into `gen_table`(`table_id`,`table_name`,`table_comment`,`sub_table_name`,`sub_table_fk_name`,`class_name`,`tpl_category`,`package_name`,`module_name`,`business_name`,`function_name`,`function_author`,`gen_type`,`gen_path`,`options`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'sys_config','参数配置表',NULL,NULL,'SysConfig','crud','com.ruoyi.system','system','config','参数配置','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(2,'sys_dept','部门表',NULL,NULL,'SysDept','crud','com.ruoyi.system','system','dept','部门','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(3,'sys_dict_data','字典数据表',NULL,NULL,'SysDictData','crud','com.ruoyi.system','system','data','字典数据','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(4,'sys_dict_type','字典类型表',NULL,NULL,'SysDictType','crud','com.ruoyi.system','system','type','字典类型','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(5,'sys_job','定时任务调度表',NULL,NULL,'SysJob','crud','com.ruoyi.system','system','job','定时任务调度','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(6,'sys_job_log','定时任务调度日志表',NULL,NULL,'SysJobLog','crud','com.ruoyi.system','system','log','定时任务调度日志','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(7,'sys_logininfor','系统访问记录',NULL,NULL,'SysLogininfor','crud','com.ruoyi.system','system','logininfor','系统访问记录','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(8,'sys_menu','菜单权限表',NULL,NULL,'SysMenu','crud','com.ruoyi.system','system','menu','菜单权限','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(9,'sys_notice','通知公告表',NULL,NULL,'SysNotice','crud','com.ruoyi.system','system','notice','通知公告','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL),
(10,'sys_oper_log','操作日志记录',NULL,NULL,'SysOperLog','crud','com.ruoyi.system','system','log','操作日志记录','ruoyi','0','/',NULL,'admin','2021-01-14 17:10:34','',NULL,NULL);

/*Table structure for table `gen_table_column` */

DROP TABLE IF EXISTS `gen_table_column`;

CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';

/*Data for the table `gen_table_column` */

insert  into `gen_table_column`(`column_id`,`table_id`,`column_name`,`column_comment`,`column_type`,`java_type`,`java_field`,`is_pk`,`is_increment`,`is_required`,`is_insert`,`is_edit`,`is_list`,`is_query`,`query_type`,`html_type`,`dict_type`,`sort`,`create_by`,`create_time`,`update_by`,`update_time`) values 
(1,'1','config_id','参数主键','int','Long','configId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(2,'1','config_name','参数名称','varchar(100)','String','configName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(3,'1','config_key','参数键名','varchar(100)','String','configKey','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(4,'1','config_value','参数键值','varchar(500)','String','configValue','0','0',NULL,'1','1','1','1','EQ','textarea','',4,'admin','2021-01-14 17:10:34','',NULL),
(5,'1','config_type','系统内置（Y是 N否）','char(1)','String','configType','0','0',NULL,'1','1','1','1','EQ','select','',5,'admin','2021-01-14 17:10:34','',NULL),
(6,'1','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(7,'1','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2021-01-14 17:10:34','',NULL),
(8,'1','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(9,'1','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',9,'admin','2021-01-14 17:10:34','',NULL),
(10,'1','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',10,'admin','2021-01-14 17:10:34','',NULL),
(11,'2','dept_id','部门id','bigint','Long','deptId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(12,'2','parent_id','父部门id','bigint','Long','parentId','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(13,'2','ancestors','祖级列表','varchar(50)','String','ancestors','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(14,'2','dept_name','部门名称','varchar(30)','String','deptName','0','0',NULL,'1','1','1','1','LIKE','input','',4,'admin','2021-01-14 17:10:34','',NULL),
(15,'2','order_num','显示顺序','int','Long','orderNum','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(16,'2','leader','负责人','varchar(20)','String','leader','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(17,'2','phone','联系电话','varchar(11)','String','phone','0','0',NULL,'1','1','1','1','EQ','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(18,'2','email','邮箱','varchar(50)','String','email','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(19,'2','status','部门状态（0正常 1停用）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',9,'admin','2021-01-14 17:10:34','',NULL),
(20,'2','del_flag','删除标志（0代表存在 2代表删除）','char(1)','String','delFlag','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',10,'admin','2021-01-14 17:10:34','',NULL),
(21,'2','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',11,'admin','2021-01-14 17:10:34','',NULL),
(22,'2','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',12,'admin','2021-01-14 17:10:34','',NULL),
(23,'2','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',13,'admin','2021-01-14 17:10:34','',NULL),
(24,'2','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',14,'admin','2021-01-14 17:10:34','',NULL),
(25,'3','dict_code','字典编码','bigint','Long','dictCode','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(26,'3','dict_sort','字典排序','int','Long','dictSort','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(27,'3','dict_label','字典标签','varchar(100)','String','dictLabel','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(28,'3','dict_value','字典键值','varchar(100)','String','dictValue','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2021-01-14 17:10:34','',NULL),
(29,'3','dict_type','字典类型','varchar(100)','String','dictType','0','0',NULL,'1','1','1','1','EQ','select','',5,'admin','2021-01-14 17:10:34','',NULL),
(30,'3','css_class','样式属性（其他样式扩展）','varchar(100)','String','cssClass','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(31,'3','list_class','表格回显样式','varchar(100)','String','listClass','0','0',NULL,'1','1','1','1','EQ','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(32,'3','is_default','是否默认（Y是 N否）','char(1)','String','isDefault','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(33,'3','status','状态（0正常 1停用）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',9,'admin','2021-01-14 17:10:34','',NULL),
(34,'3','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',10,'admin','2021-01-14 17:10:34','',NULL),
(35,'3','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',11,'admin','2021-01-14 17:10:34','',NULL),
(36,'3','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',12,'admin','2021-01-14 17:10:34','',NULL),
(37,'3','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',13,'admin','2021-01-14 17:10:34','',NULL),
(38,'3','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',14,'admin','2021-01-14 17:10:34','',NULL),
(39,'4','dict_id','字典主键','bigint','Long','dictId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(40,'4','dict_name','字典名称','varchar(100)','String','dictName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(41,'4','dict_type','字典类型','varchar(100)','String','dictType','0','0',NULL,'1','1','1','1','EQ','select','',3,'admin','2021-01-14 17:10:34','',NULL),
(42,'4','status','状态（0正常 1停用）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',4,'admin','2021-01-14 17:10:34','',NULL),
(43,'4','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(44,'4','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',6,'admin','2021-01-14 17:10:34','',NULL),
(45,'4','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(46,'4','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',8,'admin','2021-01-14 17:10:34','',NULL),
(47,'4','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',9,'admin','2021-01-14 17:10:34','',NULL),
(48,'5','job_id','任务ID','bigint','Long','jobId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(49,'5','job_name','任务名称','varchar(64)','String','jobName','1','0',NULL,'1',NULL,NULL,NULL,'LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(50,'5','job_group','任务组名','varchar(64)','String','jobGroup','1','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(51,'5','invoke_target','调用目标字符串','varchar(500)','String','invokeTarget','0','0','1','1','1','1','1','EQ','textarea','',4,'admin','2021-01-14 17:10:34','',NULL),
(52,'5','cron_expression','cron执行表达式','varchar(255)','String','cronExpression','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(53,'5','misfire_policy','计划执行错误策略（1立即执行 2执行一次 3放弃执行）','varchar(20)','String','misfirePolicy','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(54,'5','concurrent','是否并发执行（0允许 1禁止）','char(1)','String','concurrent','0','0',NULL,'1','1','1','1','EQ','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(55,'5','status','状态（0正常 1暂停）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',8,'admin','2021-01-14 17:10:34','',NULL),
(56,'5','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',9,'admin','2021-01-14 17:10:34','',NULL),
(57,'5','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',10,'admin','2021-01-14 17:10:34','',NULL),
(58,'5','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',11,'admin','2021-01-14 17:10:34','',NULL),
(59,'5','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',12,'admin','2021-01-14 17:10:34','',NULL),
(60,'5','remark','备注信息','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',13,'admin','2021-01-14 17:10:34','',NULL),
(61,'6','job_log_id','任务日志ID','bigint','Long','jobLogId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(62,'6','job_name','任务名称','varchar(64)','String','jobName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(63,'6','job_group','任务组名','varchar(64)','String','jobGroup','0','0','1','1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(64,'6','invoke_target','调用目标字符串','varchar(500)','String','invokeTarget','0','0','1','1','1','1','1','EQ','textarea','',4,'admin','2021-01-14 17:10:34','',NULL),
(65,'6','job_message','日志信息','varchar(500)','String','jobMessage','0','0',NULL,'1','1','1','1','EQ','textarea','',5,'admin','2021-01-14 17:10:34','',NULL),
(66,'6','status','执行状态（0正常 1失败）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',6,'admin','2021-01-14 17:10:34','',NULL),
(67,'6','exception_info','异常信息','varchar(2000)','String','exceptionInfo','0','0',NULL,'1','1','1','1','EQ','textarea','',7,'admin','2021-01-14 17:10:34','',NULL),
(68,'6','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',8,'admin','2021-01-14 17:10:34','',NULL),
(69,'7','info_id','访问ID','bigint','Long','infoId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(70,'7','user_name','用户账号','varchar(50)','String','userName','0','0',NULL,'1','1','1','1','LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(71,'7','ipaddr','登录IP地址','varchar(50)','String','ipaddr','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(72,'7','login_location','登录地点','varchar(255)','String','loginLocation','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2021-01-14 17:10:34','',NULL),
(73,'7','browser','浏览器类型','varchar(50)','String','browser','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(74,'7','os','操作系统','varchar(50)','String','os','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(75,'7','status','登录状态（0成功 1失败）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',7,'admin','2021-01-14 17:10:34','',NULL),
(76,'7','msg','提示消息','varchar(255)','String','msg','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(77,'7','login_time','访问时间','datetime','Date','loginTime','0','0',NULL,'1','1','1','1','EQ','datetime','',9,'admin','2021-01-14 17:10:34','',NULL),
(78,'8','menu_id','菜单ID','bigint','Long','menuId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(79,'8','menu_name','菜单名称','varchar(50)','String','menuName','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(80,'8','parent_id','父菜单ID','bigint','Long','parentId','0','0',NULL,'1','1','1','1','EQ','input','',3,'admin','2021-01-14 17:10:34','',NULL),
(81,'8','order_num','显示顺序','int','Long','orderNum','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2021-01-14 17:10:34','',NULL),
(82,'8','path','路由地址','varchar(200)','String','path','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(83,'8','component','组件路径','varchar(255)','String','component','0','0',NULL,'1','1','1','1','EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(84,'8','is_frame','是否为外链（0是 1否）','int','Long','isFrame','0','0',NULL,'1','1','1','1','EQ','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(85,'8','is_cache','是否缓存（0缓存 1不缓存）','int','Long','isCache','0','0',NULL,'1','1','1','1','EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(86,'8','menu_type','菜单类型（M目录 C菜单 F按钮）','char(1)','String','menuType','0','0',NULL,'1','1','1','1','EQ','select','',9,'admin','2021-01-14 17:10:34','',NULL),
(87,'8','visible','菜单状态（0显示 1隐藏）','char(1)','String','visible','0','0',NULL,'1','1','1','1','EQ','input','',10,'admin','2021-01-14 17:10:34','',NULL),
(88,'8','status','菜单状态（0正常 1停用）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',11,'admin','2021-01-14 17:10:34','',NULL),
(89,'8','perms','权限标识','varchar(100)','String','perms','0','0',NULL,'1','1','1','1','EQ','input','',12,'admin','2021-01-14 17:10:34','',NULL),
(90,'8','icon','菜单图标','varchar(100)','String','icon','0','0',NULL,'1','1','1','1','EQ','input','',13,'admin','2021-01-14 17:10:34','',NULL),
(91,'8','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',14,'admin','2021-01-14 17:10:34','',NULL),
(92,'8','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',15,'admin','2021-01-14 17:10:34','',NULL),
(93,'8','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',16,'admin','2021-01-14 17:10:34','',NULL),
(94,'8','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',17,'admin','2021-01-14 17:10:34','',NULL),
(95,'8','remark','备注','varchar(500)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','textarea','',18,'admin','2021-01-14 17:10:34','',NULL),
(96,'9','notice_id','公告ID','int','Long','noticeId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(97,'9','notice_title','公告标题','varchar(50)','String','noticeTitle','0','0','1','1','1','1','1','EQ','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(98,'9','notice_type','公告类型（1通知 2公告）','char(1)','String','noticeType','0','0','1','1','1','1','1','EQ','select','',3,'admin','2021-01-14 17:10:34','',NULL),
(99,'9','notice_content','公告内容','longblob','String','noticeContent','0','0',NULL,'1','1','1','1','EQ','editor','',4,'admin','2021-01-14 17:10:34','',NULL),
(100,'9','status','公告状态（0正常 1关闭）','char(1)','String','status','0','0',NULL,'1','1','1','1','EQ','radio','',5,'admin','2021-01-14 17:10:34','',NULL),
(101,'9','create_by','创建者','varchar(64)','String','createBy','0','0',NULL,'1',NULL,NULL,NULL,'EQ','input','',6,'admin','2021-01-14 17:10:34','',NULL),
(102,'9','create_time','创建时间','datetime','Date','createTime','0','0',NULL,'1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2021-01-14 17:10:34','',NULL),
(103,'9','update_by','更新者','varchar(64)','String','updateBy','0','0',NULL,'1','1',NULL,NULL,'EQ','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(104,'9','update_time','更新时间','datetime','Date','updateTime','0','0',NULL,'1','1',NULL,NULL,'EQ','datetime','',9,'admin','2021-01-14 17:10:34','',NULL),
(105,'9','remark','备注','varchar(255)','String','remark','0','0',NULL,'1','1','1',NULL,'EQ','input','',10,'admin','2021-01-14 17:10:34','',NULL),
(106,'10','oper_id','日志主键','bigint','Long','operId','1','1',NULL,'1',NULL,NULL,NULL,'EQ','input','',1,'admin','2021-01-14 17:10:34','',NULL),
(107,'10','title','模块标题','varchar(50)','String','title','0','0',NULL,'1','1','1','1','EQ','input','',2,'admin','2021-01-14 17:10:34','',NULL),
(108,'10','business_type','业务类型（0其它 1新增 2修改 3删除）','int','Long','businessType','0','0',NULL,'1','1','1','1','EQ','select','',3,'admin','2021-01-14 17:10:34','',NULL),
(109,'10','method','方法名称','varchar(100)','String','method','0','0',NULL,'1','1','1','1','EQ','input','',4,'admin','2021-01-14 17:10:34','',NULL),
(110,'10','request_method','请求方式','varchar(10)','String','requestMethod','0','0',NULL,'1','1','1','1','EQ','input','',5,'admin','2021-01-14 17:10:34','',NULL),
(111,'10','operator_type','操作类别（0其它 1后台用户 2手机端用户）','int','Long','operatorType','0','0',NULL,'1','1','1','1','EQ','select','',6,'admin','2021-01-14 17:10:34','',NULL),
(112,'10','oper_name','操作人员','varchar(50)','String','operName','0','0',NULL,'1','1','1','1','LIKE','input','',7,'admin','2021-01-14 17:10:34','',NULL),
(113,'10','dept_name','部门名称','varchar(50)','String','deptName','0','0',NULL,'1','1','1','1','LIKE','input','',8,'admin','2021-01-14 17:10:34','',NULL),
(114,'10','oper_url','请求URL','varchar(255)','String','operUrl','0','0',NULL,'1','1','1','1','EQ','input','',9,'admin','2021-01-14 17:10:34','',NULL),
(115,'10','oper_ip','主机地址','varchar(50)','String','operIp','0','0',NULL,'1','1','1','1','EQ','input','',10,'admin','2021-01-14 17:10:34','',NULL),
(116,'10','oper_location','操作地点','varchar(255)','String','operLocation','0','0',NULL,'1','1','1','1','EQ','input','',11,'admin','2021-01-14 17:10:34','',NULL),
(117,'10','oper_param','请求参数','varchar(2000)','String','operParam','0','0',NULL,'1','1','1','1','EQ','textarea','',12,'admin','2021-01-14 17:10:34','',NULL),
(118,'10','json_result','返回参数','varchar(2000)','String','jsonResult','0','0',NULL,'1','1','1','1','EQ','textarea','',13,'admin','2021-01-14 17:10:34','',NULL),
(119,'10','status','操作状态（0正常 1异常）','int','Long','status','0','0',NULL,'1','1','1','1','EQ','radio','',14,'admin','2021-01-14 17:10:34','',NULL),
(120,'10','error_msg','错误消息','varchar(2000)','String','errorMsg','0','0',NULL,'1','1','1','1','EQ','textarea','',15,'admin','2021-01-14 17:10:34','',NULL),
(121,'10','oper_time','操作时间','datetime','Date','operTime','0','0',NULL,'1','1','1','1','EQ','datetime','',16,'admin','2021-01-14 17:10:34','',NULL);

/*Table structure for table `goods_category` */

DROP TABLE IF EXISTS `goods_category`;

CREATE TABLE `goods_category` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `enable` char(2) NOT NULL COMMENT '（1：开启；0：关闭）',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父分类编号',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片',
  `sort` smallint DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='分类表';

/*Data for the table `goods_category` */

insert  into `goods_category`(`id`,`enable`,`parent_id`,`name`,`description`,`pic_url`,`sort`,`create_time`,`update_time`,`del_flag`) values 
('1352217944631853057','1','0','JooLun','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/joolun.png',1,'2021-01-21 19:34:15','2021-01-21 19:34:15','0'),
('1352217984268025858','1','1352217944631853057','JooLun二开源码','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/Joolun_logo.png',1,'2021-01-21 19:34:25','2021-01-21 19:34:25','0'),
('1352218128409477121','1','0','鞋包','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E7%AE%B1%E5%8C%85%E9%85%8D%E9%A5%B0.png',4,'2021-01-21 19:34:59','2021-01-21 19:34:59','0'),
('1353731419288924162','1','0','手机','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E6%89%8B%E6%9C%BA.png',2,'2021-01-25 23:48:16','2021-01-25 23:48:16','0'),
('1355427201956208642','1','1353731419288924162','iPhone','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg',1,'2021-01-30 16:06:42','2021-01-30 16:06:42','0'),
('1356512975786287105','1','1353731419288924162','Huawei','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-5.png',2,'2021-02-02 16:01:11','2021-02-02 16:01:11','0'),
('1356950418000826369','1','0','服饰','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E6%9C%8D%E9%A5%B0.png',5,'2021-02-03 20:59:25','2021-02-03 20:59:25','0'),
('1356973226344960001','1','0','电脑','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E7%94%B5%E8%84%91.png',3,'2021-02-03 22:30:03','2021-02-03 22:30:03','0'),
('1366609186567565314','1','0','Joolun','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/joolun.png',0,'2021-03-02 12:39:55','2021-03-02 12:39:55','0'),
('1366628215470501890','1','1366609186567565314','Uniapp 多店版','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-14.png',1,'2021-03-02 13:55:31','2021-03-02 13:55:31','0'),
('1366628362271141890','1','1366609186567565314','小程序商城版','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-17.png',2,'2021-03-02 13:56:06','2021-03-02 13:56:06','0');

/*Table structure for table `goods_spu` */

DROP TABLE IF EXISTS `goods_spu`;

CREATE TABLE `goods_spu` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `spu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'spu编码',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'spu名字',
  `sell_point` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '卖点',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `category_first` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '一级分类ID',
  `category_second` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '二级分类ID',
  `pic_urls` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品图片',
  `shelf` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否上架（1是 0否）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `sales_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `market_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
  `sale_num` int DEFAULT '0' COMMENT '销量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

/*Data for the table `goods_spu` */

insert  into `goods_spu`(`id`,`spu_code`,`name`,`sell_point`,`description`,`category_first`,`category_second`,`pic_urls`,`shelf`,`sort`,`sales_price`,`market_price`,`cost_price`,`stock`,`sale_num`,`create_time`,`update_time`,`del_flag`) values 
('1353738731164561410','1111','iPhone 12 Pro','Apple iPhone 12 Pro (A2408) 128GB 银色 支持移动联通电信5G 双卡双待手机','<p><img src=\"https://img13.360buyimg.com/cms/jfs/t1/120836/20/14832/819799/5f8604f8Eb381a921/5be9108f28a06b69.jpg\"></p>','1353731419288924162',NULL,'[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-4.png\"]','1',0,8499.00,8499.00,3000.00,998,87,'2021-01-26 00:17:19','2021-01-30 14:42:24','0'),
('1355412081553190914','','HUAWEI P40 Pro+','华为 HUAWEI P40 Pro+ 麒麟990 5G SoC芯片 5000万超感知徕卡五摄 100倍双目变焦 8GB+256GB陶瓷白全网通5G','<p><img src=\"https://img30.360buyimg.com/sku/jfs/t1/130133/7/3307/52553/5efc4742E200565f5/6fa1f1a54b11acb1.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/142066/17/1894/134849/5efc4743Eac74a71f/28cf077ca55c34f5.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148131/25/1904/192769/5efc4744E6eb9d7f3/60756a8b50447e1c.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125899/31/5847/193351/5efc4744E35d465bd/9632ee231af86d46.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/134152/32/3459/193038/5efc4744Ef7727a34/24d8e547d9118e60.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/117607/12/11317/193929/5efc4744E4d7b8c68/0fdac90cc0c6e342.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/119971/26/6154/191622/5efc4744E084350ba/3696910ce86799c7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111365/35/11222/193594/5efc4744E89bf74f7/ba23d8e693c05899.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/112101/16/11491/190346/5efc4744Eb475216b/0f591e0dd6f4aa1f.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126083/28/6084/122849/5efc4743Eb74f5730/afd5d279cea23a24.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/142626/35/1814/194436/5efc4744E5e88333b/109f6b80129f93d7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126006/14/5980/191887/5efc4744E4c04463f/3c54d7c9c7166e91.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120527/1/6004/191704/5efc4744E6431ecaf/777cc9d3a7c0f7cf.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/123133/33/6138/195167/5efc4744E33f8f88d/13ece8fb7b02773a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111241/12/11280/188793/5efc4744E68d8897a/ad4e1f2c82902493.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/145365/38/1885/191184/5efc4744E67826c6c/e6393e2dc0aa5a42.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/122722/33/5964/192778/5efc4744E6e1590bb/c9ad4caac3dead33.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148811/10/1874/190669/5efc4744Ecb41cf55/a10fea1ff0f3cae5.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/131817/38/3331/194435/5efc4744E75aea567/ada3e0cd88dd4f22.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/124719/11/5936/192993/5efc4744Eb5128be8/6be3d772b7a70d4a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125244/27/6027/189712/5efc4744E90d3dc7f/d4d6bb0539bd0fb3.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/143975/5/1798/190739/5efc4744E134f9107/e8a940cf3e0baca7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/112510/20/11095/190039/5efc4744E0162060c/64e74f3ef3047c87.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/135166/16/3342/179489/5efc4744E15e0345f/0cb71014bd9f6c38.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111410/36/11009/192128/5efc4744Ec568c450/56557756283c282f.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120081/11/6015/192773/5efc4744E3afd8bfb/2c97e85528e61a46.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/128504/31/6088/190958/5efc4744Eb69b0e3f/75e5100614a80d74.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/118043/22/11395/193721/5efc4744E74454e7a/27678fe84482fc05.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111081/40/11475/193430/5efc4744E5873afb5/66ea1428f6c318e6.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/135964/22/3408/191328/5efc4744E89841aad/ce62d8e74d50121a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125252/16/6085/93984/5efc4743Ee32e21f9/72d8eeb0167e9e24.jpg\"></p>','1353731419288924162','1356512975786287105','[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-7.png\"]','1',0,0.10,300.00,230.00,296,30,'2021-01-30 15:06:37','2021-01-30 15:06:37','0'),
('1355440649314263041','','HUAWEI Mate 40 Pro+','麒麟 9000 5G SoC 芯片，体验震撼超前','<p><img src=\"https://img30.360buyimg.com/sku/jfs/t1/157372/6/954/597397/5fed99ffEb17acdf3/ee647667ca676950.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/158979/1/946/252212/5fed9a09E1c440a55/a013c59fe0aea1c0.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/127637/31/16778/194169/5f9bc4d9E211a89ca/37e8d05bdc190993.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120464/35/16960/162360/5f9bc4d8E4f7982c3/187427af03ca09ad.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/136998/35/14081/187631/5f9bc4d9Ec474680a/a705d0a4eb7882fd.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/123445/39/16851/195095/5f9bc4d9Eaaed7687/c920bc2a6e033833.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/141780/33/12422/192173/5f9bc4d9Ecf53c668/d84d4eae9a6b7f39.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148760/37/12642/191430/5f9bc4d9E1fb46d74/bd22a7057d8a5b87.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/121538/14/16737/184789/5f9bc4d9E33d0e445/4d53e8b18005beef.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126592/29/16541/151671/5f9bc4d9E94955912/6933db658079bdcd.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/128715/7/16676/194348/5f9bc4daEaa5dfe97/896840ca8c00ce16.jpg\"></p>','1353731419288924162','1356512975786287105','[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-5.png\"]','1',0,8999.00,8999.00,8999.00,2000,33,'2021-01-30 17:00:08','2021-01-30 17:00:08','0'),
('1356242467355213826','1','1','1','<p>1</p>','1353731419288924162','1355427201956208642','[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/banner1.png\"]','0',0,1.00,1.00,1.00,1,1,'2021-02-01 22:06:17','2021-02-01 22:06:17','0'),
('1356244176836083713','1','1','1','<p>1</p>','1353731419288924162','1355427201956208642','[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-1.png\"]','0',0,1.00,1.00,1.00,1,1,'2021-02-01 22:13:05','2021-02-01 22:13:05','0'),
('1356246791175127041','1','1','1','<p>1</p>','1352217944631853057','1352217984268025858','[\"http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-2.png\"]','0',0,1.00,1.00,1.00,1,1,'2021-02-01 22:23:28','2021-02-21 09:34:47','0'),
('1366609536255078402','','Joolun uniapp多店版','Java+微服务+SaaS多租户+uniapp+b2c商城管理系统。目前已有小程序商城、公众号H5商城、APP商城（android/ios端）、APP商户端（android/ios端）等主流终端。前端采用uniapp开发，一套代码多端复用，底层设计强大，专为二开研发。','<ul><li><strong style=\"color: rgb(43, 43, 43);\">购买须知</strong></li></ul><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">购买后可获得全部源码、开发文档、部署教程，相应版本终身免费更新。懂java就能上手，二开效率高。私服git管理，可提问、交流技术。</span></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">相应版本都是免费更新的。前期部署上遇到问题我们技术人员还会提供技术协助</span><span style=\"color: rgb(0, 176, 80); background-color: initial;\">，后顾无忧</span></p><p class=\"ql-align-center\"><br></p><ul><li><strong style=\"color: rgb(0, 0, 0);\"> uniapp多店版演示环境</strong></li></ul><p>微服务Sping Cloud + uni-app 强强联合，打造的SaaS多租户B2C多商户（多店铺）商城系统</p><p><img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAgos-6_AUorbeO8AMw2AI42AI!200x200.jpg.webp\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgg8-6_AUo4sL4rwMwggI4ggI!200x200.png.webp\"></p><p><span style=\"background-color: initial; color: rgb(0, 0, 0);\">后台管理:</span>demo1.joolun.com</p><p><br></p><ul><li><strong style=\"color: rgb(0, 0, 0);\">多终端覆盖</strong></li></ul><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwLqI-QUo66LQ7QMw0A84gQY!1500x1500.png.webp\"></span></p><p>APP商户端、APP商城、PC后台管理、小程序商城、公众号H5商城</p><p><br></p><ul><li><strong style=\"color: rgb(43, 43, 43); background-color: initial;\">超前的技术选型</strong></li></ul><p><span style=\"color: rgb(139, 139, 139);\">搭载当前最先进、最流行的核心框架和SDK</span></p><p>* 核心框架：SpringBoot2+SpringCloudAlibaba+ SpringCloudGateway+RESTful规范+前后端分离</p><p>* 配置中心、注册中心：Nacos</p><p>* 安全框架：Spring Security OAuth2</p><p>* 服务间调用：OpenFeign</p><p>* 熔断限流：Sentinel</p><p>* 分布式事务：Seata</p><p>* 消息中间件：Redis</p><p>* 持久层框架：MyBatis-plus</p><p>* PC端后台技术：Vue2 + Element-UI + Avue</p><p>* PC端后台手脚架：Avue-cli</p><p>* 移动端框架：Uniapp(<span style=\"color: rgb(235, 29, 78);\">一套代码，多端发行</span>)</p><p>* 移动端CSS类库：ColorUI</p><p>* 微信开发Java SDK：WxJava</p><p>* 文件管理：阿里oss、七牛云、腾讯cos、minio</p><p>* JDK版本：Java8+</p><p>* 数据库：mysql5.7+</p><p>* 定时任务：XXL-JOB</p><p>* 图表插件：Echartsn</p><p><br></p><ul><li><strong style=\"background-color: initial; color: rgb(0, 0, 0);\">Joolun uniapp多租户/多公众号H5商城/多小程序商城/</strong><strong style=\"color: rgb(0, 0, 0);\">多APP商城/多店铺管理系统</strong></li></ul><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwL2I-QUoyoiCzgQwkAs4hwc!1000x1000.png.webp\"></span></p><p><br></p><p>小程序商城版：多商城（既多租户）一个商城就是一个店铺</p><p>uniapp多店版：多商城（既多租户）一个商城里面有多个店铺（商户）</p><p>各商城（租户）之间数据相互隔离、互不影响</p><p><span style=\"color: rgb(245, 81, 30);\">如果把一个商城（租户）比做淘宝，uniapp多店版就能同时管理多个不同的淘宝，每个淘宝下面有多个店铺。各个淘宝之间是相互独立，数据是互不影响的，各自管理各自的商城、小程序、app、公众号等</span></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg4Lyp_QUo0qujvwcw3As4tQg!800x800.png.webp\"></span></p><ul><li><strong style=\"color: rgb(0, 0, 0);\">多店铺——B2C多商户</strong></li></ul><p><br></p><p> &nbsp; &nbsp; &nbsp;一个商城可以有无数个店铺入驻，商城首页有甄选店铺的模块，点击更多可以进入店铺列表页面，商城里面的店铺就会依次展现出来，每个店铺可以单独设置自己的积分、优惠券、拼团、砍价、秒杀等活动，每个店铺拥有独立的客服、独立的后台数据，还能够进行店铺分享以及收藏等。</p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg-dKf_QUokMC8VjDcCziEBw.png.webp\"></span></p><ul><li><strong>APP商户端</strong></li></ul><p>JooLun Uniapp多店版拥有独立的IM客服管理系统，实时聊天，数据更安全</p><p><br></p><ul><li><strong>商城拖拽装修</strong></li></ul><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/3/ABUIABADGAAgju3M-gUoiPj4owEwgAU4kAM.gif.webp\"></span></p><p>可自定义文字、图片、颜色等界面素材，可自定义主题颜色、</p><p>首页轮播设置、公告设置、导航设置、首页底部TabBar设置</p><p>等。商城首页可进行拖拽装修，丰富的基础组件，随意拖拽，</p><p>装修适合自己风格的主题，让商城更多样化。</p><p><br></p><ul><li><strong>统一后台管理</strong></li></ul><p>JooLun Uniapp多店版拥有多个独立的后台，一个后台账户可以管理小程序商城<span style=\"background-color: initial;\">+</span>公众号H5商城+APP商城等多个终端。</p><p>不同权限有不同权限的登录后台</p><p>系统管理员——最高权限，能看到所有功能</p><p>商城管理员——管理商城所有功能</p><p>店铺管理员——管理本店铺数据</p><p>租户管理员——管理各个租户的信息</p><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg576j_QUolp7utgMwshM4pgo!900x900.png.webp\"></span></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgq6Wo_QUo5vuYfzDWCzixCA!900x900.png.webp\"></span></p>','1366609186567565314','1366628215470501890','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-14.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-15.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-16.png\"]','1',0,16999.00,16999.00,16999.00,999,888,'2021-03-02 12:41:18','2021-03-02 19:32:27','0'),
('1366616360643538946','','Joolun 小程序商城版','小程序商城版包含了公众号基础版所有内容。拥有多公众号后台管理系统+小程序商城系统，前端采用微信原生框架开发，完善的教程文档懂Java就能上手。','<ul><li><strong style=\"color: rgb(43, 43, 43);\">购买须知</strong></li></ul><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">购买后可获得全部源码、开发文档、部署教程，相应版本终身免费更新。懂java就能上手，二开效率高。私服git管理，可提问、交流技术。</span></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">相应版本都是免费更新的。前期部署上遇到问题我们技术人员还会提供技术协助</span><span style=\"color: rgb(0, 176, 80); background-color: initial;\">，后顾无忧</span></p><p class=\"ql-align-center\"><br></p><ul><li><strong>小程序商城版演示环境</strong></li></ul><p>支持多公众号、多小程序、多租户管理</p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAg2Ouj9QUo5P6MzAQwrgM4rgM!160x160.jpg.webp\">   <img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAgmu_j9QUowO-YbDDYAjjYAg!160x160.jpg.webp\"></span></p><p><span style=\"background-color: initial; color: rgb(0, 0, 0);\">后台管理:</span>demo.joolun.com</p><p class=\"ql-align-center\"><span style=\"color: rgb(255, 255, 255);\">公众号演示</span></p><ul><li><strong style=\"color: rgb(43, 43, 43);\">全新微服务框架</strong></li></ul><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 核心框架：Spring Boot2 + Spring Cloud Alibaba + Spring Cloud Gateway+RESTful规范+前后端分离</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 配置中心、注册中心：Nacos</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 安全框架：Spring Security OAuth2</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 服务间调用：OpenFeign</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 熔断限流：Sentinel</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 分布式事务：Seata</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 消息中间件：Redis</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 前端手脚架：Avue-cli</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 前端框架：Vue2 + Element-UI + Avue</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 持久层框架：MyBatis-plus</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 微信开发Java SDK：WxJava</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 文件管理：阿里oss、七牛云、腾讯cos、minio</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* JDK版本：Java8+</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 数据库：mysql5.7+</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 图表插件：Echarts</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">JooLun系统基于SpringCloud微服务构建，完全遵循RESTful规范，</p><p class=\"ql-align-center\">齐全swagger文档，前后端完全分离，前端采用vue组件化模式</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwrnh9wUo1fOLlAEwghY4xgU.png\" alt=\"QQ截图20200628165909.png\" height=\"710\" width=\"2818\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg4bPm9wUoucaT2wMw8hY4tgk.png\" alt=\"小程序1-1.png\" height=\"1206\" width=\"2930\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUow9vufDD4Fzi1DQ.png\" alt=\"小程序2.png\" height=\"1717\" width=\"3064\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUoqJrznQEwnxg47ws.png\" alt=\"小程序3.png\" height=\"1519\" width=\"3103\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUoqtbUigIw2Rg4qws.png\" alt=\"小程序4.png\" height=\"1451\" width=\"3161\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgxrnh9wUovoLigwMw1BU4vwk.png\" alt=\"QQ截图20200628182207.png\" height=\"1211\" width=\"2672\"></p>','1366609186567565314','1366628362271141890','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-17.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-18.png\"]','1',0,2199.00,2199.00,2199.00,999,1699,'2021-03-02 13:08:25','2021-03-02 19:32:32','0');

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单单号',
  `payment_way` char(2) NOT NULL COMMENT '支付方式1、货到付款；2、在线支付',
  `is_pay` char(2) NOT NULL COMMENT '是否支付0、未支付 1、已支付',
  `name` varchar(255) DEFAULT NULL COMMENT '订单名',
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单状态1、待发货 2、待收货 3、确认收货/已完成 5、已关闭',
  `freight_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '运费金额',
  `sales_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '销售金额',
  `payment_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额（销售金额+运费金额）',
  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receiver_time` datetime DEFAULT NULL COMMENT '收货时间',
  `closing_time` datetime DEFAULT NULL COMMENT '成交时间',
  `user_message` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '买家留言',
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '支付交易ID',
  `logistics_id` varchar(32) DEFAULT NULL COMMENT '物流id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单';

/*Data for the table `order_info` */

insert  into `order_info`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`order_no`,`payment_way`,`is_pay`,`name`,`status`,`freight_price`,`sales_price`,`payment_price`,`payment_time`,`delivery_time`,`receiver_time`,`closing_time`,`user_message`,`transaction_id`,`logistics_id`,`remark`) values 
('1354094503631306753','0','2021-01-26 23:51:02','2021-01-26 23:51:02','1352572935968165889','1354094503576731648','2','0',NULL,'5',0.00,0.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
('1354469715404148737','0','2021-01-28 00:41:59','2021-01-28 00:41:59','1352168072700571649','1354469713115086848','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354469714800168962',NULL),
('1354474070446510081','0','2021-01-28 00:59:17','2021-01-28 00:59:17','1354473059078176770','1354474068199342080','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354474069813170178',NULL),
('1354620399822884865','0','2021-01-28 10:40:45','2021-01-28 10:40:45','1354473059078176770','1354620397982580736','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354620399231488001',NULL),
('1354795347837304834','0','2021-01-28 22:15:56','2021-01-28 22:15:55','1354473059078176770','1354795346135351296','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354795347308822530',NULL),
('1354797185856794625','0','2021-01-28 22:23:14','2021-01-28 22:23:13','1354473059078176770','1354797183827705856','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354797185185705985',NULL),
('1354797794534137858','0','2021-01-28 22:25:39','2021-01-28 22:25:39','1354473059078176770','1354797792530268160','2','0','iPhone12白色','5',0.00,4999.00,4999.00,NULL,NULL,NULL,NULL,NULL,NULL,'1354797793913380865',NULL),
('1354798824059609090','0','2021-01-28 22:29:45','2021-01-28 22:29:44','1354473059078176770','1354798822391283712','2','1','iPhone12白色','3',0.00,0.01,0.01,'2021-01-28 22:30:01','2021-01-28 23:16:51','2021-01-28 23:17:41',NULL,NULL,'4200000797202101287152815447','1354798823514349569',NULL),
('1354798971141267457','0','2021-01-28 22:30:20','2021-01-28 22:30:19','1354473059078176770','1354798969477136384','2','1','iPhone12白色','3',0.00,0.01,0.01,'2021-01-28 22:32:33','2021-01-28 23:10:49','2021-01-28 23:16:19',NULL,NULL,'4200000808202101285235202004','1354798970596007937',NULL),
('1355417350676951041','0','2021-01-30 15:27:33','2021-01-30 15:27:33','1355406809988345857','1355417348219076608','2','0','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38',NULL,0.00,0.20,0.20,NULL,NULL,NULL,NULL,NULL,NULL,'1355417349930364930',NULL),
('1355418768053907457','0','2021-01-30 15:33:11','2021-01-30 15:33:11','1355406809988345857','1355418765948354560','2','0','Apple iPhone',NULL,0.00,6000.00,6000.00,NULL,NULL,NULL,NULL,NULL,NULL,'1355418767420567554',NULL),
('1355426472587714562','0','2021-01-30 16:03:48','2021-01-30 16:03:48','1355406809988345857','1355426470679281664','2','1','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','1',0.00,0.10,0.10,'2021-01-30 16:04:20',NULL,NULL,NULL,NULL,'4200000801202101302811163830','1355426471975346177',NULL),
('1357673549756682241','0','2021-02-05 20:52:53','2021-02-05 20:52:52','1354473059078176770','1357673547845074944','2','0','iPhone 12 Pro','5',0.00,8499.00,8499.00,NULL,NULL,NULL,NULL,NULL,NULL,'1357673549135925250',NULL);

/*Table structure for table `order_item` */

DROP TABLE IF EXISTS `order_item`;

CREATE TABLE `order_item` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `spu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品Id',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名',
  `pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片',
  `quantity` int NOT NULL COMMENT '商品数量',
  `sales_price` decimal(10,2) NOT NULL COMMENT '购买单价',
  `freight_price` decimal(10,2) DEFAULT '0.00' COMMENT '运费金额',
  `payment_price` decimal(10,2) DEFAULT '0.00' COMMENT '支付金额（购买单价*商品数量+运费金额）',
  `remark` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单详情';

/*Data for the table `order_item` */

insert  into `order_item`(`id`,`del_flag`,`create_time`,`update_time`,`order_id`,`spu_id`,`spu_name`,`pic_url`,`quantity`,`sales_price`,`freight_price`,`payment_price`,`remark`) values 
('1354469716075237378','0','2021-01-28 00:41:59','2021-01-28 00:41:59','1354469715404148737','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354474071088238594','0','2021-01-28 00:59:18','2021-01-28 00:59:18','1354474070446510081','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354620400435253249','0','2021-01-28 10:40:45','2021-01-28 10:40:45','1354620399822884865','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354795348378370049','0','2021-01-28 22:15:55','2021-01-28 22:15:55','1354795347837304834','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354797186527883265','0','2021-01-28 22:23:14','2021-01-28 22:23:14','1354797185856794625','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354797795180060673','0','2021-01-28 22:25:39','2021-01-28 22:25:39','1354797794534137858','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,4999.00,0.00,4999.00,NULL),
('1354798824613257217','0','2021-01-28 22:29:44','2021-01-28 22:29:44','1354798824059609090','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,0.01,0.00,0.01,NULL),
('1354798971694915585','0','2021-01-28 22:30:19','2021-01-28 22:30:19','1354798971141267457','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,0.01,0.00,0.01,NULL),
('1355417351444508674','0','2021-01-30 15:27:33','2021-01-30 15:27:33','1355417350676951041','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',2,0.10,0.00,0.20,NULL),
('1355418768758550529','0','2021-01-30 15:33:11','2021-01-30 15:33:11','1355418768053907457','1353738731164561410','Apple iPhone','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg',1,6000.00,0.00,6000.00,NULL),
('1355418868545236994','0','2021-01-30 15:33:35','2021-01-30 15:33:35','1355418867987394561','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',1,0.10,0.00,0.10,NULL),
('1355426473221054466','0','2021-01-30 16:03:48','2021-01-30 16:03:48','1355426472587714562','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',1,0.10,0.00,0.10,NULL),
('1357673550390022145','0','2021-02-05 20:52:52','2021-02-05 20:52:52','1357673549756682241','1353738731164561410','iPhone 12 Pro','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-4.png',1,8499.00,0.00,8499.00,NULL);

/*Table structure for table `order_logistics` */

DROP TABLE IF EXISTS `order_logistics`;

CREATE TABLE `order_logistics` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `postal_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮编',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人名字',
  `tel_num` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `logistics` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物流商家',
  `logistics_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物流单号',
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '快递单当前状态，包括-1错误，0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投 等7个状态',
  `is_check` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '签收标记（0：未签收；1：已签收）',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '相关信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单物流表';

/*Data for the table `order_logistics` */

insert  into `order_logistics`(`id`,`del_flag`,`create_time`,`update_time`,`postal_code`,`user_name`,`tel_num`,`address`,`logistics`,`logistics_no`,`status`,`is_check`,`message`) values 
('1354469714800168962','0','2021-01-28 00:41:59','2021-01-28 00:41:59',NULL,'张三','18602513214','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354474069813170178','0','2021-01-28 00:59:17','2021-01-28 00:59:17',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354620399231488001','0','2021-01-28 10:40:45','2021-01-28 10:40:45',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354795347308822530','0','2021-01-28 22:15:55','2021-01-28 22:15:55',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354797185185705985','0','2021-01-28 22:23:13','2021-01-28 22:23:13',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354797793913380865','0','2021-01-28 22:25:38','2021-01-28 22:25:38',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1354798823514349569','0','2021-01-28 22:29:44','2021-01-28 22:29:44',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号','yunda','48466513213213165','1',NULL,NULL),
('1354798970596007937','0','2021-01-28 22:30:19','2021-01-28 22:30:19',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),
('1355417349930364930','0','2021-01-30 15:27:33','2021-01-30 15:27:33',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),
('1355418767420567554','0','2021-01-30 15:33:11','2021-01-30 15:33:11',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),
('1355418867316305921','0','2021-01-30 15:33:35','2021-01-30 15:33:35',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),
('1355426471975346177','0','2021-01-30 16:03:48','2021-01-30 16:03:48',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),
('1357673549135925250','0','2021-02-05 20:52:52','2021-02-05 20:52:52',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_blob_triggers` */

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL,
  `calendar_name` varchar(200) NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_calendars` */

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(200) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_cron_triggers` */

insert  into `qrtz_cron_triggers`(`sched_name`,`trigger_name`,`trigger_group`,`cron_expression`,`time_zone_id`) values 
('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT','0/10 * * * * ?','Asia/Shanghai'),
('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT','0/15 * * * * ?','Asia/Shanghai'),
('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT','0/20 * * * * ?','Asia/Shanghai');

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `fired_time` bigint NOT NULL,
  `sched_time` bigint NOT NULL,
  `priority` int NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(200) DEFAULT NULL,
  `job_group` varchar(200) DEFAULT NULL,
  `is_nonconcurrent` varchar(1) DEFAULT NULL,
  `requests_recovery` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_fired_triggers` */

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_nonconcurrent` varchar(1) NOT NULL,
  `is_update_data` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_job_details` */

insert  into `qrtz_job_details`(`sched_name`,`job_name`,`job_group`,`description`,`job_class_name`,`is_durable`,`is_nonconcurrent`,`is_update_data`,`requests_recovery`,`job_data`) values 
('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT',NULL,'com.joolun.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.joolun.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0(com.joolun.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0v�Ո�xpt\0\0pppt\01t\00/10 * * * * ?t\0ryTask.ryNoParamst\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（无参）t\03t\01x\0'),
('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT',NULL,'com.joolun.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.joolun.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0(com.joolun.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0v�Ո�xpt\0\0pppt\01t\00/15 * * * * ?t\0ryTask.ryParams(\'ry\')t\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（有参）t\03t\01x\0'),
('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT',NULL,'com.joolun.quartz.util.QuartzDisallowConcurrentExecution','0','1','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0TASK_PROPERTIESsr\0com.joolun.quartz.domain.SysJob\0\0\0\0\0\0\0\0L\0\nconcurrentt\0Ljava/lang/String;L\0cronExpressionq\0~\0	L\0invokeTargetq\0~\0	L\0jobGroupq\0~\0	L\0jobIdt\0Ljava/lang/Long;L\0jobNameq\0~\0	L\0\rmisfirePolicyq\0~\0	L\0statusq\0~\0	xr\0(com.joolun.common.core.domain.BaseEntity\0\0\0\0\0\0\0\0L\0createByq\0~\0	L\0\ncreateTimet\0Ljava/util/Date;L\0paramsq\0~\0L\0remarkq\0~\0	L\0searchValueq\0~\0	L\0updateByq\0~\0	L\0\nupdateTimeq\0~\0xpt\0adminsr\0java.util.Datehj�KYt\0\0xpw\0\0v�Ո�xpt\0\0pppt\01t\00/20 * * * * ?t\08ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)t\0DEFAULTsr\0java.lang.Long;��̏#�\0J\0valuexr\0java.lang.Number������\0\0xp\0\0\0\0\0\0\0t\0系统默认（多参）t\03t\01x\0');

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL,
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_locks` */

insert  into `qrtz_locks`(`sched_name`,`lock_name`) values 
('RuoyiScheduler','STATE_ACCESS'),
('RuoyiScheduler','TRIGGER_ACCESS');

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_paused_trigger_grps` */

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint NOT NULL,
  `checkin_interval` bigint NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_scheduler_state` */

insert  into `qrtz_scheduler_state`(`sched_name`,`instance_name`,`last_checkin_time`,`checkin_interval`) values 
('RuoyiScheduler','JL-PC1648538202663',1648538235087,15000);

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `repeat_count` bigint NOT NULL,
  `repeat_interval` bigint NOT NULL,
  `times_triggered` bigint NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_simple_triggers` */

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `str_prop_1` varchar(512) DEFAULT NULL,
  `str_prop_2` varchar(512) DEFAULT NULL,
  `str_prop_3` varchar(512) DEFAULT NULL,
  `int_prop_1` int DEFAULT NULL,
  `int_prop_2` int DEFAULT NULL,
  `long_prop_1` bigint DEFAULT NULL,
  `long_prop_2` bigint DEFAULT NULL,
  `dec_prop_1` decimal(13,4) DEFAULT NULL,
  `dec_prop_2` decimal(13,4) DEFAULT NULL,
  `bool_prop_1` varchar(1) DEFAULT NULL,
  `bool_prop_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_simprop_triggers` */

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `next_fire_time` bigint DEFAULT NULL,
  `prev_fire_time` bigint DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint NOT NULL,
  `end_time` bigint DEFAULT NULL,
  `calendar_name` varchar(200) DEFAULT NULL,
  `misfire_instr` smallint DEFAULT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `qrtz_triggers` */

insert  into `qrtz_triggers`(`sched_name`,`trigger_name`,`trigger_group`,`job_name`,`job_group`,`description`,`next_fire_time`,`prev_fire_time`,`priority`,`trigger_state`,`trigger_type`,`start_time`,`end_time`,`calendar_name`,`misfire_instr`,`job_data`) values 
('RuoyiScheduler','TASK_CLASS_NAME1','DEFAULT','TASK_CLASS_NAME1','DEFAULT',NULL,1648538210000,-1,5,'PAUSED','CRON',1648538202000,0,NULL,2,''),
('RuoyiScheduler','TASK_CLASS_NAME2','DEFAULT','TASK_CLASS_NAME2','DEFAULT',NULL,1648538205000,-1,5,'PAUSED','CRON',1648538202000,0,NULL,2,''),
('RuoyiScheduler','TASK_CLASS_NAME3','DEFAULT','TASK_CLASS_NAME3','DEFAULT',NULL,1648538220000,-1,5,'PAUSED','CRON',1648538202000,0,NULL,2,'');

/*Table structure for table `shopping_cart` */

DROP TABLE IF EXISTS `shopping_cart`;

CREATE TABLE `shopping_cart` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `spu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品SPU',
  `quantity` int NOT NULL COMMENT '数量',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '加入时的spu名字',
  `add_price` decimal(10,2) DEFAULT NULL COMMENT '加入时价格',
  `pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='购物车';

/*Data for the table `shopping_cart` */

insert  into `shopping_cart`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`spu_id`,`quantity`,`spu_name`,`add_price`,`pic_url`) values 
('1353755369452634114','0','2021-01-26 01:23:26','2021-01-26 01:29:11','1352233320682930178','1353738731164561410',1,'iPhone12白色',4999.00,NULL),
('1354094384559210498','0','2021-01-26 23:50:33','2021-01-26 23:50:33','1352572935968165889','1353738731164561410',1,'iPhone12白色',4999.00,'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png'),
('1355427342960320514','0','2021-01-30 16:07:16','2021-01-30 16:07:16','1355406809988345857','1353738731164561410',1,'Apple iPhone',6000.00,'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg'),
('1357249438573252609','0','2021-02-04 16:47:37','2021-02-04 16:47:37','1356171782972882945','1355440649314263041',1,'HUAWEI Mate 40 Pro+',8999.00,'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-5.png'),
('1357249497834573826','0','2021-02-04 16:47:51','2021-02-04 16:47:51','1356171782972882945','1355412081553190914',1,'HUAWEI P40 Pro+',0.10,'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-7.png'),
('1357673715054202882','0','2021-02-05 20:53:31','2021-02-05 20:53:31','1354473059078176770','1353738731164561410',1,'iPhone 12 Pro',8499.00,'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-4.png');

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';

/*Data for the table `sys_config` */

insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2021-01-14 15:39:13','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2021-01-14 15:39:13','',NULL,'初始化密码 123456'),
(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2021-01-14 15:39:13','',NULL,'深色主题theme-dark，浅色主题theme-light');

/*Table structure for table `sys_dept` */

DROP TABLE IF EXISTS `sys_dept`;

CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';

/*Data for the table `sys_dept` */

insert  into `sys_dept`(`dept_id`,`parent_id`,`ancestors`,`dept_name`,`order_num`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values 
(100,0,'0','JooLun',0,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:55'),
(101,100,'0,100','深圳总公司',1,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:45'),
(102,100,'0,100','长沙分公司',2,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:55'),
(103,101,'0,100,101','研发部门',1,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:34'),
(104,101,'0,100,101','市场部门',2,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:37'),
(105,101,'0,100,101','测试部门',3,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:39'),
(106,101,'0,100,101','财务部门',4,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:42'),
(107,101,'0,100,101','运维部门',5,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:45'),
(108,102,'0,100,102','市场部门',1,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:52'),
(109,102,'0,100,102','财务部门',2,'JooLun','15888888888','ry@qq.com','0','0','admin','2021-01-14 15:39:13','admin','2021-01-16 00:07:55');

/*Table structure for table `sys_dict_data` */

DROP TABLE IF EXISTS `sys_dict_data`;

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';

/*Data for the table `sys_dict_data` */

insert  into `sys_dict_data`(`dict_code`,`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,1,'男','0','sys_user_sex','','','Y','0','admin','2021-01-14 15:39:13','',NULL,'性别男'),
(2,2,'女','1','sys_user_sex','','','N','0','admin','2021-01-14 15:39:13','',NULL,'性别女'),
(3,3,'未知','2','sys_user_sex','','','N','0','admin','2021-01-14 15:39:13','',NULL,'性别未知'),
(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2021-01-14 15:39:13','',NULL,'显示菜单'),
(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'隐藏菜单'),
(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2021-01-14 15:39:13','',NULL,'正常状态'),
(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'停用状态'),
(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2021-01-14 15:39:13','',NULL,'正常状态'),
(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'停用状态'),
(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2021-01-14 15:39:13','',NULL,'默认分组'),
(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2021-01-14 15:39:13','',NULL,'系统分组'),
(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2021-01-14 15:39:13','',NULL,'系统默认是'),
(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'系统默认否'),
(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2021-01-14 15:39:13','',NULL,'通知'),
(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2021-01-14 15:39:13','',NULL,'公告'),
(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2021-01-14 15:39:13','',NULL,'正常状态'),
(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'关闭状态'),
(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2021-01-14 15:39:13','',NULL,'新增操作'),
(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2021-01-14 15:39:13','',NULL,'修改操作'),
(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'删除操作'),
(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2021-01-14 15:39:13','',NULL,'授权操作'),
(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2021-01-14 15:39:13','',NULL,'导出操作'),
(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2021-01-14 15:39:13','',NULL,'导入操作'),
(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'强退操作'),
(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2021-01-14 15:39:13','',NULL,'生成操作'),
(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'清空操作'),
(27,1,'成功','0','sys_common_status','','primary','N','0','admin','2021-01-14 15:39:13','',NULL,'正常状态'),
(28,2,'失败','1','sys_common_status','','danger','N','0','admin','2021-01-14 15:39:13','',NULL,'停用状态');

/*Table structure for table `sys_dict_type` */

DROP TABLE IF EXISTS `sys_dict_type`;

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';

/*Data for the table `sys_dict_type` */

insert  into `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'用户性别','sys_user_sex','0','admin','2021-01-14 15:39:13','',NULL,'用户性别列表'),
(2,'菜单状态','sys_show_hide','0','admin','2021-01-14 15:39:13','',NULL,'菜单状态列表'),
(3,'系统开关','sys_normal_disable','0','admin','2021-01-14 15:39:13','',NULL,'系统开关列表'),
(4,'任务状态','sys_job_status','0','admin','2021-01-14 15:39:13','',NULL,'任务状态列表'),
(5,'任务分组','sys_job_group','0','admin','2021-01-14 15:39:13','',NULL,'任务分组列表'),
(6,'系统是否','sys_yes_no','0','admin','2021-01-14 15:39:13','',NULL,'系统是否列表'),
(7,'通知类型','sys_notice_type','0','admin','2021-01-14 15:39:13','',NULL,'通知类型列表'),
(8,'通知状态','sys_notice_status','0','admin','2021-01-14 15:39:13','',NULL,'通知状态列表'),
(9,'操作类型','sys_oper_type','0','admin','2021-01-14 15:39:13','',NULL,'操作类型列表'),
(10,'系统状态','sys_common_status','0','admin','2021-01-14 15:39:13','',NULL,'登录状态列表');

/*Table structure for table `sys_job` */

DROP TABLE IF EXISTS `sys_job`;

CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';

/*Data for the table `sys_job` */

insert  into `sys_job`(`job_id`,`job_name`,`job_group`,`invoke_target`,`cron_expression`,`misfire_policy`,`concurrent`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2021-01-14 15:39:13','',NULL,''),
(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2021-01-14 15:39:13','',NULL,''),
(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2021-01-14 15:39:13','',NULL,'');

/*Table structure for table `sys_job_log` */

DROP TABLE IF EXISTS `sys_job_log`;

CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';

/*Data for the table `sys_job_log` */

/*Table structure for table `sys_logininfor` */

DROP TABLE IF EXISTS `sys_logininfor`;

CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(50) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=340 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';

/*Data for the table `sys_logininfor` */

insert  into `sys_logininfor`(`info_id`,`user_name`,`ipaddr`,`login_location`,`browser`,`os`,`status`,`msg`,`login_time`) values 
(100,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码错误','2021-01-14 15:41:18'),
(101,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 15:41:22'),
(102,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码错误','2021-01-14 15:43:17'),
(103,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 15:43:20'),
(104,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 15:46:08'),
(105,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 16:50:11'),
(106,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-14 16:50:55'),
(107,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 16:50:59'),
(108,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-14 16:51:16'),
(109,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 16:51:20'),
(110,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-14 16:51:32'),
(111,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 16:51:39'),
(112,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 17:44:43'),
(113,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 18:25:57'),
(114,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 18:26:58'),
(115,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 18:30:34'),
(116,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 18:43:44'),
(117,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 22:05:03'),
(118,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-14 22:45:13'),
(119,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-14 22:45:19'),
(120,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-15 10:40:50'),
(121,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-15 19:45:31'),
(122,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-15 20:51:34'),
(123,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-15 20:52:12'),
(124,'ry','127.0.0.1','内网IP','Chrome 8','Windows 10','1','用户不存在/密码错误','2021-01-15 20:52:20'),
(125,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','用户不存在/密码错误','2021-01-15 20:52:28'),
(126,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-15 20:52:33'),
(127,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-15 20:53:38'),
(128,'test','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-15 20:53:44'),
(129,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','1','用户不存在/密码错误','2021-01-15 22:05:36'),
(130,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-15 22:05:40'),
(131,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-15 22:05:45'),
(132,'test','61.145.151.235','XX XX','Chrome','Windows 10','0','登录成功','2021-01-15 22:42:07'),
(133,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','0','退出成功','2021-01-15 23:17:46'),
(134,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-15 23:17:49'),
(135,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','退出成功','2021-01-15 23:19:23'),
(136,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-15 23:19:29'),
(137,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','退出成功','2021-01-15 23:19:49'),
(138,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-15 23:19:54'),
(139,'test','91.74.20.206','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-15 23:33:00'),
(140,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 00:05:46'),
(141,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','退出成功','2021-01-16 00:06:56'),
(142,'admin','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 00:07:01'),
(143,'test','221.218.98.34','XX XX','Chrome 58','Windows 7','0','登录成功','2021-01-16 01:25:39'),
(144,'test','142.47.103.147','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 08:57:19'),
(145,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','0','登录成功','2021-01-16 10:15:53'),
(146,'test','223.88.17.147','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 10:53:10'),
(147,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','1','验证码已失效','2021-01-16 11:07:07'),
(148,'test','142.47.103.147','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 11:09:24'),
(149,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','1','验证码已失效','2021-01-16 11:09:43'),
(150,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','1','验证码错误','2021-01-16 11:09:49'),
(151,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','0','登录成功','2021-01-16 11:10:01'),
(152,'test','1.192.246.73','XX XX','Chrome','Windows 10','0','登录成功','2021-01-16 11:31:28'),
(153,'test','220.202.217.37','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 11:55:44'),
(154,'test','113.246.79.30','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 12:09:58'),
(155,'test','223.72.41.225','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 12:11:40'),
(156,'test','142.47.103.147','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 12:29:44'),
(157,'test','223.72.85.86','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 12:59:50'),
(158,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','0','登录成功','2021-01-16 13:40:26'),
(159,'test','111.20.153.212','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-16 14:49:17'),
(160,'test','14.212.14.199','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 15:17:47'),
(161,'test','220.112.121.26','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-16 16:23:42'),
(162,'test','183.25.125.34','XX XX','Firefox 8','Windows 10','0','登录成功','2021-01-16 16:54:52'),
(163,'test','120.245.112.232','XX XX','Chrome','Windows 10','0','登录成功','2021-01-16 17:02:56'),
(164,'test','183.14.31.241','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 17:12:50'),
(165,'test','47.96.85.57','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 17:31:38'),
(166,'test','223.73.132.192','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 17:36:37'),
(167,'test','47.96.85.57','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 18:40:53'),
(168,'test','59.175.14.204','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 20:30:45'),
(169,'test','223.78.203.183','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 21:09:21'),
(170,'test','59.175.14.204','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 22:06:09'),
(171,'test','125.210.251.204','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 22:59:08'),
(172,'test','125.210.223.184','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-16 23:30:12'),
(173,'test','101.224.95.230','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 00:32:32'),
(174,'test','112.0.112.224','XX XX','Chrome','Windows 10','0','登录成功','2021-01-17 09:42:17'),
(175,'test','111.205.43.226','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 09:57:16'),
(176,'test','223.167.21.235','XX XX','Chrome 8','Mac OS X','1','验证码已失效','2021-01-17 10:10:28'),
(177,'test','223.167.21.235','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-17 10:10:33'),
(178,'test','112.0.112.224','XX XX','Chrome','Windows 10','0','登录成功','2021-01-17 10:24:57'),
(179,'test','171.44.170.121','XX XX','Chrome 59','Windows 10','0','登录成功','2021-01-17 12:19:42'),
(180,'test','8.210.254.1','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 12:57:56'),
(181,'test','111.75.252.92','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 15:27:34'),
(182,'test','124.202.200.82','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 16:58:13'),
(183,'test','59.109.153.189','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 17:21:27'),
(184,'test','117.136.33.225','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-17 21:51:57'),
(185,'test','27.38.236.28','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-17 22:28:51'),
(186,'test','125.210.251.204','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-17 22:40:18'),
(187,'test','101.88.58.166','XX XX','Safari','Mac OS X','0','登录成功','2021-01-18 00:26:42'),
(188,'test','113.66.41.106','XX XX','Chrome','Windows 10','0','登录成功','2021-01-18 02:39:11'),
(189,'test','140.207.90.173','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-18 08:59:19'),
(190,'test','140.207.90.173','XX XX','Chrome 8','Windows 10','1','验证码已失效','2021-01-18 09:04:16'),
(191,'test','140.207.90.173','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 09:04:21'),
(192,'test','222.133.7.18','XX XX','Chrome','Windows 10','0','登录成功','2021-01-18 09:12:08'),
(193,'test','119.254.252.189','XX XX','Chrome 8','Windows 7','0','登录成功','2021-01-18 09:43:40'),
(194,'test','112.30.39.71','XX XX','Chrome 8','Windows 7','0','登录成功','2021-01-18 11:52:40'),
(195,'test','218.94.111.114','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 12:56:09'),
(196,'test','220.248.209.209','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 13:21:43'),
(197,'test','112.49.134.248','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 14:05:04'),
(198,'test','222.212.90.35','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 14:48:42'),
(199,'test','43.247.230.78','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 14:53:41'),
(200,'test','222.212.90.35','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 15:04:14'),
(201,'test','140.243.12.66','XX XX','Safari','Mac OS X','0','登录成功','2021-01-18 15:13:51'),
(202,'test','61.159.254.233','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 15:36:47'),
(203,'test','122.10.38.137','XX XX','Chrome 65','Windows 10','0','登录成功','2021-01-18 15:41:25'),
(204,'test','175.10.220.250','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-18 15:55:27'),
(205,'test','175.153.195.94','XX XX','Chrome','Windows 10','0','登录成功','2021-01-18 16:20:32'),
(206,'test','221.205.74.216','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-18 16:22:17'),
(207,'test','221.205.74.216','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 16:22:21'),
(208,'test','103.8.207.241','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-18 16:28:07'),
(209,'test','223.104.213.169','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 16:33:48'),
(210,'test','103.8.207.241','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-18 16:34:26'),
(211,'test','1.85.30.162','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 16:47:00'),
(212,'test','220.178.164.188','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 17:01:39'),
(213,'test','113.132.23.193','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 17:06:28'),
(214,'test','120.230.139.76','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 17:17:10'),
(215,'test','110.185.17.93','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 17:19:14'),
(216,'test','103.8.207.241','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-18 18:14:22'),
(217,'test','120.230.139.76','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-18 18:17:19'),
(218,'test','120.230.139.76','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 18:17:24'),
(219,'test','221.205.74.216','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 18:19:06'),
(220,'test','124.127.132.138','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-18 18:51:05'),
(221,'test','124.127.132.138','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 18:51:10'),
(222,'test','223.104.6.17','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 19:38:58'),
(223,'test','218.28.24.138','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 19:46:14'),
(224,'test','125.210.251.204','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 23:17:01'),
(225,'test','61.243.41.118','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-18 23:34:18'),
(226,'test','36.27.39.190','XX XX','Chrome 8','Windows 7','0','登录成功','2021-01-18 23:49:19'),
(227,'test','114.241.237.31','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-19 04:56:56'),
(228,'test','113.66.228.190','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 08:37:46'),
(229,'test','211.136.253.191','XX XX','Chrome 8','Windows 7','1','验证码错误','2021-01-19 08:56:02'),
(230,'test','211.136.253.191','XX XX','Chrome 8','Windows 7','0','登录成功','2021-01-19 08:56:10'),
(231,'test','36.57.153.64','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-19 09:44:27'),
(232,'test','218.66.80.159','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 11:13:15'),
(233,'test','222.188.241.8','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 11:27:51'),
(234,'test','60.208.209.207','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 11:44:07'),
(235,'test','123.149.115.180','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 12:28:42'),
(236,'test','27.154.58.146','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 13:18:22'),
(237,'test','112.48.29.75','XX XX','Chrome 8','Windows 7','0','登录成功','2021-01-19 13:26:34'),
(238,'test','60.208.209.207','XX XX','Chrome 8','Windows 10','1','验证码错误','2021-01-19 14:21:52'),
(239,'test','60.208.209.207','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:22:00'),
(240,'test','27.27.235.134','XX XX','Chrome','Windows 10','0','登录成功','2021-01-19 14:22:32'),
(241,'test','106.117.97.117','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:28:35'),
(242,'test','45.76.75.125','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:28:54'),
(243,'test','183.238.224.138','XX XX','Chrome','Windows 10','0','登录成功','2021-01-19 14:29:49'),
(244,'test','106.114.180.243','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:33:21'),
(245,'test','106.117.97.117','XX XX','Chrome 8','Windows 10','0','退出成功','2021-01-19 14:35:33'),
(246,'test','221.192.178.108','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:35:51'),
(247,'test','123.149.115.180','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 14:53:29'),
(248,'test','27.19.120.211','XX XX','Chrome','Windows 10','0','登录成功','2021-01-19 15:15:28'),
(249,'test','115.214.70.4','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 15:50:59'),
(250,'test','60.2.125.182','XX XX','Chrome 61','Windows 10','0','登录成功','2021-01-19 16:05:26'),
(251,'test','223.11.3.53','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 16:20:03'),
(252,'test','218.200.109.70','XX XX','Chrome 55','Windows 10','0','登录成功','2021-01-19 16:24:38'),
(253,'test','125.76.212.101','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 16:32:55'),
(254,'test','117.14.73.96','XX XX','Chrome 8','Mac OS X','0','登录成功','2021-01-19 16:42:52'),
(255,'test','218.81.11.192','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 17:25:11'),
(256,'test','120.227.36.71','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 17:32:10'),
(257,'test','116.55.48.5','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 17:44:43'),
(258,'test','218.81.11.192','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 18:07:22'),
(259,'test','221.216.138.206','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 19:09:05'),
(260,'test','218.81.11.192','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 19:19:11'),
(261,'test','125.116.132.238','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 19:35:12'),
(262,'test','222.76.36.97','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 20:06:59'),
(263,'test','122.247.153.194','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 20:11:54'),
(264,'test','222.76.36.97','XX XX','Chrome 8','Windows 10','0','登录成功','2021-01-19 21:01:39'),
(265,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-19 22:28:16'),
(266,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-21 15:20:36'),
(267,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-21 17:43:05'),
(268,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-21 17:49:53'),
(269,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-21 17:50:03'),
(270,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-21 19:29:40'),
(271,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-21 20:14:16'),
(272,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','退出成功','2021-01-21 20:15:05'),
(273,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','验证码错误','2021-01-21 20:15:11'),
(274,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','用户不存在/密码错误','2021-01-21 20:15:15'),
(275,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','验证码错误','2021-01-21 20:15:24'),
(276,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','用户不存在/密码错误','2021-01-21 20:15:26'),
(277,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-21 20:15:33'),
(278,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-22 10:44:57'),
(279,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-22 12:06:10'),
(280,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-22 19:12:47'),
(281,'test','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','退出成功','2021-01-22 19:13:05'),
(282,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-22 19:13:31'),
(283,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','验证码错误','2021-01-25 21:36:22'),
(284,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','1','用户不存在/密码错误','2021-01-25 21:36:25'),
(285,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-25 21:37:03'),
(286,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','退出成功','2021-01-25 22:16:06'),
(287,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-25 22:16:12'),
(288,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-25 23:03:30'),
(289,'test','192.168.0.101','内网IP','Chrome 8','Mac OS X','1','验证码错误','2021-01-26 01:23:53'),
(290,'test','192.168.0.101','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-26 01:23:56'),
(291,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-26 01:24:14'),
(292,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-26 21:55:38'),
(293,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-26 22:36:07'),
(294,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-27 00:06:16'),
(295,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码已失效','2021-01-28 22:28:35'),
(296,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-28 22:28:39'),
(297,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-01-28 22:39:52'),
(298,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-01-28 22:40:03'),
(299,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-30 14:36:28'),
(300,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-30 16:06:11'),
(301,'admin','127.0.0.1','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-01-30 16:58:26'),
(302,'test','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 18:03:38'),
(303,'test','127.0.0.1, 111.22.249.94','内网IP','Chrome 56','Mac OS X','1','验证码已失效','2021-02-01 18:07:33'),
(304,'test','127.0.0.1, 111.22.249.94','内网IP','Chrome 56','Mac OS X','0','登录成功','2021-02-01 18:07:40'),
(305,'test','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 18:28:32'),
(306,'test','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 21:47:31'),
(307,'test','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','退出成功','2021-02-01 22:00:42'),
(308,'admin','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','1','用户不存在/密码错误','2021-02-01 22:00:51'),
(309,'admin','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 22:01:15'),
(310,'test','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 23:26:26'),
(311,'test','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','退出成功','2021-02-01 23:26:42'),
(312,'admin','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-01 23:26:51'),
(313,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-02 15:13:42'),
(314,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-02 18:23:56'),
(315,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','1','验证码错误','2021-02-03 18:28:32'),
(316,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-03 18:28:36'),
(317,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-03 19:36:37'),
(318,'admin','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-03 20:55:51'),
(319,'admin','192.168.0.100, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-03 22:12:00'),
(320,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','1','验证码已失效','2021-02-04 16:41:37'),
(321,'admin','127.0.0.1, 111.22.249.94','内网IP','Chrome 8','Mac OS X','0','登录成功','2021-02-04 16:41:41'),
(322,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-05 20:51:44'),
(323,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-05 21:32:46'),
(324,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-06 10:16:24'),
(325,'test','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-21 09:33:45'),
(326,'test','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-02-21 09:34:06'),
(327,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-21 09:34:14'),
(328,'admin','127.0.0.1','内网IP','Chrome 8','Windows 10','0','退出成功','2021-02-21 09:34:32'),
(329,'test','127.0.0.1','内网IP','Chrome 8','Windows 10','1','验证码错误','2021-02-21 09:34:36'),
(330,'test','127.0.0.1','内网IP','Chrome 8','Windows 10','0','登录成功','2021-02-21 09:34:39'),
(331,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-01 12:03:57'),
(332,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2021-11-01 12:04:02'),
(333,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2021-11-01 12:04:15'),
(334,'test','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-03-29 14:42:04'),
(335,'test','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2022-03-29 14:48:08'),
(336,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','1','用户不存在/密码错误','2022-03-29 14:48:18'),
(337,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-03-29 14:48:37'),
(338,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','退出成功','2022-03-29 14:53:01'),
(339,'admin','127.0.0.1','内网IP','Chrome 9','Windows 10','0','登录成功','2022-03-29 14:53:08');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2060 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'系统管理',0,10,'system',NULL,1,0,'M','0','0','','system','admin','2021-01-14 15:39:13','admin','2021-01-21 17:43:31','系统管理目录'),
(2,'系统监控',0,20,'monitor',NULL,1,0,'M','0','0','','monitor','admin','2021-01-14 15:39:13','admin','2021-01-21 17:43:38','系统监控目录'),
(3,'系统工具',0,30,'tool',NULL,1,0,'M','0','0','','tool','admin','2021-01-14 15:39:13','admin','2021-01-21 17:43:43','系统工具目录'),
(4,'公众号管理',0,0,'wxmp',NULL,1,0,'M','0','0','','wechat','admin','2018-03-16 11:33:00','admin','2020-03-05 14:59:21','若依官网地址'),
(100,'用户管理',1,1,'user','system/user/index',1,0,'C','0','0','system:user:list','user','admin','2021-01-14 15:39:13','',NULL,'用户管理菜单'),
(101,'角色管理',1,2,'role','system/role/index',1,0,'C','0','0','system:role:list','peoples','admin','2021-01-14 15:39:13','',NULL,'角色管理菜单'),
(102,'菜单管理',1,3,'menu','system/menu/index',1,0,'C','0','0','system:menu:list','tree-table','admin','2021-01-14 15:39:13','',NULL,'菜单管理菜单'),
(103,'部门管理',1,4,'dept','system/dept/index',1,0,'C','0','0','system:dept:list','tree','admin','2021-01-14 15:39:13','',NULL,'部门管理菜单'),
(104,'岗位管理',1,5,'post','system/post/index',1,0,'C','0','0','system:post:list','post','admin','2021-01-14 15:39:13','',NULL,'岗位管理菜单'),
(105,'字典管理',1,6,'dict','system/dict/index',1,0,'C','0','0','system:dict:list','dict','admin','2021-01-14 15:39:13','',NULL,'字典管理菜单'),
(106,'参数设置',1,7,'config','system/config/index',1,0,'C','0','0','system:config:list','edit','admin','2021-01-14 15:39:13','',NULL,'参数设置菜单'),
(107,'通知公告',1,8,'notice','system/notice/index',1,0,'C','0','0','system:notice:list','message','admin','2021-01-14 15:39:13','',NULL,'通知公告菜单'),
(108,'日志管理',1,9,'log','',1,0,'M','0','0','','log','admin','2021-01-14 15:39:13','',NULL,'日志管理菜单'),
(109,'在线用户',2,1,'online','monitor/online/index',1,0,'C','0','0','monitor:online:list','online','admin','2021-01-14 15:39:13','',NULL,'在线用户菜单'),
(110,'定时任务',2,2,'job','monitor/job/index',1,0,'C','0','0','monitor:job:list','job','admin','2021-01-14 15:39:13','',NULL,'定时任务菜单'),
(111,'数据监控',2,3,'druid','monitor/druid/index',1,0,'C','0','0','monitor:druid:list','druid','admin','2021-01-14 15:39:13','',NULL,'数据监控菜单'),
(112,'服务监控',2,4,'server','monitor/server/index',1,0,'C','0','0','monitor:server:list','server','admin','2021-01-14 15:39:13','',NULL,'服务监控菜单'),
(113,'缓存监控',2,5,'cache','monitor/cache/index',1,0,'C','0','0','monitor:cache:list','redis','admin','2021-01-14 15:39:13','',NULL,'缓存监控菜单'),
(114,'表单构建',3,1,'build','tool/build/index',1,0,'C','0','0','tool:build:list','build','admin','2021-01-14 15:39:13','',NULL,'表单构建菜单'),
(115,'代码生成',3,2,'gen','tool/gen/index',1,0,'C','0','0','tool:gen:list','code','admin','2021-01-14 15:39:13','',NULL,'代码生成菜单'),
(116,'系统接口',3,3,'swagger','tool/swagger/index',1,0,'C','0','0','tool:swagger:list','swagger','admin','2021-01-14 15:39:13','',NULL,'系统接口菜单'),
(500,'操作日志',108,1,'operlog','monitor/operlog/index',1,0,'C','0','0','monitor:operlog:list','form','admin','2021-01-14 15:39:13','',NULL,'操作日志菜单'),
(501,'登录日志',108,2,'logininfor','monitor/logininfor/index',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2021-01-14 15:39:13','',NULL,'登录日志菜单'),
(1001,'用户查询',100,1,'','',1,0,'F','0','0','system:user:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1002,'用户新增',100,2,'','',1,0,'F','0','0','system:user:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1003,'用户修改',100,3,'','',1,0,'F','0','0','system:user:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1004,'用户删除',100,4,'','',1,0,'F','0','0','system:user:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1005,'用户导出',100,5,'','',1,0,'F','0','0','system:user:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1006,'用户导入',100,6,'','',1,0,'F','0','0','system:user:import','#','admin','2021-01-14 15:39:13','',NULL,''),
(1007,'重置密码',100,7,'','',1,0,'F','0','0','system:user:resetPwd','#','admin','2021-01-14 15:39:13','',NULL,''),
(1008,'角色查询',101,1,'','',1,0,'F','0','0','system:role:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1009,'角色新增',101,2,'','',1,0,'F','0','0','system:role:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1010,'角色修改',101,3,'','',1,0,'F','0','0','system:role:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1011,'角色删除',101,4,'','',1,0,'F','0','0','system:role:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1012,'角色导出',101,5,'','',1,0,'F','0','0','system:role:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1013,'菜单查询',102,1,'','',1,0,'F','0','0','system:menu:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1014,'菜单新增',102,2,'','',1,0,'F','0','0','system:menu:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1015,'菜单修改',102,3,'','',1,0,'F','0','0','system:menu:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1016,'菜单删除',102,4,'','',1,0,'F','0','0','system:menu:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1017,'部门查询',103,1,'','',1,0,'F','0','0','system:dept:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1018,'部门新增',103,2,'','',1,0,'F','0','0','system:dept:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1019,'部门修改',103,3,'','',1,0,'F','0','0','system:dept:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1020,'部门删除',103,4,'','',1,0,'F','0','0','system:dept:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1021,'岗位查询',104,1,'','',1,0,'F','0','0','system:post:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1022,'岗位新增',104,2,'','',1,0,'F','0','0','system:post:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1023,'岗位修改',104,3,'','',1,0,'F','0','0','system:post:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1024,'岗位删除',104,4,'','',1,0,'F','0','0','system:post:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1025,'岗位导出',104,5,'','',1,0,'F','0','0','system:post:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1026,'字典查询',105,1,'#','',1,0,'F','0','0','system:dict:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1027,'字典新增',105,2,'#','',1,0,'F','0','0','system:dict:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1028,'字典修改',105,3,'#','',1,0,'F','0','0','system:dict:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1029,'字典删除',105,4,'#','',1,0,'F','0','0','system:dict:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1030,'字典导出',105,5,'#','',1,0,'F','0','0','system:dict:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1031,'参数查询',106,1,'#','',1,0,'F','0','0','system:config:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1032,'参数新增',106,2,'#','',1,0,'F','0','0','system:config:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1033,'参数修改',106,3,'#','',1,0,'F','0','0','system:config:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1034,'参数删除',106,4,'#','',1,0,'F','0','0','system:config:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1035,'参数导出',106,5,'#','',1,0,'F','0','0','system:config:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1036,'公告查询',107,1,'#','',1,0,'F','0','0','system:notice:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1037,'公告新增',107,2,'#','',1,0,'F','0','0','system:notice:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1038,'公告修改',107,3,'#','',1,0,'F','0','0','system:notice:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1039,'公告删除',107,4,'#','',1,0,'F','0','0','system:notice:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1040,'操作查询',500,1,'#','',1,0,'F','0','0','monitor:operlog:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1041,'操作删除',500,2,'#','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1042,'日志导出',500,4,'#','',1,0,'F','0','0','monitor:operlog:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1043,'登录查询',501,1,'#','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1044,'登录删除',501,2,'#','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1045,'日志导出',501,3,'#','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1046,'在线查询',109,1,'#','',1,0,'F','0','0','monitor:online:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1047,'批量强退',109,2,'#','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2021-01-14 15:39:13','',NULL,''),
(1048,'单条强退',109,3,'#','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2021-01-14 15:39:13','',NULL,''),
(1049,'任务查询',110,1,'#','',1,0,'F','0','0','monitor:job:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1050,'任务新增',110,2,'#','',1,0,'F','0','0','monitor:job:add','#','admin','2021-01-14 15:39:13','',NULL,''),
(1051,'任务修改',110,3,'#','',1,0,'F','0','0','monitor:job:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1052,'任务删除',110,4,'#','',1,0,'F','0','0','monitor:job:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1053,'状态修改',110,5,'#','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2021-01-14 15:39:13','',NULL,''),
(1054,'任务导出',110,7,'#','',1,0,'F','0','0','monitor:job:export','#','admin','2021-01-14 15:39:13','',NULL,''),
(1055,'生成查询',115,1,'#','',1,0,'F','0','0','tool:gen:query','#','admin','2021-01-14 15:39:13','',NULL,''),
(1056,'生成修改',115,2,'#','',1,0,'F','0','0','tool:gen:edit','#','admin','2021-01-14 15:39:13','',NULL,''),
(1057,'生成删除',115,3,'#','',1,0,'F','0','0','tool:gen:remove','#','admin','2021-01-14 15:39:13','',NULL,''),
(1058,'导入代码',115,2,'#','',1,0,'F','0','0','tool:gen:import','#','admin','2021-01-14 15:39:13','',NULL,''),
(1059,'预览代码',115,4,'#','',1,0,'F','0','0','tool:gen:preview','#','admin','2021-01-14 15:39:13','',NULL,''),
(1060,'生成代码',115,5,'#','',1,0,'F','0','0','tool:gen:code','#','admin','2021-01-14 15:39:13','',NULL,''),
(2000,'用户标签',4,10,'wxusertags','wxmp/wxusertags/index',1,0,'C','0','0','wxmp:wxusertags:list','tab','admin','2020-03-03 10:47:36','admin','2020-03-03 20:17:50',''),
(2001,'修改标签',2000,10,'',NULL,1,0,'F','1','0','wxmp:wxusertags:edit','#','admin','2020-03-03 11:16:13','',NULL,''),
(2002,'公众号用户',4,20,'wxuser','wxmp/wxuser/index',1,0,'C','0','0','wxmp:wxuser:index','peoples','admin','2020-03-04 10:13:30','',NULL,''),
(2003,'用户消息',4,30,'wxmsg','wxmp/wxmsg/index',1,0,'C','0','0','wxmp:wxmsg:index','clipboard','admin','2020-03-04 10:15:47','',NULL,''),
(2004,'素材管理',4,40,'wxmaterial','wxmp/wxmaterial/index',1,0,'C','0','0','wxmp:wxmaterial:index','example','admin','2020-03-04 10:17:21','admin','2020-03-05 21:31:33',''),
(2005,'自定义菜单',4,50,'wxmenu','wxmp/wxmenu/detail',1,0,'C','0','0','wxmp:wxmenu:get','cascader','admin','2020-03-04 10:18:02','admin','2020-03-04 10:29:20',''),
(2006,'消息自动回复',4,60,'wxautoreply','wxmp/wxautoreply/index',1,0,'C','0','0','wxmp:wxautoreply:index','dashboard','admin','2020-03-04 10:18:53','',NULL,''),
(2007,'数据统计',4,70,'wxsummary','wxmp/wxsummary/index',1,0,'C','0','0',NULL,'druid','admin','2020-03-04 10:19:53','',NULL,''),
(2008,'用户标签删除',2000,0,'',NULL,1,0,'F','0','0','wxmp:wxusertags:del','#','admin','2020-03-04 17:08:10','',NULL,''),
(2009,'用户标签新增',2000,0,'',NULL,1,0,'F','0','0','wxmp:wxusertags:add','#','admin','2020-03-04 17:08:42','',NULL,''),
(2010,'公众号用户新增',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:add','#','admin','2020-03-04 17:15:01','admin','2020-03-04 17:16:59',''),
(2011,'公众号用户修改',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:edit','#','admin','2020-03-04 17:16:17','admin','2020-03-04 17:17:09',''),
(2012,'公众号用户打标签',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:tagging','#','admin','2020-03-04 17:16:41','',NULL,''),
(2013,'公众号用户备注修改',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:edit:remark','#','admin','2020-03-04 17:17:43','',NULL,''),
(2014,'公众号用户同步',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:synchro','#','admin','2020-03-04 17:18:09','',NULL,''),
(2015,'公众号用户删除',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:del','#','admin','2020-03-04 17:18:31','',NULL,''),
(2016,'公众号用户详情',2002,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:get','#','admin','2020-03-04 17:18:55','',NULL,''),
(2017,'用户消息新增',2003,0,'',NULL,1,0,'F','0','0','wxmp:wxmsg:add','#','admin','2020-03-04 17:19:24','',NULL,''),
(2018,'用户消息修改',2003,0,'',NULL,1,0,'F','0','0','wxmp:wxmsg:edit','#','admin','2020-03-04 17:19:45','',NULL,''),
(2019,'用户消息删除',2003,0,'',NULL,1,0,'F','0','0','wxmp:wxmsg:del','#','admin','2020-03-04 17:20:03','',NULL,''),
(2020,'用户消息详情',2003,0,'',NULL,1,0,'F','0','0','wxmp:wxmsg:get','#','admin','2020-03-04 17:20:21','',NULL,''),
(2021,'素材新增',2004,0,'',NULL,1,0,'F','0','0','wxmp:wxmaterial:add','#','admin','2020-03-04 17:20:43','',NULL,''),
(2022,'素材修改',2004,0,'',NULL,1,0,'F','0','0','wxmp:wxmaterial:edit','#','admin','2020-03-04 17:21:03','',NULL,''),
(2023,'素材删除',2004,0,'',NULL,1,0,'F','0','0','wxmp:wxmaterial:del','#','admin','2020-03-04 17:21:24','',NULL,''),
(2024,'素材详情',2004,0,'',NULL,1,0,'F','0','0','wxmp:wxmaterial:get','#','admin','2020-03-04 17:21:43','',NULL,''),
(2025,'自定义菜单发布',2005,0,'',NULL,1,0,'F','0','0','wxmp:wxmenu:add','#','admin','2020-03-04 17:22:12','',NULL,''),
(2026,'消息自动回复新增',2006,0,'',NULL,1,0,'F','0','0','wxmp:wxautoreply:add','#','admin','2020-03-04 17:22:43','',NULL,''),
(2027,'消息自动回复修改',2006,0,'',NULL,1,0,'F','0','0','wxmp:wxautoreply:edit','#','admin','2020-03-04 17:23:05','',NULL,''),
(2028,'消息自动回复删除',2006,0,'',NULL,1,0,'F','0','0','wxmp:wxautoreply:del','#','admin','2020-03-04 17:23:36','',NULL,''),
(2029,'消息自动回复详情',2006,0,'',NULL,1,0,'F','0','0','wxmp:wxautoreply:get','#','admin','2020-03-04 17:23:59','',NULL,''),
(2033,'商城管理',0,5,'mall',NULL,1,0,'M','0','0',NULL,'shopping','admin','2021-01-21 17:44:55','',NULL,''),
(2034,'商品分类',2033,10,'goodscategory','mall/goodscategory/index',1,0,'C','0','0','mall:goodscategory:index','build','admin','2021-01-21 17:47:43','admin','2021-01-21 17:48:30',''),
(2035,'商品类目查询',2034,0,'',NULL,1,0,'F','0','0','mall:goodscategory:get','#','admin','2021-01-21 17:48:23','',NULL,''),
(2036,'新增商品类目',2034,0,'',NULL,1,0,'F','0','0','mall:goodscategory:add','#','admin','2021-01-21 17:48:51','',NULL,''),
(2037,'修改商品类目',2034,0,'',NULL,1,0,'F','0','0','mall:goodscategory:edit','#','admin','2021-01-21 17:49:11','',NULL,''),
(2038,'删除商品类目',2034,0,'',NULL,1,0,'F','0','0','mall:goodscategory:del','#','admin','2021-01-21 17:49:31','',NULL,''),
(2039,'商品管理',2033,10,'goodsspu','mall/goodsspu/index',1,0,'C','0','0','mall:goodsspu:index','shopping','admin','2021-01-25 22:10:44','admin','2021-01-25 22:12:13',''),
(2040,'商品查询',2039,0,'',NULL,1,0,'F','0','0','mall:goodsspu:get','#','admin','2021-01-25 22:13:08','',NULL,''),
(2041,'新增商品',2039,0,'',NULL,1,0,'F','0','0','mall:goodsspu:add','#','admin','2021-01-25 22:14:55','',NULL,''),
(2042,'修改商品',2039,0,'',NULL,1,0,'F','0','0','mall:goodsspu:edit','#','admin','2021-01-25 22:15:14','',NULL,''),
(2043,'删除商品',2039,0,'',NULL,1,0,'F','0','0','mall:goodsspu:del','#','admin','2021-01-25 22:15:35','',NULL,''),
(2044,'订单管理',2033,10,'orderinfo','mall/orderinfo/index',1,0,'C','0','0','mall:orderinfo:index','list','admin','2021-01-27 00:07:14','admin','2021-01-27 00:07:45',''),
(2045,'订单查询',2044,0,'',NULL,1,0,'F','0','0','mall:orderinfo:get','#','admin','2021-01-27 00:08:28','',NULL,''),
(2046,'商城订单修改',2044,0,'',NULL,1,0,'F','0','0','mall:orderinfo:edit','#','admin','2021-01-28 22:38:58','',NULL,''),
(2047,'商城订单新增',2044,0,'',NULL,1,0,'F','0','0','mall:orderinfo:add','#','admin','2021-01-28 22:39:21','',NULL,''),
(2048,'商城订单删除',2044,0,'',NULL,1,0,'F','0','0','mall:orderinfo:del','#','admin','2021-01-28 22:39:41','',NULL,''),
(2049,'小程序管理',0,3,'wxma',NULL,1,0,'M','0','0',NULL,'phone','admin','2021-01-28 23:45:03','',NULL,''),
(2050,'小程序用户',2049,10,'wxuser-ma','wxma/wxuser/index',1,0,'C','0','0','wxmp:wxuser:index','peoples','admin','2021-01-28 23:54:34','',NULL,''),
(2051,'小程序用户查询',2050,0,'',NULL,1,0,'F','0','0','wxmp:wxuser:get','#','admin','2021-01-28 23:57:07','',NULL,''),
(2052,'草稿箱',4,44,'wxdraft','wxmp/wxdraft/index',1,0,'C','0','0','	 wxmp:wxdraft:index','guide','admin','2022-03-29 14:48:47','admin','2022-03-29 14:51:31',''),
(2053,'新增草稿箱',2052,0,'',NULL,1,0,'F','1','0','wxmp:wxdraft:add','#','admin','2022-03-29 14:50:13','',NULL,''),
(2054,'修改草稿箱',2052,0,'',NULL,1,0,'F','0','0','wxmp:wxdraft:edit','#','admin','2022-03-29 14:50:28','',NULL,''),
(2055,'删除草稿箱',2052,0,'',NULL,1,0,'F','0','0','wxmp:wxdraft:del','#','admin','2022-03-29 14:50:41','',NULL,''),
(2057,'发布草稿',2052,0,'',NULL,1,0,'F','0','0','wxmp:wxdraft:publish','#','admin','2022-03-29 14:51:14','',NULL,''),
(2058,'已发布',4,46,'wxfreepublish','wxmp/wxfreepublish/index',1,0,'C','0','0','wxmp:wxfreepublish:index','clipboard','admin','2022-03-29 14:52:44','',NULL,''),
(2059,'删除已发布',2058,0,'',NULL,1,0,'F','0','0','wxmp:wxfreepublish:del','#','admin','2022-03-29 14:52:57','',NULL,'');

/*Table structure for table `sys_notice` */

DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';

/*Data for the table `sys_notice` */

insert  into `sys_notice`(`notice_id`,`notice_title`,`notice_type`,`notice_content`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'温馨提醒：2018-07-01 若依新版本发布啦','2','新版本内容','0','admin','2021-01-14 15:39:14','',NULL,'管理员'),
(11,'ewtwet','1',NULL,'0','admin','2021-01-14 17:45:06','',NULL,NULL);

/*Table structure for table `sys_oper_log` */

DROP TABLE IF EXISTS `sys_oper_log`;

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';

/*Data for the table `sys_oper_log` */

insert  into `sys_oper_log`(`oper_id`,`title`,`business_type`,`method`,`request_method`,`operator_type`,`oper_name`,`dept_name`,`oper_url`,`oper_ip`,`oper_location`,`oper_param`,`json_result`,`status`,`error_msg`,`oper_time`) values 
(100,'用户管理',2,'com.ruoyi.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','127.0.0.1','内网IP','{\"roles\":[{\"flag\":false,\"roleId\":2,\"admin\":false,\"dataScope\":\"2\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":false,\"menuCheckStrictly\":false,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"status\":\"0\"}],\"phonenumber\":\"15666666666\",\"admin\":false,\"loginDate\":1610609953000,\"remark\":\"测试员\",\"delFlag\":\"0\",\"password\":\"\",\"updateBy\":\"admin\",\"postIds\":[2],\"loginIp\":\"127.0.0.1\",\"email\":\"ry@qq.com\",\"nickName\":\"若依\",\"sex\":\"1\",\"deptId\":105,\"avatar\":\"\",\"dept\":{\"deptName\":\"测试部门\",\"leader\":\"若依\",\"deptId\":105,\"orderNum\":\"3\",\"params\":{},\"parentId\":101,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"ry\",\"userId\":2,\"createBy\":\"admin\",\"roleIds\":[2],\"createTime\":1610609953000,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:08:45'),
(101,'通知公告',2,'com.ruoyi.web.controller.system.SysNoticeController.edit()','PUT',1,'admin',NULL,'/system/notice','127.0.0.1','内网IP','{\"noticeContent\":\"维护内容\",\"createBy\":\"admin\",\"createTime\":1610609954000,\"updateBy\":\"admin\",\"noticeType\":\"1\",\"remark\":\"管理员\",\"params\":{},\"noticeId\":2,\"noticeTitle\":\"维护通知：2018-07-01 若依系统凌晨维护\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:08:57'),
(102,'通知公告',3,'com.ruoyi.web.controller.system.SysNoticeController.remove()','DELETE',1,'admin',NULL,'/system/notice/2','127.0.0.1','内网IP','{noticeIds=2}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:08:59'),
(103,'通知公告',1,'com.ruoyi.web.controller.system.SysNoticeController.add()','POST',1,'admin',NULL,'/system/notice','127.0.0.1','内网IP','{\"noticeContent\":\"<p><br></p>\",\"createBy\":\"admin\",\"noticeType\":\"1\",\"params\":{},\"noticeTitle\":\"rwes\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:09:04'),
(104,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin',NULL,'/tool/gen/importTable','127.0.0.1','内网IP','sys_config,sys_dept,sys_dict_data,sys_dict_type,sys_job,sys_job_log,sys_logininfor,sys_menu,sys_notice,sys_oper_log','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:10:34'),
(105,'通知公告',2,'com.joolun.web.controller.system.SysNoticeController.edit()','PUT',1,'admin',NULL,'/system/notice','127.0.0.1','内网IP','{\"noticeContent\":\"<p><br></p>\",\"createBy\":\"admin\",\"createTime\":1610615344000,\"updateBy\":\"admin\",\"noticeType\":\"1\",\"params\":{},\"noticeId\":10,\"noticeTitle\":\"rwes\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:44:58'),
(106,'通知公告',3,'com.joolun.web.controller.system.SysNoticeController.remove()','DELETE',1,'admin',NULL,'/system/notice/10','127.0.0.1','内网IP','{noticeIds=10}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:45:01'),
(107,'通知公告',1,'com.joolun.web.controller.system.SysNoticeController.add()','POST',1,'admin',NULL,'/system/notice','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeType\":\"1\",\"params\":{},\"noticeTitle\":\"ewtwet\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 17:45:06'),
(108,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"公众号用户2\",\"params\":{},\"parentId\":1,\"isCache\":\"0\",\"path\":\"wxuser\",\"component\":\"wxmp/wxuser/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"wxmp:wxuser:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:45:08'),
(109,'菜单管理',3,'com.joolun.web.controller.system.SysMenuController.remove()','DELETE',1,'admin',NULL,'/system/menu/2030','127.0.0.1','内网IP','{menuId=2030}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:46:21'),
(110,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"bug\",\"orderNum\":\"0\",\"menuName\":\"用户管理2\",\"params\":{},\"parentId\":1,\"isCache\":\"0\",\"path\":\"user2\",\"component\":\"system/user/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"system:user:list\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:48:32'),
(111,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"bug\",\"orderNum\":\"0\",\"menuName\":\"用户管理2\",\"params\":{},\"parentId\":1,\"isCache\":\"0\",\"path\":\"user2\",\"component\":\"wxmp/wxuser/index\",\"children\":[],\"createTime\":1610635712000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2031,\"menuType\":\"C\",\"perms\":\"system:user:list\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:48:59'),
(112,'菜单管理',3,'com.joolun.web.controller.system.SysMenuController.remove()','DELETE',1,'admin',NULL,'/system/menu/2031','127.0.0.1','内网IP','{menuId=2031}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:49:08'),
(113,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"chart\",\"orderNum\":\"0\",\"menuName\":\"sdgsgwe\",\"params\":{},\"parentId\":1,\"isCache\":\"0\",\"path\":\"role2\",\"component\":\"system/role2/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"system:role:list\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-14 22:54:42'),
(114,'菜单管理',3,'com.joolun.web.controller.system.SysMenuController.remove()','DELETE',1,'admin',NULL,'/system/menu/2032','127.0.0.1','内网IP','{menuId=2032}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:22:31'),
(115,'用户管理',2,'com.joolun.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','127.0.0.1','内网IP','{\"roles\":[{\"flag\":false,\"roleId\":2,\"admin\":false,\"dataScope\":\"2\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":false,\"menuCheckStrictly\":false,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"status\":\"0\"}],\"phonenumber\":\"15666666666\",\"admin\":false,\"loginDate\":1610609953000,\"remark\":\"测试员\",\"delFlag\":\"0\",\"password\":\"\",\"updateBy\":\"admin\",\"postIds\":[2],\"loginIp\":\"127.0.0.1\",\"email\":\"ry@qq.com\",\"nickName\":\"若依\",\"sex\":\"1\",\"deptId\":105,\"avatar\":\"\",\"dept\":{\"deptName\":\"测试部门\",\"leader\":\"若依\",\"deptId\":105,\"orderNum\":\"3\",\"params\":{},\"parentId\":101,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"ry\",\"userId\":2,\"createBy\":\"admin\",\"roleIds\":[2],\"createTime\":1610609953000,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:23:11'),
(116,'用户管理',2,'com.joolun.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','127.0.0.1','内网IP','{\"roles\":[{\"flag\":false,\"roleId\":2,\"admin\":false,\"dataScope\":\"2\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":false,\"menuCheckStrictly\":false,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"status\":\"0\"}],\"phonenumber\":\"15666666666\",\"admin\":false,\"loginDate\":1610609953000,\"remark\":\"测试员\",\"delFlag\":\"0\",\"password\":\"\",\"updateBy\":\"admin\",\"postIds\":[2],\"loginIp\":\"127.0.0.1\",\"email\":\"ry@qq.com\",\"nickName\":\"若依\",\"sex\":\"1\",\"deptId\":105,\"avatar\":\"\",\"dept\":{\"deptName\":\"测试部门\",\"leader\":\"若依\",\"deptId\":105,\"orderNum\":\"3\",\"params\":{},\"parentId\":101,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"ry\",\"userId\":2,\"createBy\":\"admin\",\"roleIds\":[2],\"createTime\":1610609953000,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:52:07'),
(117,'用户管理',3,'com.joolun.web.controller.system.SysUserController.remove()','DELETE',1,'admin',NULL,'/system/user/2','127.0.0.1','内网IP','{userIds=2}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:52:42'),
(118,'用户管理',1,'com.joolun.web.controller.system.SysUserController.add()','POST',1,'admin',NULL,'/system/user','127.0.0.1','内网IP','{\"admin\":false,\"password\":\"$2a$10$KTBu09ed743F.IS1Y5TCluX8Jg17xnef5VBwDHjoi.MsgpU.fsQQa\",\"postIds\":[],\"nickName\":\"test\",\"sex\":\"0\",\"deptId\":100,\"params\":{},\"userName\":\"test\",\"userId\":100,\"createBy\":\"admin\",\"roleIds\":[],\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:52:55'),
(119,'用户管理',2,'com.joolun.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','127.0.0.1','内网IP','{\"roles\":[],\"phonenumber\":\"\",\"admin\":false,\"delFlag\":\"0\",\"password\":\"\",\"updateBy\":\"admin\",\"postIds\":[],\"loginIp\":\"\",\"email\":\"\",\"nickName\":\"test\",\"sex\":\"0\",\"deptId\":100,\"avatar\":\"\",\"dept\":{\"deptName\":\"若依科技\",\"leader\":\"若依\",\"deptId\":100,\"orderNum\":\"0\",\"params\":{},\"parentId\":0,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"test\",\"userId\":100,\"createBy\":\"admin\",\"roleIds\":[2],\"createTime\":1610715175000,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 20:53:33'),
(120,'角色管理',2,'com.joolun.web.controller.system.SysRoleController.edit()','PUT',1,'admin',NULL,'/system/role','220.202.217.37','XX XX','{\"flag\":false,\"roleId\":2,\"admin\":false,\"remark\":\"普通角色\",\"dataScope\":\"2\",\"delFlag\":\"0\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":true,\"createTime\":1610609953000,\"updateBy\":\"admin\",\"menuCheckStrictly\":true,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"menuIds\":[4,2000,2008,2009,2001,2002,2010,2011,2012,2013,2014,2015,2016,2003,2017,2018,2019,2020,2004,2021,2022,2023,2024,2005,2025,2006,2026,2027,2028,2029,2007,1,100,1001,1002,1003,1004,1005,1006,1007,101,1008,1009,1010,1011,1012,102,1013,1014,1015,1016,103,1017,1018,1019,1020,104,1021,1022,1023,1024,1025,105,1026,1027,1028,1029,1030,106,1031,1032,1033,1034,1035,107,1036,1037,1038,1039,108,500,1040,1041,1042,501,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,3,114,115,1055,1056,1058,1057,1059,1060,116],\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 23:17:30'),
(121,'用户管理',2,'com.joolun.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','220.202.217.37','XX XX','{\"roles\":[{\"flag\":false,\"roleId\":2,\"admin\":false,\"dataScope\":\"2\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":false,\"menuCheckStrictly\":false,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"status\":\"0\"}],\"phonenumber\":\"18608549631\",\"admin\":false,\"remark\":\"111\",\"delFlag\":\"0\",\"password\":\"\",\"updateBy\":\"admin\",\"postIds\":[],\"loginIp\":\"\",\"email\":\"1023536325@qq.com\",\"nickName\":\"test\",\"sex\":\"0\",\"deptId\":100,\"avatar\":\"\",\"dept\":{\"deptName\":\"若依科技\",\"leader\":\"若依\",\"deptId\":100,\"orderNum\":\"0\",\"params\":{},\"parentId\":0,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"test\",\"userId\":100,\"createBy\":\"admin\",\"roleIds\":[2],\"createTime\":1583312549000,\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-15 23:17:40'),
(122,'用户管理',2,'com.joolun.web.controller.system.SysUserController.edit()','PUT',1,'admin',NULL,'/system/user','220.202.217.37','XX XX','{\"roles\":[{\"flag\":false,\"roleId\":1,\"admin\":true,\"dataScope\":\"1\",\"params\":{},\"roleSort\":\"1\",\"deptCheckStrictly\":false,\"menuCheckStrictly\":false,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"status\":\"0\"}],\"phonenumber\":\"15888888888\",\"admin\":true,\"loginDate\":1521171180000,\"remark\":\"管理员\",\"delFlag\":\"0\",\"password\":\"\",\"postIds\":[1],\"loginIp\":\"127.0.0.1\",\"email\":\"ry@163.com\",\"nickName\":\"JooLun\",\"sex\":\"1\",\"deptId\":103,\"avatar\":\"\",\"dept\":{\"deptName\":\"研发部门\",\"leader\":\"若依\",\"deptId\":103,\"orderNum\":\"1\",\"params\":{},\"parentId\":101,\"children\":[],\"status\":\"0\"},\"params\":{},\"userName\":\"admin\",\"userId\":1,\"createBy\":\"admin\",\"roleIds\":[1],\"createTime\":1521171180000,\"status\":\"0\"}','null',1,'不允许操作超级管理员用户','2021-01-16 00:07:15'),
(123,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"JooLun\",\"leader\":\"JooLun\",\"deptId\":100,\"orderNum\":\"0\",\"delFlag\":\"0\",\"params\":{},\"parentId\":0,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:28'),
(124,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"深圳总公司\",\"leader\":\"JooLun\",\"deptId\":101,\"orderNum\":\"1\",\"delFlag\":\"0\",\"params\":{},\"parentId\":100,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:32'),
(125,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"研发部门\",\"leader\":\"JooLun\",\"deptId\":103,\"orderNum\":\"1\",\"delFlag\":\"0\",\"params\":{},\"parentId\":101,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,101\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:34'),
(126,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"市场部门\",\"leader\":\"JooLun\",\"deptId\":104,\"orderNum\":\"2\",\"delFlag\":\"0\",\"params\":{},\"parentId\":101,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,101\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:37'),
(127,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"测试部门\",\"leader\":\"JooLun\",\"deptId\":105,\"orderNum\":\"3\",\"delFlag\":\"0\",\"params\":{},\"parentId\":101,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,101\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:39'),
(128,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"财务部门\",\"leader\":\"JooLun\",\"deptId\":106,\"orderNum\":\"4\",\"delFlag\":\"0\",\"params\":{},\"parentId\":101,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,101\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:42'),
(129,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"运维部门\",\"leader\":\"JooLun\",\"deptId\":107,\"orderNum\":\"5\",\"delFlag\":\"0\",\"params\":{},\"parentId\":101,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,101\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:45'),
(130,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"长沙分公司\",\"leader\":\"JooLun\",\"deptId\":102,\"orderNum\":\"2\",\"delFlag\":\"0\",\"params\":{},\"parentId\":100,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:48'),
(131,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"市场部门\",\"leader\":\"JooLun\",\"deptId\":108,\"orderNum\":\"1\",\"delFlag\":\"0\",\"params\":{},\"parentId\":102,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,102\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:52'),
(132,'部门管理',2,'com.joolun.web.controller.system.SysDeptController.edit()','PUT',1,'admin',NULL,'/system/dept','220.202.217.37','XX XX','{\"deptName\":\"财务部门\",\"leader\":\"JooLun\",\"deptId\":109,\"orderNum\":\"2\",\"delFlag\":\"0\",\"params\":{},\"parentId\":102,\"createBy\":\"admin\",\"children\":[],\"createTime\":1610609953000,\"phone\":\"15888888888\",\"updateBy\":\"admin\",\"ancestors\":\"0,100,102\",\"email\":\"ry@qq.com\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-16 00:07:55'),
(133,'代码生成',8,'com.joolun.generator.controller.GenController.batchGenCode()','GET',1,'test',NULL,'/tool/gen/batchGenCode','175.153.195.94','XX XX','{}','null',0,NULL,'2021-01-18 16:22:18'),
(134,'代码生成',8,'com.joolun.generator.controller.GenController.batchGenCode()','GET',1,'test',NULL,'/tool/gen/batchGenCode','120.230.139.76','XX XX','{}','null',0,NULL,'2021-01-18 17:38:43'),
(135,'代码生成',8,'com.joolun.generator.controller.GenController.batchGenCode()','GET',1,'test',NULL,'/tool/gen/batchGenCode','60.208.209.207','XX XX','{}','null',0,NULL,'2021-01-19 11:45:50'),
(136,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"system\",\"orderNum\":\"10\",\"menuName\":\"系统管理\",\"params\":{},\"parentId\":0,\"isCache\":\"0\",\"path\":\"system\",\"children\":[],\"createTime\":1610609953000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":1,\"menuType\":\"M\",\"perms\":\"\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:43:31'),
(137,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"monitor\",\"orderNum\":\"20\",\"menuName\":\"系统监控\",\"params\":{},\"parentId\":0,\"isCache\":\"0\",\"path\":\"monitor\",\"children\":[],\"createTime\":1610609953000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2,\"menuType\":\"M\",\"perms\":\"\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:43:38'),
(138,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"tool\",\"orderNum\":\"30\",\"menuName\":\"系统工具\",\"params\":{},\"parentId\":0,\"isCache\":\"0\",\"path\":\"tool\",\"children\":[],\"createTime\":1610609953000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":3,\"menuType\":\"M\",\"perms\":\"\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:43:43'),
(139,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"5\",\"menuName\":\"商城管理\",\"params\":{},\"parentId\":0,\"isCache\":\"0\",\"path\":\"mall\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"M\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:44:55'),
(140,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"build\",\"orderNum\":\"10\",\"menuName\":\"商城分类\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goodscategory\",\"component\":\"mall/goodscategory/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"mall:goodscategory:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:47:44'),
(141,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"商品类目查询\",\"params\":{},\"parentId\":2034,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodscategory:get\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:48:23'),
(142,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"build\",\"orderNum\":\"10\",\"menuName\":\"商品分类\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goodscategory\",\"component\":\"mall/goodscategory/index\",\"children\":[],\"createTime\":1611222463000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2034,\"menuType\":\"C\",\"perms\":\"mall:goodscategory:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:48:30'),
(143,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"新增商品类目\",\"params\":{},\"parentId\":2034,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodscategory:add\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:48:52'),
(144,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"修改商品类目\",\"params\":{},\"parentId\":2034,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodscategory:edit\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:49:11'),
(145,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"删除商品类目\",\"params\":{},\"parentId\":2034,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodscategory:del\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-21 17:49:32'),
(146,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"2\",\"menuName\":\"商品管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goods:spuu\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:10:44'),
(147,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"2\",\"menuName\":\"商品管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"mall\",\"component\":\"mall:goodsspu:index\",\"children\":[],\"createTime\":1611583844000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2039,\"menuType\":\"C\",\"perms\":\"mall:goodsspu:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:11:20'),
(148,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"2\",\"menuName\":\"商品管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goodsspu\",\"component\":\"mall:goodsspu:index\",\"children\":[],\"createTime\":1611583844000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2039,\"menuType\":\"C\",\"perms\":\"mall:goodsspu:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:11:44'),
(149,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"10\",\"menuName\":\"商品管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goodsspu\",\"component\":\"mall:goodsspu:index\",\"children\":[],\"createTime\":1611583844000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2039,\"menuType\":\"C\",\"perms\":\"mall:goodsspu:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:11:54'),
(150,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"shopping\",\"orderNum\":\"10\",\"menuName\":\"商品管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"goodsspu\",\"component\":\"mall/goodsspu/index\",\"children\":[],\"createTime\":1611583844000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2039,\"menuType\":\"C\",\"perms\":\"mall:goodsspu:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:12:13'),
(151,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"商品查询\",\"params\":{},\"parentId\":2039,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodsspu:get\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:13:08'),
(152,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"新增商品\",\"params\":{},\"parentId\":2039,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodsspu:add\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:14:55'),
(153,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"修改商品\",\"params\":{},\"parentId\":2039,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodsspu:edit\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:15:14'),
(154,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"删除商品\",\"params\":{},\"parentId\":2039,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:goodsspu:del\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-25 22:15:35'),
(155,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"list\",\"orderNum\":\"10\",\"menuName\":\"订单管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"orderinfo\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-27 00:07:14'),
(156,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"list\",\"orderNum\":\"10\",\"menuName\":\"订单管理\",\"params\":{},\"parentId\":2033,\"isCache\":\"0\",\"path\":\"orderinfo\",\"component\":\"mall/orderinfo/index\",\"children\":[],\"createTime\":1611677234000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2044,\"menuType\":\"C\",\"perms\":\"mall:orderinfo:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-27 00:07:45'),
(157,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"订单查询\",\"params\":{},\"parentId\":2044,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:orderinfo:get\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-27 00:08:28'),
(158,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"商城订单修改\",\"params\":{},\"parentId\":2044,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:orderinfo:edit\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 22:38:59'),
(159,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"商城订单新增\",\"params\":{},\"parentId\":2044,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:orderinfo:add\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 22:39:21'),
(160,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"商城订单删除\",\"params\":{},\"parentId\":2044,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"mall:orderinfo:del\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 22:39:41'),
(161,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"phone\",\"orderNum\":\"3\",\"menuName\":\"小程序管理\",\"params\":{},\"parentId\":0,\"isCache\":\"0\",\"path\":\"wxma\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"M\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 23:45:04'),
(162,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"peoples\",\"orderNum\":\"10\",\"menuName\":\"小程序用户\",\"params\":{},\"parentId\":2049,\"isCache\":\"0\",\"path\":\"wxuser-ma\",\"component\":\"wxma/wxuser/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"wxmp:wxuser:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 23:54:34'),
(163,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"小程序用户查询\",\"params\":{},\"parentId\":2050,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxuser:get\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-01-28 23:57:07'),
(164,'用户头像',2,'com.joolun.web.controller.system.SysProfileController.avatar()','POST',1,'admin',NULL,'/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"操作成功\",\"imgUrl\":\"/profile/avatar/2021/02/06/2899f110-5b01-4abb-b54c-61643f32bc86.jpeg\",\"code\":200}',0,NULL,'2021-02-06 10:59:37'),
(165,'角色管理',2,'com.joolun.web.controller.system.SysRoleController.edit()','PUT',1,'admin',NULL,'/system/role','127.0.0.1','内网IP','{\"flag\":false,\"roleId\":2,\"admin\":false,\"remark\":\"普通角色\",\"dataScope\":\"2\",\"delFlag\":\"0\",\"params\":{},\"roleSort\":\"2\",\"deptCheckStrictly\":true,\"createTime\":1610609953000,\"updateBy\":\"admin\",\"menuCheckStrictly\":true,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"menuIds\":[4,2000,2008,2009,2001,2002,2010,2011,2012,2013,2014,2015,2016,2003,2017,2018,2019,2020,2004,2021,2022,2023,2024,2005,2025,2006,2026,2027,2028,2029,2007,2049,2050,2051,2033,2034,2035,2036,2037,2038,2039,2040,2041,2042,2043,2044,2045,2046,2047,2048,1,100,1001,1002,1003,1004,1005,1006,1007,101,1008,1009,1010,1011,1012,102,1013,1014,1015,1016,103,1017,1018,1019,1020,104,1021,1022,1023,1024,1025,105,1026,1027,1028,1029,1030,106,1031,1032,1033,1034,1035,107,1036,1037,1038,1039,108,500,1040,1041,1042,501,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,3,114,115,1055,1056,1058,1057,1059,1060,116],\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2021-02-21 09:34:29'),
(166,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"guide\",\"orderNum\":\"44\",\"menuName\":\"草稿箱\",\"params\":{},\"parentId\":4,\"isCache\":\"0\",\"path\":\"views/wxmp/wxdraft/index.vue\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:48:47'),
(167,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"guide\",\"orderNum\":\"44\",\"menuName\":\"草稿箱\",\"params\":{},\"parentId\":4,\"isCache\":\"0\",\"path\":\"wxdraft\",\"component\":\"wxmp/wxdraft/index\",\"children\":[],\"createTime\":1648536527000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2052,\"menuType\":\"C\",\"perms\":\"\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:49:15'),
(168,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"1\",\"orderNum\":\"0\",\"menuName\":\"新增草稿箱\",\"params\":{},\"parentId\":2052,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxdraft:add\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:50:13'),
(169,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"修改草稿箱\",\"params\":{},\"parentId\":2052,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxdraft:edit\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:50:28'),
(170,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"删除草稿箱\",\"params\":{},\"parentId\":2052,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxdraft:del\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:50:41'),
(171,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"草稿箱列表\",\"params\":{},\"parentId\":2052,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxdraft:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:50:57'),
(172,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"发布草稿\",\"params\":{},\"parentId\":2052,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxdraft:publish\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:51:14'),
(173,'菜单管理',3,'com.joolun.web.controller.system.SysMenuController.remove()','DELETE',1,'admin',NULL,'/system/menu/2056','127.0.0.1','内网IP','{menuId=2056}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:51:23'),
(174,'菜单管理',2,'com.joolun.web.controller.system.SysMenuController.edit()','PUT',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"guide\",\"orderNum\":\"44\",\"menuName\":\"草稿箱\",\"params\":{},\"parentId\":4,\"isCache\":\"0\",\"path\":\"wxdraft\",\"component\":\"wxmp/wxdraft/index\",\"children\":[],\"createTime\":1648536527000,\"updateBy\":\"admin\",\"isFrame\":\"1\",\"menuId\":2052,\"menuType\":\"C\",\"perms\":\"\\t wxmp:wxdraft:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:51:31'),
(175,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"icon\":\"clipboard\",\"orderNum\":\"46\",\"menuName\":\"已发布\",\"params\":{},\"parentId\":4,\"isCache\":\"0\",\"path\":\"wxfreepublish\",\"component\":\"wxmp/wxfreepublish/index\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"C\",\"perms\":\"wxmp:wxfreepublish:index\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:52:44'),
(176,'菜单管理',1,'com.joolun.web.controller.system.SysMenuController.add()','POST',1,'admin',NULL,'/system/menu','127.0.0.1','内网IP','{\"visible\":\"0\",\"orderNum\":\"0\",\"menuName\":\"删除已发布\",\"params\":{},\"parentId\":2058,\"isCache\":\"0\",\"createBy\":\"admin\",\"children\":[],\"isFrame\":\"1\",\"menuType\":\"F\",\"perms\":\"wxmp:wxfreepublish:del\",\"status\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2022-03-29 14:52:57');

/*Table structure for table `sys_post` */

DROP TABLE IF EXISTS `sys_post`;

CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';

/*Data for the table `sys_post` */

insert  into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'ceo','董事长',1,'0','admin','2021-01-14 15:39:13','',NULL,''),
(2,'se','项目经理',2,'0','admin','2021-01-14 15:39:13','',NULL,''),
(3,'hr','人力资源',3,'0','admin','2021-01-14 15:39:13','',NULL,''),
(4,'user','普通员工',4,'0','admin','2021-01-14 15:39:13','',NULL,'');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`menu_check_strictly`,`dept_check_strictly`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2021-01-14 15:39:13','',NULL,'超级管理员'),
(2,'普通角色','common',2,'2',1,1,'0','0','admin','2021-01-14 15:39:13','admin','2021-02-21 09:34:28','普通角色');

/*Table structure for table `sys_role_dept` */

DROP TABLE IF EXISTS `sys_role_dept`;

CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';

/*Data for the table `sys_role_dept` */

insert  into `sys_role_dept`(`role_id`,`dept_id`) values 
(2,100),
(2,101),
(2,105);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values 
(2,1),
(2,2),
(2,3),
(2,4),
(2,100),
(2,101),
(2,102),
(2,103),
(2,104),
(2,105),
(2,106),
(2,107),
(2,108),
(2,109),
(2,110),
(2,111),
(2,112),
(2,113),
(2,114),
(2,115),
(2,116),
(2,500),
(2,501),
(2,1001),
(2,1002),
(2,1003),
(2,1004),
(2,1005),
(2,1006),
(2,1007),
(2,1008),
(2,1009),
(2,1010),
(2,1011),
(2,1012),
(2,1013),
(2,1014),
(2,1015),
(2,1016),
(2,1017),
(2,1018),
(2,1019),
(2,1020),
(2,1021),
(2,1022),
(2,1023),
(2,1024),
(2,1025),
(2,1026),
(2,1027),
(2,1028),
(2,1029),
(2,1030),
(2,1031),
(2,1032),
(2,1033),
(2,1034),
(2,1035),
(2,1036),
(2,1037),
(2,1038),
(2,1039),
(2,1040),
(2,1041),
(2,1042),
(2,1043),
(2,1044),
(2,1045),
(2,1046),
(2,1047),
(2,1048),
(2,1049),
(2,1050),
(2,1051),
(2,1052),
(2,1053),
(2,1054),
(2,1055),
(2,1056),
(2,1057),
(2,1058),
(2,1059),
(2,1060),
(2,2000),
(2,2001),
(2,2002),
(2,2003),
(2,2004),
(2,2005),
(2,2006),
(2,2007),
(2,2008),
(2,2009),
(2,2010),
(2,2011),
(2,2012),
(2,2013),
(2,2014),
(2,2015),
(2,2016),
(2,2017),
(2,2018),
(2,2019),
(2,2020),
(2,2021),
(2,2022),
(2,2023),
(2,2024),
(2,2025),
(2,2026),
(2,2027),
(2,2028),
(2,2029),
(2,2033),
(2,2034),
(2,2035),
(2,2036),
(2,2037),
(2,2038),
(2,2039),
(2,2040),
(2,2041),
(2,2042),
(2,2043),
(2,2044),
(2,2045),
(2,2046),
(2,2047),
(2,2048),
(2,2049),
(2,2050),
(2,2051);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`dept_id`,`user_name`,`nick_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`status`,`del_flag`,`login_ip`,`login_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1,103,'admin','JooLun','00','JooLun@163.com','15888888888','1','/profile/avatar/2021/02/06/2899f110-5b01-4abb-b54c-61643f32bc86.jpeg','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2021-01-14 15:39:13','admin','2021-01-14 15:39:13','',NULL,'管理员'),
(2,105,'ry','JooLun','00','JooLun@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','2','127.0.0.1','2021-01-14 15:39:13','admin','2021-01-14 15:39:13','admin','2021-01-15 20:52:07','测试员'),
(100,100,'test','test','00','','','0','','$2a$10$KTBu09ed743F.IS1Y5TCluX8Jg17xnef5VBwDHjoi.MsgpU.fsQQa','0','0','',NULL,'admin','2021-01-15 20:52:55','admin','2021-01-15 20:53:33',NULL);

/*Table structure for table `sys_user_post` */

DROP TABLE IF EXISTS `sys_user_post`;

CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';

/*Data for the table `sys_user_post` */

insert  into `sys_user_post`(`user_id`,`post_id`) values 
(1,1);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values 
(1,1),
(100,2);

/*Table structure for table `user_address` */

DROP TABLE IF EXISTS `user_address`;

CREATE TABLE `user_address` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'PK',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收货人名字',
  `postal_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮编',
  `province_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省名',
  `city_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '市名',
  `county_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区名',
  `detail_info` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情地址',
  `tel_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话号码',
  `is_default` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否默认 1是0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户地址';

/*Data for the table `user_address` */

insert  into `user_address`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`user_name`,`postal_code`,`province_name`,`city_name`,`county_name`,`detail_info`,`tel_num`,`is_default`) values 
('1354441894547988481','0','2021-01-27 22:51:26','2021-01-27 22:51:26','1352168072700571649','张三',NULL,'广东省','广州市','海珠区','新港中路397号','18602513214','1'),
('1354474056307511297','0','2021-01-28 00:59:14','2021-01-28 00:59:14','1354473059078176770','张三',NULL,'广东省','广州市','海珠区','新港中路397号','18563265321','1'),
('1355417330850476033','0','2021-01-30 15:27:29','2021-01-30 15:27:29','1355406809988345857','张三',NULL,'北京市','北京市','东城区','大冲地铁口','15580802543','1');

/*Table structure for table `wx_auto_reply` */

DROP TABLE IF EXISTS `wx_auto_reply`;

CREATE TABLE `wx_auto_reply` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `type` char(2) DEFAULT NULL COMMENT '类型（1、关注时回复；2、消息回复；3、关键词回复）',
  `req_key` varchar(64) DEFAULT NULL COMMENT '关键词',
  `req_type` char(10) DEFAULT NULL COMMENT '请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）',
  `rep_type` char(10) DEFAULT NULL COMMENT '回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）',
  `rep_mate` char(10) DEFAULT NULL COMMENT '回复类型文本匹配类型（1、全匹配，2、半匹配）',
  `rep_content` text COMMENT '回复类型文本保存文字',
  `rep_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id',
  `rep_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复的素材名、视频和音乐的标题',
  `rep_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '视频和音乐的描述',
  `rep_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链接',
  `rep_hq_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '高质量链接',
  `rep_thumb_media_id` varchar(64) DEFAULT NULL COMMENT '缩略图的媒体id',
  `rep_thumb_url` varchar(500) DEFAULT NULL COMMENT '缩略图url',
  `content` mediumtext COMMENT '图文消息的内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信自动回复';

/*Data for the table `wx_auto_reply` */

/*Table structure for table `wx_menu` */

DROP TABLE IF EXISTS `wx_menu`;

CREATE TABLE `wx_menu` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单ID（click、scancode_push、scancode_waitmsg、pic_sysphoto、pic_photo_or_album、pic_weixin、location_select：保存key）',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sort` int DEFAULT '1' COMMENT '排序值',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父菜单ID',
  `type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单类型click、view、miniprogram、scancode_push、scancode_waitmsg、pic_sysphoto、pic_photo_or_album、pic_weixin、location_select、media_id、view_limited等',
  `name` varchar(20) DEFAULT NULL COMMENT '菜单名',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'view、miniprogram保存链接',
  `ma_app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '小程序的appid',
  `ma_page_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '小程序的页面路径',
  `rep_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）',
  `rep_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Text:保存文字',
  `rep_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'imge、voice、news、video：mediaID',
  `rep_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '素材名、视频和音乐的标题',
  `rep_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '视频和音乐的描述',
  `rep_url` varchar(500) DEFAULT NULL COMMENT '链接',
  `rep_hq_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '高质量链接',
  `rep_thumb_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '缩略图的媒体id',
  `rep_thumb_url` varchar(500) DEFAULT NULL COMMENT '缩略图url',
  `content` mediumtext COMMENT '图文消息的内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='自定义菜单表';

/*Data for the table `wx_menu` */

/*Table structure for table `wx_msg` */

DROP TABLE IF EXISTS `wx_msg`;

CREATE TABLE `wx_msg` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公众号名称',
  `app_logo` varchar(500) DEFAULT NULL COMMENT '公众号logo',
  `wx_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信用户ID',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '微信用户昵称',
  `headimg_url` varchar(1000) DEFAULT NULL COMMENT '微信用户头像',
  `type` char(2) DEFAULT NULL COMMENT '消息分类（1、用户发给公众号；2、公众号发给用户；）',
  `rep_type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置；music：音乐；news：图文；event：推送事件）',
  `rep_event` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事件类型（subscribe：关注；unsubscribe：取关；CLICK、VIEW：菜单事件）',
  `rep_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '回复类型文本保存文字、地理位置信息',
  `rep_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id',
  `rep_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '回复的素材名、视频和音乐的标题',
  `rep_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '视频和音乐的描述',
  `rep_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链接',
  `rep_hq_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '高质量链接',
  `content` mediumtext COMMENT '图文消息的内容',
  `rep_thumb_media_id` varchar(64) DEFAULT NULL COMMENT '缩略图的媒体id',
  `rep_thumb_url` varchar(500) DEFAULT NULL COMMENT '缩略图url',
  `rep_location_x` double DEFAULT NULL COMMENT '地理位置维度',
  `rep_location_y` double DEFAULT NULL COMMENT '地理位置经度',
  `rep_scale` double DEFAULT NULL COMMENT '地图缩放大小',
  `read_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '已读标记（1：是；0：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信消息';

/*Data for the table `wx_msg` */

/*Table structure for table `wx_user` */

DROP TABLE IF EXISTS `wx_user`;

CREATE TABLE `wx_user` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户备注',
  `del_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `app_type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '应用类型(1:小程序，2:公众号)',
  `subscribe` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否订阅（1：是；0：否；2：网页授权用户）',
  `subscribe_scene` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他',
  `subscribe_time` datetime DEFAULT NULL COMMENT '关注时间',
  `subscribe_num` int DEFAULT NULL COMMENT '关注次数',
  `cancel_subscribe_time` datetime DEFAULT NULL COMMENT '取消关注时间',
  `open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户标识',
  `nick_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '性别（1：男，2：女，0：未知）',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在城市',
  `country` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在国家',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在省份',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
  `language` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户语言',
  `headimg_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `union_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'union_id',
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户组',
  `tagid_list` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签列表',
  `qr_scene_str` varchar(64) DEFAULT NULL COMMENT '二维码扫码场景',
  `latitude` double DEFAULT NULL COMMENT '地理位置纬度',
  `longitude` double DEFAULT NULL COMMENT '地理位置经度',
  `precision` double DEFAULT NULL COMMENT '地理位置精度',
  `session_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '会话密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信用户';

/*Data for the table `wx_user` */

insert  into `wx_user`(`id`,`create_id`,`create_time`,`update_id`,`update_time`,`remark`,`del_flag`,`app_type`,`subscribe`,`subscribe_scene`,`subscribe_time`,`subscribe_num`,`cancel_subscribe_time`,`open_id`,`nick_name`,`sex`,`city`,`country`,`province`,`phone`,`language`,`headimg_url`,`union_id`,`group_id`,`tagid_list`,`qr_scene_str`,`latitude`,`longitude`,`precision`,`session_key`) values 
('1352168072700571649',NULL,'2021-01-21 16:16:05',NULL,'2021-01-21 16:37:22',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5DyEplVd0B5lD9gLwCme8zw','JL','1','深圳','中国','广东',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKRsdzV55M85n8DAsVhH7wrS05ficLFjQMLlZUdUichYqZKKCB2GyibRGJNZ3JvPzVWg5hVVRx9hACEw/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'CNKq11a69WSezik2aobqsA=='),
('1352233320682930178',NULL,'2021-01-21 20:35:21',NULL,'2021-01-21 21:16:01',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5HBFdkSYTC4uzf9gvW3cutU','NULL','1','长沙','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/chMqczIChvg1AXBmBran0EzkD4f52jKEpRFmIweBDN1QpeC4JPN5HKE3fgUYFNAFN4warrIQhEj69SCkY2zyYA/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'jSa/lKtJmYPVHZcTl7r5kw=='),
('1352572935968165889',NULL,'2021-01-22 19:04:52',NULL,'2021-01-22 19:05:20',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5HWkzS2_iL2nBoao-nsxlgI','Ethan.D','1','益阳','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/5DPIvtrqFPv2hcU09UmW3fGQXzIwmO8iciajsHNTzz1NrlwBeVm5ou8HCaO7kXIDmVwhoqnicIibI4BXf8GlKFN7YA/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'G87A8PJ+HeqJzeVxW/tYpA=='),
('1354473059078176770',NULL,'2021-01-28 00:55:16',NULL,'2021-01-28 00:55:23',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55T2ZXs-p68eMcouJR7IFVQw','JL','1','深圳','中国','广东',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJru7MZse3ErXMMsSSQ3rcrBoESJN5F9p7xibr1u54DaGv3NGb6Z9tSTsJ07VsYCBFW7GWkQhIJc2A/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'ooeYfyhjXVSVndtbfWtLNQ=='),
('1355406809988345857',NULL,'2021-01-30 14:45:40',NULL,'2021-01-30 14:50:37',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55eVbz74-EiU2f-j1Rie_BwM','NULL','1','长沙','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/cuTB5LL4dia7CJLqAxV2ibE8OiawFCcF4yRduugIxZTnJBmye7wddrErqShW1JkmXgYibDSKgib2cicURicLaPPknGGjw/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'YM7Jk6qAfQ3yr8jNNbj2ww=='),
('1356171782972882945',NULL,'2021-02-01 17:25:25',NULL,'2021-02-02 22:44:21',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55a_buCs7ozlJOBHYgm6b_ko','Ethan.D','1','益阳','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/XdqjFObB4mmxQMURhIr5XzUgicRic3qOuXhz74OQnmHg4wKf5NUSm11ib0rXBsaIsJxGjicz1AY3a2Pz46iacqibfNqg/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'oknUXi+2mvSjisq3vyGrtw=='),
('1357673320701546498',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-01-06 14:14:44',1,NULL,'o3QwG1QnY-BOe4M724t0dvVQaUUo','魂散','0','','','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/PiajxSqBRaEK4NgUCJLPziclZYMfTnaYFXvz1GajlxariavaOkbKsXzXMoVHO6E5LKUWaaxxQccLVaicYR2Zqv5ZnA/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320701546499',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-10-29 23:34:34',1,NULL,'o3QwG1YepdGeVJZv_2bfIEjwnb_I','愈辉','1','','亚美尼亚','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OWXHibcSriareU9XpBCdBgkOd286EialAX0BtrWEdrhunepPEUq82E6wneLbtNttjKDMJSM7Y9HOnaRA/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320714129409',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-11-09 11:36:47',1,NULL,'o3QwG1ThD7gJ-qIXTDF88rly1VHg','八爪鱼','1','','中国','北京',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OW2BkZicF01PtUQera34FdW1Ga68DhZxQ7MlGMLDG3DZIBMm2Cibjueb6NMDvRMMRZFqjMVEogD9Oibw/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320722518017',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-12-05 16:04:40',1,NULL,'o3QwG1ZP0s_alsf-PuhDU7CmLQ24','十万伏特','1','成都','中国','四川',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/J6c32680OdZUtzqT9zvNO2QR8jG1jdPiaFFQVA91Szrnpke0ga7UCCXGTKqZIppyibuhv6NTRX3OXqPtSQey8Ww0qgzhqicUgGR/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320730906626',NULL,'2021-02-05 20:51:58',NULL,NULL,'备注','0','2','1','ADD_SCENE_QR_CODE','2021-01-23 11:54:59',1,NULL,'o3QwG1Z4EZBdLwtKbK9TozDunLZw','Allen','1','成都','中国','四川',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/OT05QvwvgZZ3KeIaQ25CrjHF9rQTpZhO4RM1szUEcUdfLjEcFoicD3snicPq8GIqiayc1Ned8CIY5Gk5kCInF4TrR07Isicn4gFS/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320739295234',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2021-01-18 14:27:13',1,NULL,'o3QwG1UuAz7VYM24e9rmihxyKJvg','JL','1','深圳','中国','广东',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/dMKNvxZfIaEco8NogUXngnPhXrEEzLoY69XP5ymS2RWFIyXpOGE8trxiaqydnIibicluloYMWO06qmmibuvZR6GEbYR1HmVCq41R/132',NULL,'{}','[107,2]','1',NULL,NULL,NULL,NULL),
('1357673320747683841',NULL,'2021-02-05 20:51:58',NULL,NULL,'99','0','2','1','ADD_SCENE_QR_CODE','2021-01-11 17:43:37',1,NULL,'o3QwG1XWOtVl_ifcXYbPuiaPPnMc','redis','1','','中国','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OU18kicFJhIBibV0XlvEnWzKN09tvVz3wyryA2cysGibW8BarSLyia8HeuOx8YDibGE192BibXG1xTtfC2nXf0x3MZS1x/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320747683842',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-03-16 10:32:31',1,NULL,'o3QwG1ecy727RcaP3XyevHbPK33M','、','1','厦门','中国','福建',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/OT05QvwvgZYuck1R4BqYzwFzicuAicDHSeJTKI21VvxgrUxEWnVxiaEseEVLnM2tzibxTIfUiaZ1aSLn4hJ8FSgu7EBgeID2LCh9s/132',NULL,'{}','[2]','1',NULL,NULL,NULL,NULL),
('1357673320747683843',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-11-09 20:41:24',1,NULL,'o3QwG1RLqJDTP-KZfNxMrMOKpl1U','gameover!!!','1','武汉','中国','湖北',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/upjJ1bex0ocf0rsbPbSW6yorFpT2SicGibyia5bYRjqLpWDgnYR4icEtQ87TcDibO3qujm8wkhDib4CPQCldShq1FHovW9J2ibSsfFH/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320747683844',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-10-14 14:32:49',1,NULL,'o3QwG1f7sT5V_FV_EVj4kaQ09Zzs','壹杯淸茶。','1','青岛','中国','山东',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ajNVdqHZLLCYmmGPrvvXcib0iaiaGQba4yFtwt35zEUgOAzGwPcwG2GIqmejmo8fxRibwQzSDibejrXV4dia1uiaanvXrZ3SKZyZiaEo3G2K8WhDTjs/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320756072449',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-02-24 16:49:28',1,NULL,'o3QwG1eaqyTxxW4VisfbaKL0BcWY','.Llkoi','1','长沙','中国','湖南',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/TBfgdHR2VFWloL25J3r1DfDE3a5R3yctJD3wc5CSoe3xWmy4lZPzxRZpj2x14dl87ndzlRXAN1ZN2W7w1n8bYtKWOMxG8ahq/132',NULL,'{}','[2]','1',NULL,NULL,NULL,NULL),
('1357673320760266753',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-06-17 22:14:41',1,NULL,'o3QwG1d4Bq8lg-NbUOOYdaaVWhnE','Quentin','1','南京','中国','江苏',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ceebSkrkkFTBe1cSDicrLGq05uMsfRkzNWhKp3JY6eISxwCoiagt6q2L4RGcGh61jnWWTI3xeXsAmFCEpozdSIDQKBhNosic8TY/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),
('1357673320760266754',NULL,'2021-02-05 20:51:58',NULL,NULL,'后来','0','2','1','ADD_SCENE_QR_CODE','2019-06-04 22:22:21',1,NULL,'o3QwG1aKxN5AMEaNSbDV-vHJHtvM','安安晨晨','2','益阳','中国','湖南',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ceebSkrkkFTRWgtVgYzPOETJtkqz0TIOzpVber8ic5DlUTky6zpgTGJHic6gG4wH7B7iay12QHo7BF3Iv0r6vTfS2GkcdywCmN8/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
