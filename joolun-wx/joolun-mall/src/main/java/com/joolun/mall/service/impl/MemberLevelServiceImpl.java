package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.joolun.mall.entity.MemberLevel;
import com.joolun.mall.mapper.MemberLevelMapper;
import com.joolun.mall.service.MemberLevelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

	@Override
	public MemberLevel findCurrent(Integer growthValue) {
		// getOne 内部用 RowBounds(0, 2) 取数，PageHelper 会把它翻译成 LIMIT，
		// 这里不能再写 last("limit 1")，否则 SQL 会同时出现两个 LIMIT。
		return getOne(Wrappers.<MemberLevel>lambdaQuery()
				.eq(MemberLevel::getStatus, "1")
				.le(MemberLevel::getMinGrowth, defaultGrowth(growthValue))
				.orderByDesc(MemberLevel::getMinGrowth), false);
	}

	@Override
	public MemberLevel findNext(Integer growthValue) {
		return getOne(Wrappers.<MemberLevel>lambdaQuery()
				.eq(MemberLevel::getStatus, "1")
				.gt(MemberLevel::getMinGrowth, defaultGrowth(growthValue))
				.orderByAsc(MemberLevel::getMinGrowth), false);
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
