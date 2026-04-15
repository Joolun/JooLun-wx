/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import lombok.Data;

/**
 * 小程序手机号绑定参数。
 * 新版基础库优先通过 code 换取手机号，旧版机型仍可回退到 encryptedData + iv 解密。
 *
 * @author www.joolun.com
 */
@Data
public class WxPhoneNumberDTO {

	/**
	 * 小程序 appId。
	 */
	private String appId;

	/**
	 * 当前微信用户主键。
	 */
	private String userId;

	/**
	 * 当前小程序 sessionKey。
	 */
	private String sessionKey;

	/**
	 * getPhoneNumber 回调里的临时 code。
	 */
	private String code;

	/**
	 * 回调错误信息。
	 */
	private String errMsg;

	/**
	 * 旧版基础库加密数据。
	 */
	private String encryptedData;

	/**
	 * 旧版基础库偏移量。
	 */
	private String iv;
}
