/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import lombok.Data;

/**
 * 微信开发数据
 *
 * @author www.joolun.com
 */
@Data
public class WxOpenDataDTO {
	private String appId;
	private String userId;
	private String encryptedData;
	private String errMsg;
	private String iv;
	private String rawData;
	private String signature;
	private String sessionKey;
}
