/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.weixin.constant.MyReturnCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品api
 *
 * @author JL
 * @date 2019-08-12 16:25:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/goodsspu")
public class GoodsSpuApi {

    private final GoodsSpuService goodsSpuService;

	/**
	* 分页查询
	* @param page 分页对象
	* @param goodsSpu spu商品
	* @return
	*/
    @GetMapping("/page")
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu, String couponUserId) {
		goodsSpu.setShelf(CommonConstants.YES);
        return AjaxResult.success(goodsSpuService.page1(page, goodsSpu));
    }

    /**
    * 通过id查询spu商品
    * @param id
    * @return R
    */
    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") String id){
		GoodsSpu goodsSpu = goodsSpuService.getById2(id);
		if(goodsSpu == null){
			return AjaxResult.error(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
		}
        return AjaxResult.success(goodsSpu);
    }

}
