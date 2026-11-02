/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.2 升级 3.9.3：商城运费模块。
-- 1. 新增运费模板表 mall_freight_templat（买家承担运费 / 卖家包邮，按件数 / 按重量 / 按体积计费）。
-- 2. 商品 SPU 绑定运费模板，SKU 增加重量、体积作为计费依据。
-- 3. 订单增加配送方式（快递 / 自提），订单明细记录运费模板快照。
-- 4. 存量商品统一回填到内置的"默认包邮"模板，升级后运费行为与升级前保持一致（均为 0 元）。

-- ---------------------------------------------------------------------------
-- 一、运费模板表
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `mall_freight_templat` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `type` char(1) NOT NULL DEFAULT '1' COMMENT '模板类型1买家承担运费、2卖家包邮',
  `charge_type` char(1) NOT NULL DEFAULT '1' COMMENT '计费方式1按件数、2按重量、3按体积',
  `first_num` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '首件、首重量(kg)、首体积(m³)',
  `first_freight` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '首运费',
  `continue_num` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '续件、续重量(kg)、续体积(m³)',
  `continue_freight` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '续运费',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态0停用、1启用',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记0显示、1隐藏',
  PRIMARY KEY (`id`),
  KEY `idx_mall_freight_templat_status_sort` (`status`,`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运费模板';

-- ---------------------------------------------------------------------------
-- 二、内置"默认包邮"模板
-- 用途：1) 存量商品回填，保证升级后不涨价；2) 新建商品的数据库兜底值。
-- 该模板在后台"商城管理 - 运费模板"中可见，但不应删除（被商品引用时删除会被拦截）。
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO `mall_freight_templat`
(`id`,`name`,`type`,`charge_type`,`first_num`,`first_freight`,`continue_num`,`continue_freight`,`sort`,`status`,`remark`,`create_time`,`update_time`,`del_flag`) VALUES
('freight_default_free','默认包邮','2','1','0.00','0.00','0.00','0.00',0,'1','系统内置模板，存量商品升级后默认绑定，保证升级不涨价','2026-11-02 00:00:00','2026-11-02 00:00:00','0');

-- ---------------------------------------------------------------------------
-- 三、字段升级（幂等）
-- ---------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;
DELIMITER $$
CREATE PROCEDURE `mall_add_column_if_not_exists`(
  IN in_table_name VARCHAR(128), IN in_column_name VARCHAR(128), IN in_column_sql TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = in_table_name AND COLUMN_NAME = in_column_name
  ) THEN
    SET @ddl_sql = in_column_sql;
    PREPARE stmt FROM @ddl_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$
DELIMITER ;

-- 商品绑定运费模板：商品维度的必填项。
-- 带默认值的目的有两个：ALTER TABLE 时存量行自动回填默认包邮模板；脚本或历史数据漏传时不会直接插入失败。
CALL `mall_add_column_if_not_exists`('mall_goods_spu', 'freight_template_id',
  'ALTER TABLE `mall_goods_spu` ADD COLUMN `freight_template_id` varchar(32) NOT NULL DEFAULT ''freight_default_free'' COMMENT ''运费模板ID'' AFTER `spec_type`');

-- SKU 计费依据
CALL `mall_add_column_if_not_exists`('mall_goods_sku', 'weight',
  'ALTER TABLE `mall_goods_sku` ADD COLUMN `weight` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''重量(kg)'' AFTER `stock`');
CALL `mall_add_column_if_not_exists`('mall_goods_sku', 'volume',
  'ALTER TABLE `mall_goods_sku` ADD COLUMN `volume` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''体积(m³)'' AFTER `weight`');

-- 订单配送方式：自提不计运费，必须落库，否则事后无法解释运费为 0 的原因。
CALL `mall_add_column_if_not_exists`('mall_order_info', 'delivery_way',
  'ALTER TABLE `mall_order_info` ADD COLUMN `delivery_way` char(2) NOT NULL DEFAULT ''1'' COMMENT ''配送方式1普通快递、2上门自提'' AFTER `payment_way`');

-- 订单明细的运费模板快照，便于对账和客诉追溯。
CALL `mall_add_column_if_not_exists`('mall_order_item', 'freight_template_id',
  'ALTER TABLE `mall_order_item` ADD COLUMN `freight_template_id` varchar(32) DEFAULT NULL COMMENT ''运费模板ID快照'' AFTER `freight_price`');

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;

-- 兜底回填：字段此前若以可空方式存在过，或商品数据曾写入空值，这里统一收敛到默认包邮模板。
UPDATE `mall_goods_spu` SET `freight_template_id` = 'freight_default_free'
 WHERE `freight_template_id` IS NULL OR `freight_template_id` = '';

-- ---------------------------------------------------------------------------
-- 四、后台菜单与权限
-- ---------------------------------------------------------------------------
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2120, '运费模板', 2033, 90, 'freighttemplat', 'mall/freighttemplat/index', NULL, 'MallFreightTemplat', 1, 0, 'C', '0', '0', 'mall:freighttemplat:index', 'money', 'admin', NOW(), '', NULL, '商城运费模板'
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2120);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2121, '运费模板查询', 2120, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:freighttemplat:get', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2121);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2122, '运费模板新增', 2120, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:freighttemplat:add', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2122);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2123, '运费模板修改', 2120, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:freighttemplat:edit', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2123);

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
SELECT 2124, '运费模板删除', 2120, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'mall:freighttemplat:del', '#', 'admin', NOW(), '', NULL, ''
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_id` = 2124);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2120 WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2120);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2121 WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2121);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2122 WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2122);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2123 WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2123);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 2, 2124 WHERE NOT EXISTS (SELECT 1 FROM `sys_role_menu` WHERE `role_id` = 2 AND `menu_id` = 2124);
