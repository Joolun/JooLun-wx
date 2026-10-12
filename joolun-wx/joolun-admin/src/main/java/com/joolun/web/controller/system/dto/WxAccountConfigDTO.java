/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 微信账号配置。
 * 敏感字段查询时不返回原文，通过 configured 标记提示前端是否已经配置。
 *
 * @author www.joolun.com
 */
@Data
public class WxAccountConfigDTO {

	@Valid
	@NotNull(message = "公众号配置不能为空")
	private MpConfig mp = new MpConfig();

	@Valid
	@NotNull(message = "小程序配置不能为空")
	private MaConfig ma = new MaConfig();

	@Data
	public static class MpConfig {
		@NotBlank(message = "公众号 AppID 不能为空")
		@Size(max = 64, message = "公众号 AppID 不能超过64个字符")
		private String appId;

		@Size(max = 200, message = "公众号 AppSecret 不能超过200个字符")
		private String secret;

		@Size(max = 200, message = "公众号 Token 不能超过200个字符")
		private String token;

		@Size(max = 200, message = "公众号 EncodingAESKey 不能超过200个字符")
		private String aesKey;

		private boolean secretConfigured;
		private boolean tokenConfigured;
		private boolean aesKeyConfigured;
	}

	@Data
	public static class MaConfig {
		@NotBlank(message = "小程序 AppID 不能为空")
		@Size(max = 64, message = "小程序 AppID 不能超过64个字符")
		private String appId;

		@Size(max = 200, message = "小程序 AppSecret 不能超过200个字符")
		private String secret;

		@Size(max = 200, message = "小程序 Token 不能超过200个字符")
		private String token;

		@Size(max = 200, message = "小程序 EncodingAESKey 不能超过200个字符")
		private String aesKey;

		@Size(max = 10, message = "消息格式不能超过10个字符")
		@Pattern(regexp = "(?i)JSON|XML", message = "小程序消息格式只能是 JSON 或 XML")
		private String msgDataFormat = "JSON";

		@NotBlank(message = "微信支付商户号不能为空")
		@Size(max = 64, message = "微信支付商户号不能超过64个字符")
		private String mchId;

		private boolean secretConfigured;
		private boolean tokenConfigured;
		private boolean aesKeyConfigured;

		@Size(max = 200, message = "API v3 密钥不能超过200个字符")
		private String apiV3Key;

		@NotBlank(message = "商户 API 证书序列号不能为空")
		@Size(max = 64, message = "商户 API 证书序列号不能超过64个字符")
		@Pattern(regexp = "(?i)^[0-9a-f]+$", message = "商户 API 证书序列号只能包含十六进制字符")
		private String certSerialNo;

		@NotBlank(message = "微信支付公钥 ID 不能为空")
		@Size(max = 128, message = "微信支付公钥 ID 不能超过128个字符")
		private String publicKeyId;

		private boolean apiV3KeyConfigured;
		private boolean privateKeyConfigured;
		private boolean publicKeyConfigured;
	}
}
