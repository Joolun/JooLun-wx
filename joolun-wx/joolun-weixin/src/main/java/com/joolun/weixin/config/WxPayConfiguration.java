/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.config;

import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付Configuration
 * @author www.joolun.com
 *
 */
@Slf4j
@Configuration
public class WxPayConfiguration {

	private static WxRuntimeConfigService runtimeConfigService;

	@Autowired
	public WxPayConfiguration(WxRuntimeConfigService runtimeConfigService) {
		WxPayConfiguration.runtimeConfigService = runtimeConfigService;
	}

	/**
	 *  获取WxMpService
	 * @return
	 */
	public static WxPayService getPayService() {
		WxMaProperties.Config config = runtimeConfigService.getMaConfigs().get(0);
		validateApiV3Config(config);
		WxPayService wxPayService = null;
		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(config.getAppId());
		payConfig.setMchId(config.getMchId());
		payConfig.setApiV3Key(config.getApiV3Key());
		payConfig.setPrivateKeyPath(config.getPrivateKeyPath());
		payConfig.setCertSerialNo(config.getCertSerialNo());
		payConfig.setPublicKeyId(config.getPublicKeyId());
		payConfig.setPublicKeyPath(config.getPublicKeyPath());
		payConfig.setFullPublicKeyModel(true);
		wxPayService = new WxPayServiceImpl();
		wxPayService.setConfig(payConfig);
		return wxPayService;
    }

	private static void validateApiV3Config(WxMaProperties.Config config) {
		if (StrUtil.hasBlank(config.getAppId(), config.getMchId(), config.getApiV3Key(), config.getPrivateKeyPath(),
				config.getCertSerialNo(),
				config.getPublicKeyId(), config.getPublicKeyPath())) {
			throw new IllegalStateException("微信支付 API v3 配置不完整，请在“系统管理 -> 微信账号配置”中配置商户号、API v3 密钥、apiclient_key.pem、商户 API 证书序列号、微信支付公钥 ID 和公钥文件");
		}
	}

}
