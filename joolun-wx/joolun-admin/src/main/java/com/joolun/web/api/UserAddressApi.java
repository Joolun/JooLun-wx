/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.UserAddress;
import com.joolun.mall.service.UserAddressService;
import com.joolun.web.api.support.MallUserSessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序用户收货地址接口。
 * 收货地址表中的 userId 统一使用商城用户主键，接口层负责会话态到商城用户的映射。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/useraddress")
public class UserAddressApi {

	private final UserAddressService userAddressService;
	private final MallUserSessionService mallUserSessionService;

	/**
	 * 分页查询当前用户收货地址。
	 *
	 * @param page 分页对象
	 * @param userAddress 查询条件
	 * @return 地址分页数据
	 */
	@GetMapping("/page")
	public AjaxResult getUserAddressPage(Page page, UserAddress userAddress) {
		userAddress.setUserId(mallUserSessionService.getCurrentMallUserId());
		return AjaxResult.success(userAddressService.page(page, Wrappers.query(userAddress)));
	}

	/**
	 * 新增或修改当前用户收货地址。
	 * 当携带 id 更新时，会先校验地址归属，防止修改他人地址。
	 *
	 * @param userAddress 收货地址
	 * @return 处理结果
	 */
	@PostMapping
	public AjaxResult save(@RequestBody UserAddress userAddress) {
		String currentMallUserId = mallUserSessionService.getCurrentMallUserId();
		if (userAddress.getId() != null) {
			UserAddress current = userAddressService.getById(userAddress.getId());
			if (current == null || !currentMallUserId.equals(current.getUserId())) {
				return AjaxResult.error("收货地址不存在");
			}
		}
		userAddress.setUserId(currentMallUserId);
		return AjaxResult.success(userAddressService.saveOrUpdate(userAddress));
	}

	/**
	 * 删除当前用户收货地址。
	 *
	 * @param id 地址主键
	 * @return 处理结果
	 */
	@DeleteMapping("/{id}")
	public AjaxResult removeById(@PathVariable String id) {
		return AjaxResult.success(userAddressService.remove(Wrappers.<UserAddress>lambdaQuery()
				.eq(UserAddress::getId, id)
				.eq(UserAddress::getUserId, mallUserSessionService.getCurrentMallUserId())));
	}
}
