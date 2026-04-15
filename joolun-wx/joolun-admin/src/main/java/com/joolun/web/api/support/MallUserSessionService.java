/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api.support;

import com.joolun.mall.entity.MallUser;
import com.joolun.mall.service.MallUserService;
import com.joolun.weixin.entity.ThirdSession;
import com.joolun.weixin.utils.ThirdSessionHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 商城用户会话服务。
 * 负责把当前小程序登录态统一转换成 mall_user，并集中提供 openId、sessionKey、appId 等会话数据。
 * 这样订单、购物车、地址、会员资料等接口就不需要各自重复写一遍会话解析逻辑。
 *
 * @author www.joolun.com
 */
@Component
@AllArgsConstructor
public class MallUserSessionService {

	private final MallUserService mallUserService;

	/**
	 * 获取当前商城用户。
	 * 每次都会基于当前微信登录态刷新一次商城用户快照，保证 mall_user 数据尽量和渠道资料保持同步。
	 *
	 * @return 当前商城用户
	 */
	public MallUser getCurrentMallUser() {
		MallUser mallUser = mallUserService.syncByWxUserId(getCurrentWxUserId());
		if (mallUser == null) {
			throw new IllegalStateException("商城用户不存在");
		}
		return mallUser;
	}

	/**
	 * 获取当前商城用户主键。
	 *
	 * @return 商城用户主键
	 */
	public String getCurrentMallUserId() {
		return getCurrentMallUser().getId();
	}

	/**
	 * 获取当前微信用户主键。
	 *
	 * @return 微信用户主键
	 */
	public String getCurrentWxUserId() {
		return requireThirdSession().getWxUserId();
	}

	/**
	 * 获取当前 openId。
	 *
	 * @return openId
	 */
	public String getCurrentOpenId() {
		return requireThirdSession().getOpenId();
	}

	/**
	 * 获取当前 sessionKey。
	 *
	 * @return sessionKey
	 */
	public String getCurrentSessionKey() {
		return requireThirdSession().getSessionKey();
	}

	/**
	 * 获取当前小程序 appId。
	 *
	 * @return appId
	 */
	public String getCurrentAppId() {
		return requireThirdSession().getAppId();
	}

	/**
	 * 读取并校验当前第三方会话。
	 *
	 * @return 当前第三方会话
	 */
	private ThirdSession requireThirdSession() {
		ThirdSession thirdSession = ThirdSessionHolder.getThirdSession();
		if (thirdSession == null) {
			throw new IllegalStateException("登录态已失效，请重新登录");
		}
		return thirdSession;
	}
}
