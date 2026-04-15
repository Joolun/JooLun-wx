/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

DROP TABLE IF EXISTS `mall_home_template_item`;
DROP TABLE IF EXISTS `mall_home_template`;

CREATE TABLE `mall_home_template` (
  `id` varchar(32) NOT NULL COMMENT '模板ID',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `current_flag` char(1) NOT NULL DEFAULT '1' COMMENT '是否当前模板（1是 0否）',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '模板状态（1启用 0停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0显示 1隐藏）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mall_home_template_code` (`template_code`),
  KEY `idx_mall_home_template_current` (`current_flag`),
  KEY `idx_mall_home_template_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城首页装修模板表';

CREATE TABLE `mall_home_template_item` (
  `id` varchar(32) NOT NULL COMMENT '组件ID',
  `template_id` varchar(32) NOT NULL COMMENT '模板ID',
  `item_type` varchar(32) NOT NULL COMMENT '组件类型',
  `item_name` varchar(100) NOT NULL COMMENT '组件名称',
  `item_sort` int NOT NULL DEFAULT '0' COMMENT '组件排序',
  `item_status` char(1) NOT NULL DEFAULT '1' COMMENT '组件状态（1启用 0停用）',
  `content_json` text COMMENT '组件配置JSON',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记（0显示 1隐藏）',
  PRIMARY KEY (`id`),
  KEY `idx_mall_home_template_item_template` (`template_id`),
  KEY `idx_mall_home_template_item_type` (`item_type`),
  KEY `idx_mall_home_template_item_status` (`item_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城首页装修组件表';
