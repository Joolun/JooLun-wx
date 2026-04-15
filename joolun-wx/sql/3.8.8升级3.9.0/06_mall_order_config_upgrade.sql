/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城下单配置升级脚本
-- 1. 复用 sys_config 维护商城交易开关
-- 2. mall.order.needMemberPhone = true 表示下单前必须先绑定手机号
-- 3. 后续可直接在“系统管理 -> 参数设置”里修改

DELETE FROM `sys_config`
WHERE `config_key` = 'mall.order.needMemberPhone';

INSERT INTO `sys_config` (
  `config_name`,
  `config_key`,
  `config_value`,
  `config_type`,
  `create_by`,
  `create_time`,
  `update_by`,
  `update_time`,
  `remark`
) VALUES (
  '下单是否必须绑定手机号',
  'mall.order.needMemberPhone',
  'true',
  'N',
  'admin',
  NOW(),
  'admin',
  NOW(),
  'true：必须先绑定手机号才允许提交订单；false：不限制'
);
