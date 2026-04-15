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
import com.joolun.mall.mapper.MallUserMapper;
import com.joolun.mall.service.MallUserService;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.service.WxUserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商城用户服务实现。
 * 商城领域从这里开始使用独立的 mall_user.id，不再复用 wx_user.id。
 * 微信用户仍然作为来源渠道存在，商城用户通过 wx_user_id 与之关联。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class MallUserServiceImpl extends ServiceImpl<MallUserMapper, MallUser> implements MallUserService {

	private static final String DEFAULT_STATUS = "0";
	private static final String DEFAULT_DEL_FLAG = "0";
	private static final String DEFAULT_MEMBER_FLAG = "0";
	private static final String DEFAULT_NICK_NAME = "微信用户";
	private static final String DEFAULT_REMARK = "由 wx_user 自动建档";

	private final WxUserService wxUserService;

	@Override
	public MallUser getByWxUserId(String wxUserId) {
		if (StrUtil.isBlank(wxUserId)) {
			return null;
		}
		return getOne(Wrappers.<MallUser>lambdaQuery()
				.eq(MallUser::getWxUserId, wxUserId)
				.last("limit 1"));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public MallUser syncByWxUserId(String wxUserId) {
		if (StrUtil.isBlank(wxUserId)) {
			return null;
		}
		WxUser wxUser = wxUserService.getById(wxUserId);
		if (wxUser == null) {
			throw new IllegalArgumentException("微信用户不存在");
		}

		MallUser mallUser = getByWxUserId(wxUserId);
		if (mallUser == null) {
			mallUser = buildNewMallUser(wxUser);
			try {
				save(mallUser);
			} catch (DuplicateKeyException ex) {
				mallUser = getByWxUserId(wxUserId);
			}
		}
		if (mallUser == null) {
			mallUser = getByWxUserId(wxUserId);
		}
		if (mallUser == null) {
			throw new IllegalStateException("商城用户建档失败");
		}

		mergeWxSnapshot(mallUser, wxUser);
		updateById(mallUser);
		return getById(mallUser.getId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recordOrder(String mallUserId) {
		MallUser mallUser = getById(mallUserId);
		if (mallUser == null) {
			return;
		}
		mallUser.setOrderCount(defaultInt(mallUser.getOrderCount()) + 1);
		touchMallUser(mallUser);
		updateById(mallUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recordConsume(String mallUserId, BigDecimal amount) {
		MallUser mallUser = getById(mallUserId);
		if (mallUser == null) {
			return;
		}
		mallUser.setConsumeCount(defaultInt(mallUser.getConsumeCount()) + 1);
		mallUser.setConsumeAmount(defaultDecimal(mallUser.getConsumeAmount()).add(defaultDecimal(amount)));
		touchMallUser(mallUser);
		updateById(mallUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recordRefund(String mallUserId) {
		MallUser mallUser = getById(mallUserId);
		if (mallUser == null) {
			return;
		}
		mallUser.setRefundCount(defaultInt(mallUser.getRefundCount()) + 1);
		touchMallUser(mallUser);
		updateById(mallUser);
	}

	@Override
	public Map<String, Object> getAdminSummary(MallUser mallUser) {
		return baseMapper.selectSummary(mallUser);
	}

	@Override
	public List<Map<String, Object>> getAdminTagSummary(MallUser mallUser) {
		List<String> tagValues = baseMapper.selectTagValues(mallUser);
		if (tagValues == null || tagValues.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, Long> tagCountMap = tagValues.stream()
				.flatMap(tagValue -> splitUserTags(tagValue).stream())
				.collect(Collectors.groupingBy(tag -> tag, LinkedHashMap::new, Collectors.counting()));
		return tagCountMap.entrySet().stream()
				.sorted(Comparator.comparingLong((Map.Entry<String, Long> item) -> item.getValue()).reversed()
						.thenComparing(Map.Entry::getKey))
				.limit(12)
				.map(item -> {
					Map<String, Object> tagSummary = new LinkedHashMap<>(2);
					tagSummary.put("tag", item.getKey());
					tagSummary.put("count", item.getValue());
					return tagSummary;
				})
				.collect(Collectors.toList());
	}

	/**
	 * 初始化商城用户档案。
	 * 首次建档时生成独立的商城用户主键和会员编号。
	 *
	 * @param wxUser 微信用户
	 * @return 新的商城用户档案
	 */
	private MallUser buildNewMallUser(WxUser wxUser) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime registerTime = wxUser.getCreateTime() == null ? now : wxUser.getCreateTime();
		MallUser mallUser = new MallUser();
		mallUser.setId(IdUtil.simpleUUID());
		mallUser.setWxUserId(wxUser.getId());
		mallUser.setUserNo(buildUserNo());
		mallUser.setRegisterTime(registerTime);
		mallUser.setLastLoginTime(now);
		mallUser.setOrderCount(0);
		mallUser.setConsumeCount(0);
		mallUser.setRefundCount(0);
		mallUser.setConsumeAmount(BigDecimal.ZERO);
		mallUser.setMemberFlag(DEFAULT_MEMBER_FLAG);
		mallUser.setMemberTime(null);
		mallUser.setStatus(DEFAULT_STATUS);
		mallUser.setRemark(DEFAULT_REMARK);
		mallUser.setCreateTime(registerTime);
		mallUser.setUpdateTime(now);
		mallUser.setDelFlag(DEFAULT_DEL_FLAG);
		mergeWxSnapshot(mallUser, wxUser);
		return mallUser;
	}

	/**
	 * 将微信用户中的快照字段同步到商城用户。
	 * 这里只覆盖与微信资料直接对应的字段，不去影响商城自有扩展字段。
	 *
	 * @param mallUser 商城用户
	 * @param wxUser 微信用户
	 */
	private void mergeWxSnapshot(MallUser mallUser, WxUser wxUser) {
		LocalDateTime now = LocalDateTime.now();
		mallUser.setWxUserId(wxUser.getId());
		mallUser.setUserNo(StrUtil.blankToDefault(mallUser.getUserNo(), buildUserNo()));
		mallUser.setOpenId(chooseText(mallUser.getOpenId(), wxUser.getOpenId()));
		mallUser.setUnionId(chooseText(mallUser.getUnionId(), wxUser.getUnionId()));
		mallUser.setNickName(chooseText(mallUser.getNickName(), wxUser.getNickName(), DEFAULT_NICK_NAME));
		mallUser.setAvatarUrl(chooseText(mallUser.getAvatarUrl(), wxUser.getHeadimgUrl()));
		mallUser.setMobile(chooseText(mallUser.getMobile(), wxUser.getPhone()));
		refreshMemberIdentity(mallUser, now);
		mallUser.setSex(normalizeSex(wxUser.getSex(), mallUser.getSex()));
		mallUser.setCountry(chooseText(mallUser.getCountry(), wxUser.getCountry()));
		mallUser.setProvince(chooseText(mallUser.getProvince(), wxUser.getProvince()));
		mallUser.setCity(chooseText(mallUser.getCity(), wxUser.getCity()));
		mallUser.setRegisterSource(StrUtil.blankToDefault(mallUser.getRegisterSource(), resolveRegisterSource(wxUser.getAppType())));
		mallUser.setRegisterTime(mallUser.getRegisterTime() == null ? defaultTime(wxUser.getCreateTime(), now) : mallUser.getRegisterTime());
		mallUser.setLastLoginTime(now);
		mallUser.setStatus(StrUtil.blankToDefault(mallUser.getStatus(), DEFAULT_STATUS));
		mallUser.setRemark(StrUtil.blankToDefault(mallUser.getRemark(), DEFAULT_REMARK));
		mallUser.setCreateTime(mallUser.getCreateTime() == null ? defaultTime(wxUser.getCreateTime(), now) : mallUser.getCreateTime());
		mallUser.setUpdateTime(now);
		mallUser.setDelFlag(StrUtil.blankToDefault(wxUser.getDelFlag(), StrUtil.blankToDefault(mallUser.getDelFlag(), DEFAULT_DEL_FLAG)));
		mallUser.setOrderCount(defaultInt(mallUser.getOrderCount()));
		mallUser.setConsumeCount(defaultInt(mallUser.getConsumeCount()));
		mallUser.setRefundCount(defaultInt(mallUser.getRefundCount()));
		mallUser.setConsumeAmount(defaultDecimal(mallUser.getConsumeAmount()));
	}

	/**
	 * 按当前手机号状态维护会员身份。
	 * 当前项目定义：绑定手机号的商城用户才算会员。
	 *
	 * @param mallUser 商城用户
	 * @param now 当前时间
	 */
	private void refreshMemberIdentity(MallUser mallUser, LocalDateTime now) {
		if (StrUtil.isBlank(mallUser.getMobile())) {
			mallUser.setMemberFlag(DEFAULT_MEMBER_FLAG);
			mallUser.setMemberTime(null);
			return;
		}
		mallUser.setMemberFlag("1");
		if (mallUser.getMemberTime() == null) {
			mallUser.setMemberTime(now);
		}
	}

	/**
	 * 统一维护最近活跃时间和更新时间。
	 *
	 * @param mallUser 商城用户
	 */
	private void touchMallUser(MallUser mallUser) {
		LocalDateTime now = LocalDateTime.now();
		mallUser.setLastLoginTime(now);
		mallUser.setUpdateTime(now);
	}

	/**
	 * 生成商城会员编号。
	 * 会员编号只用于展示和检索，不参与业务主键关系。
	 *
	 * @return 会员编号
	 */
	private String buildUserNo() {
		return "M" + IdUtil.getSnowflake(0, 0).nextIdStr();
	}

	/**
	 * 拆分商城用户运营标签。
	 * 一个用户即使录入了多个标签，同一个标签在同一用户内也只统计一次。
	 *
	 * @param userTag 运营标签原文
	 * @return 拆分后的标签列表
	 */
	private List<String> splitUserTags(String userTag) {
		if (StrUtil.isBlank(userTag)) {
			return Collections.emptyList();
		}
		return Arrays.stream(userTag.split("[，,、]"))
				.map(StrUtil::trim)
				.filter(StrUtil::isNotBlank)
				.distinct()
				.collect(Collectors.toList());
	}

	private String chooseText(String currentValue, String wxValue) {
		return chooseText(currentValue, wxValue, currentValue);
	}

	private String chooseText(String currentValue, String wxValue, String defaultValue) {
		if (StrUtil.isNotBlank(wxValue)) {
			return wxValue;
		}
		return StrUtil.blankToDefault(currentValue, defaultValue);
	}

	private String normalizeSex(String wxSex, String currentSex) {
		if ("1".equals(wxSex) || "2".equals(wxSex)) {
			return wxSex;
		}
		return StrUtil.blankToDefault(currentSex, "0");
	}

	private String resolveRegisterSource(String appType) {
		if ("1".equals(appType)) {
			return "1";
		}
		if ("2".equals(appType)) {
			return "2";
		}
		return "9";
	}

	private Integer defaultInt(Integer value) {
		return value == null ? 0 : value;
	}

	private BigDecimal defaultDecimal(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}

	private LocalDateTime defaultTime(LocalDateTime time, LocalDateTime defaultTime) {
		return time == null ? defaultTime : time;
	}
}
