/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 8.0.19 : Database - joolun_ry
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
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';

/*Data for the table `gen_table` */

/*Table structure for table `gen_table_column` */

DROP TABLE IF EXISTS `gen_table_column`;

CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';

/*Data for the table `gen_table_column` */

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

insert  into `goods_category`(`id`,`enable`,`parent_id`,`name`,`description`,`pic_url`,`sort`,`create_time`,`update_time`,`del_flag`) values ('1352217944631853057','1','0','JooLun','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/joolun.png',1,'2021-01-21 19:34:15','2021-01-21 19:34:15','0'),('1352217984268025858','1','1352217944631853057','JooLun二开源码','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/Joolun_logo.png',1,'2021-01-21 19:34:25','2021-01-21 19:34:25','0'),('1352218128409477121','1','0','鞋包','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E7%AE%B1%E5%8C%85%E9%85%8D%E9%A5%B0.png',4,'2021-01-21 19:34:59','2021-01-21 19:34:59','0'),('1353731419288924162','1','0','手机','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E6%89%8B%E6%9C%BA.png',2,'2021-01-25 23:48:16','2021-01-25 23:48:16','0'),('1355427201956208642','1','1353731419288924162','iPhone','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg',1,'2021-01-30 16:06:42','2021-01-30 16:06:42','0'),('1356512975786287105','1','1353731419288924162','Huawei','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-5.png',2,'2021-02-02 16:01:11','2021-02-02 16:01:11','0'),('1356950418000826369','1','0','服饰','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E6%9C%8D%E9%A5%B0.png',5,'2021-02-03 20:59:25','2021-02-03 20:59:25','0'),('1356973226344960001','1','0','电脑','','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/%E5%88%86%E7%B1%BB-%E7%94%B5%E8%84%91.png',3,'2021-02-03 22:30:03','2021-02-03 22:30:03','0'),('1366609186567565314','1','0','Joolun','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/joolun.png',0,'2021-03-02 12:39:55','2021-03-02 12:39:55','0'),('1366628215470501890','1','1366609186567565314','Uniapp 多店版','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-14.png',1,'2021-03-02 13:55:31','2021-03-02 13:55:31','0'),('1366628362271141890','1','1366609186567565314','小程序商城版','','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-17.png',2,'2021-03-02 13:56:06','2021-03-02 13:56:06','0');

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
  `version` int DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

/*Data for the table `goods_spu` */

insert  into `goods_spu`(`id`,`spu_code`,`name`,`sell_point`,`description`,`category_first`,`category_second`,`pic_urls`,`shelf`,`sort`,`sales_price`,`market_price`,`cost_price`,`stock`,`sale_num`,`create_time`,`update_time`,`del_flag`,`version`) values ('1353738731164561410','1111','iPhone 12 Pro','Apple iPhone 12 Pro (A2408) 128GB 银色 支持移动联通电信5G 双卡双待手机','<p><img src=\"https://img13.360buyimg.com/cms/jfs/t1/120836/20/14832/819799/5f8604f8Eb381a921/5be9108f28a06b69.jpg\"></p>','1353731419288924162',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-10.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-13.png\"]','1',0,'8499.00','8499.00','3000.00',954,90,'2021-01-26 00:17:19','2021-09-30 15:40:45','0',0),('1355412081553190914','','HUAWEI P40 Pro+','华为 HUAWEI P40 Pro+ 麒麟990 5G SoC芯片 5000万超感知徕卡五摄 100倍双目变焦 8GB+256GB陶瓷白全网通5G','<p><img src=\"https://img30.360buyimg.com/sku/jfs/t1/130133/7/3307/52553/5efc4742E200565f5/6fa1f1a54b11acb1.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/142066/17/1894/134849/5efc4743Eac74a71f/28cf077ca55c34f5.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148131/25/1904/192769/5efc4744E6eb9d7f3/60756a8b50447e1c.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125899/31/5847/193351/5efc4744E35d465bd/9632ee231af86d46.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/134152/32/3459/193038/5efc4744Ef7727a34/24d8e547d9118e60.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/117607/12/11317/193929/5efc4744E4d7b8c68/0fdac90cc0c6e342.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/119971/26/6154/191622/5efc4744E084350ba/3696910ce86799c7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111365/35/11222/193594/5efc4744E89bf74f7/ba23d8e693c05899.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/112101/16/11491/190346/5efc4744Eb475216b/0f591e0dd6f4aa1f.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126083/28/6084/122849/5efc4743Eb74f5730/afd5d279cea23a24.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/142626/35/1814/194436/5efc4744E5e88333b/109f6b80129f93d7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126006/14/5980/191887/5efc4744E4c04463f/3c54d7c9c7166e91.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120527/1/6004/191704/5efc4744E6431ecaf/777cc9d3a7c0f7cf.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/123133/33/6138/195167/5efc4744E33f8f88d/13ece8fb7b02773a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111241/12/11280/188793/5efc4744E68d8897a/ad4e1f2c82902493.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/145365/38/1885/191184/5efc4744E67826c6c/e6393e2dc0aa5a42.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/122722/33/5964/192778/5efc4744E6e1590bb/c9ad4caac3dead33.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148811/10/1874/190669/5efc4744Ecb41cf55/a10fea1ff0f3cae5.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/131817/38/3331/194435/5efc4744E75aea567/ada3e0cd88dd4f22.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/124719/11/5936/192993/5efc4744Eb5128be8/6be3d772b7a70d4a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125244/27/6027/189712/5efc4744E90d3dc7f/d4d6bb0539bd0fb3.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/143975/5/1798/190739/5efc4744E134f9107/e8a940cf3e0baca7.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/112510/20/11095/190039/5efc4744E0162060c/64e74f3ef3047c87.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/135166/16/3342/179489/5efc4744E15e0345f/0cb71014bd9f6c38.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111410/36/11009/192128/5efc4744Ec568c450/56557756283c282f.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120081/11/6015/192773/5efc4744E3afd8bfb/2c97e85528e61a46.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/128504/31/6088/190958/5efc4744Eb69b0e3f/75e5100614a80d74.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/118043/22/11395/193721/5efc4744E74454e7a/27678fe84482fc05.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/111081/40/11475/193430/5efc4744E5873afb5/66ea1428f6c318e6.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/135964/22/3408/191328/5efc4744E89841aad/ce62d8e74d50121a.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/125252/16/6085/93984/5efc4743Ee32e21f9/72d8eeb0167e9e24.jpg\"></p>','1353731419288924162','1356512975786287105','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-11.png\"]','1',0,'0.10','300.00','230.00',1554,216,'2021-01-30 15:06:37','2021-09-30 15:40:42','0',2),('1355440649314263041','','HUAWEI Mate 40 Pro+','超感知徕卡五摄，全时段高清超远距双目变焦系统，10 倍 光学变焦，6.58 英寸满溢屏，经典陶瓷机身','<p><img src=\"https://img30.360buyimg.com/sku/jfs/t1/157372/6/954/597397/5fed99ffEb17acdf3/ee647667ca676950.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/158979/1/946/252212/5fed9a09E1c440a55/a013c59fe0aea1c0.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/127637/31/16778/194169/5f9bc4d9E211a89ca/37e8d05bdc190993.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/120464/35/16960/162360/5f9bc4d8E4f7982c3/187427af03ca09ad.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/136998/35/14081/187631/5f9bc4d9Ec474680a/a705d0a4eb7882fd.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/123445/39/16851/195095/5f9bc4d9Eaaed7687/c920bc2a6e033833.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/141780/33/12422/192173/5f9bc4d9Ecf53c668/d84d4eae9a6b7f39.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/148760/37/12642/191430/5f9bc4d9E1fb46d74/bd22a7057d8a5b87.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/121538/14/16737/184789/5f9bc4d9E33d0e445/4d53e8b18005beef.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/126592/29/16541/151671/5f9bc4d9E94955912/6933db658079bdcd.jpg\"><img src=\"https://img30.360buyimg.com/sku/jfs/t1/128715/7/16676/194348/5f9bc4daEaa5dfe97/896840ca8c00ce16.jpg\"></p>','1353731419288924162','1356512975786287105','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-12.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-9.png\"]','1',0,'8999.00','8999.00','8999.00',1905,39,'2021-01-30 17:00:08','2021-09-30 15:40:38','0',0),('1366609536255078402','','Joolun uniapp多店版','Java+微服务+SaaS多租户+uniapp+b2c商城管理系统。目前已有小程序商城、公众号H5商城、APP商城（android/ios端）、APP商户端（android/ios端）等主流终端。前端采用uniapp开发，一套代码多端复用，底层设计强大，专为二开研发。','<ul><li><strong style=\"color: rgb(43, 43, 43);\">购买须知</strong></li></ul><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">购买后可获得全部源码、开发文档、部署教程，相应版本终身免费更新。懂java就能上手，二开效率高。私服git管理，可提问、交流技术。</span></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">相应版本都是免费更新的。前期部署上遇到问题我们技术人员还会提供技术协助</span><span style=\"color: rgb(0, 176, 80); background-color: initial;\">，后顾无忧</span></p><p class=\"ql-align-center\"><br></p><ul><li><strong style=\"color: rgb(0, 0, 0);\"> uniapp多店版演示环境</strong></li></ul><p>微服务Sping Cloud + uni-app 强强联合，打造的SaaS多租户B2C多商户（多店铺）商城系统</p><p><img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAgos-6_AUorbeO8AMw2AI42AI!200x200.jpg.webp\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgg8-6_AUo4sL4rwMwggI4ggI!200x200.png.webp\"></p><p><span style=\"background-color: initial; color: rgb(0, 0, 0);\">后台管理:</span>demo1.joolun.com</p><p><br></p><ul><li><strong style=\"color: rgb(0, 0, 0);\">多终端覆盖</strong></li></ul><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwLqI-QUo66LQ7QMw0A84gQY!1500x1500.png.webp\"></span></p><p>APP商户端、APP商城、PC后台管理、小程序商城、公众号H5商城</p><p><br></p><ul><li><strong style=\"color: rgb(43, 43, 43); background-color: initial;\">超前的技术选型</strong></li></ul><p><span style=\"color: rgb(139, 139, 139);\">搭载当前最先进、最流行的核心框架和SDK</span></p><p>* 核心框架：SpringBoot2+SpringCloudAlibaba+ SpringCloudGateway+RESTful规范+前后端分离</p><p>* 配置中心、注册中心：Nacos</p><p>* 安全框架：Spring Security OAuth2</p><p>* 服务间调用：OpenFeign</p><p>* 熔断限流：Sentinel</p><p>* 分布式事务：Seata</p><p>* 消息中间件：Redis</p><p>* 持久层框架：MyBatis-plus</p><p>* PC端后台技术：Vue2 + Element-UI + Avue</p><p>* PC端后台手脚架：Avue-cli</p><p>* 移动端框架：Uniapp(<span style=\"color: rgb(235, 29, 78);\">一套代码，多端发行</span>)</p><p>* 移动端CSS类库：ColorUI</p><p>* 微信开发Java SDK：WxJava</p><p>* 文件管理：阿里oss、七牛云、腾讯cos、minio</p><p>* JDK版本：Java8+</p><p>* 数据库：mysql5.7+</p><p>* 定时任务：XXL-JOB</p><p>* 图表插件：Echartsn</p><p><br></p><ul><li><strong style=\"background-color: initial; color: rgb(0, 0, 0);\">Joolun uniapp多租户/多公众号H5商城/多小程序商城/</strong><strong style=\"color: rgb(0, 0, 0);\">多APP商城/多店铺管理系统</strong></li></ul><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwL2I-QUoyoiCzgQwkAs4hwc!1000x1000.png.webp\"></span></p><p><br></p><p>小程序商城版：多商城（既多租户）一个商城就是一个店铺</p><p>uniapp多店版：多商城（既多租户）一个商城里面有多个店铺（商户）</p><p>各商城（租户）之间数据相互隔离、互不影响</p><p><span style=\"color: rgb(245, 81, 30);\">如果把一个商城（租户）比做淘宝，uniapp多店版就能同时管理多个不同的淘宝，每个淘宝下面有多个店铺。各个淘宝之间是相互独立，数据是互不影响的，各自管理各自的商城、小程序、app、公众号等</span></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg4Lyp_QUo0qujvwcw3As4tQg!800x800.png.webp\"></span></p><ul><li><strong style=\"color: rgb(0, 0, 0);\">多店铺——B2C多商户</strong></li></ul><p><br></p><p> &nbsp; &nbsp; &nbsp;一个商城可以有无数个店铺入驻，商城首页有甄选店铺的模块，点击更多可以进入店铺列表页面，商城里面的店铺就会依次展现出来，每个店铺可以单独设置自己的积分、优惠券、拼团、砍价、秒杀等活动，每个店铺拥有独立的客服、独立的后台数据，还能够进行店铺分享以及收藏等。</p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg-dKf_QUokMC8VjDcCziEBw.png.webp\"></span></p><ul><li><strong>APP商户端</strong></li></ul><p>JooLun Uniapp多店版拥有独立的IM客服管理系统，实时聊天，数据更安全</p><p><br></p><ul><li><strong>商城拖拽装修</strong></li></ul><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/3/ABUIABADGAAgju3M-gUoiPj4owEwgAU4kAM.gif.webp\"></span></p><p>可自定义文字、图片、颜色等界面素材，可自定义主题颜色、</p><p>首页轮播设置、公告设置、导航设置、首页底部TabBar设置</p><p>等。商城首页可进行拖拽装修，丰富的基础组件，随意拖拽，</p><p>装修适合自己风格的主题，让商城更多样化。</p><p><br></p><ul><li><strong>统一后台管理</strong></li></ul><p>JooLun Uniapp多店版拥有多个独立的后台，一个后台账户可以管理小程序商城<span style=\"background-color: initial;\">+</span>公众号H5商城+APP商城等多个终端。</p><p>不同权限有不同权限的登录后台</p><p>系统管理员——最高权限，能看到所有功能</p><p>商城管理员——管理商城所有功能</p><p>店铺管理员——管理本店铺数据</p><p>租户管理员——管理各个租户的信息</p><p><br></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg576j_QUolp7utgMwshM4pgo!900x900.png.webp\"></span></p><p><span style=\"color: rgb(102, 102, 102);\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgq6Wo_QUo5vuYfzDWCzixCA!900x900.png.webp\"></span></p>','1366609186567565314','1366628215470501890','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-14.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-15.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-16.png\"]','0',0,'18999.00','89999.00','18999.00',979,888,'2021-03-02 12:41:18','2021-07-21 14:01:42','0',0),('1366616360643538946','','Joolun 小程序商城版','小程序商城版包含了公众号基础版所有内容。拥有多公众号后台管理系统+小程序商城系统，前端采用微信原生框架开发，完善的教程文档懂Java就能上手。','<p class=\"ql-align-center\"><strong style=\"color: rgb(43, 43, 43);\" class=\"ql-size-large\">购买须知</strong></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">购买后可获得全部源码、开发文档、部署教程，相应版本终身免费更新。懂java就能上手，二开效率高。私服git管理，可提问、交流技术。</span></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 176, 80);\">相应版本都是免费更新的。前期部署上遇到问题我们技术人员还会提供技术协助</span><span style=\"color: rgb(0, 176, 80); background-color: initial;\">，后顾无忧</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong class=\"ql-size-large\">小程序商城版演示环境</strong></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">支持多公众号、多小程序、多租户管理</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAg2Ouj9QUo5P6MzAQwrgM4rgM!160x160.jpg.webp\"><span style=\"color: rgb(102, 102, 102);\">  <img src=\"https://23592599.s21i.faiusr.com/2/ABUIABACGAAgmu_j9QUowO-YbDDYAjjYAg!160x160.jpg.webp\"></span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><span style=\"color: rgb(0, 0, 0); background-color: initial;\">后台管理:</span>demo.joolun.com</p><p class=\"ql-align-center\"><span style=\"color: rgb(255, 255, 255);\">公众号演示</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong style=\"color: rgb(43, 43, 43);\" class=\"ql-size-large\">全新微服务框架</strong></p><p class=\"ql-align-center\"><br></p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 核心框架：Spring Boot2 + Spring Cloud Alibaba + Spring Cloud Gateway+RESTful规范+前后端分离</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 配置中心、注册中心：Nacos</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 安全框架：Spring Security OAuth2</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 服务间调用：OpenFeign</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 熔断限流：Sentinel</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 分布式事务：Seata</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 消息中间件：Redis</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 前端手脚架：Avue-cli</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 前端框架：Vue2 + Element-UI + Avue</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 持久层框架：MyBatis-plus</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 微信开发Java SDK：WxJava</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 文件管理：阿里oss、七牛云、腾讯cos、minio</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* JDK版本：Java8+</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 数据库：mysql5.7+</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 图表插件：Echarts</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">JooLun系统基于SpringCloud微服务构建，完全遵循RESTful规范，</p><p class=\"ql-align-center\">齐全swagger文档，前后端完全分离，前端采用vue组件化模式</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgwrnh9wUo1fOLlAEwghY4xgU.png\" alt=\"QQ截图20200628165909.png\" height=\"710\" width=\"2818\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAg4bPm9wUoucaT2wMw8hY4tgk.png\" alt=\"小程序1-1.png\" height=\"1206\" width=\"2930\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUow9vufDD4Fzi1DQ.png\" alt=\"小程序2.png\" height=\"1717\" width=\"3064\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUoqJrznQEwnxg47ws.png\" alt=\"小程序3.png\" height=\"1519\" width=\"3103\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgprDm9wUoqtbUigIw2Rg4qws.png\" alt=\"小程序4.png\" height=\"1451\" width=\"3161\"><img src=\"https://23592599.s21i.faiusr.com/4/ABUIABAEGAAgxrnh9wUovoLigwMw1BU4vwk.png\" alt=\"QQ截图20200628182207.png\" height=\"1211\" width=\"2672\"></p>','1366609186567565314','1366628362271141890','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-17.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-18.png\"]','0',0,'2199.00','5999.00','2199.00',976,1699,'2021-03-02 13:08:25','2021-07-21 14:01:41','0',0),('1442505794278191105','1','Apple iPhone 13 Pro','真的强，Pro 级摄像头系统，能力强悍提升；全新 OLED 显示屏','<p><img src=\"https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-13-pro-gallery-1?wid=4056&amp;hei=2400&amp;fmt=jpeg&amp;qlt=80&amp;.v=1629956757000\" alt=\"展开的图库图片 1\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/132.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/133.png\"]','1',0,'8999.00','8999.00',NULL,9951,42,'2021-09-27 23:05:56','2021-09-27 23:05:56','0',2),('1442506933904142338','','Apple iPhone 13','超先进双摄系统，精进再精进。低光拍摄性能最高可有 47% 的提升。','<p><img src=\"https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-13-2021-gallery-1?wid=4056&amp;hei=2400&amp;fmt=jpeg&amp;qlt=80&amp;.v=1630720516000\" alt=\"展开的图库图片 1\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/133.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/132.png\"]','1',0,'0.98','5999.00',NULL,988,10,'2021-09-27 23:10:28','2021-09-27 23:10:28','0',0),('1442511161297006594','','Apple iPhone 12 Pro','Apple iPhone 12 Pro (A2404) 128GB 白色 支持移动联通电信5G 双卡双待手机','<p><img src=\"https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-12-pro-gallery-1?wid=2138&amp;hei=814&amp;fmt=jpeg&amp;qlt=80&amp;.v=1604612540000\"><img src=\"https://img14.360buyimg.com/cms/jfs/t1/203778/2/2249/1433641/611f1bc2E0e9afb22/17565e464b9e554e.jpg\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/49.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/50.png\"]','1',0,'8999.00','8999.00',NULL,996,1,'2021-09-27 23:27:16','2021-09-27 23:27:16','0',0),('1442511572003254273','','Apple iPhone 12','Apple iPhone 12 (A2408) 256GB 紫色 支持移动联通电信5G 双卡双待手机','<p><img src=\"https://img14.360buyimg.com/cms/jfs/t1/203778/2/2249/1433641/611f1bc2E0e9afb22/17565e464b9e554e.jpg\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/50.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/49.png\"]','1',0,'5999.00','5999.00',NULL,1000,0,'2021-09-27 23:28:53','2021-09-27 23:28:53','0',0),('1442512050032275457','','Apple iPad mini','全面屏设计的 8.3 英寸 Liquid 视网膜屏格外绚丽','<p><img src=\"https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/ipad-mini-select-202109?wid=1080&amp;hei=1060&amp;fmt=jpeg&amp;qlt=80&amp;.v=1629840394000\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/134.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/59.png\"]','1',0,'3799.00','3999.00',NULL,995,3,'2021-09-27 23:30:47','2021-09-27 23:30:47','0',0),('1442512382615416833','','Apple iPad Pro','12.9 英寸 Liquid 视网膜 XDR 屏','<p><img src=\"https://img10.360buyimg.com/cms/jfs/t1/170137/38/20116/112791/607f3951E89601259/485bda2785ac1eeb.jpg\"></p>','1353731419288924162','1355427201956208642','[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/59.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/60.png\"]','1',0,'8499.00','8999.00',NULL,992,7,'2021-09-27 23:32:07','2021-09-27 23:32:07','0',0),('1442512958581436418','','Apple MacBook Pro 16英寸','2.6GHz 6 核处理器 512GB 存储容量 AMD Radeon Pro 5300M','<p><img src=\"https://img14.360buyimg.com/cms/jfs/t1/138296/20/1266/398082/5ef06ac7Efad45bc6/32db385165ef0927.jpg\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/56.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/55.png\"]','0',0,'18999.00','19999.00',NULL,1068,32,'2021-09-27 23:34:24','2023-03-02 16:13:05','0',0),('1442513254305034241','','Apple MacBook Pro','Apple M1 芯片，配备 8 核中央处理器和 8 核图形处理器 512GB 存储容量','<p><img src=\"https://img13.360buyimg.com/cms/jfs/t1/123076/24/18281/711442/5faafc48E581545a5/aefc8b6131a2d36e.jpg\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/55.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/56.png\"]','0',0,'11999.00','12999.00',NULL,998,2,'2021-09-27 23:35:35','2023-03-02 16:13:04','0',0),('1442513594978988034','','Apple Watch Series 6','蓝色铝金属表壳；编织单圈表带','<p><img src=\"https://img13.360buyimg.com/cms/jfs/t1/143910/12/8290/881398/5f614b7fEa9c33572/70c2ca76160cd178.jpg\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/62.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/64.png\"]','1',0,'3599.00','3699.00',NULL,993,4,'2021-09-27 23:36:56','2021-09-27 23:36:56','0',0),('1442513916795351042','','Apple Watch Series 3','银色铝金属表壳搭配白色运动型表带','<p><img src=\"https://img10.360buyimg.com/cms/jfs/t1/139652/40/1024/385619/5eec612aEef6b4fcc/d3da0bef95dc9e88.jpg\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/64.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/65.png\"]','0',0,'1499.00','1699.00',NULL,997,1,'2021-09-27 23:38:13','2023-03-02 16:12:34','0',0),('1442514202062548994','','AirPods Pro','Apple AirPods Pro 主动降噪无线蓝牙耳机 适用iPhone/iPad/Apple Watch','<p><img src=\"https://img12.360buyimg.com/cms/jfs/t1/87237/38/973/1713314/5db72bb8E3ef84ced/01587e901af36baf.png\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/67.png\",\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/68.png\"]','0',0,'1799.00','1899.00',NULL,991,7,'2021-09-27 23:39:21','2023-03-02 16:12:32','0',0),('1442514509798633473','','Apple AirPods','主动降噪无线蓝牙耳机 适用iPhone/iPad/Apple Watch','<p><img src=\"https://img12.360buyimg.com/cms/jfs/t1/87237/38/973/1713314/5db72bb8E3ef84ced/01587e901af36baf.png\"></p>','1356973226344960001',NULL,'[\"https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/68.png\"]','1',0,'1.00','1399.00',NULL,998,0,'2021-09-27 23:40:34','2023-03-22 09:48:47','0',1);

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

insert  into `order_info`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`order_no`,`payment_way`,`is_pay`,`name`,`status`,`freight_price`,`sales_price`,`payment_price`,`payment_time`,`delivery_time`,`receiver_time`,`closing_time`,`user_message`,`transaction_id`,`logistics_id`,`remark`) values ('1354094503631306753','0','2021-01-26 23:51:02','2021-01-26 23:51:02','1352572935968165889','1354094503576731648','2','0',NULL,'5','0.00','0.00','0.00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('1354469715404148737','0','2021-01-28 00:41:59','2021-01-28 00:41:59','1352168072700571649','1354469713115086848','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354469714800168962',NULL),('1354474070446510081','0','2021-01-28 00:59:17','2021-01-28 00:59:17','1354473059078176770','1354474068199342080','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354474069813170178',NULL),('1354620399822884865','0','2021-01-28 10:40:45','2021-01-28 10:40:45','1354473059078176770','1354620397982580736','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354620399231488001',NULL),('1354795347837304834','0','2021-01-28 22:15:56','2021-01-28 22:15:55','1354473059078176770','1354795346135351296','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354795347308822530',NULL),('1354797185856794625','0','2021-01-28 22:23:14','2021-01-28 22:23:13','1354473059078176770','1354797183827705856','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354797185185705985',NULL),('1354797794534137858','0','2021-01-28 22:25:39','2021-01-28 22:25:39','1354473059078176770','1354797792530268160','2','0','iPhone12白色','5','0.00','4999.00','4999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1354797793913380865',NULL),('1354798824059609090','0','2021-01-28 22:29:45','2021-01-28 22:29:44','1354473059078176770','1354798822391283712','2','1','iPhone12白色','3','0.00','0.01','0.01','2021-01-28 22:30:01','2021-01-28 23:16:51','2021-01-28 23:17:41',NULL,NULL,'4200000797202101287152815447','1354798823514349569',NULL),('1354798971141267457','0','2021-01-28 22:30:20','2021-01-28 22:30:19','1354473059078176770','1354798969477136384','2','1','iPhone12白色','3','0.00','0.01','0.01','2021-01-28 22:32:33','2021-01-28 23:10:49','2021-01-28 23:16:19',NULL,NULL,'4200000808202101285235202004','1354798970596007937',NULL),('1355417350676951041','0','2021-01-30 15:27:33','2021-01-30 15:27:33','1355406809988345857','1355417348219076608','2','0','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38',NULL,'0.00','0.20','0.20',NULL,NULL,NULL,NULL,NULL,NULL,'1355417349930364930',NULL),('1355418768053907457','0','2021-01-30 15:33:11','2021-01-30 15:33:11','1355406809988345857','1355418765948354560','2','0','Apple iPhone',NULL,'0.00','6000.00','6000.00',NULL,NULL,NULL,NULL,NULL,NULL,'1355418767420567554',NULL),('1355426472587714562','0','2021-01-30 16:03:48','2021-01-30 16:03:48','1355406809988345857','1355426470679281664','2','1','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','1','0.00','0.10','0.10','2021-01-30 16:04:20',NULL,NULL,NULL,NULL,'4200000801202101302811163830','1355426471975346177',NULL),('1357673549756682241','0','2021-02-05 20:52:53','2021-02-05 20:52:52','1354473059078176770','1357673547845074944','2','0','iPhone 12 Pro','5','0.00','8499.00','8499.00',NULL,NULL,NULL,NULL,NULL,NULL,'1357673549135925250',NULL),('1632993114357575681','0','2023-03-07 14:34:33','2023-03-07 14:34:32','1354473059078176770','1632993114207551488','2','0','HUAWEI P40 Pro+','5','0.00','0.98','0.98',NULL,NULL,NULL,NULL,NULL,NULL,'1632993114324021250',NULL),('1632994752065449985','0','2023-03-07 14:41:03','2023-03-07 14:41:03','1354473059078176770','1632994751923879936','2','0','HUAWEI P40 Pro+','5','0.00','0.10','0.10',NULL,NULL,NULL,NULL,NULL,NULL,'1632994752031895553',NULL),('1632995133256437763','0','2023-03-07 14:42:34','2023-03-07 14:42:34','1354473059078176770','1632995133102227456','2','1','HUAWEI P40 Pro+','5','0.00','0.10','0.10','2023-03-07 14:42:56','2023-03-07 14:59:46',NULL,NULL,NULL,'4200001772202303073968832224','1632995133256437762',NULL),('1645623820711706626','0','2023-04-11 11:04:28','2023-04-11 11:04:27','1354473059078176770','1645623820574261248','2','0','Apple iPhone 13 Pro','5','0.00','8999.00','8999.00',NULL,NULL,NULL,NULL,NULL,NULL,'1645623820711706625',NULL),('1645635415705706497','0','2023-04-11 11:50:32','2023-04-11 11:50:32','1354473059078176770','1645635415513759744','2','1','HUAWEI P40 Pro+','5','0.00','0.10','0.10','2023-04-11 11:50:49',NULL,NULL,NULL,NULL,'4200001784202304119379049641','1645635415605043201',NULL);

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
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '状态0：正常；1：退款中；2:拒绝退款；3：同意退款',
  `is_refund` char(2) DEFAULT '0' COMMENT '是否退款0:否 1：是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单详情';

/*Data for the table `order_item` */

insert  into `order_item`(`id`,`del_flag`,`create_time`,`update_time`,`order_id`,`spu_id`,`spu_name`,`pic_url`,`quantity`,`sales_price`,`freight_price`,`payment_price`,`remark`,`status`,`is_refund`) values ('1354469716075237378','0','2021-01-28 00:41:59','2021-01-28 00:41:59','1354469715404148737','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354474071088238594','0','2021-01-28 00:59:18','2021-01-28 00:59:18','1354474070446510081','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354620400435253249','0','2021-01-28 10:40:45','2021-01-28 10:40:45','1354620399822884865','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354795348378370049','0','2021-01-28 22:15:55','2021-01-28 22:15:55','1354795347837304834','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354797186527883265','0','2021-01-28 22:23:14','2021-01-28 22:23:14','1354797185856794625','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354797795180060673','0','2021-01-28 22:25:39','2021-01-28 22:25:39','1354797794534137858','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'4999.00','0.00','4999.00',NULL,'0','0'),('1354798824613257217','0','2021-01-28 22:29:44','2021-01-28 22:29:44','1354798824059609090','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'0.01','0.00','0.01',NULL,'0','0'),('1354798971694915585','0','2021-01-28 22:30:19','2021-01-28 22:30:19','1354798971141267457','1353738731164561410','iPhone12白色','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png',1,'0.01','0.00','0.01',NULL,'0','0'),('1355417351444508674','0','2021-01-30 15:27:33','2021-01-30 15:27:33','1355417350676951041','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',2,'0.10','0.00','0.20',NULL,'0','0'),('1355418768758550529','0','2021-01-30 15:33:11','2021-01-30 15:33:11','1355418768053907457','1353738731164561410','Apple iPhone','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg',1,'6000.00','0.00','6000.00',NULL,'0','0'),('1355418868545236994','0','2021-01-30 15:33:35','2021-01-30 15:33:35','1355418867987394561','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',1,'0.10','0.00','0.10',NULL,'0','0'),('1355426473221054466','0','2021-01-30 16:03:48','2021-01-30 16:03:48','1355426472587714562','1355412081553190914','【时尚博主推荐】Daphne/达芙妮2020春季新款闪耀水晶扣时装鞋淑雅女单鞋 粉红112 38','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/aed00a45-1598-490d-9ea9-b35c386ae6b3.png',1,'0.10','0.00','0.10',NULL,'0','0'),('1357673550390022145','0','2021-02-05 20:52:52','2021-02-05 20:52:52','1357673549756682241','1353738731164561410','iPhone 12 Pro','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-4.png',1,'8499.00','0.00','8499.00',NULL,'0','0'),('1632993114403713025','0','2023-03-07 14:34:32','2023-03-07 14:34:32','1632993114357575681','1355412081553190914','HUAWEI P40 Pro+','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-11.png',1,'0.98','0.00','0.98',NULL,'0','0'),('1632994752065449986','0','2023-03-07 14:41:03','2023-03-07 14:41:03','1632994752065449985','1355412081553190914','HUAWEI P40 Pro+','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-11.png',1,'0.10','0.00','0.10',NULL,'0','0'),('1632995133310963713','0','2023-03-07 14:42:34','2023-03-07 14:42:34','1632995133256437763','1355412081553190914','HUAWEI P40 Pro+','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-11.png',1,'0.10','0.00','0.10',NULL,'3','1'),('1645623820762038274','0','2023-04-11 11:04:27','2023-04-11 11:04:27','1645623820711706626','1442505794278191105','Apple iPhone 13 Pro','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/132.png',1,'8999.00','0.00','8999.00',NULL,'0','0'),('1645635415768621058','0','2023-04-11 11:50:32','2023-04-11 11:50:32','1645635415705706497','1355412081553190914','HUAWEI P40 Pro+','https://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-11.png',1,'0.10','0.00','0.10',NULL,'3','1');

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
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '快递单当前状态，包括-1错误，0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投?等7个状态',
  `is_check` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '签收标记（0：未签收；1：已签收）',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '相关信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单物流表';

/*Data for the table `order_logistics` */

insert  into `order_logistics`(`id`,`del_flag`,`create_time`,`update_time`,`postal_code`,`user_name`,`tel_num`,`address`,`logistics`,`logistics_no`,`status`,`is_check`,`message`) values ('1354469714800168962','0','2021-01-28 00:41:59','2021-01-28 00:41:59',NULL,'张三','18602513214','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354474069813170178','0','2021-01-28 00:59:17','2021-01-28 00:59:17',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354620399231488001','0','2021-01-28 10:40:45','2021-01-28 10:40:45',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354795347308822530','0','2021-01-28 22:15:55','2021-01-28 22:15:55',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354797185185705985','0','2021-01-28 22:23:13','2021-01-28 22:23:13',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354797793913380865','0','2021-01-28 22:25:38','2021-01-28 22:25:38',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1354798823514349569','0','2021-01-28 22:29:44','2021-01-28 22:29:44',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号','yunda','48466513213213165','1',NULL,NULL),('1354798970596007937','0','2021-01-28 22:30:19','2021-01-28 22:30:19',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1355417349930364930','0','2021-01-30 15:27:33','2021-01-30 15:27:33',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),('1355418767420567554','0','2021-01-30 15:33:11','2021-01-30 15:33:11',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),('1355418867316305921','0','2021-01-30 15:33:35','2021-01-30 15:33:35',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),('1355426471975346177','0','2021-01-30 16:03:48','2021-01-30 16:03:48',NULL,'张三','15580802543','北京市北京市东城区大冲地铁口',NULL,NULL,NULL,NULL,NULL),('1357673549135925250','0','2021-02-05 20:52:52','2021-02-05 20:52:52',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1632993114324021250','0','2023-03-07 14:34:32','2023-03-07 14:34:32',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1632994752031895553','0','2023-03-07 14:41:03','2023-03-07 14:41:03',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1632995133256437762','0','2023-03-07 14:42:34','2023-03-07 14:42:34',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号','huitongkuaidi','566985565255','1',NULL,NULL),('1645623820711706625','0','2023-04-11 11:04:27','2023-04-11 11:04:27',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL),('1645635415605043201','0','2023-04-11 11:50:32','2023-04-11 11:50:32',NULL,'张三','18563265321','广东省广州市海珠区新港中路397号',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blob类型的触发器表';

/*Data for the table `qrtz_blob_triggers` */

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日历信息表';

/*Data for the table `qrtz_calendars` */

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Cron类型的触发器表';

/*Data for the table `qrtz_cron_triggers` */

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已触发的触发器表';

/*Data for the table `qrtz_fired_triggers` */

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务详细信息表';

/*Data for the table `qrtz_job_details` */

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储的悲观锁信息表';

/*Data for the table `qrtz_locks` */

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='暂停的触发器表';

/*Data for the table `qrtz_paused_trigger_grps` */

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度器状态表';

/*Data for the table `qrtz_scheduler_state` */

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='简单触发器的信息表';

/*Data for the table `qrtz_simple_triggers` */

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='同步机制的行锁表';

/*Data for the table `qrtz_simprop_triggers` */

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='触发器详细信息表';

/*Data for the table `qrtz_triggers` */

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

insert  into `shopping_cart`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`spu_id`,`quantity`,`spu_name`,`add_price`,`pic_url`) values ('1353755369452634114','0','2021-01-26 01:23:26','2021-01-26 01:29:11','1352233320682930178','1353738731164561410',1,'iPhone12白色','4999.00',NULL),('1354094384559210498','0','2021-01-26 23:50:33','2021-01-26 23:50:33','1352572935968165889','1353738731164561410',1,'iPhone12白色','4999.00','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/category-9.png'),('1355427342960320514','0','2021-01-30 16:07:16','2021-01-30 16:07:16','1355406809988345857','1353738731164561410',1,'Apple iPhone','6000.00','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/10952adc-cad0-4c53-8762-9906d1dde220.jpg'),('1357249438573252609','0','2021-02-04 16:47:37','2021-02-04 16:47:37','1356171782972882945','1355440649314263041',1,'HUAWEI Mate 40 Pro+','8999.00','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-5.png'),('1357249497834573826','0','2021-02-04 16:47:51','2021-02-04 16:47:51','1356171782972882945','1355412081553190914',1,'HUAWEI P40 Pro+','0.10','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-7.png'),('1357673715054202882','0','2021-02-05 20:53:31','2021-02-05 20:53:31','1354473059078176770','1353738731164561410',1,'iPhone 12 Pro','8499.00','http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/goods-4.png');

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

insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2024-09-11 09:31:32','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2024-09-11 09:31:32','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2024-09-11 09:31:32','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2024-09-11 09:31:32','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2024-09-11 09:31:32','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2024-09-11 09:31:32','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

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

insert  into `sys_dept`(`dept_id`,`parent_id`,`ancestors`,`dept_name`,`order_num`,`leader`,`phone`,`email`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2024-09-11 09:31:30','',NULL);

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

insert  into `sys_dict_data`(`dict_code`,`dict_sort`,`dict_label`,`dict_value`,`dict_type`,`css_class`,`list_class`,`is_default`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,1,'男','0','sys_user_sex','','','Y','0','admin','2024-09-11 09:31:32','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2024-09-11 09:31:32','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2024-09-11 09:31:32','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2024-09-11 09:31:32','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2024-09-11 09:31:32','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2024-09-11 09:31:32','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2024-09-11 09:31:32','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2024-09-11 09:31:32','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2024-09-11 09:31:32','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2024-09-11 09:31:32','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2024-09-11 09:31:32','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2024-09-11 09:31:32','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2024-09-11 09:31:32','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2024-09-11 09:31:32','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2024-09-11 09:31:32','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2024-09-11 09:31:32','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2024-09-11 09:31:32','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2024-09-11 09:31:32','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2024-09-11 09:31:32','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2024-09-11 09:31:32','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2024-09-11 09:31:32','',NULL,'停用状态');

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

insert  into `sys_dict_type`(`dict_id`,`dict_name`,`dict_type`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'用户性别','sys_user_sex','0','admin','2024-09-11 09:31:32','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2024-09-11 09:31:32','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2024-09-11 09:31:32','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2024-09-11 09:31:32','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2024-09-11 09:31:32','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2024-09-11 09:31:32','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2024-09-11 09:31:32','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2024-09-11 09:31:32','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2024-09-11 09:31:32','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2024-09-11 09:31:32','',NULL,'登录状态列表');

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

insert  into `sys_job`(`job_id`,`job_name`,`job_group`,`invoke_target`,`cron_expression`,`misfire_policy`,`concurrent`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2024-09-11 09:31:32','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2024-09-11 09:31:32','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2024-09-11 09:31:32','',NULL,'');

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
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';

/*Data for the table `sys_logininfor` */

insert  into `sys_logininfor`(`info_id`,`user_name`,`ipaddr`,`login_location`,`browser`,`os`,`status`,`msg`,`login_time`) values (100,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 09:36:42'),(101,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 10:06:16'),(102,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 10:19:10'),(103,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 15:58:28'),(104,'test','127.0.0.1','内网IP','Chrome 13','Windows 10','1','用户不存在/密码错误','2024-09-11 16:13:48'),(105,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 16:14:04'),(106,'admin','127.0.0.1','内网IP','Chrome 13','Windows 10','0','登录成功','2024-09-11 16:23:14');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
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

insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`route_name`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2024-09-11 09:31:31','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2024-09-11 09:31:31','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2024-09-11 09:31:31','',NULL,'系统工具目录'),(4,'公众号管理',0,4,'wxmp',NULL,'','',1,0,'M','0','0','','wechat','admin','2024-09-11 09:31:31','',NULL,'公众号管理'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2024-09-11 09:31:31','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2024-09-11 09:31:31','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2024-09-11 09:31:31','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2024-09-11 09:31:31','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2024-09-11 09:31:31','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2024-09-11 09:31:31','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2024-09-11 09:31:31','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2024-09-11 09:31:31','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2024-09-11 09:31:31','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2024-09-11 09:31:31','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2024-09-11 09:31:31','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2024-09-11 09:31:31','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2024-09-11 09:31:31','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2024-09-11 09:31:31','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2024-09-11 09:31:31','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2024-09-11 09:31:31','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2024-09-11 09:31:31','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2024-09-11 09:31:31','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2024-09-11 09:31:31','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2024-09-11 09:31:31','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2024-09-11 09:31:31','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2024-09-11 09:31:31','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2024-09-11 09:31:31','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2024-09-11 09:31:31','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2024-09-11 09:31:31','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2024-09-11 09:31:31','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2024-09-11 09:31:31','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2024-09-11 09:31:31','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2024-09-11 09:31:31','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2024-09-11 09:31:31','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2024-09-11 09:31:31','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2024-09-11 09:31:31','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2024-09-11 09:31:31','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2024-09-11 09:31:31','',NULL,''),(2000,'用户标签',4,10,'wxusertags','wxmp/wxusertags/index',NULL,'',1,0,'C','0','0','wxmp:wxusertags:list','tab','admin','2020-03-03 10:47:36','admin','2020-03-03 20:17:50',''),(2001,'修改标签',2000,10,'',NULL,NULL,'',1,0,'F','1','0','wxmp:wxusertags:edit','#','admin','2020-03-03 11:16:13','',NULL,''),(2002,'公众号用户',4,20,'wxuser','wxmp/wxuser/index',NULL,'',1,0,'C','0','0','wxmp:wxuser:index','peoples','admin','2020-03-04 10:13:30','',NULL,''),(2003,'用户消息',4,30,'wxmsg','wxmp/wxmsg/index',NULL,'',1,0,'C','0','0','wxmp:wxmsg:index','clipboard','admin','2020-03-04 10:15:47','',NULL,''),(2004,'素材管理',4,40,'wxmaterial','wxmp/wxmaterial/index',NULL,'',1,0,'C','0','0','wxmp:wxmaterial:index','example','admin','2020-03-04 10:17:21','admin','2020-03-05 21:31:33',''),(2005,'自定义菜单',4,50,'wxmenu','wxmp/wxmenu/detail',NULL,'',1,0,'C','0','0','wxmp:wxmenu:get','cascader','admin','2020-03-04 10:18:02','admin','2020-03-04 10:29:20',''),(2006,'消息自动回复',4,60,'wxautoreply','wxmp/wxautoreply/index',NULL,'',1,0,'C','0','0','wxmp:wxautoreply:index','dashboard','admin','2020-03-04 10:18:53','',NULL,''),(2007,'数据统计',4,70,'wxsummary','wxmp/wxsummary/index',NULL,'',1,0,'C','0','0',NULL,'druid','admin','2020-03-04 10:19:53','',NULL,''),(2008,'用户标签删除',2000,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxusertags:del','#','admin','2020-03-04 17:08:10','',NULL,''),(2009,'用户标签新增',2000,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxusertags:add','#','admin','2020-03-04 17:08:42','',NULL,''),(2010,'公众号用户新增',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:add','#','admin','2020-03-04 17:15:01','admin','2020-03-04 17:16:59',''),(2011,'公众号用户修改',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:edit','#','admin','2020-03-04 17:16:17','admin','2020-03-04 17:17:09',''),(2012,'公众号用户打标签',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:tagging','#','admin','2020-03-04 17:16:41','',NULL,''),(2013,'公众号用户备注修改',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:edit:remark','#','admin','2020-03-04 17:17:43','',NULL,''),(2014,'公众号用户同步',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:synchro','#','admin','2020-03-04 17:18:09','',NULL,''),(2015,'公众号用户删除',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:del','#','admin','2020-03-04 17:18:31','',NULL,''),(2016,'公众号用户详情',2002,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:get','#','admin','2020-03-04 17:18:55','',NULL,''),(2017,'用户消息新增',2003,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmsg:add','#','admin','2020-03-04 17:19:24','',NULL,''),(2018,'用户消息修改',2003,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmsg:edit','#','admin','2020-03-04 17:19:45','',NULL,''),(2019,'用户消息删除',2003,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmsg:del','#','admin','2020-03-04 17:20:03','',NULL,''),(2020,'用户消息详情',2003,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmsg:get','#','admin','2020-03-04 17:20:21','',NULL,''),(2021,'素材新增',2004,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmaterial:add','#','admin','2020-03-04 17:20:43','',NULL,''),(2022,'素材修改',2004,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmaterial:edit','#','admin','2020-03-04 17:21:03','',NULL,''),(2023,'素材删除',2004,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmaterial:del','#','admin','2020-03-04 17:21:24','',NULL,''),(2024,'素材详情',2004,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmaterial:get','#','admin','2020-03-04 17:21:43','',NULL,''),(2025,'自定义菜单发布',2005,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxmenu:add','#','admin','2020-03-04 17:22:12','',NULL,''),(2026,'消息自动回复新增',2006,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxautoreply:add','#','admin','2020-03-04 17:22:43','',NULL,''),(2027,'消息自动回复修改',2006,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxautoreply:edit','#','admin','2020-03-04 17:23:05','',NULL,''),(2028,'消息自动回复删除',2006,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxautoreply:del','#','admin','2020-03-04 17:23:36','',NULL,''),(2029,'消息自动回复详情',2006,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxautoreply:get','#','admin','2020-03-04 17:23:59','',NULL,''),(2033,'商城管理',0,5,'mall',NULL,NULL,'',1,0,'M','0','0',NULL,'shopping','admin','2021-01-21 17:44:55','',NULL,''),(2034,'商品分类',2033,10,'goodscategory','mall/goodscategory/index',NULL,'',1,0,'C','0','0','mall:goodscategory:index','build','admin','2021-01-21 17:47:43','admin','2021-01-21 17:48:30',''),(2035,'商品类目查询',2034,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodscategory:get','#','admin','2021-01-21 17:48:23','',NULL,''),(2036,'新增商品类目',2034,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodscategory:add','#','admin','2021-01-21 17:48:51','',NULL,''),(2037,'修改商品类目',2034,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodscategory:edit','#','admin','2021-01-21 17:49:11','',NULL,''),(2038,'删除商品类目',2034,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodscategory:del','#','admin','2021-01-21 17:49:31','',NULL,''),(2039,'商品管理',2033,10,'goodsspu','mall/goodsspu/index',NULL,'',1,0,'C','0','0','mall:goodsspu:index','shopping','admin','2021-01-25 22:10:44','admin','2021-01-25 22:12:13',''),(2040,'商品查询',2039,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodsspu:get','#','admin','2021-01-25 22:13:08','',NULL,''),(2041,'新增商品',2039,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodsspu:add','#','admin','2021-01-25 22:14:55','',NULL,''),(2042,'修改商品',2039,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodsspu:edit','#','admin','2021-01-25 22:15:14','',NULL,''),(2043,'删除商品',2039,0,'',NULL,NULL,'',1,0,'F','0','0','mall:goodsspu:del','#','admin','2021-01-25 22:15:35','',NULL,''),(2044,'订单管理',2033,10,'orderinfo','mall/orderinfo/index',NULL,'',1,0,'C','0','0','mall:orderinfo:index','list','admin','2021-01-27 00:07:14','admin','2021-01-27 00:07:45',''),(2045,'订单查询',2044,0,'',NULL,NULL,'',1,0,'F','0','0','mall:orderinfo:get','#','admin','2021-01-27 00:08:28','',NULL,''),(2046,'商城订单修改',2044,0,'',NULL,NULL,'',1,0,'F','0','0','mall:orderinfo:edit','#','admin','2021-01-28 22:38:58','',NULL,''),(2047,'商城订单新增',2044,0,'',NULL,NULL,'',1,0,'F','0','0','mall:orderinfo:add','#','admin','2021-01-28 22:39:21','',NULL,''),(2048,'商城订单删除',2044,0,'',NULL,NULL,'',1,0,'F','0','0','mall:orderinfo:del','#','admin','2021-01-28 22:39:41','',NULL,''),(2049,'小程序管理',0,3,'wxma',NULL,NULL,'',1,0,'M','0','0',NULL,'phone','admin','2021-01-28 23:45:03','',NULL,''),(2050,'小程序用户',2049,10,'wxuser-ma','wxma/wxuser/index',NULL,'',1,0,'C','0','0','wxmp:wxuser:index','peoples','admin','2021-01-28 23:54:34','',NULL,''),(2051,'小程序用户查询',2050,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxuser:get','#','admin','2021-01-28 23:57:07','',NULL,''),(2052,'草稿箱',4,44,'wxdraft','wxmp/wxdraft/index',NULL,'',1,0,'C','0','0','	 wxmp:wxdraft:index','guide','admin','2022-03-29 14:48:47','admin','2022-03-29 14:51:31',''),(2053,'新增草稿箱',2052,0,'',NULL,NULL,'',1,0,'F','1','0','wxmp:wxdraft:add','#','admin','2022-03-29 14:50:13','',NULL,''),(2054,'修改草稿箱',2052,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxdraft:edit','#','admin','2022-03-29 14:50:28','',NULL,''),(2055,'删除草稿箱',2052,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxdraft:del','#','admin','2022-03-29 14:50:41','',NULL,''),(2057,'发布草稿',2052,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxdraft:publish','#','admin','2022-03-29 14:51:14','',NULL,''),(2058,'已发布',4,46,'wxfreepublish','wxmp/wxfreepublish/index',NULL,'',1,0,'C','0','0','wxmp:wxfreepublish:index','clipboard','admin','2022-03-29 14:52:44','',NULL,''),(2059,'删除已发布',2058,0,'',NULL,NULL,'',1,0,'F','0','0','wxmp:wxfreepublish:del','#','admin','2022-03-29 14:52:57','',NULL,'');

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';

/*Data for the table `sys_notice` */

insert  into `sys_notice`(`notice_id`,`notice_title`,`notice_type`,`notice_content`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'温馨提醒：2018-07-01 若依新版本发布啦','2','新版本内容','0','admin','2024-09-11 09:31:32','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1','维护内容','0','admin','2024-09-11 09:31:32','',NULL,'管理员');

/*Table structure for table `sys_oper_log` */

DROP TABLE IF EXISTS `sys_oper_log`;

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';

/*Data for the table `sys_oper_log` */

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

insert  into `sys_post`(`post_id`,`post_code`,`post_name`,`post_sort`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'ceo','董事长',1,'0','admin','2024-09-11 09:31:31','',NULL,''),(2,'se','项目经理',2,'0','admin','2024-09-11 09:31:31','',NULL,''),(3,'hr','人力资源',3,'0','admin','2024-09-11 09:31:31','',NULL,''),(4,'user','普通员工',4,'0','admin','2024-09-11 09:31:31','',NULL,'');

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

insert  into `sys_role`(`role_id`,`role_name`,`role_key`,`role_sort`,`data_scope`,`menu_check_strictly`,`dept_check_strictly`,`status`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2024-09-11 09:31:31','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2024-09-11 09:31:31','',NULL,'普通角色');

/*Table structure for table `sys_role_dept` */

DROP TABLE IF EXISTS `sys_role_dept`;

CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';

/*Data for the table `sys_role_dept` */

insert  into `sys_role_dept`(`role_id`,`dept_id`) values (2,100),(2,101),(2,105);

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);

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
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_id`,`dept_id`,`user_name`,`nick_name`,`user_type`,`email`,`phonenumber`,`sex`,`avatar`,`password`,`status`,`del_flag`,`login_ip`,`login_date`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2024-09-11 16:23:15','admin','2024-09-11 09:31:31','','2024-09-11 16:23:14','管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2024-09-11 09:31:31','admin','2024-09-11 09:31:31','',NULL,'测试员');

/*Table structure for table `sys_user_post` */

DROP TABLE IF EXISTS `sys_user_post`;

CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';

/*Data for the table `sys_user_post` */

insert  into `sys_user_post`(`user_id`,`post_id`) values (1,1),(2,2);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values (1,1),(2,2);

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

insert  into `user_address`(`id`,`del_flag`,`create_time`,`update_time`,`user_id`,`user_name`,`postal_code`,`province_name`,`city_name`,`county_name`,`detail_info`,`tel_num`,`is_default`) values ('1354441894547988481','0','2021-01-27 22:51:26','2021-01-27 22:51:26','1352168072700571649','张三',NULL,'广东省','广州市','海珠区','新港中路397号','18602513214','1'),('1354474056307511297','0','2021-01-28 00:59:14','2021-01-28 00:59:14','1354473059078176770','张三',NULL,'广东省','广州市','海珠区','新港中路397号','18563265321','1'),('1355417330850476033','0','2021-01-30 15:27:29','2021-01-30 15:27:29','1355406809988345857','张三',NULL,'北京市','北京市','东城区','大冲地铁口','15580802543','1');

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

insert  into `wx_msg`(`id`,`create_id`,`create_time`,`update_id`,`update_time`,`remark`,`del_flag`,`app_name`,`app_logo`,`wx_user_id`,`nick_name`,`headimg_url`,`type`,`rep_type`,`rep_event`,`rep_content`,`rep_media_id`,`rep_name`,`rep_desc`,`rep_url`,`rep_hq_url`,`content`,`rep_thumb_media_id`,`rep_thumb_url`,`rep_location_x`,`rep_location_y`,`rep_scale`,`read_flag`) values ('1632267995913953281',NULL,'2023-03-05 14:33:11',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','subscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632272703470731266',NULL,'2023-03-05 14:51:53',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','unsubscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632272808005369857',NULL,'2023-03-05 14:52:18',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','subscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632272917996797954',NULL,'2023-03-05 14:52:44',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','unsubscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632272989350297601',NULL,'2023-03-05 14:53:01',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','subscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632273483217068033',NULL,'2023-03-05 14:54:59',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','unsubscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632273535528427521',NULL,'2023-03-05 14:55:12',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','event','subscribe',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632273641581404162',NULL,'2023-03-05 14:55:37',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','text',NULL,'小鲜肉',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632273658178265090',NULL,'2023-03-05 14:55:41',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','text',NULL,'的',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632273702734356482',NULL,'2023-03-05 14:55:52',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','image',NULL,NULL,'eRUJFkjKKJcKu_vvI-uXy6eMNbSS-ftq1a_JB6M0BAAcvHK6VA0UpVc1IG-_csAI',NULL,NULL,'http://mmbiz.qpic.cn/mmbiz_jpg/5X3iagjL72nicRx8gcY0hZT2wepsfvTDxYicF0xY2KkEfNKje4mGbvDXXbOWVs1cukF6GDLcS3aXSwAZTIGuOTyVg/0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0'),('1632878647481229313',NULL,'2023-03-07 06:59:42',NULL,NULL,NULL,'0',NULL,NULL,'1632267995788124162',NULL,NULL,'1','text',NULL,'提交',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0');

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

insert  into `wx_user`(`id`,`create_id`,`create_time`,`update_id`,`update_time`,`remark`,`del_flag`,`app_type`,`subscribe`,`subscribe_scene`,`subscribe_time`,`subscribe_num`,`cancel_subscribe_time`,`open_id`,`nick_name`,`sex`,`city`,`country`,`province`,`phone`,`language`,`headimg_url`,`union_id`,`group_id`,`tagid_list`,`qr_scene_str`,`latitude`,`longitude`,`precision`,`session_key`) values ('1352168072700571649',NULL,'2021-01-21 16:16:05',NULL,'2021-01-21 16:37:22',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5DyEplVd0B5lD9gLwCme8zw','JL','1','深圳','中国','广东',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKRsdzV55M85n8DAsVhH7wrS05ficLFjQMLlZUdUichYqZKKCB2GyibRGJNZ3JvPzVWg5hVVRx9hACEw/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'CNKq11a69WSezik2aobqsA=='),('1352233320682930178',NULL,'2021-01-21 20:35:21',NULL,'2021-01-21 21:16:01',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5HBFdkSYTC4uzf9gvW3cutU','NULL','1','长沙','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/chMqczIChvg1AXBmBran0EzkD4f52jKEpRFmIweBDN1QpeC4JPN5HKE3fgUYFNAFN4warrIQhEj69SCkY2zyYA/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'jSa/lKtJmYPVHZcTl7r5kw=='),('1352572935968165889',NULL,'2021-01-22 19:04:52',NULL,'2021-01-22 19:05:20',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'ol3ea5HWkzS2_iL2nBoao-nsxlgI','Ethan.D','1','益阳','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/5DPIvtrqFPv2hcU09UmW3fGQXzIwmO8iciajsHNTzz1NrlwBeVm5ou8HCaO7kXIDmVwhoqnicIibI4BXf8GlKFN7YA/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'G87A8PJ+HeqJzeVxW/tYpA=='),('1354473059078176770',NULL,'2021-01-28 00:55:16',NULL,'2021-01-28 00:55:23',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55T2ZXs-p68eMcouJR7IFVQw','JL','1','深圳','中国','广东',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJru7MZse3ErXMMsSSQ3rcrBoESJN5F9p7xibr1u54DaGv3NGb6Z9tSTsJ07VsYCBFW7GWkQhIJc2A/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'WlJiMoAA/VZs0wI33kieFw=='),('1355406809988345857',NULL,'2021-01-30 14:45:40',NULL,'2021-01-30 14:50:37',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55eVbz74-EiU2f-j1Rie_BwM','NULL','1','长沙','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/cuTB5LL4dia7CJLqAxV2ibE8OiawFCcF4yRduugIxZTnJBmye7wddrErqShW1JkmXgYibDSKgib2cicURicLaPPknGGjw/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'YM7Jk6qAfQ3yr8jNNbj2ww=='),('1356171782972882945',NULL,'2021-02-01 17:25:25',NULL,'2021-02-02 22:44:21',NULL,'0','1',NULL,NULL,NULL,NULL,NULL,'oJ-q55a_buCs7ozlJOBHYgm6b_ko','Ethan.D','1','益阳','中国','湖南',NULL,'zh_CN','https://thirdwx.qlogo.cn/mmopen/vi_32/XdqjFObB4mmxQMURhIr5XzUgicRic3qOuXhz74OQnmHg4wKf5NUSm11ib0rXBsaIsJxGjicz1AY3a2Pz46iacqibfNqg/132',NULL,NULL,'[]',NULL,NULL,NULL,NULL,'oknUXi+2mvSjisq3vyGrtw=='),('1357673320701546498',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-01-06 14:14:44',1,NULL,'o3QwG1QnY-BOe4M724t0dvVQaUUo','魂散','0','','','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/PiajxSqBRaEK4NgUCJLPziclZYMfTnaYFXvz1GajlxariavaOkbKsXzXMoVHO6E5LKUWaaxxQccLVaicYR2Zqv5ZnA/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320701546499',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-10-29 23:34:34',1,NULL,'o3QwG1YepdGeVJZv_2bfIEjwnb_I','愈辉','1','','亚美尼亚','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OWXHibcSriareU9XpBCdBgkOd286EialAX0BtrWEdrhunepPEUq82E6wneLbtNttjKDMJSM7Y9HOnaRA/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320714129409',NULL,'2021-02-05 20:51:57',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-11-09 11:36:47',1,NULL,'o3QwG1ThD7gJ-qIXTDF88rly1VHg','八爪鱼','1','','中国','北京',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OW2BkZicF01PtUQera34FdW1Ga68DhZxQ7MlGMLDG3DZIBMm2Cibjueb6NMDvRMMRZFqjMVEogD9Oibw/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320722518017',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-12-05 16:04:40',1,NULL,'o3QwG1ZP0s_alsf-PuhDU7CmLQ24','十万伏特','1','成都','中国','四川',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/J6c32680OdZUtzqT9zvNO2QR8jG1jdPiaFFQVA91Szrnpke0ga7UCCXGTKqZIppyibuhv6NTRX3OXqPtSQey8Ww0qgzhqicUgGR/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320730906626',NULL,'2021-02-05 20:51:58',NULL,NULL,'备注','0','2','1','ADD_SCENE_QR_CODE','2021-01-23 11:54:59',1,NULL,'o3QwG1Z4EZBdLwtKbK9TozDunLZw','Allen','1','成都','中国','四川',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/OT05QvwvgZZ3KeIaQ25CrjHF9rQTpZhO4RM1szUEcUdfLjEcFoicD3snicPq8GIqiayc1Ned8CIY5Gk5kCInF4TrR07Isicn4gFS/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320739295234',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2021-01-18 14:27:13',1,NULL,'o3QwG1UuAz7VYM24e9rmihxyKJvg','JL','1','深圳','中国','广东',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/dMKNvxZfIaEco8NogUXngnPhXrEEzLoY69XP5ymS2RWFIyXpOGE8trxiaqydnIibicluloYMWO06qmmibuvZR6GEbYR1HmVCq41R/132',NULL,'{}','[107,2]','1',NULL,NULL,NULL,NULL),('1357673320747683841',NULL,'2021-02-05 20:51:58',NULL,NULL,'99','0','2','1','ADD_SCENE_QR_CODE','2021-01-11 17:43:37',1,NULL,'o3QwG1XWOtVl_ifcXYbPuiaPPnMc','redis','1','','中国','',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/lV0d907m3OU18kicFJhIBibV0XlvEnWzKN09tvVz3wyryA2cysGibW8BarSLyia8HeuOx8YDibGE192BibXG1xTtfC2nXf0x3MZS1x/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320747683842',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-03-16 10:32:31',1,NULL,'o3QwG1ecy727RcaP3XyevHbPK33M','、','1','厦门','中国','福建',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/OT05QvwvgZYuck1R4BqYzwFzicuAicDHSeJTKI21VvxgrUxEWnVxiaEseEVLnM2tzibxTIfUiaZ1aSLn4hJ8FSgu7EBgeID2LCh9s/132',NULL,'{}','[2]','1',NULL,NULL,NULL,NULL),('1357673320747683843',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-11-09 20:41:24',1,NULL,'o3QwG1RLqJDTP-KZfNxMrMOKpl1U','gameover!!!','1','武汉','中国','湖北',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/upjJ1bex0ocf0rsbPbSW6yorFpT2SicGibyia5bYRjqLpWDgnYR4icEtQ87TcDibO3qujm8wkhDib4CPQCldShq1FHovW9J2ibSsfFH/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320747683844',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-10-14 14:32:49',1,NULL,'o3QwG1f7sT5V_FV_EVj4kaQ09Zzs','壹杯淸茶。','1','青岛','中国','山东',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ajNVdqHZLLCYmmGPrvvXcib0iaiaGQba4yFtwt35zEUgOAzGwPcwG2GIqmejmo8fxRibwQzSDibejrXV4dia1uiaanvXrZ3SKZyZiaEo3G2K8WhDTjs/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320756072449',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-02-24 16:49:28',1,NULL,'o3QwG1eaqyTxxW4VisfbaKL0BcWY','.Llkoi','1','长沙','中国','湖南',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/TBfgdHR2VFWloL25J3r1DfDE3a5R3yctJD3wc5CSoe3xWmy4lZPzxRZpj2x14dl87ndzlRXAN1ZN2W7w1n8bYtKWOMxG8ahq/132',NULL,'{}','[2]','1',NULL,NULL,NULL,NULL),('1357673320760266753',NULL,'2021-02-05 20:51:58',NULL,NULL,'','0','2','1','ADD_SCENE_QR_CODE','2020-06-17 22:14:41',1,NULL,'o3QwG1d4Bq8lg-NbUOOYdaaVWhnE','Quentin','1','南京','中国','江苏',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ceebSkrkkFTBe1cSDicrLGq05uMsfRkzNWhKp3JY6eISxwCoiagt6q2L4RGcGh61jnWWTI3xeXsAmFCEpozdSIDQKBhNosic8TY/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1357673320760266754',NULL,'2021-02-05 20:51:58',NULL,NULL,'后来','0','2','1','ADD_SCENE_QR_CODE','2019-06-04 22:22:21',1,NULL,'o3QwG1aKxN5AMEaNSbDV-vHJHtvM','安安晨晨','2','益阳','中国','湖南',NULL,'zh_CN','http://thirdwx.qlogo.cn/mmopen/ceebSkrkkFTRWgtVgYzPOETJtkqz0TIOzpVber8ic5DlUTky6zpgTGJHic6gG4wH7B7iay12QHo7BF3Iv0r6vTfS2GkcdywCmN8/132',NULL,'{}','[]','1',NULL,NULL,NULL,NULL),('1632267995788124162',NULL,'2023-03-05 14:33:10',NULL,'2023-03-05 14:51:53','','0','2','1','ADD_SCENE_QR_CODE','2023-03-05 14:55:09',4,'2023-03-05 14:54:59','obe_Pt4Ot_pap7RZwkVAfqBDoAQM',NULL,NULL,NULL,NULL,NULL,NULL,'zh_CN',NULL,NULL,'{}','[]','jl-wiki:1677999301202',NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
