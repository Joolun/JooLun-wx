/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api.support;

import com.joolun.common.core.text.Convert;
import com.joolun.mall.constant.MallConfigKeyConstants;
import com.joolun.mall.dto.MallRuntimeConfigDTO;
import com.joolun.mall.service.MallTradeConfigService;
import com.joolun.system.service.ISysConfigService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 基于系统参数的商城交易配置实现。
 * 通过复用 sys_config，让商城开关直接走现成参数管理页面，不额外新增后台配置页。
 *
 * @author www.joolun.com
 */
@Service
@Primary
@AllArgsConstructor
public class SysConfigMallTradeConfigService implements MallTradeConfigService {

	private final ISysConfigService sysConfigService;

	@Override
	public MallRuntimeConfigDTO getRuntimeConfig() {
		MallRuntimeConfigDTO runtimeConfig = new MallRuntimeConfigDTO();
		runtimeConfig.setNeedMemberPhoneForOrder(needMemberPhoneForOrder());
		return runtimeConfig;
	}

	@Override
	public boolean needMemberPhoneForOrder() {
		String configValue = sysConfigService.selectConfigByKey(MallConfigKeyConstants.ORDER_NEED_MEMBER_PHONE);
		return Convert.toBool(configValue, true);
	}
}
