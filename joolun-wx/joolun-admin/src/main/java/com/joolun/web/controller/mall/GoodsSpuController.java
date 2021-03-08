/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.service.GoodsSpuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * spu商品
 *
 * @author JL
 * @date 2019-08-12 16:25:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsspu")
@Api(value = "goodsspu", tags = "spu商品管理")
public class GoodsSpuController extends BaseController {

    private final GoodsSpuService goodsSpuService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpu spu商品
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:index')")
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu) {
		return AjaxResult.success(goodsSpuService.page1(page, goodsSpu));
    }

	/**
	 * list查询
	 * @param goodsSpu
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	public List<GoodsSpu> getList(GoodsSpu goodsSpu) {
		return goodsSpuService.list(Wrappers.query(goodsSpu).lambda()
						.select(GoodsSpu::getId,
								GoodsSpu::getName)
				);
	}

	/**
	 * 查询数量
	 * @param goodsSpu
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public AjaxResult getCount(GoodsSpu goodsSpu) {
		return AjaxResult.success(goodsSpuService.count(Wrappers.query(goodsSpu)));
	}

    /**
    * 通过id查询spu商品
    * @param id
    * @return AjaxResult
    */
	@ApiOperation(value = "通过id查询spu商品")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:get')")
    public AjaxResult getById(@PathVariable("id") String id){
        return AjaxResult.success(goodsSpuService.getById1(id));
    }

    /**
    * 新增spu商品
    * @param goodsSpu spu商品
    * @return AjaxResult
    */
	@ApiOperation(value = "新增spu商品")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:add')")
    public AjaxResult save(@RequestBody GoodsSpu goodsSpu){
        return AjaxResult.success(goodsSpuService.save1(goodsSpu));
    }

    /**
    * 修改spu商品
    * @param goodsSpu spu商品
    * @return AjaxResult
    */
	@ApiOperation(value = "修改spu商品")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult updateById(@RequestBody GoodsSpu goodsSpu){
        return AjaxResult.success(goodsSpuService.updateById1(goodsSpu));
    }

	/**
	 * 商品上下架操作
	 * @param shelf
	 * @param ids
	 * @return AjaxResult
	 */
	@ApiOperation(value = "商品上下架操作")
	@PutMapping("/shelf")
	@PreAuthorize("@ss.hasPermi('mall:goodsspu:edit')")
	public AjaxResult updateById(@RequestParam(value = "shelf") String shelf, @RequestParam(value = "ids") String ids){
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setShelf(shelf);
		return AjaxResult.success(goodsSpuService.update(goodsSpu,Wrappers.<GoodsSpu>lambdaQuery()
				.in(GoodsSpu::getId, Convert.toList(ids))));
	}

    /**
    * 通过id删除spu商品
    * @param id
    * @return AjaxResult
    */
	@ApiOperation(value = "通过id删除spu商品")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(goodsSpuService.removeById(id));
    }

}
