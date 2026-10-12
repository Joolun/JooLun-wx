package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.MemberLevel;
import com.joolun.mall.mapper.MemberLevelMapper;
import com.joolun.mall.service.MemberLevelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

	@Override
	public MemberLevel findCurrent(Integer growthValue) {
		return getOne(Wrappers.<MemberLevel>lambdaQuery()
				.eq(MemberLevel::getStatus, "1")
				.le(MemberLevel::getMinGrowth, defaultGrowth(growthValue))
				.orderByDesc(MemberLevel::getMinGrowth)
				.last("limit 1"));
	}

	@Override
	public MemberLevel findNext(Integer growthValue) {
		return getOne(Wrappers.<MemberLevel>lambdaQuery()
				.eq(MemberLevel::getStatus, "1")
				.gt(MemberLevel::getMinGrowth, defaultGrowth(growthValue))
				.orderByAsc(MemberLevel::getMinGrowth)
				.last("limit 1"));
	}

	@Override
	public BigDecimal resolveDiscountRate(Integer growthValue) {
		MemberLevel level = findCurrent(growthValue);
		return level == null || level.getDiscountRate() == null ? new BigDecimal("100") : level.getDiscountRate();
	}

	private int defaultGrowth(Integer growthValue) {
		return growthValue == null ? 0 : growthValue;
	}
}
