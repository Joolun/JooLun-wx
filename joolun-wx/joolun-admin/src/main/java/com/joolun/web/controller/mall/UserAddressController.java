/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.UserAddress;
import com.joolun.mall.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收货地址
 *
 * @author JL
 * @date 2019-09-11 14:28:59
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/useraddress")
@Api(value = "useraddress", tags = "用户收货地址管理")
public class UserAddressController extends BaseController {

    private final UserAddressService userAddressService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param userAddress 用户收货地址
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:useraddress:index')")
    public AjaxResult getUserAddressPage(Page page, UserAddress userAddress) {
        return AjaxResult.success(userAddressService.page(page,Wrappers.query(userAddress)));
    }

    /**
    * 通过id查询用户收货地址
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询用户收货地址")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:useraddress:get')")
    public AjaxResult getById(@PathVariable("id") String id){
        return AjaxResult.success(userAddressService.getById(id));
    }

    /**
    * 新增用户收货地址
    * @param userAddress 用户收货地址
    * @return R
    */
	@ApiOperation(value = "新增用户收货地址")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:useraddress:add')")
    public AjaxResult save(@RequestBody UserAddress userAddress){
        return AjaxResult.success(userAddressService.save(userAddress));
    }

    /**
    * 修改用户收货地址
    * @param userAddress 用户收货地址
    * @return R
    */
	@ApiOperation(value = "修改用户收货地址")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:useraddress:edit')")
    public AjaxResult updateById(@RequestBody UserAddress userAddress){
        return AjaxResult.success(userAddressService.updateById(userAddress));
    }

    /**
    * 通过id删除用户收货地址
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除用户收货地址")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:useraddress:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(userAddressService.removeById(id));
    }

}
