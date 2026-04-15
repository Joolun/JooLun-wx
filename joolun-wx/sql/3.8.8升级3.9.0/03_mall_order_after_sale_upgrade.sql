/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

-- 商城订单售后链路升级脚本
-- 目标：
-- 1. 为订单项补齐退款申请原因、申请时间、审核备注、审核时间、退款完成时间。
-- 2. 支撑后台订单页展示完整售后轨迹。
-- 3. 支撑客服审核退款时留痕，方便后续追踪和复盘。

ALTER TABLE `mall_order_item`
  ADD COLUMN `refund_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '退款申请原因' AFTER `spec_info`,
  ADD COLUMN `refund_apply_time` datetime DEFAULT NULL COMMENT '退款申请时间' AFTER `refund_reason`,
  ADD COLUMN `refund_audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '退款审核备注' AFTER `refund_apply_time`,
  ADD COLUMN `refund_audit_time` datetime DEFAULT NULL COMMENT '退款审核时间' AFTER `refund_audit_remark`,
  ADD COLUMN `refund_success_time` datetime DEFAULT NULL COMMENT '退款完成时间' AFTER `refund_audit_time`;
