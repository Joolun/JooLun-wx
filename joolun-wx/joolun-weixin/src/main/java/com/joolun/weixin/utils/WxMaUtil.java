/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.utils;

import com.joolun.weixin.constant.ConfigConstant;
import jakarta.servlet.http.HttpServletRequest;


/**
 * @author www.joolun.com
 * 小程序工具类
 */
public class WxMaUtil {
	/**
	 * 通过request获取appId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getAppId(HttpServletRequest request) {
		String appId = request.getHeader(ConfigConstant.HEADER_APP_ID);
		return appId;
	}
}
