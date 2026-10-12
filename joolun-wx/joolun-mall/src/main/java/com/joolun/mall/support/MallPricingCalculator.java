package com.joolun.mall.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城结算计算器。
 *
 * <p>会员折扣先于优惠券计算，优惠券只抵扣商品金额，运费不参与优惠。
 * 该类无数据库依赖，便于通过单元测试固定金额和分摊规则。</p>
 */
public final class MallPricingCalculator {

	private static final BigDecimal HUNDRED = new BigDecimal("100");

	private MallPricingCalculator() {
	}

	public static PricingResult calculate(List<PricingLine> lines, BigDecimal discountRate,
			BigDecimal couponDiscount) {
		if (lines == null || lines.isEmpty()) {
			throw new IllegalArgumentException("结算商品不能为空");
		}
		BigDecimal rate = discountRate == null ? HUNDRED : discountRate;
		if (rate.signum() <= 0 || rate.compareTo(HUNDRED) > 0) {
			throw new IllegalArgumentException("会员折扣必须大于0且不超过100");
		}
		BigDecimal salesTotal = lines.stream().map(PricingLine::salesAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
		BigDecimal freightTotal = lines.stream().map(PricingLine::freightAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
		BigDecimal discountedSales = salesTotal.multiply(rate).divide(HUNDRED, 2, RoundingMode.HALF_UP);
		BigDecimal memberDiscount = salesTotal.subtract(discountedSales);
		BigDecimal coupon = couponDiscount == null ? BigDecimal.ZERO : couponDiscount.max(BigDecimal.ZERO);
		coupon = coupon.min(discountedSales).setScale(2, RoundingMode.HALF_UP);

		List<BigDecimal> memberShares = allocate(memberDiscount, lines, salesTotal);
		List<BigDecimal> couponShares = allocate(coupon, lines, salesTotal);
		List<PricingLineResult> results = new ArrayList<>(lines.size());
		for (int index = 0; index < lines.size(); index++) {
			PricingLine line = lines.get(index);
			BigDecimal payment = line.salesAmount().add(line.freightAmount())
					.subtract(memberShares.get(index)).subtract(couponShares.get(index))
					.setScale(2, RoundingMode.HALF_UP);
			results.add(new PricingLineResult(line.lineId(), memberShares.get(index), couponShares.get(index), payment));
		}
		return new PricingResult(salesTotal, freightTotal, memberDiscount, coupon,
				discountedSales.subtract(coupon).add(freightTotal), results);
	}

	private static List<BigDecimal> allocate(BigDecimal total, List<PricingLine> lines, BigDecimal salesTotal) {
		List<BigDecimal> shares = new ArrayList<>(lines.size());
		BigDecimal allocated = BigDecimal.ZERO;
		for (int index = 0; index < lines.size(); index++) {
			BigDecimal share;
			if (index == lines.size() - 1) {
				share = total.subtract(allocated);
			} else if (salesTotal.signum() == 0) {
				share = BigDecimal.ZERO.setScale(2);
			} else {
				share = total.multiply(lines.get(index).salesAmount())
						.divide(salesTotal, 2, RoundingMode.HALF_UP);
				share = share.min(total.subtract(allocated)).max(BigDecimal.ZERO);
				allocated = allocated.add(share);
			}
			shares.add(share.setScale(2, RoundingMode.HALF_UP));
		}
		return shares;
	}

	public record PricingLine(String lineId, BigDecimal salesAmount, BigDecimal freightAmount) {
		public PricingLine {
			if (lineId == null || salesAmount == null || freightAmount == null
					|| salesAmount.signum() < 0 || freightAmount.signum() < 0) {
				throw new IllegalArgumentException("结算明细参数不正确");
			}
		}
	}

	public record PricingLineResult(String lineId, BigDecimal memberDiscountAmount,
			BigDecimal couponDiscountAmount, BigDecimal paymentAmount) {
	}

	public record PricingResult(BigDecimal salesAmount, BigDecimal freightAmount,
			BigDecimal memberDiscountAmount, BigDecimal couponDiscountAmount,
			BigDecimal paymentAmount, List<PricingLineResult> lines) {
	}
}
