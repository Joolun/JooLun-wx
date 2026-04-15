/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import com.joolun.mall.dto.MallRuntimeConfigDTO;
import com.joolun.mall.service.MallTradeConfigService;
import org.springframework.stereotype.Service;

/**
 * 商城交易配置默认实现。
 * 当宿主工程没有提供系统参数实现时，默认仍按“下单必须绑定手机号”执行，保证后端校验不会失效。
 *
 * @author www.joolun.com
 */
@Service
public class MallTradeConfigServiceImpl implements MallTradeConfigService {

	@Override
	public MallRuntimeConfigDTO getRuntimeConfig() {
		MallRuntimeConfigDTO runtimeConfig = new MallRuntimeConfigDTO();
		runtimeConfig.setNeedMemberPhoneForOrder(needMemberPhoneForOrder());
		return runtimeConfig;
	}

	@Override
	public boolean needMemberPhoneForOrder() {
		return true;
	}
}
