/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.support;

import com.joolun.mall.entity.FreightTemplat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费计算器。
 *
 * <p>计算口径：</p>
 * <ol>
 *   <li>一次下单内按运费模板分组，同一模板的商品合并计算首件与续件，不同模板各自收取首运费。</li>
 *   <li>计费量由模板的计费方式决定：按件数取数量，按重量取"重量 × 数量"，按体积取"体积 × 数量"。</li>
 *   <li>卖家包邮模板直接为 0；上门自提由调用方传入不计运费。</li>
 *   <li>模板运费按各明细的计费量占比回摊，最后一条吃掉舍入差额，
 *       保证"明细运费之和 == 模板运费"，退款按明细分摊金额退回时不会多退或少退。</li>
 * </ol>
 *
 * <p>该类无数据库依赖，便于通过单元测试固定金额和分摊规则。</p>
 *
 * @author www.joolun.com
 */
public final class FreightCalculator {

	/** 模板类型：买家承担运费。 */
	public static final String TEMPLAT_TYPE_BUYER = "1";

	/** 模板类型：卖家包邮。 */
	public static final String TEMPLAT_TYPE_FREE = "2";

	/** 计费方式：按件数。 */
	public static final String CHARGE_TYPE_QUANTITY = "1";

	/** 计费方式：按重量。 */
	public static final String CHARGE_TYPE_WEIGHT = "2";

	/** 计费方式：按体积。 */
	public static final String CHARGE_TYPE_VOLUME = "3";

	private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

	private FreightCalculator() {
	}

	/**
	 * 计算运费（默认计运费）。
	 *
	 * @param lines 结算明细
	 * @param rules 模板 ID -> 计价规则，缺失的模板按不计运费处理
	 * @return 运费计算结果
	 */
	public static FreightResult calculate(List<FreightLine> lines, Map<String, FreightRule> rules) {
		return calculate(lines, rules, true);
	}

	/**
	 * 计算运费。
	 *
	 * @param lines 结算明细
	 * @param rules 模板 ID -> 计价规则，缺失的模板按不计运费处理
	 * @param chargeFreight 是否计运费，上门自提时传 false
	 * @return 运费计算结果
	 */
	public static FreightResult calculate(List<FreightLine> lines, Map<String, FreightRule> rules,
			boolean chargeFreight) {
		if (lines == null || lines.isEmpty()) {
			return new FreightResult(ZERO, Collections.emptyList());
		}
		BigDecimal[] amounts = new BigDecimal[lines.size()];
		Arrays.fill(amounts, ZERO);
		if (chargeFreight) {
			// 分组顺序保持明细首次出现的顺序，便于排查和测试断言。
			Map<String, List<Integer>> groups = new LinkedHashMap<>();
			for (int index = 0; index < lines.size(); index++) {
				String templateId = lines.get(index).templateId();
				if (templateId == null || templateId.isBlank()) {
					continue;
				}
				groups.computeIfAbsent(templateId, key -> new ArrayList<>()).add(index);
			}
			for (Map.Entry<String, List<Integer>> group : groups.entrySet()) {
				FreightRule rule = rules == null ? null : rules.get(group.getKey());
				if (rule == null) {
					continue;
				}
				BigDecimal[] shares = computeShares(rule, lines, group.getValue());
				for (int position = 0; position < shares.length; position++) {
					amounts[group.getValue().get(position)] = shares[position];
				}
			}
		}
		BigDecimal total = ZERO;
		List<FreightLineResult> results = new ArrayList<>(lines.size());
		for (int index = 0; index < lines.size(); index++) {
			BigDecimal amount = amounts[index].setScale(2, RoundingMode.HALF_UP);
			total = total.add(amount);
			results.add(new FreightLineResult(lines.get(index).lineId(), amount));
		}
		return new FreightResult(total.setScale(2, RoundingMode.HALF_UP), results);
	}

	/**
	 * 计算单条计费明细的计费量。
	 *
	 * @param chargeType 计费方式
	 * @param line 结算明细
	 * @return 计费量
	 */
	public static BigDecimal billableOf(String chargeType, FreightLine line) {
		BigDecimal quantity = BigDecimal.valueOf(Math.max(line.quantity(), 0));
		if (CHARGE_TYPE_WEIGHT.equals(chargeType)) {
			return nz(line.weight()).multiply(quantity);
		}
		if (CHARGE_TYPE_VOLUME.equals(chargeType)) {
			return nz(line.volume()).multiply(quantity);
		}
		return quantity;
	}

	/**
	 * 计算同一模板下所有明细的运费，并按计费量占比回摊到各明细。
	 *
	 * @param rule 计价规则
	 * @param lines 全部结算明细
	 * @param indexes 属于该模板的明细下标
	 * @return 与 indexes 顺序一致的运费份额
	 */
	private static BigDecimal[] computeShares(FreightRule rule, List<FreightLine> lines, List<Integer> indexes) {
		BigDecimal[] weights = new BigDecimal[indexes.size()];
		BigDecimal totalWeight = BigDecimal.ZERO;
		for (int position = 0; position < indexes.size(); position++) {
			weights[position] = billableOf(rule.chargeType(), lines.get(indexes.get(position)));
			totalWeight = totalWeight.add(weights[position]);
		}
		BigDecimal groupFreight = groupFreight(rule, totalWeight);
		BigDecimal[] shares = new BigDecimal[indexes.size()];
		Arrays.fill(shares, ZERO);
		if (groupFreight.signum() <= 0) {
			return shares;
		}
		// 计费量全为 0（例如按重量计费但运营没填重量）时退化用商品金额分摊，
		// 金额也全为 0 时把运费全部落在该模板的第一条明细上，避免运费丢失。
		if (totalWeight.signum() <= 0) {
			totalWeight = BigDecimal.ZERO;
			for (int position = 0; position < indexes.size(); position++) {
				weights[position] = nz(lines.get(indexes.get(position)).salesAmount());
				totalWeight = totalWeight.add(weights[position]);
			}
		}
		if (totalWeight.signum() <= 0) {
			shares[0] = groupFreight.setScale(2, RoundingMode.HALF_UP);
			return shares;
		}
		BigDecimal allocated = ZERO;
		for (int position = 0; position < indexes.size(); position++) {
			BigDecimal share;
			if (position == indexes.size() - 1) {
				share = groupFreight.subtract(allocated);
			} else {
				share = groupFreight.multiply(weights[position])
						.divide(totalWeight, 2, RoundingMode.HALF_UP)
						.min(groupFreight.subtract(allocated))
						.max(BigDecimal.ZERO);
				allocated = allocated.add(share);
			}
			shares[position] = share.setScale(2, RoundingMode.HALF_UP);
		}
		return shares;
	}

	/**
	 * 按首件 + 续件规则计算模板运费。
	 *
	 * @param rule 计价规则
	 * @param billable 该模板下所有明细的计费量合计
	 * @return 模板运费
	 */
	private static BigDecimal groupFreight(FreightRule rule, BigDecimal billable) {
		if (TEMPLAT_TYPE_FREE.equals(rule.type())) {
			return ZERO;
		}
		BigDecimal firstNum = nz(rule.firstNum());
		BigDecimal firstFreight = nz(rule.firstFreight());
		BigDecimal continueNum = nz(rule.continueNum());
		BigDecimal continueFreight = nz(rule.continueFreight());
		BigDecimal safeBillable = billable == null ? BigDecimal.ZERO : billable;
		if (continueNum.signum() <= 0 || safeBillable.compareTo(firstNum) <= 0) {
			return firstFreight.setScale(2, RoundingMode.HALF_UP);
		}
		BigDecimal steps = safeBillable.subtract(firstNum).divide(continueNum, 0, RoundingMode.CEILING);
		if (steps.signum() < 0) {
			steps = BigDecimal.ZERO;
		}
		return firstFreight.add(steps.multiply(continueFreight)).setScale(2, RoundingMode.HALF_UP);
	}

	private static BigDecimal nz(BigDecimal value) {
		return value == null ? BigDecimal.ZERO : value;
	}

	/**
	 * 计价规则，来自运费模板。
	 */
	public record FreightRule(String id, String type, String chargeType, BigDecimal firstNum,
			BigDecimal firstFreight, BigDecimal continueNum, BigDecimal continueFreight) {

		public static FreightRule from(FreightTemplat templat) {
			if (templat == null) {
				return null;
			}
			return new FreightRule(templat.getId(), templat.getType(), templat.getChargeType(),
					templat.getFirstNum(), templat.getFirstFreight(),
					templat.getContinueNum(), templat.getContinueFreight());
		}
	}

	/**
	 * 结算明细。
	 *
	 * @param lineId 明细标识，回传时保持一一对应
	 * @param templateId 运费模板 ID，为空表示该明细不计运费
	 * @param quantity 数量
	 * @param weight SKU 重量(kg)
	 * @param volume SKU 体积(m³)
	 * @param salesAmount 明细商品金额，仅用于计费量为 0 时兜底分摊
	 */
	public record FreightLine(String lineId, String templateId, int quantity, BigDecimal weight,
			BigDecimal volume, BigDecimal salesAmount) {

		public FreightLine {
			if (lineId == null || quantity < 0 || nz(weight).signum() < 0
					|| nz(volume).signum() < 0 || nz(salesAmount).signum() < 0) {
				throw new IllegalArgumentException("结算运费明细参数不正确");
			}
		}
	}

	/**
	 * 单条明细的运费分摊结果。
	 */
	public record FreightLineResult(String lineId, BigDecimal freightAmount) {
	}

	/**
	 * 运费计算结果。
	 */
	public record FreightResult(BigDecimal totalAmount, List<FreightLineResult> lines) {
	}
}
