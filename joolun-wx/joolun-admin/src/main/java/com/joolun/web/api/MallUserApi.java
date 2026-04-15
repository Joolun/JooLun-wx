/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import cn.hutool.core.util.StrUtil;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.dto.MallUserProfileDTO;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.service.MallUserService;
import com.joolun.web.api.support.MallUserSessionService;
import com.joolun.weixin.entity.LoginMaDTO;
import com.joolun.weixin.entity.WxOpenDataDTO;
import com.joolun.weixin.entity.WxPhoneNumberDTO;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.service.WxUserService;
import com.joolun.weixin.utils.WxMaUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 小程序商城用户接口。
 * 登录态底层仍依赖微信授权，但商城前台访问用户资料时统一围绕 mall_user 展开。
 * 为了减少前端切换成本，当前同时兼容旧的 /wxuser 路径和新的 /malluser 路径。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({"/weixin/api/ma/malluser", "/weixin/api/ma/wxuser"})
public class MallUserApi {

	private final WxUserService wxUserService;
	private final MallUserService mallUserService;
	private final MallUserSessionService mallUserSessionService;

	/**
	 * 小程序用户登录。
	 * 登录成功后立即同步商城用户档案，确保用户进入商城时已经具备商城主数据。
	 *
	 * @param request 请求对象
	 * @param loginMaDTO 登录参数
	 * @return 微信登录态信息
	 */
	@PostMapping("/login")
	public AjaxResult login(HttpServletRequest request, @RequestBody LoginMaDTO loginMaDTO) {
		try {
			WxUser wxUser = wxUserService.loginMa(WxMaUtil.getAppId(request), loginMaDTO.getJsCode());
			mallUserService.syncByWxUserId(wxUser.getId());
			return AjaxResult.success(wxUser);
		} catch (Exception e) {
			log.error("小程序用户登录失败", e);
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 获取当前商城用户信息。
	 * 每次获取前都会先刷新微信快照，保证商城用户资料尽量保持最新。
	 *
	 * @return 当前商城用户
	 */
	@GetMapping
	public AjaxResult get() {
		return AjaxResult.success(mallUserSessionService.getCurrentMallUser());
	}

	/**
	 * 保存或更新微信授权资料。
	 * 微信头像、昵称等渠道资料落库后，立即返回同步后的商城用户对象。
	 *
	 * @param wxOpenDataDTO 微信开放数据
	 * @return 更新后的商城用户
	 */
	@PostMapping
	public AjaxResult saveOrUptateWxUser(@RequestBody WxOpenDataDTO wxOpenDataDTO) {
		wxOpenDataDTO.setAppId(mallUserSessionService.getCurrentAppId());
		wxOpenDataDTO.setUserId(mallUserSessionService.getCurrentWxUserId());
		wxOpenDataDTO.setSessionKey(mallUserSessionService.getCurrentSessionKey());
		WxUser wxUser = wxUserService.saveOrUptateWxUser(wxOpenDataDTO);
		MallUser mallUser = mallUserService.syncByWxUserId(wxUser.getId());
		return AjaxResult.success(mallUser);
	}

	/**
	 * 更新当前商城用户基础资料。
	 * 这里只开放真实姓名、性别、生日等商城侧自有字段，避免前端误改渠道快照和统计字段。
	 *
	 * @param mallUserProfileDTO 资料编辑参数
	 * @return 更新后的商城用户
	 */
	@PutMapping
	public AjaxResult updateMallUserProfile(@RequestBody MallUserProfileDTO mallUserProfileDTO) {
		MallUser mallUser = mallUserSessionService.getCurrentMallUser();
		mallUser.setRealName(StrUtil.emptyToNull(StrUtil.trim(mallUserProfileDTO.getRealName())));
		mallUser.setSex(resolveSex(mallUserProfileDTO.getSex()));
		mallUser.setBirthday(parseBirthday(mallUserProfileDTO.getBirthday()));
		mallUserService.updateById(mallUser);
		return AjaxResult.success(mallUserService.getById(mallUser.getId()));
	}

	/**
	 * 绑定当前小程序用户手机号。
	 * 绑定成功后会同步刷新 mall_user，当前项目会据此自动把用户转为会员。
	 *
	 * @param wxPhoneNumberDTO 手机号授权参数
	 * @return 更新后的商城用户
	 */
	@PostMapping("/phone")
	public AjaxResult bindPhoneNumber(@RequestBody WxPhoneNumberDTO wxPhoneNumberDTO) {
		if (wxPhoneNumberDTO == null) {
			return AjaxResult.error("手机号授权参数不能为空");
		}
		wxPhoneNumberDTO.setAppId(mallUserSessionService.getCurrentAppId());
		wxPhoneNumberDTO.setUserId(mallUserSessionService.getCurrentWxUserId());
		wxPhoneNumberDTO.setSessionKey(mallUserSessionService.getCurrentSessionKey());
		WxUser wxUser = wxUserService.bindPhoneNumber(wxPhoneNumberDTO);
		MallUser mallUser = mallUserService.syncByWxUserId(wxUser.getId());
		return AjaxResult.success(mallUser);
	}

	/**
	 * 规范化性别值。
	 *
	 * @param sex 性别参数
	 * @return 规范化后的性别值
	 */
	private String resolveSex(String sex) {
		if ("1".equals(sex) || "2".equals(sex)) {
			return sex;
		}
		return "0";
	}

	/**
	 * 解析生日字段。
	 *
	 * @param birthday 生日字符串
	 * @return 生日日期
	 */
	private LocalDate parseBirthday(String birthday) {
		if (StrUtil.isBlank(birthday)) {
			return null;
		}
		try {
			return LocalDate.parse(birthday);
		} catch (Exception ex) {
			throw new IllegalArgumentException("生日格式不正确");
		}
	}
}
