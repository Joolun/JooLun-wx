/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

/**
 * 多规格订单确认页说明：
 * 1. 页面从本地缓存 param-orderConfirm 读取待结算的 SKU 快照
 * 2. 每一行都已经包含 skuId、specInfo 和实际结算价格
 * 3. 提交订单时直接把这份 SKU 列表传给后端，不在本页重新拼装规格
 *
 * @author www.joolun.com
 */
const util = require('../../../utils/util.js')
const app = getApp()

/** 金额统一保留两位，避免浮点误差在分摊时被放大。 */
function round2 (value) {
  return Math.round(Number(value || 0) * 100) / 100
}

Page({
  data: {
    orderConfirmData: [],
    salesPrice: 0,
    paymentPrice: 0,
    freightPrice: 0,
    freeShipping: true,
    memberDiscountAmount: 0,
    couponDiscountAmount: 0,
    memberRate: 100,
    availableCoupons: [{ id: '', couponName: '不使用优惠券', discountAmount: 0, available: true }],
    selectedCouponIndex: 0,
    userAddress: null,
    orderSubParm: {
      paymentType: '1',
      deliveryWay: '1'
    },
    loading: false,
    mallUser: null,
    mallConfig: {
      needMemberPhoneForOrder: true
    },
    orderNoticeList: [],
    afterSaleNoticeList: [],
    spuIds: ''
  },
  onShow () {
    this.syncMallUserFromGlobal()
  },
  onLoad () {
    // 收货地址、商城用户信息和运行时配置拆开加载，避免互相阻塞。
    this.refreshOrderNoticeList()
    this.refreshAfterSaleNoticeList()
    this.orderConfirmDo()
    app.initPage()
      .then(() => {
        return Promise.allSettled([this.mallConfigGet(), this.userAddressPage(), this.mallUserGet()])
      })
  },
  deliveryWayChange (e) {
    // 上门自提不计运费，切换配送方式后必须重算。
    this.setData({
      ['orderSubParm.deliveryWay']: e.detail.value
    }, () => {
      this.refreshFreight()
    })
  },
  orderConfirmDo () {
    // 读取商品详情页或购物车页提前写入的临时结算快照。
    wx.getStorage({
      key: 'param-orderConfirm',
      success: (res) => {
        const orderConfirmData = (res.data || []).map((item) => {
          const currentPrice = Number(item.salesPrice || item.addPrice || 0)
          return Object.assign({}, item, {
            picUrl: util.resolveResourceUrl(item.picUrl, '/public/img/no_pic.png'),
            paymentPrice: (currentPrice * item.quantity).toFixed(2),
            freightPrice: 0
          })
        })
        let salesPrice = 0
        let spuIds = ''
        orderConfirmData.forEach((orderConfirm) => {
          spuIds = spuIds ? `${spuIds},${orderConfirm.spuId}` : orderConfirm.spuId
          salesPrice = round2(salesPrice + Number(orderConfirm.paymentPrice))
        })
        this.setData({
          orderConfirmData,
          salesPrice: salesPrice.toFixed(2),
          paymentPrice: salesPrice.toFixed(2),
          spuIds
        }, () => {
          this.refreshFreight()
        })
        this.loadPricingBenefits(Number(salesPrice))
      }
    })
  },
  loadPricingBenefits (salesPrice) {
    return app.api.memberProfile().then((profileRes) => {
      const profile = profileRes.data || {}
      const memberRate = Number(profile.currentLevel && profile.currentLevel.discountRate || 100)
      const memberDiscountAmount = Number((salesPrice * (100 - memberRate) / 100).toFixed(2))
      const discountedSales = Number((salesPrice - memberDiscountAmount).toFixed(2))
      return app.api.couponMine({ status: '0', orderAmount: discountedSales }).then((couponRes) => {
        const coupons = [{ id: '', couponName: '不使用优惠券', discountAmount: 0, available: true }]
          .concat((couponRes.data || []).filter(item => item.available))
        this.setData({ memberRate, memberDiscountAmount, availableCoupons: coupons, selectedCouponIndex: 0 })
        this.refreshBenefitAmount()
      })
    }).catch(() => {})
  },
  couponChange (e) {
    const selectedCouponIndex = Number(e.detail.value || 0)
    this.setData({ selectedCouponIndex })
    this.refreshBenefitAmount()
  },
  refreshBenefitAmount () {
    const coupon = this.data.availableCoupons[this.data.selectedCouponIndex] || {}
    const salesPrice = Number(this.data.salesPrice || 0)
    const memberDiscount = Number(this.data.memberDiscountAmount || 0)
    const couponDiscount = Math.min(Number(coupon.discountAmount || 0), Math.max(0, salesPrice - memberDiscount))
    this.setData({
      couponDiscountAmount: couponDiscount.toFixed(2),
      paymentPrice: Math.max(0, salesPrice - memberDiscount - couponDiscount).toFixed(2),
      ['orderSubParm.couponUserId']: coupon.id || null
    })
  },
  /**
   * 重新计算整单运费。
   * 规则与服务端 FreightCalculator 保持一致：
   * 1. 同一运费模板的商品合并计算首件/续件，不同模板各自收取首运费；
   * 2. 模板运费按计费量占比回摊到明细，保证明细运费之和等于模板运费；
   * 3. 上门自提不计运费。
   */
  refreshFreight () {
    const items = (this.data.orderConfirmData || []).map((item) => Object.assign({}, item))
    const chargeFreight = this.data.orderSubParm.deliveryWay !== '2'
    const groups = {}
    const groupOrder = []
    items.forEach((item, index) => {
      item.freightPrice = 0
      if (!chargeFreight || !item.freightTemplat || !item.freightTemplat.id) {
        return
      }
      const templateId = item.freightTemplat.id
      if (!groups[templateId]) {
        groups[templateId] = { templat: item.freightTemplat, indexes: [] }
        groupOrder.push(templateId)
      }
      groups[templateId].indexes.push(index)
    })
    groupOrder.forEach((templateId) => {
      const group = groups[templateId]
      const billableOf = (item) => {
        const quantity = Number(item.quantity || 0)
        if (group.templat.chargeType === '2') {
          return round2(Number(item.weight || 0) * quantity)
        }
        if (group.templat.chargeType === '3') {
          return round2(Number(item.volume || 0) * quantity)
        }
        return quantity
      }
      let totalBillable = 0
      group.indexes.forEach((index) => {
        totalBillable = round2(totalBillable + billableOf(items[index]))
      })
      this.allocateFreight(this.countFreight(group.templat, totalBillable), items, group.indexes, billableOf)
    })
    let freightPrice = 0
    items.forEach((item) => {
      freightPrice = round2(freightPrice + Number(item.freightPrice || 0))
    })
    this.setData({
      orderConfirmData: items,
      freightPrice: freightPrice.toFixed(2),
      freeShipping: freightPrice <= 0
    })
  },
  /** 按首件 + 续件规则计算单个运费模板的运费。 */
  countFreight (freightTemplat, billable) {
    if (!freightTemplat || freightTemplat.type === '2') {
      return 0
    }
    const firstNum = Number(freightTemplat.firstNum || 0)
    const firstFreight = Number(freightTemplat.firstFreight || 0)
    const continueNum = Number(freightTemplat.continueNum || 0)
    const continueFreight = Number(freightTemplat.continueFreight || 0)
    if (continueNum <= 0 || billable <= firstNum) {
      return round2(firstFreight)
    }
    const steps = Math.ceil(round2(billable - firstNum) / continueNum)
    return round2(firstFreight + steps * continueFreight)
  },
  /** 把模板运费按计费量占比回摊到明细，最后一条吃掉舍入差额。 */
  allocateFreight (groupFreight, items, indexes, billableOf) {
    const total = round2(groupFreight)
    if (total <= 0) {
      return
    }
    let weights = indexes.map((index) => billableOf(items[index]))
    let totalWeight = weights.reduce((sum, value) => round2(sum + value), 0)
    if (totalWeight <= 0) {
      // 计费量全为 0（例如按重量计费但商品没填重量）时退化用商品金额分摊。
      weights = indexes.map((index) => round2(Number(items[index].paymentPrice || 0)))
      totalWeight = weights.reduce((sum, value) => round2(sum + value), 0)
    }
    if (totalWeight <= 0) {
      items[indexes[0]].freightPrice = total
      return
    }
    let allocated = 0
    indexes.forEach((index, position) => {
      let share
      if (position === indexes.length - 1) {
        share = round2(total - allocated)
      } else {
        share = Math.max(Math.min(round2(total * weights[position] / totalWeight), round2(total - allocated)), 0)
        allocated = round2(allocated + share)
      }
      items[index].freightPrice = share
    })
  },
  userAddressPage () {
    // 快递配送时优先带出默认收货地址，减少用户下单前的重复操作。
    return app.api.userAddressPage({
      searchCount: false,
      current: 1,
      size: 1,
      isDefault: '1'
    }).then((res) => {
      const records = res.data.records
      if (records && records.length > 0) {
        this.setData({
          userAddress: records[0]
        })
      }
      return records
    })
  },
  mallUserGet () {
    return app.api.mallUserGet().then((res) => {
      const mallUser = res.data || null
      this.applyMallUser(mallUser)
      return mallUser
    })
  },
  mallConfigGet () {
    return app.api.mallConfigGet()
      .then((res) => {
        const mallConfig = this.normalizeMallConfig(res.data)
        this.applyMallConfig(mallConfig)
        return mallConfig
      })
      .catch(() => {
        const mallConfig = this.normalizeMallConfig(app.globalData.mallConfig)
        this.applyMallConfig(mallConfig)
        return mallConfig
      })
  },
  userMessageInput (e) {
    this.setData({
      ['orderSubParm.userMessage']: e.detail.value
    })
  },
  handleMallUserChange (e) {
    this.applyMallUser((e.detail && e.detail.mallUser) || null)
  },
  orderSub () {
    // 直接提交当前缓存里的 SKU 快照，确认页展示的价格就是本次下单价格。
    const userAddress = this.data.userAddress
    if (!this.isMallUserReady()) {
      wx.showToast({
        title: '用户信息加载中',
        icon: 'none',
        duration: 2000
      })
      return
    }
    if (this.needBindPhoneBeforeOrder()) {
      wx.showToast({
        title: '请先绑定手机号',
        icon: 'none',
        duration: 2000
      })
      return
    }
    if (this.data.orderSubParm.deliveryWay === '1' && userAddress == null) {
      wx.showToast({
        title: '请选择收货地址',
        icon: 'none',
        duration: 2000
      })
      return
    }
    this.setData({
      loading: true
    })
    const orderSubParm = this.data.orderSubParm
    // 只提交下单需要的字段：运费由服务端按运费模板重新计算，
    // 这里带的 freightPrice 仅作为展示值，服务端不会采信。
    orderSubParm.skus = this.data.orderConfirmData.map((item) => ({
      spuId: item.spuId,
      skuId: item.skuId,
      quantity: item.quantity,
      salesPrice: item.salesPrice,
      spuName: item.spuName,
      picUrl: item.picUrl,
      specInfo: item.specInfo,
      freightPrice: item.freightPrice
    }))
    app.api.orderSub(Object.assign(
      {},
      { userAddressId: this.data.orderSubParm.deliveryWay === '1' ? userAddress.id : null },
      orderSubParm
    ))
      .then((res) => {
        wx.redirectTo({
          url: '/pages/order/order-detail/index?callPay=true&id=' + res.data.id
        })
      })
      .catch(() => {
        this.setData({
          loading: false
        })
      })
  },
  applyMallUser (mallUser) {
    this.setData({
      mallUser
    })
    app.globalData.mallUser = mallUser
    this.refreshOrderNoticeList()
  },
  applyMallConfig (mallConfig) {
    this.setData({
      mallConfig
    })
    app.globalData.mallConfig = mallConfig
    this.refreshOrderNoticeList()
  },
  normalizeMallConfig (mallConfig) {
    return {
      needMemberPhoneForOrder: !(mallConfig && mallConfig.needMemberPhoneForOrder === false)
    }
  },
  syncMallUserFromGlobal () {
    if (app.globalData.mallUser) {
      this.setData({
        mallUser: app.globalData.mallUser
      })
    }
  },
  isMallUserReady () {
    const mallUser = this.data.mallUser
    return !!(mallUser && mallUser.id)
  },
  needBindPhoneBeforeOrder () {
    const mallConfig = this.data.mallConfig || {}
    const mallUser = this.data.mallUser || {}
    if (!mallConfig.needMemberPhoneForOrder || !this.isMallUserReady()) {
      return false
    }
    return mallUser.memberFlag !== '1' || !mallUser.mobile
  },
  /**
   * 刷新下单须知。
   * 这里把会员绑定、地址选择和规格确认等关键前置条件明确展示给用户。
   */
  refreshOrderNoticeList () {
    const needPhone = this.data.mallConfig && this.data.mallConfig.needMemberPhoneForOrder
    const mallUser = this.data.mallUser || {}
    const noticeList = [
      needPhone
        ? (mallUser.memberFlag === '1' && mallUser.mobile
          ? '当前账号已完成会员绑定，可直接提交订单'
          : '当前项目以绑定手机号作为会员认定标准，下单前需先完成手机号授权')
        : '当前订单可直接提交，无需额外会员校验',
      this.data.orderSubParm.deliveryWay === '1'
        ? '快递配送请先确认默认收货地址是否正确'
        : '到店自提请在备注里补充取货信息',
      '请再次核对商品规格、数量和结算金额，提交后将按当前 SKU 快照下单'
    ]
    this.setData({
      orderNoticeList: noticeList
    })
  },
  /**
   * 刷新售后说明。
   * 下单前把售后口径讲清楚，减少用户对退款流程和处理时效的误解。
   */
  refreshAfterSaleNoticeList () {
    this.setData({
      afterSaleNoticeList: [
        '退款申请按订单项处理，不同商品会分别审核和退款',
        '商家审核结果会同步到订单详情和售后中心，可随时回看处理进度',
        '商家同意退款后，系统会等待退款结果回写，到账时间以原支付渠道为准'
      ]
    })
  }
})
