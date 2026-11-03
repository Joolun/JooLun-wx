/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.2 升级 3.9.3：订单支付单号。
-- 微信要求同一商户订单号的下单金额保持一致，改价后需要换新的支付单号重新下单。

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

-- 当前支付单号：为空表示还未发起支付，直接使用 order_no；
-- 改价后写入新的支付单号，下单与回调都使用该字段。
CALL `mall_add_column_if_not_exists`('mall_order_info', 'pay_trade_no',
  'ALTER TABLE `mall_order_info` ADD COLUMN `pay_trade_no` varchar(64) DEFAULT NULL COMMENT ''当前支付单号，改价后重新生成'' AFTER `transaction_id`');

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;
