package com.joolun.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.GoodsReview;
import com.joolun.mall.service.GoodsReviewService;
import com.joolun.web.api.support.MallUserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/** 小程序商品评价接口。 */
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/goodsreview")
public class GoodsReviewApi {
	private final GoodsReviewService goodsReviewService;
	private final MallUserSessionService mallUserSessionService;

	@GetMapping("/page")
	public AjaxResult page(Page<GoodsReview> page, String spuId) {
		return AjaxResult.success(goodsReviewService.pageVisible(page, spuId));
	}

	@GetMapping("/summary/{spuId}")
	public AjaxResult summary(@PathVariable String spuId) {
		return AjaxResult.success(goodsReviewService.summary(spuId));
	}

	@PostMapping
	public AjaxResult submit(@RequestBody GoodsReview review) {
		return AjaxResult.success(goodsReviewService.submit(review,
				mallUserSessionService.getCurrentMallUserId()));
	}
}
