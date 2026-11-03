package com.joolun.mall.support;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderPriceAdjustAllocatorTest {

	@Test
	void shouldSplitDiscountByCurrentAmount() {
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(new BigDecimal("-10"),
				List.of(new BigDecimal("40"), new BigDecimal("60")));

		assertEquals(new BigDecimal("-4.00"), shares.get(0));
		assertEquals(new BigDecimal("-6.00"), shares.get(1));
		assertEquals(new BigDecimal("-10.00"), sum(shares));
	}

	@Test
	void shouldSplitIncreaseByCurrentAmount() {
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(new BigDecimal("5"),
				List.of(new BigDecimal("40"), new BigDecimal("60")));

		assertEquals(new BigDecimal("2.00"), shares.get(0));
		assertEquals(new BigDecimal("3.00"), shares.get(1));
		assertEquals(new BigDecimal("5.00"), sum(shares));
	}

	@Test
	void shouldKeepLastShareAsRemainder() {
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(new BigDecimal("-0.01"),
				List.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1")));

		assertEquals(3, shares.size());
		assertEquals(new BigDecimal("-0.01"), sum(shares));
	}

	@Test
	void shouldSplitEvenlyWhenAllAmountsAreZero() {
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(new BigDecimal("-3"),
				List.of(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

		assertEquals(new BigDecimal("-1.00"), shares.get(0));
		assertEquals(new BigDecimal("-1.00"), shares.get(1));
		assertEquals(new BigDecimal("-1.00"), shares.get(2));
	}

	@Test
	void shouldReturnZeroSharesWhenDeltaIsZero() {
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(BigDecimal.ZERO,
				List.of(new BigDecimal("40"), new BigDecimal("60")));

		assertTrue(shares.stream().allMatch(share -> share.signum() == 0));
	}

	@Test
	void shouldReturnEmptyWhenNoAmounts() {
		assertTrue(OrderPriceAdjustAllocator.allocate(new BigDecimal("-1"), List.of()).isEmpty());
	}

	@Test
	void shouldNeverAllocateMoreThanRemaining() {
		// 三份等额金额减 0.02，前两条可能各舍入成 -0.01，最后一条必须为 0.00，不能反向变成正数。
		List<BigDecimal> shares = OrderPriceAdjustAllocator.allocate(new BigDecimal("-0.02"),
				List.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1")));

		assertEquals(new BigDecimal("-0.02"), sum(shares));
		assertTrue(shares.stream().allMatch(share -> share.signum() <= 0));
	}

	private BigDecimal sum(List<BigDecimal> shares) {
		return shares.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
