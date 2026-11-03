package com.joolun.mall.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单改价分摊器。
 *
 * <p>后台改价是整单行为，但退款、售后都按订单项金额处理，
 * 因此改价差额必须按现有订单项金额占比分摊回去，
 * 保证“订单项实付金额之和 == 订单实付金额”这一约束不被破坏。</p>
 *
 * <p>该类无数据库依赖，便于通过单元测试固定分摊规则。</p>
 *
 * @author www.joolun.com
 */
public final class OrderPriceAdjustAllocator {

	private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

	private OrderPriceAdjustAllocator() {
	}

	/**
	 * 把改价差额按现有金额占比分摊到各明细。
	 *
	 * @param delta 改价差额，正数为加价，负数为减价
	 * @param currentAmounts 各明细当前实付金额
	 * @return 与 currentAmounts 顺序一致的分摊金额，合计等于 delta
	 */
	public static List<BigDecimal> allocate(BigDecimal delta, List<BigDecimal> currentAmounts) {
		if (currentAmounts == null || currentAmounts.isEmpty()) {
			return Collections.emptyList();
		}
		BigDecimal totalDelta = delta == null ? ZERO : delta.setScale(2, RoundingMode.HALF_UP);
		List<BigDecimal> shares = new ArrayList<>(currentAmounts.size());
		if (totalDelta.signum() == 0) {
			for (int index = 0; index < currentAmounts.size(); index++) {
				shares.add(ZERO);
			}
			return shares;
		}
		List<BigDecimal> weights = new ArrayList<>(currentAmounts.size());
		BigDecimal totalWeight = BigDecimal.ZERO;
		for (BigDecimal amount : currentAmounts) {
			BigDecimal weight = amount == null ? BigDecimal.ZERO : amount.max(BigDecimal.ZERO);
			weights.add(weight);
			totalWeight = totalWeight.add(weight);
		}
		// 明细金额全为 0 时退化为平均分摊，避免差额无处安放。
		if (totalWeight.signum() == 0) {
			for (int index = 0; index < weights.size(); index++) {
				weights.set(index, BigDecimal.ONE);
			}
			totalWeight = BigDecimal.valueOf(weights.size());
		}
		BigDecimal allocated = ZERO;
		for (int index = 0; index < weights.size(); index++) {
			BigDecimal share;
			if (index == weights.size() - 1) {
				// 最后一条吃掉舍入差额，保证合计严格等于 totalDelta。
				share = totalDelta.subtract(allocated);
			} else {
				share = totalDelta.multiply(weights.get(index))
						.divide(totalWeight, 2, RoundingMode.HALF_UP);
				BigDecimal remaining = totalDelta.subtract(allocated);
				// 按符号方向限制，避免某一条分摊超过剩余差额。
				share = totalDelta.signum() >= 0
						? share.min(remaining).max(BigDecimal.ZERO)
						: share.max(remaining).min(BigDecimal.ZERO);
				allocated = allocated.add(share);
			}
			shares.add(share.setScale(2, RoundingMode.HALF_UP));
		}
		return shares;
	}
}
