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
import com.joolun.mall.entity.MallUserOperateLog;
import com.joolun.mall.mapper.MallUserOperateLogMapper;
import com.joolun.mall.service.MallUserOperateLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商城用户运营记录服务实现。
 *
 * @author www.joolun.com
 */
@Service
public class MallUserOperateLogServiceImpl extends ServiceImpl<MallUserOperateLogMapper, MallUserOperateLog> implements MallUserOperateLogService {

	private static final String DEFAULT_DEL_FLAG = "0";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOperateLog(String userId, String operateType, String operateTitle, String operateContent,
			String operatorId, String operatorName, String extraInfo) {
		MallUserOperateLog operateLog = buildOperateLog(userId, operateType, operateTitle, operateContent, operatorId, operatorName, extraInfo);
		if (operateLog == null) {
			return;
		}
		save(operateLog);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveBatchOperateLog(List<String> userIds, String operateType, String operateTitle, String operateContent,
			String operatorId, String operatorName, String extraInfo) {
		if (userIds == null || userIds.isEmpty()) {
			return;
		}
		List<String> validUserIds = userIds.stream()
				.filter(StrUtil::isNotBlank)
				.map(String::trim)
				.collect(Collectors.collectingAndThen(Collectors.toCollection(LinkedHashSet::new), List::copyOf));
		List<MallUserOperateLog> operateLogList = validUserIds.stream()
				.map(userId -> buildOperateLog(userId, operateType, operateTitle, operateContent, operatorId, operatorName, extraInfo))
				.filter(operateLog -> operateLog != null)
				.collect(Collectors.toList());
		if (operateLogList.isEmpty()) {
			return;
		}
		saveBatch(operateLogList);
	}

	@Override
	public List<MallUserOperateLog> listRecentByUserId(String userId, int limit) {
		if (StrUtil.isBlank(userId) || limit <= 0) {
			return Collections.emptyList();
		}
		return list(Wrappers.<MallUserOperateLog>lambdaQuery()
				.eq(MallUserOperateLog::getUserId, userId)
				.eq(MallUserOperateLog::getDelFlag, DEFAULT_DEL_FLAG)
				.orderByDesc(MallUserOperateLog::getCreateTime)
				.last("limit " + limit));
	}

	/**
	 * 组装运营日志实体。
	 *
	 * @param userId 商城用户ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param operatorId 操作人ID
	 * @param operatorName 操作人账号
	 * @param extraInfo 扩展信息
	 * @return 运营日志实体
	 */
	private MallUserOperateLog buildOperateLog(String userId, String operateType, String operateTitle, String operateContent,
			String operatorId, String operatorName, String extraInfo) {
		if (StrUtil.isBlank(userId) || StrUtil.isBlank(operateContent)) {
			return null;
		}
		LocalDateTime now = LocalDateTime.now();
		MallUserOperateLog operateLog = new MallUserOperateLog();
		operateLog.setId(IdUtil.simpleUUID());
		operateLog.setUserId(userId.trim());
		operateLog.setOperateType(StrUtil.blankToDefault(StrUtil.trim(operateType), "FOLLOW_UP"));
		operateLog.setOperateTitle(StrUtil.blankToDefault(StrUtil.trim(operateTitle), "会员运营记录"));
		operateLog.setOperateContent(operateContent.trim());
		operateLog.setOperatorId(StrUtil.blankToDefault(StrUtil.trim(operatorId), "0"));
		operateLog.setOperatorName(StrUtil.blankToDefault(StrUtil.trim(operatorName), "system"));
		operateLog.setExtraInfo(StrUtil.emptyToNull(StrUtil.trim(extraInfo)));
		operateLog.setCreateTime(now);
		operateLog.setDelFlag(DEFAULT_DEL_FLAG);
		return operateLog;
	}
}
