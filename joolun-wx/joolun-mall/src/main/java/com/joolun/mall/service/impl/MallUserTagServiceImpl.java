/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.MallUserTag;
import com.joolun.mall.mapper.MallUserMapper;
import com.joolun.mall.mapper.MallUserTagMapper;
import com.joolun.mall.service.MallUserTagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商城用户标签库服务实现。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class MallUserTagServiceImpl extends ServiceImpl<MallUserTagMapper, MallUserTag> implements MallUserTagService {

	private static final String DEFAULT_STATUS = "0";
	private static final String DEFAULT_DEL_FLAG = "0";

	private final MallUserMapper mallUserMapper;

	@Override
	public List<MallUserTag> listActiveTags() {
		return list(Wrappers.<MallUserTag>lambdaQuery()
				.eq(MallUserTag::getStatus, DEFAULT_STATUS)
				.eq(MallUserTag::getDelFlag, DEFAULT_DEL_FLAG)
				.orderByAsc(MallUserTag::getSort)
				.orderByAsc(MallUserTag::getCreateTime));
	}

	@Override
	public List<MallUserTag> listTagLibrary() {
		List<MallUserTag> tagList = list(Wrappers.<MallUserTag>lambdaQuery()
				.eq(MallUserTag::getDelFlag, DEFAULT_DEL_FLAG)
				.orderByAsc(MallUserTag::getSort)
				.orderByDesc(MallUserTag::getCreateTime));
		if (tagList.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, Integer> useCountMap = buildUseCountMap();
		tagList.forEach(item -> item.setUseCount(useCountMap.getOrDefault(item.getTagName(), 0)));
		return tagList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveIfAbsent(List<String> tagNames) {
		if (tagNames == null || tagNames.isEmpty()) {
			return;
		}
		List<String> normalizedTags = tagNames.stream()
				.map(this::normalizeTagName)
				.filter(StrUtil::isNotBlank)
				.collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), List::copyOf));
		if (normalizedTags.isEmpty()) {
			return;
		}
		List<String> existedNames = list(Wrappers.<MallUserTag>lambdaQuery()
				.in(MallUserTag::getTagName, normalizedTags)
				.eq(MallUserTag::getDelFlag, DEFAULT_DEL_FLAG))
			.stream()
			.map(MallUserTag::getTagName)
			.collect(Collectors.toList());
		List<MallUserTag> saveList = normalizedTags.stream()
				.filter(tagName -> !existedNames.contains(tagName))
				.map(this::buildNewTag)
				.collect(Collectors.toList());
		if (!saveList.isEmpty()) {
			saveBatch(saveList);
		}
	}

	/**
	 * 构建标签使用人数映射。
	 * 因为 mall_user.user_tag 仍是文本字段，这里按当前全量会员数据拆分后在服务层聚合。
	 *
	 * @return 使用人数映射
	 */
	private Map<String, Integer> buildUseCountMap() {
		List<String> userTagValues = mallUserMapper.selectTagValues(new MallUser());
		if (userTagValues == null || userTagValues.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Long> countMap = userTagValues.stream()
				.flatMap(tagValue -> splitTags(tagValue).stream())
				.collect(Collectors.groupingBy(tag -> tag, LinkedHashMap::new, Collectors.counting()));
		return countMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue().intValue(), (left, right) -> left, LinkedHashMap::new));
	}

	/**
	 * 拆分标签文本。
	 *
	 * @param tagValue 标签原文
	 * @return 标签列表
	 */
	private List<String> splitTags(String tagValue) {
		if (StrUtil.isBlank(tagValue)) {
			return Collections.emptyList();
		}
		return Arrays.stream(tagValue.split("[，,、]"))
				.map(this::normalizeTagName)
				.filter(StrUtil::isNotBlank)
				.distinct()
				.collect(Collectors.toList());
	}

	/**
	 * 规范化标签名称。
	 *
	 * @param tagName 标签名称
	 * @return 规范化后的标签名称
	 */
	private String normalizeTagName(String tagName) {
		return StrUtil.trimToEmpty(tagName).replaceAll("[，,、]+", "");
	}

	/**
	 * 构建新标签实体。
	 *
	 * @param tagName 标签名
	 * @return 标签实体
	 */
	private MallUserTag buildNewTag(String tagName) {
		LocalDateTime now = LocalDateTime.now();
		MallUserTag mallUserTag = new MallUserTag();
		mallUserTag.setId(IdUtil.simpleUUID());
		mallUserTag.setTagName(tagName);
		mallUserTag.setStatus(DEFAULT_STATUS);
		mallUserTag.setSort(resolveNextSort());
		mallUserTag.setRemark("自动由会员打标签沉淀");
		mallUserTag.setCreateTime(now);
		mallUserTag.setUpdateTime(now);
		mallUserTag.setDelFlag(DEFAULT_DEL_FLAG);
		return mallUserTag;
	}

	/**
	 * 计算下一个排序值。
	 *
	 * @return 排序值
	 */
	private Integer resolveNextSort() {
		MallUserTag lastTag = getOne(Wrappers.<MallUserTag>lambdaQuery()
				.eq(MallUserTag::getDelFlag, DEFAULT_DEL_FLAG)
				.orderByDesc(MallUserTag::getSort)
				.orderByDesc(MallUserTag::getCreateTime)
				.last("limit 1"));
		if (lastTag == null || lastTag.getSort() == null) {
			return 0;
		}
		return lastTag.getSort() + 10;
	}
}
