/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城模块表名前缀升级脚本
-- 用途：
-- 1. 将商城模块旧表名统一升级为 mall_ 前缀
-- 2. 兼容“已做多规格改造”与“未做多规格改造”两种数据库状态
-- 3. 仅在“旧表存在且新表不存在”时执行重命名，避免重复执行报错

DROP PROCEDURE IF EXISTS mall_rename_table_if_needed;

DELIMITER $$

CREATE PROCEDURE mall_rename_table_if_needed(
    IN old_table_name VARCHAR(128),
    IN new_table_name VARCHAR(128)
)
BEGIN
    DECLARE old_count INT DEFAULT 0;
    DECLARE new_count INT DEFAULT 0;

    SELECT COUNT(1)
    INTO old_count
    FROM information_schema.TABLES
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = old_table_name;

    SELECT COUNT(1)
    INTO new_count
    FROM information_schema.TABLES
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = new_table_name;

    IF old_count > 0 AND new_count = 0 THEN
        SET @rename_sql = CONCAT(
            'RENAME TABLE `',
            REPLACE(old_table_name, '`', '``'),
            '` TO `',
            REPLACE(new_table_name, '`', '``'),
            '`'
        );
        PREPARE stmt FROM @rename_sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

-- 基础商城表
CALL mall_rename_table_if_needed('goods_category', 'mall_goods_category');
CALL mall_rename_table_if_needed('goods_spu', 'mall_goods_spu');
CALL mall_rename_table_if_needed('shopping_cart', 'mall_shopping_cart');
CALL mall_rename_table_if_needed('order_info', 'mall_order_info');
CALL mall_rename_table_if_needed('order_item', 'mall_order_item');
CALL mall_rename_table_if_needed('order_logistics', 'mall_order_logistics');
CALL mall_rename_table_if_needed('user_address', 'mall_user_address');

-- 多规格相关表
CALL mall_rename_table_if_needed('goods_sku', 'mall_goods_sku');
CALL mall_rename_table_if_needed('goods_spec', 'mall_goods_spec');
CALL mall_rename_table_if_needed('goods_spec_value', 'mall_goods_spec_value');
CALL mall_rename_table_if_needed('goods_spu_spec', 'mall_goods_spu_spec');
CALL mall_rename_table_if_needed('goods_sku_spec_value', 'mall_goods_sku_spec_value');

DROP PROCEDURE IF EXISTS mall_rename_table_if_needed;
