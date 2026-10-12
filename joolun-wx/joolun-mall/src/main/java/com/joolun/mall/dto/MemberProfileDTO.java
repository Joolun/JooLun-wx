/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.dto;

import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.MemberLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 小程序会员中心展示模型。 */
@Data
@AllArgsConstructor
public class MemberProfileDTO {
	private MallUser user;
	private MemberLevel currentLevel;
	private MemberLevel nextLevel;
}
