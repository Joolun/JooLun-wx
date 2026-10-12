/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.config;

import com.joolun.common.utils.StringUtils;
import com.joolun.system.service.ISysConfigService;
import com.joolun.weixin.constant.WxConfigKeyConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信运行配置服务。
 * 系统参数优先，application.yml 作为默认值，兼容未执行配置保存的安装。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class WxRuntimeConfigService {

	private final ISysConfigService sysConfigService;
	private final WxMpProperties wxMpProperties;
	private final WxMaProperties wxMaProperties;

	public List<WxMpProperties.MpConfig> getMpConfigs() {
		List<WxMpProperties.MpConfig> source = wxMpProperties.getConfigs();
		List<WxMpProperties.MpConfig> result = new ArrayList<>();
		if (source != null) {
			source.forEach(item -> result.add(copyMpConfig(item)));
		}
		if (result.isEmpty()) {
			result.add(new WxMpProperties.MpConfig());
		}

		WxMpProperties.MpConfig first = result.get(0);
		first.setAppId(resolve(WxConfigKeyConstants.MP_APP_ID, first.getAppId()));
		first.setSecret(resolve(WxConfigKeyConstants.MP_SECRET, first.getSecret()));
		first.setToken(resolve(WxConfigKeyConstants.MP_TOKEN, first.getToken()));
		first.setAesKey(resolve(WxConfigKeyConstants.MP_AES_KEY, first.getAesKey()));
		return result;
	}

	public List<WxMaProperties.Config> getMaConfigs() {
		List<WxMaProperties.Config> source = wxMaProperties.getConfigs();
		List<WxMaProperties.Config> result = new ArrayList<>();
		if (source != null) {
			source.forEach(item -> result.add(copyMaConfig(item)));
		}
		if (result.isEmpty()) {
			result.add(new WxMaProperties.Config());
		}

		WxMaProperties.Config first = result.get(0);
		first.setAppId(resolve(WxConfigKeyConstants.MA_APP_ID, first.getAppId()));
		first.setSecret(resolve(WxConfigKeyConstants.MA_SECRET, first.getSecret()));
		first.setToken(resolve(WxConfigKeyConstants.MA_TOKEN, first.getToken()));
		first.setAesKey(resolve(WxConfigKeyConstants.MA_AES_KEY, first.getAesKey()));
		first.setMsgDataFormat(resolve(WxConfigKeyConstants.MA_MSG_DATA_FORMAT,
				StringUtils.isEmpty(first.getMsgDataFormat()) ? "JSON" : first.getMsgDataFormat()));
		first.setMchId(resolve(WxConfigKeyConstants.MA_MCH_ID, first.getMchId()));
		first.setApiV3Key(resolve(WxConfigKeyConstants.MA_API_V3_KEY, first.getApiV3Key()));
		first.setPrivateKeyPath(resolve(WxConfigKeyConstants.MA_PRIVATE_KEY_PATH, first.getPrivateKeyPath()));
		first.setCertSerialNo(resolve(WxConfigKeyConstants.MA_CERT_SERIAL_NO, first.getCertSerialNo()));
		first.setPublicKeyId(resolve(WxConfigKeyConstants.MA_PUBLIC_KEY_ID, first.getPublicKeyId()));
		first.setPublicKeyPath(resolve(WxConfigKeyConstants.MA_PUBLIC_KEY_PATH, first.getPublicKeyPath()));
		return result;
	}

	private String resolve(String key, String defaultValue) {
		String value = sysConfigService.selectConfigByKey(key);
		return StringUtils.isNotEmpty(value) ? value : defaultValue;
	}

	private WxMpProperties.MpConfig copyMpConfig(WxMpProperties.MpConfig source) {
		WxMpProperties.MpConfig target = new WxMpProperties.MpConfig();
		target.setAppId(source.getAppId());
		target.setSecret(source.getSecret());
		target.setToken(source.getToken());
		target.setAesKey(source.getAesKey());
		return target;
	}

	private WxMaProperties.Config copyMaConfig(WxMaProperties.Config source) {
		WxMaProperties.Config target = new WxMaProperties.Config();
		target.setAppId(source.getAppId());
		target.setSecret(source.getSecret());
		target.setToken(source.getToken());
		target.setAesKey(source.getAesKey());
		target.setMsgDataFormat(source.getMsgDataFormat());
		target.setMchId(source.getMchId());
		target.setApiV3Key(source.getApiV3Key());
		target.setPrivateKeyPath(source.getPrivateKeyPath());
		target.setCertSerialNo(source.getCertSerialNo());
		target.setPublicKeyId(source.getPublicKeyId());
		target.setPublicKeyPath(source.getPublicKeyPath());
		return target;
	}
}
