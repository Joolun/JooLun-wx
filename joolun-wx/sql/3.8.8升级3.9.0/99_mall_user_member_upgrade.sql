/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城会员身份升级脚本
-- 目标：
-- 1. 为 mall_user 增加会员标记和入会时间字段。
-- 2. 将“绑定手机号后才算会员”的业务规则固化到数据库层。
-- 3. 回填已有商城用户的会员身份，便于后台按会员/非会员做运营筛选。

ALTER TABLE `mall_user`
  ADD COLUMN `member_flag` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '会员标记（0未入会 1已入会）' AFTER `mobile`,
  ADD COLUMN `member_time` datetime DEFAULT NULL COMMENT '入会时间' AFTER `member_flag`;

UPDATE `mall_user`
SET `member_flag` = CASE
    WHEN `mobile` IS NOT NULL AND `mobile` <> '' THEN '1'
    ELSE '0'
  END,
  `member_time` = CASE
    WHEN `mobile` IS NOT NULL AND `mobile` <> '' THEN COALESCE(`member_time`, `update_time`, `last_login_time`, `register_time`, `create_time`, NOW())
    ELSE NULL
  END;
