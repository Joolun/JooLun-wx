/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城订单与数据看板性能优化脚本
-- 目标：
-- 1. 为订单列表分页、订单汇总、售后统计补齐高频索引
-- 2. 为数据看板的趋势、今日成交、会员增长统计补齐时间索引
-- 3. 避免重复执行时因索引已存在导致脚本中断

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
	'idx_mall_order_info_order_no',
	'ALTER TABLE `mall_order_info` ADD INDEX `idx_mall_order_info_order_no` (`order_no`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_info',
	'idx_mall_order_info_user_create_time',
	'ALTER TABLE `mall_order_info` ADD INDEX `idx_mall_order_info_user_create_time` (`user_id`, `create_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_info',
	'idx_mall_order_info_status_is_pay_create_time',
	'ALTER TABLE `mall_order_info` ADD INDEX `idx_mall_order_info_status_is_pay_create_time` (`status`, `is_pay`, `create_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_info',
	'idx_mall_order_info_payment_time',
	'ALTER TABLE `mall_order_info` ADD INDEX `idx_mall_order_info_payment_time` (`payment_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_item',
	'idx_mall_order_item_order_id_create_time',
	'ALTER TABLE `mall_order_item` ADD INDEX `idx_mall_order_item_order_id_create_time` (`order_id`, `create_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_item',
	'idx_mall_order_item_order_id_status_refund',
	'ALTER TABLE `mall_order_item` ADD INDEX `idx_mall_order_item_order_id_status_refund` (`order_id`, `status`, `is_refund`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_item',
	'idx_mall_order_item_refund_success_time',
	'ALTER TABLE `mall_order_item` ADD INDEX `idx_mall_order_item_refund_success_time` (`refund_success_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_order_item',
	'idx_mall_order_item_refund_apply_time',
	'ALTER TABLE `mall_order_item` ADD INDEX `idx_mall_order_item_refund_apply_time` (`refund_apply_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_user',
	'idx_mall_user_register_time',
	'ALTER TABLE `mall_user` ADD INDEX `idx_mall_user_register_time` (`register_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_user',
	'idx_mall_user_member_time',
	'ALTER TABLE `mall_user` ADD INDEX `idx_mall_user_member_time` (`member_time`) USING BTREE'
);

CALL `mall_add_index_if_not_exists`(
	'mall_user',
	'idx_mall_user_last_login_time',
	'ALTER TABLE `mall_user` ADD INDEX `idx_mall_user_last_login_time` (`last_login_time`) USING BTREE'
);

DROP PROCEDURE IF EXISTS `mall_add_index_if_not_exists`;
