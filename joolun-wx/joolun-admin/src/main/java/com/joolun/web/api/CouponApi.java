package com.joolun.web.api;

import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.service.CouponService;
import com.joolun.web.api.support.MallUserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/** 小程序优惠券中心。 */
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/coupon")
public class CouponApi {
	private final CouponService couponService;
	private final MallUserSessionService mallUserSessionService;

	@GetMapping("/center")
	public AjaxResult center() {
		return AjaxResult.success(couponService.listClaimable(mallUserSessionService.getCurrentMallUserId()));
	}

	@GetMapping("/mine")
	public AjaxResult mine(String status, BigDecimal orderAmount) {
		return AjaxResult.success(couponService.listMine(
				mallUserSessionService.getCurrentMallUserId(), status, orderAmount));
	}

	@PostMapping("/{couponId}/claim")
	public AjaxResult claim(@PathVariable String couponId) {
		couponService.claim(couponId, mallUserSessionService.getCurrentMallUserId());
		return AjaxResult.success();
	}
}
