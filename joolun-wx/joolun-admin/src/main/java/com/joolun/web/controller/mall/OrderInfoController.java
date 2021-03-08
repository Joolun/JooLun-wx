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
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.constant.MallConstants;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderLogisticsService;
import com.joolun.weixin.constant.MyReturnCode;
import com.joolun.weixin.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商城订单
 *
 * @author JL
 * @date 2019-09-10 15:21:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderinfo")
@Api(value = "orderinfo", tags = "商城订单管理")
public class OrderInfoController extends BaseController {

    private final OrderInfoService orderInfoService;
	private final OrderLogisticsService orderLogisticsService;
	private final WxUserService wxUserService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param orderInfo 商城订单
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:index')")
    public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
        return AjaxResult.success(orderInfoService.page1(page, Wrappers.query(orderInfo)));
    }

	/**
	 * 查询数量
	 * @param orderInfo
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public AjaxResult getCount(OrderInfo orderInfo) {
		return AjaxResult.success(orderInfoService.count(Wrappers.query(orderInfo)));
	}

    /**
    * 通过id查询商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商城订单")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:get')")
    public AjaxResult getById(@PathVariable("id") String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		OrderLogistics orderLogistics = orderLogisticsService.getById(orderInfo.getLogisticsId());
		orderInfo.setOrderLogistics(orderLogistics);
		orderInfo.setUserInfo(wxUserService.getById(orderInfo.getUserId()));
        return AjaxResult.success(orderInfo);
    }

    /**
    * 新增商城订单
    * @param orderInfo 商城订单
    * @return R
    */
	@ApiOperation(value = "新增商城订单")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:add')")
    public AjaxResult save(@RequestBody OrderInfo orderInfo){
        return AjaxResult.success(orderInfoService.save(orderInfo));
    }

    /**
    * 修改商城订单
    * @param orderInfo 商城订单
    * @return R
    */
	@ApiOperation(value = "修改商城订单")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
    public AjaxResult updateById(@RequestBody OrderInfo orderInfo){
        return AjaxResult.success(orderInfoService.updateById(orderInfo));
    }

    /**
    * 通过id删除商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商城订单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:orderinfo:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(orderInfoService.removeById(id));
    }

	/**
	 * 取消商城订单
	 * @param id 商城订单
	 * @return R
	 */
	@ApiOperation(value = "取消商城订单")
	@PutMapping("/cancel/{id}")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult orderCancel(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!CommonConstants.NO.equals(orderInfo.getIsPay())){//只有未支付订单能取消
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		return AjaxResult.success();
	}

}
