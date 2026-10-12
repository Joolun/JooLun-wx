/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.1 升级 3.9.2：商城订单与微信小程序发货信息管理关联。
-- 保存微信同步状态、最后同步时间、失败原因及尝试次数，支持同步中心筛选和失败重试。

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;

DELIMITER $$
CREATE PROCEDURE `mall_add_column_if_not_exists`(
  IN in_table_name VARCHAR(128),
  IN in_column_name VARCHAR(128),
  IN in_column_sql TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = in_table_name
      AND COLUMN_NAME = in_column_name
  ) THEN
    SET @ddl_sql = in_column_sql;
    PREPARE stmt FROM @ddl_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$
DELIMITER ;

CALL `mall_add_column_if_not_exists`(
  'mall_order_info',
  'wx_shipping_status',
  'ALTER TABLE `mall_order_info` ADD COLUMN `wx_shipping_status` char(1) NOT NULL DEFAULT ''0'' COMMENT ''微信发货同步状态：0未同步、1成功、2失败、3跳过'' AFTER `transaction_id`'
);

CALL `mall_add_column_if_not_exists`(
  'mall_order_info',
  'wx_shipping_sync_time',
  'ALTER TABLE `mall_order_info` ADD COLUMN `wx_shipping_sync_time` datetime DEFAULT NULL COMMENT ''微信发货最后同步时间'' AFTER `wx_shipping_status`'
);

CALL `mall_add_column_if_not_exists`(
  'mall_order_info',
  'wx_shipping_error',
  'ALTER TABLE `mall_order_info` ADD COLUMN `wx_shipping_error` varchar(1000) DEFAULT NULL COMMENT ''微信发货最后失败或跳过原因'' AFTER `wx_shipping_sync_time`'
);

CALL `mall_add_column_if_not_exists`(
  'mall_order_info',
  'wx_shipping_retry_count',
  'ALTER TABLE `mall_order_info` ADD COLUMN `wx_shipping_retry_count` int NOT NULL DEFAULT 0 COMMENT ''微信发货同步尝试次数'' AFTER `wx_shipping_error`'
);

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;

DROP PROCEDURE IF EXISTS `mall_add_index_if_not_exists`;

DELIMITER $$
CREATE PROCEDURE `mall_add_index_if_not_exists`(
  IN in_table_name VARCHAR(128),
  IN in_index_name VARCHAR(128),
  IN in_index_sql TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = in_table_name
      AND INDEX_NAME = in_index_name
  ) THEN
    SET @ddl_sql = in_index_sql;
    PREPARE stmt FROM @ddl_sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$
DELIMITER ;

CALL `mall_add_index_if_not_exists`(
  'mall_order_info',
  'idx_mall_order_info_wx_shipping_status',
  'ALTER TABLE `mall_order_info` ADD INDEX `idx_mall_order_info_wx_shipping_status` (`wx_shipping_status`, `delivery_time`) USING BTREE'
);

DROP PROCEDURE IF EXISTS `mall_add_index_if_not_exists`;
