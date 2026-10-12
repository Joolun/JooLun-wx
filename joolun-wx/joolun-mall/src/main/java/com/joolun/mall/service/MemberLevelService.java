package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.MemberLevel;

import java.math.BigDecimal;

public interface MemberLevelService extends IService<MemberLevel> {
	MemberLevel findCurrent(Integer growthValue);

	MemberLevel findNext(Integer growthValue);

	BigDecimal resolveDiscountRate(Integer growthValue);
}
