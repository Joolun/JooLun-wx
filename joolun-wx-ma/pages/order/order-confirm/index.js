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

Page({
  data: {
    orderConfirmData: [],
    salesPrice: 0,
    paymentPrice: 0,
    freightPrice: 0,
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
    this.setData({
      ['orderSubParm.deliveryWay']: e.detail.value,
      freightMap: null
    })
    this.orderConfirmDo()
  },
  orderConfirmDo () {
    // 读取商品详情页或购物车页提前写入的临时结算快照。
    wx.getStorage({
      key: 'param-orderConfirm',
      success: (res) => {
        const orderConfirmData = (res.data || []).map((item) => {
          return Object.assign({}, item, {
            picUrl: util.resolveResourceUrl(item.picUrl, '/public/img/no_pic.png')
          })
        })
        let salesPrice = 0
        let freightPrice = 0
        let spuIds = ''
        orderConfirmData.forEach((orderConfirm) => {
          spuIds = spuIds ? `${spuIds},${orderConfirm.spuId}` : orderConfirm.spuId
          const currentPrice = Number(orderConfirm.salesPrice || orderConfirm.addPrice || 0)
          salesPrice = (Number(salesPrice) + currentPrice * orderConfirm.quantity).toFixed(2)
          orderConfirm.paymentPrice = (currentPrice * orderConfirm.quantity).toFixed(2)
          orderConfirm.freightPrice = 0
          freightPrice = (Number(freightPrice) + Number(orderConfirm.freightPrice)).toFixed(2)
        })
        this.setData({
          orderConfirmData,
          salesPrice,
          freightPrice,
          paymentPrice: salesPrice,
          spuIds
        })
      }
    })
  },
  countFreight (orderConfirm, freightTemplat, quantity) {
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
    orderSubParm.skus = this.data.orderConfirmData
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
