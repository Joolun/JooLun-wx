/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.joolun.weixin.entity.ThirdSession;
import lombok.experimental.UtilityClass;

/**
 * @author www.joolun.com
 * thirdSession工具类
 */
@UtilityClass
public class ThirdSessionHolder {

	private final ThreadLocal<ThirdSession> THREAD_LOCAL_THIRD_SESSION = new TransmittableThreadLocal<>();


	/**
	 * TTL 设置thirdSession
	 *
	 * @param thirdSession
	 */
	public void setThirdSession(ThirdSession thirdSession) {
		THREAD_LOCAL_THIRD_SESSION.set(thirdSession);
	}

	/**
	 * 获取TTL中的thirdSession
	 *
	 * @return
	 */
	public ThirdSession getThirdSession() {
		return THREAD_LOCAL_THIRD_SESSION.get();
	}

	public void clear() {
		THREAD_LOCAL_THIRD_SESSION.remove();
	}



	/**
	 * 获取用户商城ID
	 * @return
	 */
	public String getWxUserId(){
		return THREAD_LOCAL_THIRD_SESSION.get().getWxUserId();
	}
}
