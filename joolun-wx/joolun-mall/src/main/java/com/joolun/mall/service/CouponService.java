package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.dto.CouponDiscountDTO;
import com.joolun.mall.entity.Coupon;
import com.joolun.mall.entity.CouponUser;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService extends IService<Coupon> {
	List<Coupon> listClaimable(String userId);

	List<CouponUser> listMine(String userId, String status, BigDecimal orderAmount);

	void claim(String couponId, String userId);

	CouponDiscountDTO lockForOrder(String couponUserId, String userId, String orderId, BigDecimal orderAmount);

	void markUsed(String couponUserId, String orderId);

	void release(String couponUserId, String orderId);
}
