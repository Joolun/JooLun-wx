/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.constant;

/**
 * @author www.joolun.com
 */
public interface WxMaConstants {

	/**
	 * redis中3rd_session过期时间(单位：小时)
	 */
	long TIME_OUT_SESSION = 6;
	/**
	 * redis中3rd_session拼接前缀
	 */
	String THIRD_SESSION_BEGIN = "wx:ma:3rd_session";
}
