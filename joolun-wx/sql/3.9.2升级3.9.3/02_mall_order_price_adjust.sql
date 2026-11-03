/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.2 升级 3.9.3：订单改价。
-- 支持用户下单未支付时，后台调整订单应付金额；改价差额按订单项金额占比分摊回明细。

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

-- 累计改价金额：正数为加价，负数为减价。
CALL `mall_add_column_if_not_exists`('mall_order_info', 'adjust_amount',
  'ALTER TABLE `mall_order_info` ADD COLUMN `adjust_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''累计改价金额，正数加价、负数减价'' AFTER `member_discount_amount`');

-- 最近一次改价说明，便于客服追溯改价原因。
CALL `mall_add_column_if_not_exists`('mall_order_info', 'adjust_remark',
  'ALTER TABLE `mall_order_info` ADD COLUMN `adjust_remark` varchar(255) DEFAULT NULL COMMENT ''最近一次改价说明'' AFTER `adjust_amount`');

-- 最近一次改价时间。
CALL `mall_add_column_if_not_exists`('mall_order_info', 'adjust_time',
  'ALTER TABLE `mall_order_info` ADD COLUMN `adjust_time` datetime DEFAULT NULL COMMENT ''最近一次改价时间'' AFTER `adjust_remark`');

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;
