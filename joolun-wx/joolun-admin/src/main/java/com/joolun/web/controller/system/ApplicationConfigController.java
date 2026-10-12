/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.system;

import com.joolun.common.annotation.Log;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.BusinessType;
import com.joolun.common.exception.ServiceException;
import com.joolun.common.utils.StringUtils;
import com.joolun.mall.config.MallRuntimeConfigService;
import com.joolun.mall.constant.MallApplicationConfigKeyConstants;
import com.joolun.system.service.ISysConfigService;
import com.joolun.web.controller.system.dto.MallApplicationConfigDTO;
import com.joolun.web.controller.system.dto.WxAccountConfigDTO;
import com.joolun.weixin.config.WxMaConfiguration;
import com.joolun.weixin.config.WxMaProperties;
import com.joolun.weixin.config.WxMpConfiguration;
import com.joolun.weixin.config.WxMpProperties;
import com.joolun.weixin.config.WxRuntimeConfigService;
import com.joolun.weixin.constant.WxConfigKeyConstants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

/**
 * application.yml 业务配置管理。
 * 保存值进入 sys_config，并优先于 application.yml 的默认值生效。
 *
 * @author www.joolun.com
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/system/app-config")
public class ApplicationConfigController extends BaseController {

	private final ISysConfigService sysConfigService;
	private final WxRuntimeConfigService wxRuntimeConfigService;
	private final WxMpConfiguration wxMpConfiguration;
	private final WxMaConfiguration wxMaConfiguration;
	private final WxMpService wxMpService;
	private final MallRuntimeConfigService mallRuntimeConfigService;
	private final WxPayPemStorageService wxPayPemStorageService;

	@PreAuthorize("@ss.hasPermi('system:wxconfig:query')")
	@GetMapping("/wx")
	public AjaxResult getWxConfig() {
		WxMpProperties.MpConfig mp = wxRuntimeConfigService.getMpConfigs().get(0);
		WxMaProperties.Config ma = wxRuntimeConfigService.getMaConfigs().get(0);

		WxAccountConfigDTO result = new WxAccountConfigDTO();
		result.getMp().setAppId(mp.getAppId());
		result.getMp().setSecretConfigured(StringUtils.isNotEmpty(mp.getSecret()));
		result.getMp().setTokenConfigured(StringUtils.isNotEmpty(mp.getToken()));
		result.getMp().setAesKeyConfigured(StringUtils.isNotEmpty(mp.getAesKey()));
		result.getMa().setAppId(ma.getAppId());
		result.getMa().setTokenConfigured(StringUtils.isNotEmpty(ma.getToken()));
		result.getMa().setAesKeyConfigured(StringUtils.isNotEmpty(ma.getAesKey()));
		result.getMa().setMsgDataFormat(StringUtils.isEmpty(ma.getMsgDataFormat()) ? "JSON" : ma.getMsgDataFormat());
		result.getMa().setMchId(ma.getMchId());
		result.getMa().setSecretConfigured(StringUtils.isNotEmpty(ma.getSecret()));
		result.getMa().setApiV3KeyConfigured(StringUtils.isNotEmpty(ma.getApiV3Key()));
		result.getMa().setPrivateKeyConfigured(StringUtils.isNotEmpty(ma.getPrivateKeyPath()));
		result.getMa().setCertSerialNo(ma.getCertSerialNo());
		result.getMa().setPublicKeyId(ma.getPublicKeyId());
		result.getMa().setPublicKeyConfigured(StringUtils.isNotEmpty(ma.getPublicKeyPath()));
		return success(result);
	}

	@PreAuthorize("@ss.hasPermi('system:wxconfig:edit')")
	@Log(title = "微信账号配置", businessType = BusinessType.UPDATE,
			excludeParamNames = {"secret", "token", "aesKey", "apiV3Key"})
	@Transactional(rollbackFor = Exception.class)
	@PutMapping("/wx")
	public AjaxResult updateWxConfig(@Valid @RequestBody WxAccountConfigDTO config) {
		WxMpProperties.MpConfig currentMp = wxRuntimeConfigService.getMpConfigs().get(0);
		WxMaProperties.Config currentMa = wxRuntimeConfigService.getMaConfigs().get(0);
		ensureSensitiveValue(config.getMp().getSecret(), currentMp.getSecret(), "公众号 AppSecret");
		ensureSensitiveValue(config.getMa().getSecret(), currentMa.getSecret(), "小程序 AppSecret");
		ensureSensitiveValue(config.getMa().getApiV3Key(), currentMa.getApiV3Key(), "微信支付 API v3 密钥");
		ensureSensitiveValue(null, currentMa.getPrivateKeyPath(), "商户 API 私钥 apiclient_key.pem");
		ensureSensitiveValue(null, currentMa.getPublicKeyPath(), "微信支付公钥 pub_key.pem");

		String username = getUsername();
		save(WxConfigKeyConstants.MP_APP_ID, config.getMp().getAppId(), "微信公众号 AppID", username, "对应 wx.mp.configs[0].appId");
		saveSensitive(WxConfigKeyConstants.MP_SECRET, config.getMp().getSecret(), "微信公众号 AppSecret", username, "对应 wx.mp.configs[0].secret");
		saveSensitive(WxConfigKeyConstants.MP_TOKEN, config.getMp().getToken(), "微信公众号 Token", username, "对应 wx.mp.configs[0].token");
		saveSensitive(WxConfigKeyConstants.MP_AES_KEY, config.getMp().getAesKey(), "微信公众号 EncodingAESKey", username, "对应 wx.mp.configs[0].aesKey");
		save(WxConfigKeyConstants.MA_APP_ID, config.getMa().getAppId(), "微信小程序 AppID", username, "对应 wx.ma.configs[0].appId");
		saveSensitive(WxConfigKeyConstants.MA_SECRET, config.getMa().getSecret(), "微信小程序 AppSecret", username, "对应 wx.ma.configs[0].secret");
		saveSensitive(WxConfigKeyConstants.MA_TOKEN, config.getMa().getToken(), "微信小程序 Token", username, "对应 wx.ma.configs[0].token");
		saveSensitive(WxConfigKeyConstants.MA_AES_KEY, config.getMa().getAesKey(), "微信小程序 EncodingAESKey", username, "对应 wx.ma.configs[0].aesKey");
		save(WxConfigKeyConstants.MA_MSG_DATA_FORMAT,
				StringUtils.isEmpty(config.getMa().getMsgDataFormat()) ? "JSON" : config.getMa().getMsgDataFormat().toUpperCase(),
				"微信小程序消息格式", username, "对应 wx.ma.configs[0].msgDataFormat");
		save(WxConfigKeyConstants.MA_MCH_ID, config.getMa().getMchId(), "微信支付商户号", username, "对应 wx.ma.configs[0].mchId");
		saveSensitive(WxConfigKeyConstants.MA_API_V3_KEY, config.getMa().getApiV3Key(), "微信支付 API v3 密钥", username, "对应 wx.ma.configs[0].apiV3Key");
		save(WxConfigKeyConstants.MA_CERT_SERIAL_NO, config.getMa().getCertSerialNo().toUpperCase(Locale.ROOT),
				"商户 API 证书序列号", username, "对应 wx.ma.configs[0].certSerialNo");
		save(WxConfigKeyConstants.MA_PUBLIC_KEY_ID, config.getMa().getPublicKeyId(), "微信支付公钥 ID", username, "对应 wx.ma.configs[0].publicKeyId");

		wxMpConfiguration.refresh(wxMpService);
		wxMaConfiguration.refresh();
		return success();
	}

	@PreAuthorize("@ss.hasPermi('system:wxconfig:edit')")
	@Log(title = "微信支付商户私钥", businessType = BusinessType.UPDATE, excludeParamNames = {"file"})
	@PostMapping("/wx/merchant-private-key")
	public AjaxResult uploadMerchantPrivateKey(@RequestParam("file") MultipartFile file) {
		String privateKeyPath = wxPayPemStorageService.storeMerchantPrivateKey(file);
		save(WxConfigKeyConstants.MA_PRIVATE_KEY_PATH, privateKeyPath, "微信支付商户 API 私钥路径",
				getUsername(), "后台上传的 apiclient_key.pem，仅保存服务器内部路径");
		return success("apiclient_key.pem 上传成功");
	}

	@PreAuthorize("@ss.hasPermi('system:wxconfig:edit')")
	@Log(title = "微信支付公钥", businessType = BusinessType.UPDATE, excludeParamNames = {"file"})
	@PostMapping("/wx/wechat-pay-public-key")
	public AjaxResult uploadWechatPayPublicKey(@RequestParam("file") MultipartFile file) {
		String publicKeyPath = wxPayPemStorageService.storeWechatPayPublicKey(file);
		save(WxConfigKeyConstants.MA_PUBLIC_KEY_PATH, publicKeyPath, "微信支付公钥路径",
				getUsername(), "后台上传的 pub_key.pem，仅保存服务器内部路径");
		return success("微信支付公钥上传成功");
	}

	@PreAuthorize("@ss.hasPermi('system:mallconfig:query')")
	@GetMapping("/mall")
	public AjaxResult getMallConfig() {
		MallApplicationConfigDTO result = new MallApplicationConfigDTO();
		result.setNotifyHost(mallRuntimeConfigService.getNotifyHost());
		return success(result);
	}

	@PreAuthorize("@ss.hasPermi('system:mallconfig:edit')")
	@Log(title = "商城配置", businessType = BusinessType.UPDATE)
	@PutMapping("/mall")
	public AjaxResult updateMallConfig(@Valid @RequestBody MallApplicationConfigDTO config) {
		String notifyHost = StringUtils.trim(config.getNotifyHost());
		while (notifyHost.endsWith("/")) {
			notifyHost = notifyHost.substring(0, notifyHost.length() - 1);
		}
		save(MallApplicationConfigKeyConstants.NOTIFY_HOST, notifyHost, "商城支付回调域名", getUsername(), "对应 mall.notify-host");
		return success();
	}

	private void save(String key, String value, String name, String username, String remark) {
		sysConfigService.saveConfigByKey(key, StringUtils.trim(value), name, username, remark);
	}

	private void saveSensitive(String key, String value, String name, String username, String remark) {
		if (StringUtils.isNotEmpty(value)) {
			save(key, value, name, username, remark);
		}
	}

	private void ensureSensitiveValue(String submittedValue, String currentValue, String name) {
		if (StringUtils.isEmpty(submittedValue) && StringUtils.isEmpty(currentValue)) {
			throw new ServiceException(name + "不能为空");
		}
	}
}
