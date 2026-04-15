/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.mapper;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商城数据看板 Mapper。
 * 这里专门承接后台经营看板的聚合查询，避免把统计 SQL 散落到订单、会员、商品等各个模块里。
 *
 * @author www.joolun.com
 */
public interface MallDashboardMapper {

	/**
	 * 查询核心经营概览。
	 * 这里聚合订单总量、支付订单量和今日成交情况，供看板顶部概览卡片直接使用。
	 *
	 * @param todayStart 今日开始时间
	 * @param todayEnd 今日结束时间的下一刻
	 * @return 概览统计
	 */
	Map<String, Object> selectOverview(@Param("todayStart") LocalDateTime todayStart,
			@Param("todayEnd") LocalDateTime todayEnd);

	/**
	 * 查询订单状态看板统计。
	 * 用于展示待付款、待发货、待收货、已完成、已取消以及处理中售后的订单分布。
	 *
	 * @return 订单状态统计
	 */
	Map<String, Object> selectOrderStats();

	/**
	 * 查询商城用户与会员统计。
	 * 统计口径基于 mall_user 主表，适合后台经营分析，不依赖微信侧临时字段。
	 *
	 * @param todayStart 今日开始时间
	 * @param todayEnd 今日结束时间的下一刻
	 * @return 用户与会员统计
	 */
	Map<String, Object> selectMemberStats(@Param("todayStart") LocalDateTime todayStart,
			@Param("todayEnd") LocalDateTime todayEnd);

	/**
	 * 查询商品与库存统计。
	 * 这里同时统计 SPU 和 SKU，方便后台快速判断商品供给和库存健康度。
	 *
	 * @return 商品统计
	 */
	Map<String, Object> selectGoodsStats();

	/**
	 * 查询售后概览统计。
	 * 统计当前售后池规模、各状态数量以及累计退款金额。
	 *
	 * @param todayStart 今日开始时间
	 * @param todayEnd 今日结束时间的下一刻
	 * @return 售后统计
	 */
	Map<String, Object> selectAfterSaleStats(@Param("todayStart") LocalDateTime todayStart,
			@Param("todayEnd") LocalDateTime todayEnd);

	/**
	 * 查询按天汇总的经营趋势。
	 * 这里把订单、退款、注册、入会等关键指标压成统一的日期维度，前端可以直接绘图。
	 *
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return 趋势列表
	 */
	List<Map<String, Object>> selectTrend(@Param("beginDate") LocalDate beginDate,
			@Param("endDate") LocalDate endDate,
			@Param("beginTime") LocalDateTime beginTime,
			@Param("endTime") LocalDateTime endTime);

	/**
	 * 查询热销商品榜单。
	 * 默认统计最近一段时间的已支付订单明细，适合后台快速识别当前主卖商品。
	 *
	 * @param beginTime 统计开始时间
	 * @param limit 限制条数
	 * @return 热销商品列表
	 */
	List<Map<String, Object>> selectTopGoods(@Param("beginTime") LocalDateTime beginTime,
			@Param("limit") Integer limit);
}
