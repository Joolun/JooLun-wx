/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author www.joolun.com
 */
@Data
public class ThirdSession implements Serializable {
	/**
	 * 微信用户ID
	 */
	private String wxUserId;
	/**
	 * 配置项ID
	 */
	private String appId;
	/**
	 * 微信sessionKey
	 */
	private String sessionKey;
	/**
	 * 用户标识
	 */
	private String openId;
}
