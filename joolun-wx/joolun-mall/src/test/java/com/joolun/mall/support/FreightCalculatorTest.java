package com.joolun.mall.support;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FreightCalculatorTest {

	/** 按件数：首 2 件 5 元，每续 2 件 3 元。 */
	private static final FreightCalculator.FreightRule QUANTITY_RULE = new FreightCalculator.FreightRule(
			"tpl-quantity", FreightCalculator.TEMPLAT_TYPE_BUYER, FreightCalculator.CHARGE_TYPE_QUANTITY,
			new BigDecimal("2"), new BigDecimal("5"), new BigDecimal("2"), new BigDecimal("3"));

	/** 按重量：首 1kg 5 元，每续 0.5kg 2 元。 */
	private static final FreightCalculator.FreightRule WEIGHT_RULE = new FreightCalculator.FreightRule(
			"tpl-weight", FreightCalculator.TEMPLAT_TYPE_BUYER, FreightCalculator.CHARGE_TYPE_WEIGHT,
			new BigDecimal("1"), new BigDecimal("5"), new BigDecimal("0.5"), new BigDecimal("2"));

	/** 卖家包邮。 */
	private static final FreightCalculator.FreightRule FREE_RULE = new FreightCalculator.FreightRule(
			"tpl-free", FreightCalculator.TEMPLAT_TYPE_FREE, FreightCalculator.CHARGE_TYPE_QUANTITY,
			BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

	private static FreightCalculator.FreightLine quantityLine(String id, String templateId, int quantity) {
		return new FreightCalculator.FreightLine(id, templateId, quantity,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE);
	}

	@Test
	void shouldChargeFirstPieceThenContinuePiece() {
		Map<String, FreightCalculator.FreightRule> rules = Map.of("tpl-quantity", QUANTITY_RULE);

		assertEquals(new BigDecimal("5.00"), calculate(quantityLine("a", "tpl-quantity", 1), rules));
		assertEquals(new BigDecimal("5.00"), calculate(quantityLine("a", "tpl-quantity", 2), rules));
		// 3 件 = 首 2 件 5 元 + 续 1 件按 1 个续件段计 3 元
		assertEquals(new BigDecimal("8.00"), calculate(quantityLine("a", "tpl-quantity", 3), rules));
		// 5 件 = 5 + ceil(3/2) * 3
		assertEquals(new BigDecimal("11.00"), calculate(quantityLine("a", "tpl-quantity", 5), rules));
	}

	@Test
	void shouldChargeByWeightWithDecimalStep() {
		Map<String, FreightCalculator.FreightRule> rules = Map.of("tpl-weight", WEIGHT_RULE);
		FreightCalculator.FreightLine line = new FreightCalculator.FreightLine("a", "tpl-weight", 1,
				new BigDecimal("1.5"), BigDecimal.ZERO, BigDecimal.TEN);

		// 1.5kg = 首重 1kg 5 元 + 续重 0.5kg 2 元
		assertEquals(new BigDecimal("7.00"), calculate(line, rules));
	}

	@Test
	void shouldNotChargeFreeTemplate() {
		Map<String, FreightCalculator.FreightRule> rules = Map.of("tpl-free", FREE_RULE);
		assertEquals(new BigDecimal("0.00"), calculate(quantityLine("a", "tpl-free", 10), rules));
	}

	@Test
	void shouldNotChargeWhenPickUpBySelf() {
		FreightCalculator.FreightResult result = FreightCalculator.calculate(
				List.of(quantityLine("a", "tpl-quantity", 10), quantityLine("b", "tpl-quantity", 20)),
				Map.of("tpl-quantity", QUANTITY_RULE), false);

		assertEquals(new BigDecimal("0.00"), result.totalAmount());
		assertEquals(new BigDecimal("0.00"), result.lines().get(0).freightAmount());
		assertEquals(new BigDecimal("0.00"), result.lines().get(1).freightAmount());
	}

	@Test
	void shouldShareFirstPieceBetweenLinesOfSameTemplate() {
		FreightCalculator.FreightResult result = FreightCalculator.calculate(List.of(
				quantityLine("a", "tpl-quantity", 1),
				quantityLine("b", "tpl-quantity", 3)),
				Map.of("tpl-quantity", QUANTITY_RULE));

		// 合计 4 件 = 5 + ceil(2/2) * 3 = 8，按件数占比 1:3 分摊
		assertEquals(new BigDecimal("8.00"), result.totalAmount());
		assertEquals(new BigDecimal("2.00"), result.lines().get(0).freightAmount());
		assertEquals(new BigDecimal("6.00"), result.lines().get(1).freightAmount());
	}

	@Test
	void shouldChargeFirstFreightPerDifferentTemplate() {
		Map<String, FreightCalculator.FreightRule> rules = Map.of(
				"tpl-quantity", QUANTITY_RULE,
				"tpl-free", FREE_RULE);
		FreightCalculator.FreightResult result = FreightCalculator.calculate(List.of(
				quantityLine("a", "tpl-quantity", 1),
				quantityLine("b", "tpl-free", 1)), rules);

		assertEquals(new BigDecimal("5.00"), result.totalAmount());
	}

	@Test
	void shouldNotChargeLinesWithoutTemplateOrRule() {
		FreightCalculator.FreightResult result = FreightCalculator.calculate(List.of(
				quantityLine("a", null, 3),
				quantityLine("b", "tpl-missing", 3)),
				Map.of("tpl-quantity", QUANTITY_RULE));

		assertEquals(new BigDecimal("0.00"), result.totalAmount());
		assertEquals("a", result.lines().get(0).lineId());
		assertEquals("b", result.lines().get(1).lineId());
	}

	@Test
	void shouldOnlyChargeFirstFreightWhenContinueNumberIsZero() {
		FreightCalculator.FreightRule rule = new FreightCalculator.FreightRule(
				"tpl-single", FreightCalculator.TEMPLAT_TYPE_BUYER, FreightCalculator.CHARGE_TYPE_QUANTITY,
				new BigDecimal("1"), new BigDecimal("4"), BigDecimal.ZERO, new BigDecimal("9"));

		assertEquals(new BigDecimal("4.00"),
				calculate(quantityLine("a", "tpl-single", 100), Map.of("tpl-single", rule)));
	}

	@Test
	void shouldFallbackToSalesAmountWhenBillableWeightIsZero() {
		// 按重量计费但运营没填重量：用商品金额分摊，运费不丢失。
		Map<String, FreightCalculator.FreightRule> rules = Map.of("tpl-weight", WEIGHT_RULE);
		FreightCalculator.FreightResult result = FreightCalculator.calculate(List.of(
				new FreightCalculator.FreightLine("a", "tpl-weight", 1, BigDecimal.ZERO, BigDecimal.ZERO,
						new BigDecimal("10")),
				new FreightCalculator.FreightLine("b", "tpl-weight", 1, BigDecimal.ZERO, BigDecimal.ZERO,
						new BigDecimal("30"))), rules);

		assertEquals(new BigDecimal("5.00"), result.totalAmount());
		assertEquals(new BigDecimal("1.25"), result.lines().get(0).freightAmount());
		assertEquals(new BigDecimal("3.75"), result.lines().get(1).freightAmount());
	}

	@Test
	void shouldKeepEveryLineResultInOrderWhenMixed() {
		FreightCalculator.FreightResult result = FreightCalculator.calculate(List.of(
				quantityLine("first", "tpl-free", 1),
				quantityLine("second", "tpl-quantity", 3),
				quantityLine("third", "tpl-quantity", 1)), Map.of(
				"tpl-free", FREE_RULE,
				"tpl-quantity", QUANTITY_RULE));

		assertEquals(3, result.lines().size());
		assertEquals("first", result.lines().get(0).lineId());
		assertEquals("second", result.lines().get(1).lineId());
		assertEquals("third", result.lines().get(2).lineId());
		// 同模板合计 4 件 = 8 元，按 3:1 分摊
		assertEquals(new BigDecimal("6.00"), result.lines().get(1).freightAmount());
		assertEquals(new BigDecimal("2.00"), result.lines().get(2).freightAmount());
		assertEquals(new BigDecimal("8.00"), result.totalAmount());
	}

	@Test
	void shouldRejectInvalidLine() {
		assertThrows(IllegalArgumentException.class, () -> new FreightCalculator.FreightLine(
				"a", "tpl", -1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE));
	}

	private BigDecimal calculate(FreightCalculator.FreightLine line,
			Map<String, FreightCalculator.FreightRule> rules) {
		return FreightCalculator.calculate(List.of(line), rules).totalAmount();
	}
}
