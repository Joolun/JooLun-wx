package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.MemberLevel;
import com.joolun.mall.service.MemberLevelService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/** 简单会员等级后台管理。 */
@RestController
@AllArgsConstructor
@RequestMapping("/memberlevel")
public class MemberLevelController extends BaseController {

	private final MemberLevelService memberLevelService;

	@GetMapping("/list")
	@PreAuthorize("@ss.hasPermi('mall:memberlevel:index')")
	public AjaxResult list() {
		return AjaxResult.success(memberLevelService.list(Wrappers.<MemberLevel>lambdaQuery()
				.orderByAsc(MemberLevel::getMinGrowth)));
	}

	@PostMapping
	@PreAuthorize("@ss.hasPermi('mall:memberlevel:add')")
	public AjaxResult save(@RequestBody MemberLevel level) {
		validate(level);
		level.setCreateTime(LocalDateTime.now());
		level.setUpdateTime(LocalDateTime.now());
		level.setDelFlag("0");
		return AjaxResult.success(memberLevelService.save(level));
	}

	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:memberlevel:edit')")
	public AjaxResult update(@RequestBody MemberLevel level) {
		validate(level);
		level.setUpdateTime(LocalDateTime.now());
		return AjaxResult.success(memberLevelService.updateById(level));
	}

	private void validate(MemberLevel level) {
		if (level.getName() == null || level.getName().isBlank() || level.getLevelValue() == null
				|| level.getMinGrowth() == null || level.getMinGrowth() < 0 || level.getDiscountRate() == null
				|| level.getDiscountRate().signum() <= 0 || level.getDiscountRate().compareTo(new java.math.BigDecimal("100")) > 0) {
			throw new IllegalArgumentException("请填写正确的等级、成长值和折扣");
		}
	}
}
