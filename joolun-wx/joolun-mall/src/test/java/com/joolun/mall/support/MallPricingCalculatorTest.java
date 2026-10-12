package com.joolun.mall.support;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MallPricingCalculatorTest {

	@Test
	void shouldApplyMemberDiscountThenCouponAndKeepFreight() {
		MallPricingCalculator.PricingResult result = MallPricingCalculator.calculate(List.of(
				new MallPricingCalculator.PricingLine("a", new BigDecimal("80"), new BigDecimal("5")),
				new MallPricingCalculator.PricingLine("b", new BigDecimal("20"), BigDecimal.ZERO)),
				new BigDecimal("95"), new BigDecimal("10"));

		assertEquals(new BigDecimal("100.00"), result.salesAmount());
		assertEquals(new BigDecimal("5.00"), result.memberDiscountAmount());
		assertEquals(new BigDecimal("10.00"), result.couponDiscountAmount());
		assertEquals(new BigDecimal("90.00"), result.paymentAmount());
		assertEquals(new BigDecimal("4.00"), result.lines().get(0).memberDiscountAmount());
		assertEquals(new BigDecimal("8.00"), result.lines().get(0).couponDiscountAmount());
	}

	@Test
	void shouldCapCouponAtDiscountedMerchandiseAmount() {
		MallPricingCalculator.PricingResult result = MallPricingCalculator.calculate(List.of(
				new MallPricingCalculator.PricingLine("a", new BigDecimal("8"), new BigDecimal("3"))),
				new BigDecimal("100"), new BigDecimal("20"));

		assertEquals(new BigDecimal("8.00"), result.couponDiscountAmount());
		assertEquals(new BigDecimal("3.00"), result.paymentAmount());
	}

	@Test
	void shouldRejectInvalidMemberDiscount() {
		assertThrows(IllegalArgumentException.class, () -> MallPricingCalculator.calculate(List.of(
				new MallPricingCalculator.PricingLine("a", BigDecimal.ONE, BigDecimal.ZERO)),
				new BigDecimal("101"), BigDecimal.ZERO));
	}

	@Test
	void shouldNeverCreateNegativeRemainderWhenSeveralSharesRoundUp() {
		List<MallPricingCalculator.PricingLine> lines = List.of(
				new MallPricingCalculator.PricingLine("a", BigDecimal.ONE, BigDecimal.ZERO),
				new MallPricingCalculator.PricingLine("b", BigDecimal.ONE, BigDecimal.ZERO),
				new MallPricingCalculator.PricingLine("c", BigDecimal.ONE, BigDecimal.ZERO),
				new MallPricingCalculator.PricingLine("d", BigDecimal.ONE, BigDecimal.ZERO));

		MallPricingCalculator.PricingResult result = MallPricingCalculator.calculate(
				lines, new BigDecimal("99.5"), BigDecimal.ZERO);

		assertEquals(new BigDecimal("0.02"), result.memberDiscountAmount());
		assertEquals(new BigDecimal("0.00"), result.lines().get(3).memberDiscountAmount());
	}
}
