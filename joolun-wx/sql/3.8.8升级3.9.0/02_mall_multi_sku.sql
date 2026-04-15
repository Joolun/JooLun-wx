DROP TABLE IF EXISTS `mall_goods_sku_spec_value`;
DROP TABLE IF EXISTS `mall_goods_spu_spec`;
DROP TABLE IF EXISTS `mall_goods_spec_value`;
DROP TABLE IF EXISTS `mall_goods_spec`;
DROP TABLE IF EXISTS `mall_goods_sku`;

ALTER TABLE `mall_goods_spu`
  DROP COLUMN `sales_price`,
  DROP COLUMN `market_price`,
  DROP COLUMN `cost_price`,
  DROP COLUMN `stock`,
  ADD COLUMN `price_down` decimal(10,2) DEFAULT NULL COMMENT 'lowest sales price' AFTER `sort`,
  ADD COLUMN `price_up` decimal(10,2) DEFAULT NULL COMMENT 'highest sales price' AFTER `price_down`,
  ADD COLUMN `spec_type` char(2) NOT NULL DEFAULT '0' COMMENT '0=single sku,1=multi sku' AFTER `update_time`;

CREATE TABLE `mall_goods_sku` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `sku_code` varchar(64) DEFAULT NULL COMMENT 'sku code',
  `spu_id` varchar(32) NOT NULL COMMENT 'spu id',
  `pic_url` varchar(500) DEFAULT NULL COMMENT 'sku image',
  `sales_price` decimal(10,2) DEFAULT NULL COMMENT 'sales price',
  `market_price` decimal(10,2) DEFAULT NULL COMMENT 'market price',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT 'cost price',
  `stock` int NOT NULL DEFAULT '0' COMMENT 'stock',
  `enable` char(2) NOT NULL DEFAULT '1' COMMENT '0=disabled,1=enabled',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  `del_flag` char(2) NOT NULL DEFAULT '0' COMMENT 'delete flag',
  `version` int DEFAULT '0' COMMENT 'version',
  PRIMARY KEY (`id`),
  KEY `idx_mall_goods_sku_spu_id` (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='goods sku';

CREATE TABLE `mall_goods_spec` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `name` varchar(100) NOT NULL COMMENT 'spec name',
  `del_flag` char(2) NOT NULL DEFAULT '0' COMMENT 'delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='goods spec';

CREATE TABLE `mall_goods_spec_value` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `spec_id` varchar(32) NOT NULL COMMENT 'spec id',
  `name` varchar(100) NOT NULL COMMENT 'spec value name',
  `del_flag` char(2) NOT NULL DEFAULT '0' COMMENT 'delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_mall_goods_spec_value_spec_id` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='goods spec value';

CREATE TABLE `mall_goods_spu_spec` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `spu_id` varchar(32) NOT NULL COMMENT 'spu id',
  `spec_id` varchar(32) NOT NULL COMMENT 'spec id',
  `spec_name` varchar(100) NOT NULL COMMENT 'spec name snapshot',
  `sort` int NOT NULL DEFAULT '0' COMMENT 'sort',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_mall_goods_spu_spec_spu_id` (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='spu spec relation';

CREATE TABLE `mall_goods_sku_spec_value` (
  `id` varchar(32) NOT NULL COMMENT 'PK',
  `spu_id` varchar(32) NOT NULL COMMENT 'spu id',
  `sku_id` varchar(32) NOT NULL COMMENT 'sku id',
  `spec_value_id` varchar(32) NOT NULL COMMENT 'spec value id',
  `sort` int NOT NULL DEFAULT '0' COMMENT 'sort',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  PRIMARY KEY (`id`),
  KEY `idx_mall_goods_sku_spec_value_sku_id` (`sku_id`),
  KEY `idx_mall_goods_sku_spec_value_spu_id` (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sku spec value relation';

ALTER TABLE `mall_shopping_cart`
  ADD COLUMN `sku_id` varchar(32) NOT NULL COMMENT 'sku id' AFTER `spu_id`,
  ADD COLUMN `spec_info` varchar(255) DEFAULT NULL COMMENT 'spec info snapshot' AFTER `pic_url`;

ALTER TABLE `mall_order_item`
  ADD COLUMN `sku_id` varchar(32) NOT NULL COMMENT 'sku id' AFTER `spu_id`,
  ADD COLUMN `spec_info` varchar(255) DEFAULT NULL COMMENT 'spec info snapshot' AFTER `pic_url`;
