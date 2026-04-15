/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.MallUserOperateLog;

import java.util.List;

/**
 * 商城用户运营记录服务接口。
 *
 * @author www.joolun.com
 */
public interface MallUserOperateLogService extends IService<MallUserOperateLog> {

	/**
	 * 记录一条会员运营日志。
	 *
	 * @param userId 商城用户ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param operatorId 操作人ID
	 * @param operatorName 操作人账号
	 * @param extraInfo 扩展信息
	 */
	void saveOperateLog(String userId, String operateType, String operateTitle, String operateContent,
			String operatorId, String operatorName, String extraInfo);

	/**
	 * 批量记录会员运营日志。
	 *
	 * @param userIds 商城用户ID集合
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param operatorId 操作人ID
	 * @param operatorName 操作人账号
	 * @param extraInfo 扩展信息
	 */
	void saveBatchOperateLog(List<String> userIds, String operateType, String operateTitle, String operateContent,
			String operatorId, String operatorName, String extraInfo);

	/**
	 * 查询会员最近运营记录。
	 *
	 * @param userId 商城用户ID
	 * @param limit 条数上限
	 * @return 最近运营记录
	 */
	List<MallUserOperateLog> listRecentByUserId(String userId, int limit);
}
