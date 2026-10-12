package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.GoodsReview;
import com.joolun.mall.service.GoodsReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** 商品评价后台查询、显示控制和商家回复。 */
@RestController
@AllArgsConstructor
@RequestMapping("/goodsreview")
public class GoodsReviewController extends BaseController {

	private final GoodsReviewService goodsReviewService;

	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:goodsreview:index')")
	public AjaxResult page(Page<GoodsReview> page, GoodsReview review) {
		return AjaxResult.success(goodsReviewService.page(page, Wrappers.<GoodsReview>lambdaQuery()
				.like(review.getSpuName() != null, GoodsReview::getSpuName, review.getSpuName())
				.eq(review.getRating() != null, GoodsReview::getRating, review.getRating())
				.eq(review.getStatus() != null, GoodsReview::getStatus, review.getStatus())
				.orderByDesc(GoodsReview::getCreateTime)));
	}

	@PutMapping("/{id}/reply")
	@PreAuthorize("@ss.hasPermi('mall:goodsreview:edit')")
	public AjaxResult reply(@PathVariable String id, @RequestBody GoodsReview review) {
		goodsReviewService.reply(id, review.getReplyContent(), review.getStatus());
		return AjaxResult.success();
	}
}
