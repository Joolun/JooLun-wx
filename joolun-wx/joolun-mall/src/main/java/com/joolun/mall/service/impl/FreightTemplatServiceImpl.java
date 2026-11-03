/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.joolun.mall.entity.FreightTemplat;
import com.joolun.mall.mapper.FreightTemplatMapper;
import com.joolun.mall.service.FreightTemplatService;
import com.joolun.mall.support.FreightCalculator;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 运费模板服务实现。
 *
 * @author www.joolun.com
 */
@Service
public class FreightTemplatServiceImpl extends ServiceImpl<FreightTemplatMapper, FreightTemplat>
		implements FreightTemplatService {

	@Override
	public List<FreightTemplat> listEnabled() {
		return list(Wrappers.<FreightTemplat>lambdaQuery()
				.eq(FreightTemplat::getStatus, "1")
				.eq(FreightTemplat::getDelFlag, "0")
				.orderByAsc(FreightTemplat::getSort)
				.orderByAsc(FreightTemplat::getId));
	}

	@Override
	public Map<String, FreightCalculator.FreightRule> getRuleMap(Collection<String> ids) {
		Set<String> validIds = ids == null ? Collections.emptySet() : ids.stream()
				.filter(StrUtil::isNotBlank)
				.collect(Collectors.toSet());
		if (validIds.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, FreightCalculator.FreightRule> result = new LinkedHashMap<>();
		for (FreightTemplat templat : listByIds(validIds)) {
			if (templat == null || !"1".equals(templat.getStatus())) {
				continue;
			}
			result.put(templat.getId(), FreightCalculator.FreightRule.from(templat));
		}
		return result;
	}
}
