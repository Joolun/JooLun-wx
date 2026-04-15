/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.MallUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商城用户服务接口。
 * 负责商城用户建档、微信资料同步，以及会员统计字段维护。
 *
 * @author www.joolun.com
 */
public interface MallUserService extends IService<MallUser> {

	/**
	 * 根据微信用户主键查询商城用户。
	 *
	 * @param wxUserId 微信用户主键
	 * @return 商城用户，不存在时返回 null
	 */
	MallUser getByWxUserId(String wxUserId);

	/**
	 * 根据微信用户主键同步商城用户档案。
	 * 如果商城用户不存在，则自动创建一条独立的商城用户主数据；
	 * 如果已存在，则刷新商城侧保存的微信快照字段。
	 *
	 * @param wxUserId 微信用户主键
	 * @return 同步后的商城用户
	 */
	MallUser syncByWxUserId(String wxUserId);

	/**
	 * 记录一次下单行为。
	 *
	 * @param mallUserId 商城用户主键
	 */
	void recordOrder(String mallUserId);

	/**
	 * 记录一次支付成功行为，并累计消费金额。
	 *
	 * @param mallUserId 商城用户主键
	 * @param amount 本次支付金额
	 */
	void recordConsume(String mallUserId, BigDecimal amount);

	/**
	 * 记录一次退款成功行为。
	 *
	 * @param mallUserId 商城用户主键
	 */
	void recordRefund(String mallUserId);

	/**
	 * 查询后台会员概览统计。
	 *
	 * @param mallUser 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getAdminSummary(MallUser mallUser);

	/**
	 * 查询后台会员运营标签分布。
	 * 统计口径与后台当前筛选条件保持一致，便于运营快速看当前人群的打标情况。
	 *
	 * @param mallUser 查询条件
	 * @return 标签统计列表
	 */
	List<Map<String, Object>> getAdminTagSummary(MallUser mallUser);
}
