/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城用户体系第二阶段升级脚本
-- 目标：
-- 1. 重建 mall_user，改为独立主键。
-- 2. 将 mall_order_info、mall_shopping_cart、mall_user_address 的 user_id 从 wx_user.id 迁移为 mall_user.id。
-- 3. 回填商城用户基础统计字段，保证后台用户列表、订单列表语义一致。

DROP TABLE IF EXISTS `mall_user`;

CREATE TABLE `mall_user` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商城用户ID',
  `user_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商城会员编号',
  `wx_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '绑定的微信用户ID',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信开放ID快照',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信全平台唯一ID',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商城昵称',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `member_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '会员标记（0未入会 1已入会）',
  `member_time` datetime DEFAULT NULL COMMENT '入会时间',
  `user_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运营标签',
  `sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '性别（0未知 1男 2女）',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `country` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '国家',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '城市',
  `register_source` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '注册来源（1小程序 2公众号 3H5 4APP 9后台）',
  `register_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '注册IP',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `order_count` int NOT NULL DEFAULT '0' COMMENT '累计下单次数',
  `consume_count` int NOT NULL DEFAULT '0' COMMENT '累计支付订单数',
  `refund_count` int NOT NULL DEFAULT '0' COMMENT '累计退款单数',
  `consume_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计消费金额',
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1禁用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0显示 1隐藏）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_mall_user_user_no` (`user_no`) USING BTREE,
  UNIQUE KEY `uk_mall_user_wx_user_id` (`wx_user_id`) USING BTREE,
  KEY `idx_mall_user_mobile` (`mobile`) USING BTREE,
  KEY `idx_mall_user_member_flag` (`member_flag`) USING BTREE,
  KEY `idx_mall_user_user_tag` (`user_tag`) USING BTREE,
  KEY `idx_mall_user_union_id` (`union_id`) USING BTREE,
  KEY `idx_mall_user_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商城用户表';

INSERT INTO `mall_user` (
  `id`,
  `user_no`,
  `wx_user_id`,
  `open_id`,
  `union_id`,
  `nick_name`,
  `avatar_url`,
  `mobile`,
  `member_flag`,
  `member_time`,
  `sex`,
  `country`,
  `province`,
  `city`,
  `register_source`,
  `register_time`,
  `last_login_time`,
  `status`,
  `remark`,
  `create_time`,
  `update_time`,
  `del_flag`
)
SELECT
  LOWER(REPLACE(UUID(), '-', '')) AS `id`,
  CONCAT('M', UPPER(LEFT(REPLACE(UUID(), '-', ''), 15))) AS `user_no`,
  w.`id` AS `wx_user_id`,
  w.`open_id`,
  w.`union_id`,
  IFNULL(NULLIF(w.`nick_name`, ''), '微信用户') AS `nick_name`,
  w.`headimg_url` AS `avatar_url`,
  w.`phone` AS `mobile`,
  CASE
    WHEN w.`phone` IS NOT NULL AND w.`phone` <> '' THEN '1'
    ELSE '0'
  END AS `member_flag`,
  CASE
    WHEN w.`phone` IS NOT NULL AND w.`phone` <> '' THEN COALESCE(w.`update_time`, w.`create_time`, NOW())
    ELSE NULL
  END AS `member_time`,
  CASE
    WHEN w.`sex` IN ('1', '2') THEN w.`sex`
    ELSE '0'
  END AS `sex`,
  w.`country`,
  w.`province`,
  w.`city`,
  CASE
    WHEN w.`app_type` = '1' THEN '1'
    WHEN w.`app_type` = '2' THEN '2'
    ELSE '9'
  END AS `register_source`,
  w.`create_time` AS `register_time`,
  w.`update_time` AS `last_login_time`,
  '0' AS `status`,
  '由 wx_user 初始化生成' AS `remark`,
  IFNULL(w.`create_time`, NOW()) AS `create_time`,
  IFNULL(w.`update_time`, NOW()) AS `update_time`,
  IFNULL(w.`del_flag`, '0') AS `del_flag`
FROM `wx_user` w;

ALTER TABLE `mall_order_info`
  MODIFY COLUMN `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商城用户ID';

ALTER TABLE `mall_shopping_cart`
  MODIFY COLUMN `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商城用户ID';

ALTER TABLE `mall_user_address`
  MODIFY COLUMN `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商城用户ID';

UPDATE `mall_order_info` oi
INNER JOIN `mall_user` mu ON mu.`wx_user_id` = oi.`user_id`
SET oi.`user_id` = mu.`id`;

UPDATE `mall_shopping_cart` sc
INNER JOIN `mall_user` mu ON mu.`wx_user_id` = sc.`user_id`
SET sc.`user_id` = mu.`id`;

UPDATE `mall_user_address` ua
INNER JOIN `mall_user` mu ON mu.`wx_user_id` = ua.`user_id`
SET ua.`user_id` = mu.`id`;

UPDATE `mall_user` mu
LEFT JOIN (
  SELECT
    oi.`user_id`,
    COUNT(1) AS `order_count`,
    SUM(CASE WHEN oi.`is_pay` = '1' THEN 1 ELSE 0 END) AS `consume_count`,
    SUM(CASE WHEN oi.`is_pay` = '1' THEN oi.`payment_price` ELSE 0 END) AS `consume_amount`
  FROM `mall_order_info` oi
  WHERE oi.`del_flag` = '0'
  GROUP BY oi.`user_id`
) order_stat ON order_stat.`user_id` = mu.`id`
LEFT JOIN (
  SELECT
    oi.`user_id`,
    COUNT(1) AS `refund_count`
  FROM `mall_order_info` oi
  INNER JOIN `mall_order_item` item ON item.`order_id` = oi.`id`
  WHERE oi.`del_flag` = '0'
    AND item.`is_refund` = '1'
  GROUP BY oi.`user_id`
) refund_stat ON refund_stat.`user_id` = mu.`id`
SET mu.`order_count` = IFNULL(order_stat.`order_count`, 0),
    mu.`consume_count` = IFNULL(order_stat.`consume_count`, 0),
    mu.`consume_amount` = IFNULL(order_stat.`consume_amount`, 0.00),
    mu.`refund_count` = IFNULL(refund_stat.`refund_count`, 0),
    mu.`update_time` = CURRENT_TIMESTAMP;

DROP TABLE IF EXISTS `mall_user_operate_log`;

CREATE TABLE `mall_user_operate_log` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商城用户ID',
  `operate_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `operate_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作标题',
  `operate_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `operator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作人账号',
  `extra_info` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0显示 1隐藏）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_mall_user_operate_log_user_id` (`user_id`) USING BTREE,
  KEY `idx_mall_user_operate_log_type` (`operate_type`) USING BTREE,
  KEY `idx_mall_user_operate_log_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商城用户运营记录表';

DROP TABLE IF EXISTS `mall_user_tag`;

CREATE TABLE `mall_user_tag` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0启用 1停用）',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序值',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0显示 1隐藏）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_mall_user_tag_tag_name` (`tag_name`) USING BTREE,
  KEY `idx_mall_user_tag_status` (`status`) USING BTREE,
  KEY `idx_mall_user_tag_sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商城用户标签库表';
