/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

const util = require('../../../utils/util.js')
const app = getApp()

Page({
  data: {
    orderInfo: null,
    id: null,
    callPay: false,//是否直接调起支付
    refundPopupVisible: false,
    refundSubmitting: false,
    refundReason: '',
    refundTarget: null
  },
  onShow() {
    app.initPage()
      .then(res => {
        this.orderGet(this.data.id)
      })
  },
  onLoad(options) {
    this.setData({
      id: options.id
    })
    if (options.callPay){
      this.setData({
        callPay: true
      })
    }
  },
  orderGet(id){
    let that = this
    app.api.orderGet(id)
      .then(res => {
        let orderInfo = res.data
        if (!orderInfo){
          wx.redirectTo({
            url: '/pages/order/order-list/index'
          })
        }
        orderInfo = this.decorateOrderInfo(orderInfo)
        this.setData({
          orderInfo: orderInfo
        })
        setTimeout(function () {
          that.setData({
            callPay: false
          })
        }, 4000)
      })
  },
  /**
   * 统一补齐订单详情页展示字段。
   * 这里把售后文案、时间线和按钮开关都提前整理好，避免 WXML 中写过多复杂判断。
   *
   * @param {Object} orderInfo 订单详情
   * @returns {Object}
   */
  decorateOrderInfo(orderInfo) {
    const nextOrderInfo = Object.assign({}, orderInfo)
    nextOrderInfo.listOrderItem = (orderInfo.listOrderItem || []).map((item) => this.decorateOrderItem(item, orderInfo))
    return nextOrderInfo
  },
  /**
   * 补齐订单项售后展示字段。
   *
   * @param {Object} orderItem 订单项
   * @param {Object} orderInfo 订单
   * @returns {Object}
   */
  decorateOrderItem(orderItem, orderInfo) {
    const nextItem = Object.assign({}, orderItem)
    nextItem.picUrl = util.resolveResourceUrl(orderItem.picUrl, '/public/img/no_pic.png')
    nextItem.afterSaleStatusText = this.buildAfterSaleStatusText(orderItem)
    nextItem.afterSaleVisible = nextItem.isRefund === '1' || nextItem.status === '1' || nextItem.status === '2' || nextItem.status === '3'
    nextItem.canApplyRefund = orderInfo.isPay === '1' && nextItem.isRefund === '0' && (nextItem.status === '0' || nextItem.status === '2')
    nextItem.afterSaleTimeline = this.buildAfterSaleTimeline(orderItem)
    return nextItem
  },
  /**
   * 生成订单项售后状态文案。
   *
   * @param {Object} orderItem 订单项
   * @returns {string}
   */
  buildAfterSaleStatusText(orderItem) {
    if (orderItem.isRefund === '1') {
      return '已退款'
    }
    if (orderItem.status === '1') {
      return '申请退款中'
    }
    if (orderItem.status === '2') {
      return '退款被拒绝'
    }
    if (orderItem.status === '3') {
      return '退款处理中'
    }
    return '正常'
  },
  /**
   * 构建订单项售后时间线。
   * 用户最关心的是“我什么时候申请”“商家什么时候处理”“最终退款有没有完成”，因此按这三个阶段组织。
   *
   * @param {Object} orderItem 订单项
   * @returns {Array}
   */
  buildAfterSaleTimeline(orderItem) {
    const timeline = []
    if (orderItem.refundApplyTime) {
      timeline.push({
        title: '已提交退款申请',
        time: this.formatDateTime(orderItem.refundApplyTime),
        desc: `申请原因：${orderItem.refundReason || '未填写'}`
      })
    }
    if (orderItem.refundAuditTime) {
      timeline.push({
        title: orderItem.status === '2' ? '商家已拒绝退款' : '商家已处理退款',
        time: this.formatDateTime(orderItem.refundAuditTime),
        desc: `审核备注：${orderItem.refundAuditRemark || (orderItem.status === '2' ? '商家已拒绝本次退款申请' : '商家已同意退款，等待系统处理')}`
      })
    }
    if (orderItem.refundSuccessTime || orderItem.isRefund === '1') {
      timeline.push({
        title: '退款完成',
        time: this.formatDateTime(orderItem.refundSuccessTime),
        desc: '退款结果已回写，请留意原支付账户到账通知'
      })
    }
    return timeline
  },
  /**
   * 统一格式化时间，兼容带 T 的后端时间格式。
   *
   * @param {string} value 时间值
   * @returns {string}
   */
  formatDateTime(value) {
    if (!value) {
      return '处理中'
    }
    return String(value).replace('T', ' ').substring(0, 19)
  },
  //复制内容
  copyData(e) {
    wx.setClipboardData({
      data: e.currentTarget.dataset.data
    })
  },
  /**
   * 打开退款申请下弹层。
   * 这里按照订单项维度发起售后，避免一个订单多个商品时申请对象不明确。
   *
   * @param {Object} e 页面事件
   */
  openRefundPopup(e) {
    const dataset = e.currentTarget.dataset || {}
    this.setData({
      refundPopupVisible: true,
      refundSubmitting: false,
      refundReason: '',
      refundTarget: {
        id: dataset.id || '',
        spuName: dataset.spuName || '',
        specInfo: dataset.specInfo || '',
        paymentPrice: dataset.paymentPrice || '0.00'
      }
    })
  },
  /**
   * 关闭退款申请下弹层，并清理当前选中的订单项。
   */
  closeRefundPopup() {
    if (this.data.refundSubmitting) {
      return
    }
    this.setData({
      refundPopupVisible: false,
      refundReason: '',
      refundTarget: null
    })
  },
  /**
   * 阻止点击弹层内容区域时触发遮罩关闭。
   */
  preventTouchMove() {},
  /**
   * 同步退款原因输入。
   *
   * @param {Object} e 输入事件
   */
  handleRefundReasonInput(e) {
    this.setData({
      refundReason: e.detail.value || ''
    })
  },
  /**
   * 提交退款申请。
   * 当前要求用户填写明确原因，便于后台客服审核和后续问题复盘。
   */
  submitRefundApply() {
    const refundTarget = this.data.refundTarget || {}
    const refundReason = (this.data.refundReason || '').trim()
    if (!refundTarget.id) {
      wx.showToast({
        title: '退款商品不存在',
        icon: 'none'
      })
      return
    }
    if (!refundReason) {
      wx.showToast({
        title: '请输入退款原因',
        icon: 'none'
      })
      return
    }
    this.setData({
      refundSubmitting: true
    })
    app.api.orderRefunds({
      id: refundTarget.id,
      refundReason: refundReason
    })
      .then(() => {
        wx.showToast({
          title: '已提交申请',
          icon: 'success'
        })
        this.setData({
          refundPopupVisible: false,
          refundSubmitting: false,
          refundReason: '',
          refundTarget: null
        })
        this.orderGet(this.data.orderInfo.id)
      })
      .catch(() => {
        this.setData({
          refundSubmitting: false
        })
      })
  },
  /**
   * 记录客服入口触达。
   * 当前先复用微信原生客服能力，这里仅预留事件，方便后续补埋点。
   *
   * @param {Object} e 客服回调
   */
  handleContact(e) {
    console.log('order-detail-contact', e)
  },
  orderCancel(){
    let id = this.data.orderInfo.id
    this.orderGet(id)
  },
  orderDel(){
    wx.navigateBack()
  },
  unifiedOrder() {
    this.onShow()
  },
  countDownDone(){
    this.orderGet(this.data.id)
  }
})
