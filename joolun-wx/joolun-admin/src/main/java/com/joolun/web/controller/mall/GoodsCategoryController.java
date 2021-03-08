/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.GoodsCategory;
import com.joolun.mall.service.GoodsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品类目
 *
 * @author www.joolun.com
 * @date 2019-08-12 11:46:28
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodscategory")
@Api(value = "goodscategory", tags = "商品类目管理")
public class GoodsCategoryController extends BaseController {

    private final GoodsCategoryService goodsCategoryService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsCategory 商品类目
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:index')")
    public AjaxResult getGoodsCategoryPage(Page page, GoodsCategory goodsCategory) {
        return AjaxResult.success(goodsCategoryService.page(page,Wrappers.query(goodsCategory)));
    }

	/**
	 *  返回树形集合
	 * @return
	 */
	@ApiOperation(value = "返回树形集合")
	@GetMapping("/tree")
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:index')")
	public AjaxResult getGoodsCategoryTree() {
		return AjaxResult.success(goodsCategoryService.selectTree(null));
	}

    /**
    * 通过id查询商品类目
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商品类目")
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:get')")
    public AjaxResult getById(@PathVariable("id") String id){
        return AjaxResult.success(goodsCategoryService.getById(id));
    }

    /**
    * 新增商品类目
    * @param goodsCategory 商品类目
    * @return R
    */
	@ApiOperation(value = "新增商品类目")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:add')")
    public AjaxResult save(@RequestBody GoodsCategory goodsCategory){
        return AjaxResult.success(goodsCategoryService.save(goodsCategory));
    }

    /**
    * 修改商品类目
    * @param goodsCategory 商品类目
    * @return R
    */
	@ApiOperation(value = "修改商品类目")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:edit')")
    public AjaxResult updateById(@RequestBody GoodsCategory goodsCategory){
    	if(goodsCategory.getId().equals(goodsCategory.getParentId())){
			return AjaxResult.error("不能将本级设为父类");
		}
        return AjaxResult.success(goodsCategoryService.updateById(goodsCategory));
    }

    /**
    * 通过id删除商品类目
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商品类目")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodscategory:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(goodsCategoryService.removeById(id));
    }

}
