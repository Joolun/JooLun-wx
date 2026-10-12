/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.constant;

import java.util.Set;

/**
 * 微信运行配置在系统参数中的键名。
 *
 * @author www.joolun.com
 */
public final class WxConfigKeyConstants {

	public static final String MP_APP_ID = "wx.mp.appId";
	public static final String MP_SECRET = "wx.mp.secret";
	public static final String MP_TOKEN = "wx.mp.token";
	public static final String MP_AES_KEY = "wx.mp.aesKey";
	public static final String MA_APP_ID = "wx.ma.appId";
	public static final String MA_SECRET = "wx.ma.secret";
	public static final String MA_TOKEN = "wx.ma.token";
	public static final String MA_AES_KEY = "wx.ma.aesKey";
	public static final String MA_MSG_DATA_FORMAT = "wx.ma.msgDataFormat";
	public static final String MA_MCH_ID = "wx.ma.mchId";
	public static final String MA_API_V3_KEY = "wx.ma.apiV3Key";
	public static final String MA_PRIVATE_KEY_PATH = "wx.ma.privateKeyPath";
	public static final String MA_CERT_SERIAL_NO = "wx.ma.certSerialNo";
	public static final String MA_PUBLIC_KEY_ID = "wx.ma.publicKeyId";
	public static final String MA_PUBLIC_KEY_PATH = "wx.ma.publicKeyPath";

	private static final Set<String> SENSITIVE_KEYS = Set.of(
			MP_SECRET, MP_TOKEN, MP_AES_KEY, MA_SECRET, MA_TOKEN, MA_AES_KEY, MA_API_V3_KEY
	);

	public static boolean isSensitive(String configKey) {
		return SENSITIVE_KEYS.contains(configKey);
	}

	private WxConfigKeyConstants() {
	}
}
