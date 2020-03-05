package com.ruoyi.project.weixin.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdSession implements Serializable {
	/**
	 * 所属租户
	 */
	private String tenantId;
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
	/**
	 * 商城用户ID
	 */
	private String mallUserId;
}
