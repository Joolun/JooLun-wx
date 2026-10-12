package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.GoodsReview;

import java.util.Map;

public interface GoodsReviewService extends IService<GoodsReview> {
	GoodsReview submit(GoodsReview review, String userId);

	IPage<GoodsReview> pageVisible(Page<GoodsReview> page, String spuId);

	Map<String, Object> summary(String spuId);

	void reply(String reviewId, String replyContent, String status);
}
