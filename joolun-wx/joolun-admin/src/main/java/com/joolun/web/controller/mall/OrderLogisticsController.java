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
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.enums.OrderLogisticsEnum;
import com.joolun.mall.service.OrderLogisticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 订单物流
 *
 * @author www.joolun.com
 * @date 2019-09-16 09:53:17
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderlogistics")
public class OrderLogisticsController extends BaseController {

    private final OrderLogisticsService orderLogisticsService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param orderLogistics 订单物流
    * @return
    */
    @GetMapping("/page")
    @PreAuthorize("@ato.hasAuthority('mall:orderlogistics:index')")
    public AjaxResult getOrderLogisticsPage(Page page, OrderLogistics orderLogistics) {
        return AjaxResult.success(orderLogisticsService.page(page,Wrappers.query(orderLogistics)));
    }

    /**
    * 通过id查询订单物流
    * @param id
    * @return R
    */
    @GetMapping("/{id}")
    @PreAuthorize("@ato.hasAuthority('mall:orderlogistics:get')")
    public AjaxResult getById(@PathVariable("id") String id){
        return AjaxResult.success(orderLogisticsService.getById(id));
    }

    /**
    * 新增订单物流
    * @param orderLogistics 订单物流
    * @return R
    */
    @PostMapping
    @PreAuthorize("@ato.hasAuthority('mall:orderlogistics:add')")
    public AjaxResult save(@RequestBody OrderLogistics orderLogistics){
        return AjaxResult.success(orderLogisticsService.save(orderLogistics));
    }

    /**
    * 修改订单物流
    * @param orderLogistics 订单物流
    * @return R
    */
    @PutMapping
    @PreAuthorize("@ato.hasAuthority('mall:orderlogistics:edit')")
    public AjaxResult updateById(@RequestBody OrderLogistics orderLogistics){
        return AjaxResult.success(orderLogisticsService.updateById(orderLogistics));
    }

    /**
    * 通过id删除订单物流
    * @param id
    * @return R
    */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ato.hasAuthority('mall:orderlogistics:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(orderLogisticsService.removeById(id));
    }

	/**
	 * 获取相关枚举数据的字典
	 * @param type
	 * @return
	 */
	@GetMapping("/dict/{type}")
	public AjaxResult getDictByType(@PathVariable String type) {
		return AjaxResult.success(OrderLogisticsEnum.queryAll(type));
	}
}
