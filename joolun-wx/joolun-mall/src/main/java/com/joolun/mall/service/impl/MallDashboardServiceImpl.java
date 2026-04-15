/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import com.joolun.mall.mapper.MallDashboardMapper;
import com.joolun.mall.service.MallDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城数据看板服务实现。
 * 这里负责把多张统计表拼成后台页面可直接消费的数据结构，并补齐派生指标。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class MallDashboardServiceImpl implements MallDashboardService {

	/**
	 * 趋势图最少支持 7 天，避免页面跨度过短时没有分析价值。
	 */
	private static final int MIN_DAY_RANGE = 7;

	/**
	 * 趋势图最大支持 30 天，控制一次查询的数据量，避免页面首屏过重。
	 */
	private static final int MAX_DAY_RANGE = 30;

	/**
	 * 看板默认展示近 15 天。
	 */
	private static final int DEFAULT_DAY_RANGE = 15;

	/**
	 * 热销商品榜默认展示 10 条。
	 */
	private static final int DEFAULT_TOP_GOODS_LIMIT = 10;

	/**
	 * 热销商品榜默认统计最近 30 天。
	 */
	private static final int DEFAULT_TOP_GOODS_DAY_RANGE = 30;

	private final MallDashboardMapper mallDashboardMapper;

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getDashboardData(Integer dayRange) {
		int normalizedDayRange = normalizeDayRange(dayRange);
		LocalDate today = LocalDate.now();
		LocalDateTime todayStart = today.atStartOfDay();
		LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
		LocalDate beginDate = today.minusDays(normalizedDayRange - 1L);
		LocalDateTime beginTime = beginDate.atStartOfDay();
		LocalDateTime topGoodsBeginTime = today.minusDays(DEFAULT_TOP_GOODS_DAY_RANGE - 1L).atStartOfDay();

		Map<String, Object> overview = new LinkedHashMap<>(safeMap(mallDashboardMapper.selectOverview(todayStart, todayEnd)));
		Map<String, Object> orderStats = new LinkedHashMap<>(safeMap(mallDashboardMapper.selectOrderStats()));
		Map<String, Object> memberStats = new LinkedHashMap<>(safeMap(mallDashboardMapper.selectMemberStats(todayStart, todayEnd)));
		Map<String, Object> goodsStats = new LinkedHashMap<>(safeMap(mallDashboardMapper.selectGoodsStats()));
		Map<String, Object> afterSaleStats = new LinkedHashMap<>(safeMap(mallDashboardMapper.selectAfterSaleStats(todayStart, todayEnd)));
		List<Map<String, Object>> trendList = mallDashboardMapper.selectTrend(beginDate, today, beginTime, todayEnd);
		List<Map<String, Object>> topGoodsList = mallDashboardMapper.selectTopGoods(topGoodsBeginTime, DEFAULT_TOP_GOODS_LIMIT);

		fillOverviewDerivedMetrics(overview);
		fillOrderDerivedMetrics(orderStats, overview);
		fillMemberDerivedMetrics(memberStats);
		fillGoodsDerivedMetrics(goodsStats);
		fillAfterSaleDerivedMetrics(afterSaleStats);

		Map<String, Object> dashboardData = new LinkedHashMap<>(8);
		dashboardData.put("dayRange", normalizedDayRange);
		dashboardData.put("overview", overview);
		dashboardData.put("orderStats", orderStats);
		dashboardData.put("memberStats", memberStats);
		dashboardData.put("goodsStats", goodsStats);
		dashboardData.put("afterSaleStats", afterSaleStats);
		dashboardData.put("trend", trendList);
		dashboardData.put("topGoods", topGoodsList);
		return dashboardData;
	}

	/**
	 * 规范化趋势天数。
	 * 这样前端即使传了异常值，也不会把 SQL 查询拉到过大范围。
	 *
	 * @param dayRange 前端传入天数
	 * @return 合法天数
	 */
	private int normalizeDayRange(Integer dayRange) {
		if (dayRange == null) {
			return DEFAULT_DAY_RANGE;
		}
		return Math.max(MIN_DAY_RANGE, Math.min(MAX_DAY_RANGE, dayRange));
	}

	/**
	 * 兜底空 Map。
	 * MyBatis 聚合查询理论上都会返回一行，但这里仍做保护，避免极端情况下空指针。
	 *
	 * @param source 原始统计 Map
	 * @return 非空 Map
	 */
	private Map<String, Object> safeMap(Map<String, Object> source) {
		return source == null ? new LinkedHashMap<>() : source;
	}

	/**
	 * 补齐核心概览的支付率等派生指标。
	 *
	 * @param overview 核心概览
	 */
	private void fillOverviewDerivedMetrics(Map<String, Object> overview) {
		long totalOrderCount = getLong(overview.get("totalOrderCount"));
		long paidOrderCount = getLong(overview.get("paidOrderCount"));
		long todayOrderCount = getLong(overview.get("todayOrderCount"));
		long todayPaidOrderCount = getLong(overview.get("todayPaidOrderCount"));
		overview.put("paidOrderRate", calculatePercent(paidOrderCount, totalOrderCount));
		overview.put("todayPaidOrderRate", calculatePercent(todayPaidOrderCount, todayOrderCount));
	}

	/**
	 * 补齐订单版块的客单价、完成率等派生指标。
	 *
	 * @param orderStats 订单统计
	 * @param overview 核心概览
	 */
	private void fillOrderDerivedMetrics(Map<String, Object> orderStats, Map<String, Object> overview) {
		long totalOrderCount = getLong(overview.get("totalOrderCount"));
		long paidOrderCount = getLong(overview.get("paidOrderCount"));
		long finishedCount = getLong(orderStats.get("finishedCount"));
		BigDecimal totalPaidAmount = getBigDecimal(overview.get("totalPaidAmount"));

		orderStats.put("paidOrderRate", calculatePercent(paidOrderCount, totalOrderCount));
		orderStats.put("finishRate", calculatePercent(finishedCount, totalOrderCount));
		orderStats.put("averagePaidAmount", calculateDivide(totalPaidAmount, paidOrderCount));
	}

	/**
	 * 补齐会员版块的会员转化率等派生指标。
	 *
	 * @param memberStats 会员统计
	 */
	private void fillMemberDerivedMetrics(Map<String, Object> memberStats) {
		long totalUserCount = getLong(memberStats.get("totalUserCount"));
		long memberCount = getLong(memberStats.get("memberCount"));
		long todayNewUserCount = getLong(memberStats.get("todayNewUserCount"));
		long todayNewMemberCount = getLong(memberStats.get("todayNewMemberCount"));
		memberStats.put("memberRate", calculatePercent(memberCount, totalUserCount));
		memberStats.put("todayMemberRate", calculatePercent(todayNewMemberCount, todayNewUserCount));
	}

	/**
	 * 补齐商品版块的上架率、可售率等派生指标。
	 *
	 * @param goodsStats 商品统计
	 */
	private void fillGoodsDerivedMetrics(Map<String, Object> goodsStats) {
		long totalGoodsCount = getLong(goodsStats.get("totalGoodsCount"));
		long onShelfGoodsCount = getLong(goodsStats.get("onShelfGoodsCount"));
		long totalSkuCount = getLong(goodsStats.get("totalSkuCount"));
		long enabledSkuCount = getLong(goodsStats.get("enabledSkuCount"));
		goodsStats.put("onShelfRate", calculatePercent(onShelfGoodsCount, totalGoodsCount));
		goodsStats.put("enabledSkuRate", calculatePercent(enabledSkuCount, totalSkuCount));
	}

	/**
	 * 补齐售后版块的退款完成率等派生指标。
	 *
	 * @param afterSaleStats 售后统计
	 */
	private void fillAfterSaleDerivedMetrics(Map<String, Object> afterSaleStats) {
		long totalCount = getLong(afterSaleStats.get("totalCount"));
		long refundedCount = getLong(afterSaleStats.get("refundedCount"));
		afterSaleStats.put("refundFinishRate", calculatePercent(refundedCount, totalCount));
	}

	/**
	 * 安全提取长整型统计值。
	 *
	 * @param value 原始值
	 * @return 长整型结果
	 */
	private long getLong(Object value) {
		if (value == null) {
			return 0L;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return new BigDecimal(String.valueOf(value)).longValue();
	}

	/**
	 * 安全提取金额值。
	 *
	 * @param value 原始金额
	 * @return BigDecimal 金额
	 */
	private BigDecimal getBigDecimal(Object value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		return new BigDecimal(String.valueOf(value));
	}

	/**
	 * 计算百分比。
	 * 这里统一返回百分数值，例如 56.32 表示 56.32%。
	 *
	 * @param numerator 分子
	 * @param denominator 分母
	 * @return 百分数
	 */
	private BigDecimal calculatePercent(long numerator, long denominator) {
		if (denominator <= 0) {
			return BigDecimal.ZERO;
		}
		return BigDecimal.valueOf(numerator)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
	}

	/**
	 * 计算除法结果。
	 * 主要用于客单价等金额类指标，统一保留两位小数。
	 *
	 * @param numerator 分子
	 * @param denominator 分母
	 * @return 计算结果
	 */
	private BigDecimal calculateDivide(BigDecimal numerator, long denominator) {
		if (denominator <= 0) {
			return BigDecimal.ZERO;
		}
		return numerator.divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
	}
}
