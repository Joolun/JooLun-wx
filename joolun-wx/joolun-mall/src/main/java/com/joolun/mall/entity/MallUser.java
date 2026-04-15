/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城用户实体。
 * 该表用于承接商城领域自己的用户主数据，并通过 wx_user_id 关联微信渠道用户。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_user")
@EqualsAndHashCode(callSuper = true)
public class MallUser extends Model<MallUser> {
	private static final long serialVersionUID = 1L;

	/**
	 * 商城用户主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 商城会员编号。
	 */
	private String userNo;

	/**
	 * 绑定的微信用户主键。
	 */
	private String wxUserId;

	/**
	 * 微信 openId 快照。
	 */
	private String openId;

	/**
	 * 微信 unionId 快照。
	 */
	private String unionId;

	/**
	 * 商城昵称。
	 */
	private String nickName;

	/**
	 * 商城头像地址。
	 */
	private String avatarUrl;

	/**
	 * 真实姓名。
	 */
	private String realName;

	/**
	 * 手机号。
	 */
	private String mobile;

	/**
	 * 运营标签。
	 * 用于后台对会员做人群分组、运营标识或人工打标。
	 */
	private String userTag;

	/**
	 * 会员标记。
	 * 0：未入会，1：已入会。
	 * 当前约定绑定手机号后自动视为会员。
	 */
	private String memberFlag;

	/**
	 * 入会时间。
	 * 首次识别到手机号绑定成功时写入。
	 */
	private LocalDateTime memberTime;

	/**
	 * 性别。
	 * 0：未知，1：男，2：女。
	 */
	private String sex;

	/**
	 * 生日。
	 */
	private LocalDate birthday;

	/**
	 * 国家。
	 */
	private String country;

	/**
	 * 省份。
	 */
	private String province;

	/**
	 * 城市。
	 */
	private String city;

	/**
	 * 注册来源。
	 * 1：小程序，2：公众号，3：H5，4：APP，9：后台或其他来源。
	 */
	private String registerSource;

	/**
	 * 注册 IP。
	 */
	private String registerIp;

	/**
	 * 注册时间。
	 */
	private LocalDateTime registerTime;

	/**
	 * 最后登录 IP。
	 */
	private String lastLoginIp;

	/**
	 * 最后登录时间。
	 */
	private LocalDateTime lastLoginTime;

	/**
	 * 累计下单次数。
	 */
	private Integer orderCount;

	/**
	 * 累计支付订单数。
	 */
	private Integer consumeCount;

	/**
	 * 累计退款单数。
	 */
	private Integer refundCount;

	/**
	 * 累计消费金额。
	 */
	private BigDecimal consumeAmount;

	/**
	 * 状态。
	 * 0：正常，1：禁用。
	 */
	private String status;

	/**
	 * 备注。
	 */
	private String remark;

	/**
	 * 创建时间。
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间。
	 */
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除标记。
	 * 0：显示，1：隐藏。
	 */
	private String delFlag;

	/**
	 * 批量操作时的用户主键集合。
	 * 仅用于后台批量启用、禁用、备注，不落库。
	 */
	@TableField(exist = false)
	private List<String> ids;

	/**
	 * 查询条件：是否有下单记录。
	 * 1 表示有下单，0 表示无下单。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private Integer hasOrder;

	/**
	 * 查询条件：是否有支付记录。
	 * 1 表示有支付，0 表示无支付。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private Integer hasConsume;

	/**
	 * 查询条件：注册时间开始。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerTimeBegin;

	/**
	 * 查询条件：注册时间结束。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerTimeEnd;

	/**
	 * 查询条件：最近登录时间开始。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLoginTimeBegin;

	/**
	 * 查询条件：最近登录时间结束。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLoginTimeEnd;

	/**
	 * 查询条件：入会时间开始。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime memberTimeBegin;

	/**
	 * 查询条件：入会时间结束。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime memberTimeEnd;

	/**
	 * 查询条件：活跃状态场景。
	 * active7：近 7 天活跃。
	 * wake30：近 30 天活跃但近 7 天未登录。
	 * silent30：超过 30 天未登录或从未登录。
	 * neverLogin：从未登录。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private String activityScene;

	/**
	 * 查询条件：累计消费金额下限。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private BigDecimal minConsumeAmount;

	/**
	 * 查询条件：累计消费金额上限。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private BigDecimal maxConsumeAmount;

	/**
	 * 查询条件：累计下单次数下限。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private Integer minOrderCount;

	/**
	 * 查询条件：累计下单次数上限。
	 * 仅用于后台会员运营筛选，不落库。
	 */
	@TableField(exist = false)
	private Integer maxOrderCount;

	/**
	 * 最近下单时间。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private LocalDateTime lastOrderTime;

	/**
	 * 收货地址数量。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer addressCount;

	/**
	 * 待付款订单数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer waitPayOrderCount;

	/**
	 * 待发货订单数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer waitDeliveryOrderCount;

	/**
	 * 待收货订单数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer waitReceiveOrderCount;

	/**
	 * 最近活跃时间。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private LocalDateTime recentActiveTime;

	/**
	 * 入会天数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Long memberAgeDays;

	/**
	 * 最近支付时间。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private LocalDateTime recentPaymentTime;

	/**
	 * 最近支付订单号。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private String recentPaymentOrderNo;

	/**
	 * 近 30 天订单数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer thirtyDayOrderCount;

	/**
	 * 近 30 天消费金额。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private BigDecimal thirtyDayConsumeAmount;

	/**
	 * 售后商品件数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer afterSaleCount;

	/**
	 * 已退款金额。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private BigDecimal refundedAmount;

	/**
	 * 购物车商品行数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer cartSkuCount;

	/**
	 * 购物车商品总件数。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private Integer cartQuantity;

	/**
	 * 默认收货地址摘要。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private String defaultAddressText;

	/**
	 * 最近订单列表。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private List<OrderInfo> recentOrderList;

	/**
	 * 最近运营记录列表。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private List<MallUserOperateLog> recentOperateLogList;

	/**
	 * 最近收货地址列表。
	 * 仅用于后台会员详情展示，不落库。
	 */
	@TableField(exist = false)
	private List<UserAddress> recentAddressList;
}
