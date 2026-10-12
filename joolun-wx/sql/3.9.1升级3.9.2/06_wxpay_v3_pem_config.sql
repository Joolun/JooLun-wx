/*
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */

-- 3.9.1 升级 3.9.2：微信支付 API v3 商户签名由 PKCS#12 改为 PEM 私钥。
-- apiclient_key.pem 需在“系统管理 -> 微信账号配置”中重新上传，并填写对应商户 API 证书序列号。

DELETE FROM `sys_config`
WHERE `config_key` = 'wx.ma.pkcs12Path';
