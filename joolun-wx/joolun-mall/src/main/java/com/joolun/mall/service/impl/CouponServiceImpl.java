package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.dto.CouponDiscountDTO;
import com.joolun.mall.entity.Coupon;
import com.joolun.mall.entity.CouponUser;
import com.joolun.mall.mapper.CouponMapper;
import com.joolun.mall.mapper.CouponUserMapper;
import com.joolun.mall.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

	private static final String UNUSED = "0";
	private static final String LOCKED = "1";
	private static final String USED = "2";

	private final CouponUserMapper couponUserMapper;

	@Override
	public List<Coupon> listClaimable(String userId) {
		LocalDateTime now = LocalDateTime.now();
		List<Coupon> coupons = list(Wrappers.<Coupon>lambdaQuery()
				.eq(Coupon::getStatus, "1")
				.eq(Coupon::getDelFlag, "0")
				.le(Coupon::getStartTime, now)
				.ge(Coupon::getEndTime, now)
				.and(query -> query.eq(Coupon::getTotalQuantity, 0)
						.or().apply("received_quantity < total_quantity"))
				.orderByAsc(Coupon::getThresholdAmount));
		if (coupons.isEmpty()) {
			return coupons;
		}
		Set<String> receivedIds = couponUserMapper.selectList(Wrappers.<CouponUser>lambdaQuery()
				.eq(CouponUser::getUserId, userId)
				.in(CouponUser::getCouponId, coupons.stream().map(Coupon::getId).toList()))
				.stream().map(CouponUser::getCouponId).collect(Collectors.toSet());
		coupons.forEach(coupon -> coupon.setReceived(receivedIds.contains(coupon.getId())));
		return coupons;
	}

	@Override
	public List<CouponUser> listMine(String userId, String status, BigDecimal orderAmount) {
		List<CouponUser> records = couponUserMapper.selectList(Wrappers.<CouponUser>lambdaQuery()
				.eq(CouponUser::getUserId, userId)
				.eq(status != null && !status.isBlank(), CouponUser::getStatus, status)
				.orderByDesc(CouponUser::getReceiveTime));
		if (records.isEmpty()) {
			return records;
		}
		Map<String, Coupon> couponMap = listByIds(records.stream().map(CouponUser::getCouponId).toList())
				.stream().collect(Collectors.toMap(Coupon::getId, Function.identity()));
		LocalDateTime now = LocalDateTime.now();
		for (CouponUser record : records) {
			Coupon coupon = couponMap.get(record.getCouponId());
			if (coupon == null) {
				continue;
			}
			fillCouponSnapshot(record, coupon);
			boolean amountSatisfied = orderAmount == null || orderAmount.compareTo(coupon.getThresholdAmount()) >= 0;
			record.setAvailable(UNUSED.equals(record.getStatus()) && "1".equals(coupon.getStatus())
					&& !now.isBefore(coupon.getStartTime()) && !now.isAfter(coupon.getEndTime()) && amountSatisfied);
		}
		return records;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void claim(String couponId, String userId) {
		Coupon coupon = getById(couponId);
		validateActive(coupon);
		boolean increased = update(Wrappers.<Coupon>lambdaUpdate()
				.eq(Coupon::getId, couponId)
				.eq(Coupon::getStatus, "1")
				.and(query -> query.eq(Coupon::getTotalQuantity, 0)
						.or().apply("received_quantity < total_quantity"))
				.setSql("received_quantity = received_quantity + 1"));
		if (!increased) {
			throw new IllegalArgumentException("优惠券已领完");
		}
		CouponUser couponUser = new CouponUser();
		couponUser.setCouponId(couponId);
		couponUser.setUserId(userId);
		couponUser.setStatus(UNUSED);
		couponUser.setReceiveTime(LocalDateTime.now());
		couponUser.setCreateTime(LocalDateTime.now());
		couponUser.setUpdateTime(LocalDateTime.now());
		couponUser.setDelFlag("0");
		couponUser.setVersion(0);
		try {
			couponUserMapper.insert(couponUser);
		} catch (DuplicateKeyException exception) {
			throw new IllegalArgumentException("每人限领一张，请勿重复领取");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CouponDiscountDTO lockForOrder(String couponUserId, String userId, String orderId, BigDecimal orderAmount) {
		if (couponUserId == null || couponUserId.isBlank()) {
			return null;
		}
		CouponUser couponUser = couponUserMapper.selectById(couponUserId);
		if (couponUser == null || !userId.equals(couponUser.getUserId()) || !UNUSED.equals(couponUser.getStatus())) {
			throw new IllegalArgumentException("优惠券不可用或已被使用");
		}
		Coupon coupon = getById(couponUser.getCouponId());
		validateActive(coupon);
		BigDecimal amount = orderAmount == null ? BigDecimal.ZERO : orderAmount;
		if (amount.compareTo(coupon.getThresholdAmount()) < 0) {
			throw new IllegalArgumentException("订单金额未达到优惠券使用门槛");
		}
		boolean locked = couponUserMapper.update(null, Wrappers.<CouponUser>lambdaUpdate()
				.eq(CouponUser::getId, couponUserId)
				.eq(CouponUser::getUserId, userId)
				.eq(CouponUser::getStatus, UNUSED)
				.set(CouponUser::getStatus, LOCKED)
				.set(CouponUser::getOrderId, orderId)
				.set(CouponUser::getUpdateTime, LocalDateTime.now())) == 1;
		if (!locked) {
			throw new IllegalArgumentException("优惠券已被其他订单占用");
		}
		return new CouponDiscountDTO(couponUserId, coupon.getName(), coupon.getDiscountAmount().min(amount));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void markUsed(String couponUserId, String orderId) {
		if (couponUserId == null || couponUserId.isBlank()) {
			return;
		}
		CouponUser couponUser = couponUserMapper.selectById(couponUserId);
		if (couponUser == null) {
			return;
		}
		int changed = couponUserMapper.update(null, Wrappers.<CouponUser>lambdaUpdate()
				.eq(CouponUser::getId, couponUserId)
				.eq(CouponUser::getOrderId, orderId)
				.eq(CouponUser::getStatus, LOCKED)
				.set(CouponUser::getStatus, USED)
				.set(CouponUser::getUseTime, LocalDateTime.now())
				.set(CouponUser::getUpdateTime, LocalDateTime.now()));
		if (changed == 1) {
			update(Wrappers.<Coupon>lambdaUpdate().eq(Coupon::getId, couponUser.getCouponId())
					.setSql("used_quantity = used_quantity + 1"));
		}
	}

	@Override
	public void release(String couponUserId, String orderId) {
		if (couponUserId == null || couponUserId.isBlank()) {
			return;
		}
		couponUserMapper.update(null, Wrappers.<CouponUser>lambdaUpdate()
				.eq(CouponUser::getId, couponUserId)
				.eq(CouponUser::getOrderId, orderId)
				.eq(CouponUser::getStatus, LOCKED)
				.set(CouponUser::getStatus, UNUSED)
				.set(CouponUser::getOrderId, null)
				.set(CouponUser::getUpdateTime, LocalDateTime.now()));
	}

	private void validateActive(Coupon coupon) {
		LocalDateTime now = LocalDateTime.now();
		if (coupon == null || !"1".equals(coupon.getStatus()) || !"0".equals(coupon.getDelFlag())
				|| coupon.getStartTime() == null || coupon.getEndTime() == null
				|| now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
			throw new IllegalArgumentException("优惠券不在可用时间内");
		}
		if (coupon.getThresholdAmount() == null || coupon.getDiscountAmount() == null
				|| coupon.getThresholdAmount().signum() < 0 || coupon.getDiscountAmount().signum() <= 0) {
			throw new IllegalArgumentException("优惠券配置不正确");
		}
	}

	private void fillCouponSnapshot(CouponUser target, Coupon coupon) {
		target.setCouponName(coupon.getName());
		target.setThresholdAmount(coupon.getThresholdAmount());
		target.setDiscountAmount(coupon.getDiscountAmount());
		target.setStartTime(coupon.getStartTime());
		target.setEndTime(coupon.getEndTime());
		target.setDescription(coupon.getDescription());
	}
}
