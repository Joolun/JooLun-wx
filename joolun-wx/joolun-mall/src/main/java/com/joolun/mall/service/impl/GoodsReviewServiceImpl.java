package com.joolun.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.GoodsReview;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.mall.mapper.GoodsReviewMapper;
import com.joolun.mall.service.GoodsReviewService;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GoodsReviewServiceImpl extends ServiceImpl<GoodsReviewMapper, GoodsReview>
		implements GoodsReviewService {

	private final OrderInfoService orderInfoService;
	private final OrderItemService orderItemService;
	private final MallUserService mallUserService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GoodsReview submit(GoodsReview review, String userId) {
		if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
			throw new IllegalArgumentException("评分必须为1至5星");
		}
		OrderItem item = orderItemService.getById(review.getOrderItemId());
		OrderInfo order = item == null ? null : orderInfoService.getById(item.getOrderId());
		if (item == null || order == null || !userId.equals(order.getUserId())
				|| !OrderInfoEnum.STATUS_3.getValue().equals(order.getStatus())) {
			throw new IllegalArgumentException("仅已完成订单可以评价");
		}
		if (!CommonConstants.NO.equals(item.getIsRefund())) {
			throw new IllegalArgumentException("已退款商品不能评价");
		}
		if (count(Wrappers.<GoodsReview>lambdaQuery().eq(GoodsReview::getOrderItemId, item.getId())) > 0) {
			throw new IllegalArgumentException("该商品已经评价");
		}
		MallUser user = mallUserService.getById(userId);
		review.setId(null);
		review.setOrderId(order.getId());
		review.setUserId(userId);
		review.setSpuId(item.getSpuId());
		review.setSkuId(item.getSkuId());
		review.setSpuName(item.getSpuName());
		review.setSpecInfo(item.getSpecInfo());
		review.setNickName(user == null ? "微信用户" : StrUtil.blankToDefault(user.getNickName(), "微信用户"));
		review.setAvatarUrl(user == null ? null : user.getAvatarUrl());
		review.setAnonymousFlag("1".equals(review.getAnonymousFlag()) ? "1" : "0");
		review.setStatus("1");
		review.setContent(StrUtil.sub(StrUtil.trim(review.getContent()), 0, 500));
		review.setCreateTime(LocalDateTime.now());
		review.setUpdateTime(LocalDateTime.now());
		review.setDelFlag("0");
		save(review);
		item.setReviewStatus("1");
		orderItemService.updateById(item);
		return review;
	}

	@Override
	public IPage<GoodsReview> pageVisible(Page<GoodsReview> page, String spuId) {
		IPage<GoodsReview> result = page(page, Wrappers.<GoodsReview>lambdaQuery()
				.eq(GoodsReview::getSpuId, spuId)
				.eq(GoodsReview::getStatus, "1")
				.eq(GoodsReview::getDelFlag, "0")
				.orderByDesc(GoodsReview::getCreateTime));
		result.getRecords().forEach(this::maskAnonymous);
		return result;
	}

	@Override
	public Map<String, Object> summary(String spuId) {
		List<GoodsReview> reviews = list(Wrappers.<GoodsReview>lambdaQuery()
				.eq(GoodsReview::getSpuId, spuId)
				.eq(GoodsReview::getStatus, "1")
				.eq(GoodsReview::getDelFlag, "0"));
		Map<String, Object> result = new LinkedHashMap<>();
		result.put("count", reviews.size());
		BigDecimal average = reviews.isEmpty() ? BigDecimal.ZERO : BigDecimal.valueOf(
				reviews.stream().mapToInt(GoodsReview::getRating).average().orElse(0D)).setScale(1, RoundingMode.HALF_UP);
		result.put("averageRating", average);
		return result;
	}

	@Override
	public void reply(String reviewId, String replyContent, String status) {
		GoodsReview review = getById(reviewId);
		if (review == null) {
			throw new IllegalArgumentException("评价不存在");
		}
		if (status != null) {
			review.setStatus("1".equals(status) ? "1" : "0");
		}
		if (replyContent != null) {
			review.setReplyContent(StrUtil.sub(StrUtil.trim(replyContent), 0, 500));
			review.setReplyTime(StrUtil.isBlank(review.getReplyContent()) ? null : LocalDateTime.now());
		}
		review.setUpdateTime(LocalDateTime.now());
		updateById(review);
	}

	private void maskAnonymous(GoodsReview review) {
		if ("1".equals(review.getAnonymousFlag())) {
			review.setNickName("匿名用户");
			review.setAvatarUrl(null);
		}
	}
}
