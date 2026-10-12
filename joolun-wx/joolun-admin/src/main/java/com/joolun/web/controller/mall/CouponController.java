package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.Coupon;
import com.joolun.mall.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/** 全场满减券后台管理。 */
@RestController
@AllArgsConstructor
@RequestMapping("/coupon")
public class CouponController extends BaseController {

	private final CouponService couponService;

	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:coupon:index')")
	public AjaxResult page(Page<Coupon> page, Coupon coupon) {
		return AjaxResult.success(couponService.page(page, Wrappers.<Coupon>lambdaQuery()
				.like(coupon.getName() != null, Coupon::getName, coupon.getName())
				.eq(coupon.getStatus() != null, Coupon::getStatus, coupon.getStatus())
				.orderByDesc(Coupon::getCreateTime)));
	}

	@PostMapping
	@PreAuthorize("@ss.hasPermi('mall:coupon:add')")
	public AjaxResult save(@RequestBody Coupon coupon) {
		validate(coupon);
		coupon.setReceivedQuantity(0);
		coupon.setUsedQuantity(0);
		coupon.setCreateTime(LocalDateTime.now());
		coupon.setUpdateTime(LocalDateTime.now());
		coupon.setDelFlag("0");
		coupon.setVersion(0);
		return AjaxResult.success(couponService.save(coupon));
	}

	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:coupon:edit')")
	public AjaxResult update(@RequestBody Coupon coupon) {
		validate(coupon);
		coupon.setUpdateTime(LocalDateTime.now());
		coupon.setReceivedQuantity(null);
		coupon.setUsedQuantity(null);
		return AjaxResult.success(couponService.updateById(coupon));
	}

	private void validate(Coupon coupon) {
		if (coupon.getName() == null || coupon.getName().isBlank()
				|| coupon.getThresholdAmount() == null || coupon.getThresholdAmount().signum() < 0
				|| coupon.getDiscountAmount() == null || coupon.getDiscountAmount().signum() <= 0
				|| coupon.getStartTime() == null || coupon.getEndTime() == null
				|| !coupon.getEndTime().isAfter(coupon.getStartTime())) {
			throw new IllegalArgumentException("请填写正确的名称、金额和有效期");
		}
		if (coupon.getTotalQuantity() == null || coupon.getTotalQuantity() < 0) {
			throw new IllegalArgumentException("发行量不能小于0，0表示不限量");
		}
	}
}
