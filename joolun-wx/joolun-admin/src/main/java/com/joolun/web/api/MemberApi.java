package com.joolun.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.dto.MemberProfileDTO;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.MemberPointLog;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.MemberLevelService;
import com.joolun.mall.service.MemberPointLogService;
import com.joolun.web.api.support.MallUserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 小程序会员中心。 */
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/member")
public class MemberApi {
	private final MallUserSessionService mallUserSessionService;
	private final MallUserService mallUserService;
	private final MemberLevelService memberLevelService;
	private final MemberPointLogService memberPointLogService;

	@GetMapping("/profile")
	public AjaxResult profile() {
		MallUser user = mallUserService.getById(mallUserSessionService.getCurrentMallUserId());
		int growthValue = user == null || user.getGrowthValue() == null ? 0 : user.getGrowthValue();
		MemberProfileDTO profile = new MemberProfileDTO(user,
				memberLevelService.findCurrent(growthValue), memberLevelService.findNext(growthValue));
		return AjaxResult.success(profile);
	}

	@GetMapping("/points")
	public AjaxResult points(Page<MemberPointLog> page) {
		return AjaxResult.success(memberPointLogService.page(page, Wrappers.<MemberPointLog>lambdaQuery()
				.eq(MemberPointLog::getUserId, mallUserSessionService.getCurrentMallUserId())
				.orderByDesc(MemberPointLog::getCreateTime)));
	}
}
