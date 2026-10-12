/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.1 升级 3.9.2：优惠券、会员等级积分、商品评价 MVP。
-- 优惠券仅支持全场满减；积分仅累计展示；评价仅支持星级、文字、匿名和商家回复。

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

CALL `mall_add_column_if_not_exists`('mall_user', 'level_id',
  'ALTER TABLE `mall_user` ADD COLUMN `level_id` varchar(32) DEFAULT NULL COMMENT ''会员等级ID'' AFTER `member_time`');
CALL `mall_add_column_if_not_exists`('mall_user', 'growth_value',
  'ALTER TABLE `mall_user` ADD COLUMN `growth_value` int NOT NULL DEFAULT 0 COMMENT ''成长值'' AFTER `level_id`');
CALL `mall_add_column_if_not_exists`('mall_user', 'points',
  'ALTER TABLE `mall_user` ADD COLUMN `points` int NOT NULL DEFAULT 0 COMMENT ''会员积分'' AFTER `growth_value`');
CALL `mall_add_column_if_not_exists`('mall_order_info', 'coupon_user_id',
  'ALTER TABLE `mall_order_info` ADD COLUMN `coupon_user_id` varchar(32) DEFAULT NULL COMMENT ''用户优惠券ID'' AFTER `payment_price`');
CALL `mall_add_column_if_not_exists`('mall_order_info', 'coupon_discount_amount',
  'ALTER TABLE `mall_order_info` ADD COLUMN `coupon_discount_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''优惠券减免金额'' AFTER `coupon_user_id`');
CALL `mall_add_column_if_not_exists`('mall_order_info', 'member_discount_amount',
  'ALTER TABLE `mall_order_info` ADD COLUMN `member_discount_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''会员减免金额'' AFTER `coupon_discount_amount`');
CALL `mall_add_column_if_not_exists`('mall_order_item', 'member_discount_amount',
  'ALTER TABLE `mall_order_item` ADD COLUMN `member_discount_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''会员优惠分摊'' AFTER `payment_price`');
CALL `mall_add_column_if_not_exists`('mall_order_item', 'coupon_discount_amount',
  'ALTER TABLE `mall_order_item` ADD COLUMN `coupon_discount_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT ''优惠券优惠分摊'' AFTER `member_discount_amount`');
CALL `mall_add_column_if_not_exists`('mall_order_item', 'review_status',
  'ALTER TABLE `mall_order_item` ADD COLUMN `review_status` char(1) NOT NULL DEFAULT ''0'' COMMENT ''评价状态：0待评价、1已评价'' AFTER `coupon_discount_amount`');

DROP PROCEDURE IF EXISTS `mall_add_column_if_not_exists`;

CREATE TABLE IF NOT EXISTS `mall_coupon` (
  `id` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `threshold_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '使用门槛',
  `discount_amount` decimal(10,2) NOT NULL COMMENT '减免金额',
  `total_quantity` int NOT NULL DEFAULT 0 COMMENT '发行量，0不限量',
  `received_quantity` int NOT NULL DEFAULT 0 COMMENT '领取量',
  `used_quantity` int NOT NULL DEFAULT 0 COMMENT '使用量',
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '0停用、1启用',
  `description` varchar(500) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0',
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_mall_coupon_status_time` (`status`,`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板';

CREATE TABLE IF NOT EXISTS `mall_coupon_user` (
  `id` varchar(32) NOT NULL,
  `coupon_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0未使用、1锁定、2已使用',
  `receive_time` datetime NOT NULL,
  `use_time` datetime DEFAULT NULL,
  `order_id` varchar(32) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0',
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mall_coupon_user_once` (`coupon_id`,`user_id`),
  KEY `idx_mall_coupon_user_user_status` (`user_id`,`status`),
  KEY `idx_mall_coupon_user_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券';

CREATE TABLE IF NOT EXISTS `mall_member_level` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL,
  `level_value` int NOT NULL,
  `min_growth` int NOT NULL DEFAULT 0,
  `discount_rate` decimal(5,2) NOT NULL DEFAULT 100.00 COMMENT '100无折扣、95九五折',
  `status` char(1) NOT NULL DEFAULT '1',
  `color` varchar(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mall_member_level_value` (`level_value`),
  UNIQUE KEY `uk_mall_member_level_growth` (`min_growth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级';

CREATE TABLE IF NOT EXISTS `mall_member_point_log` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `change_value` int NOT NULL,
  `balance` int NOT NULL,
  `source_type` varchar(20) NOT NULL,
  `source_id` varchar(32) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_mall_point_user_time` (`user_id`,`create_time`),
  UNIQUE KEY `uk_mall_point_source` (`source_type`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员积分流水';

CREATE TABLE IF NOT EXISTS `mall_goods_review` (
  `id` varchar(32) NOT NULL,
  `order_id` varchar(32) NOT NULL,
  `order_item_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `spu_id` varchar(32) NOT NULL,
  `sku_id` varchar(32) DEFAULT NULL,
  `spu_name` varchar(200) DEFAULT NULL,
  `spec_info` varchar(255) DEFAULT NULL,
  `nick_name` varchar(100) DEFAULT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `rating` tinyint NOT NULL,
  `content` varchar(500) DEFAULT NULL,
  `anonymous_flag` char(1) NOT NULL DEFAULT '0',
  `status` char(1) NOT NULL DEFAULT '1',
  `reply_content` varchar(500) DEFAULT NULL,
  `reply_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mall_goods_review_item` (`order_item_id`),
  KEY `idx_mall_goods_review_spu_status_time` (`spu_id`,`status`,`create_time`),
  CONSTRAINT `ck_mall_goods_review_rating` CHECK (`rating` BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价';

INSERT IGNORE INTO `mall_member_level`
(`id`,`name`,`level_value`,`min_growth`,`discount_rate`,`status`,`color`,`remark`,`del_flag`) VALUES
('member_level_1','普通会员',1,0,100.00,'1','#909399','默认等级','0'),
('member_level_2','银卡会员',2,1000,98.00,'1','#8a9baa','成长值达到1000','0'),
('member_level_3','金卡会员',3,5000,95.00,'1','#c99a2e','成长值达到5000','0');

INSERT IGNORE INTO `sys_menu`
(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`route_name`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`remark`) VALUES
(2110,'优惠券',2033,60,'coupon','mall/coupon/index',NULL,'MallCoupon',1,0,'C','0','0','mall:coupon:index','money','admin',NOW(),'商城优惠券'),
(2111,'优惠券新增',2110,1,'',NULL,NULL,'',1,0,'F','0','0','mall:coupon:add','#','admin',NOW(),''),
(2112,'优惠券修改',2110,2,'',NULL,NULL,'',1,0,'F','0','0','mall:coupon:edit','#','admin',NOW(),''),
(2113,'会员等级',2033,70,'memberlevel','mall/memberlevel/index',NULL,'MallMemberLevel',1,0,'C','0','0','mall:memberlevel:index','peoples','admin',NOW(),'商城会员等级'),
(2114,'会员等级新增',2113,1,'',NULL,NULL,'',1,0,'F','0','0','mall:memberlevel:add','#','admin',NOW(),''),
(2115,'会员等级修改',2113,2,'',NULL,NULL,'',1,0,'F','0','0','mall:memberlevel:edit','#','admin',NOW(),''),
(2116,'商品评价',2033,80,'goodsreview','mall/goodsreview/index',NULL,'MallGoodsReview',1,0,'C','0','0','mall:goodsreview:index','edit','admin',NOW(),'商品评价'),
(2117,'评价回复',2116,1,'',NULL,NULL,'',1,0,'F','0','0','mall:goodsreview:edit','#','admin',NOW(),'');
