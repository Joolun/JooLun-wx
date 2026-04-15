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
    tabCur: 0,
    initialized: false,
    orderStatus: [
      {
        value: '全部订单',
        key: ''
      },
      {
        value: '待付款',
        key: '0'
      }, {
        value: '待发货',
        key: '1'
      }, {
        value: '待收货',
        key: '2'
      }, {
        value: '已完成',
        key: '4'
      }
    ],
    afterSaleQuickFilters: [
      {
        label: '全部订单',
        key: ''
      },
      {
        label: '售后中',
        key: 'ALL'
      },
      {
        label: '退款申请中',
        key: '1'
      },
      {
        label: '退款被拒绝',
        key: '2'
      },
      {
        label: '已退款',
        key: '4'
      }
    ],
    page: {
      searchCount: false,
      current: 1,
      size: 10,
      ascs: '',//升序字段
      descs: 'create_time'
    },
    parameter: {},
    loadmore: true,
    orderList: []
  },
  onShow() {
    if (!this.data.initialized) {
      return
    }
    this.refresh()
  },
  onLoad: function (options) {
    let that = this
    if (options.status) {
      this.setData({
        ['parameter.status']: options.status
      })
      this.data.orderStatus.forEach(function (status, index) {
        if (status.key == options.status){
          that.setData({
            tabCur: index
          })
        }
      })
    }
    if (options.afterSaleStatus) {
      this.setData({
        ['parameter.afterSaleStatus']: options.afterSaleStatus
      })
    }
    app.initPage()
      .then(res => {
        return this.orderPage()
      })
      .finally(() => {
        this.setData({
          initialized: true
        })
      })
  },
  orderPage() {
    return app.api.orderPage(Object.assign(
      {},
      this.data.page,
      util.filterForm(this.data.parameter)
    ))
      .then(res => {
        let orderList = this.decorateOrderList(res.data.records || [])
        this.setData({
          orderList: [...this.data.orderList, ...orderList]
        })
        if (orderList.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  /**
   * 补齐订单列表页展示字段。
   *
   * @param {Array} orderList 订单列表
   * @returns {Array}
   */
  decorateOrderList(orderList) {
    return (orderList || []).map((order) => {
      const nextOrder = Object.assign({}, order)
      nextOrder.listOrderItem = (order.listOrderItem || []).map((orderItem) => {
        return Object.assign({}, orderItem, {
          picUrl: util.resolveResourceUrl(orderItem.picUrl, '/public/img/no_pic.png'),
          afterSaleStatusText: this.buildAfterSaleStatusText(orderItem),
          afterSaleVisible: this.isAfterSaleItem(orderItem)
        })
      })
      nextOrder.orderAfterSaleStatus = this.buildOrderAfterSaleStatus(nextOrder.listOrderItem)
      nextOrder.orderAfterSaleSummary = this.buildOrderAfterSaleSummary(nextOrder.listOrderItem)
      return nextOrder
    })
  },
  /**
   * 判断订单项是否已进入售后链路。
   *
   * @param {Object} orderItem 订单项
   * @returns {boolean}
   */
  isAfterSaleItem(orderItem) {
    return orderItem && (orderItem.isRefund === '1' || orderItem.status === '1' || orderItem.status === '2' || orderItem.status === '3')
  },
  /**
   * 构建订单项售后状态文案。
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
   * 汇总订单级售后主状态。
   * 一个订单可能包含多个订单项，优先展示当前最需要关注的阶段。
   *
   * @param {Array} orderItemList 订单项列表
   * @returns {Object|null}
   */
  buildOrderAfterSaleStatus(orderItemList) {
    const list = orderItemList || []
    if (list.some((item) => item.status === '1' && item.isRefund !== '1')) {
      return {
        label: '售后处理中',
        type: 'is-warning'
      }
    }
    if (list.some((item) => item.status === '3' && item.isRefund !== '1')) {
      return {
        label: '退款处理中',
        type: 'is-info'
      }
    }
    if (list.some((item) => item.status === '2' && item.isRefund !== '1')) {
      return {
        label: '存在拒绝退款',
        type: 'is-danger'
      }
    }
    if (list.some((item) => item.isRefund === '1')) {
      return {
        label: '已完成退款',
        type: 'is-success'
      }
    }
    return null
  },
  /**
   * 生成订单级售后摘要，便于用户在列表页直接看到涉及几件商品。
   *
   * @param {Array} orderItemList 订单项列表
   * @returns {string}
   */
  buildOrderAfterSaleSummary(orderItemList) {
    const summary = []
    const applyingCount = (orderItemList || []).filter((item) => item.status === '1' && item.isRefund !== '1').length
    const rejectCount = (orderItemList || []).filter((item) => item.status === '2' && item.isRefund !== '1').length
    const refundedCount = (orderItemList || []).filter((item) => item.isRefund === '1').length
    if (applyingCount > 0) {
      summary.push(`申请中 ${applyingCount} 件`)
    }
    if (rejectCount > 0) {
      summary.push(`被拒绝 ${rejectCount} 件`)
    }
    if (refundedCount > 0) {
      summary.push(`已退款 ${refundedCount} 件`)
    }
    return summary.join('，')
  },
  onReachBottom() {
    if (this.data.loadmore) {
      this.setData({
        ['page.current']: this.data.page.current + 1
      })
      this.orderPage()
    }
  },
  refresh(){
    this.setData({
      loadmore: true,
      orderList: [],
      ['page.current']: 1
    })
    return this.orderPage()
  },
  onPullDownRefresh() {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading()
    this.refresh()
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading()
    // 停止下拉动作
    wx.stopPullDownRefresh()
  },
  tabSelect(e) {
    let dataset = e.currentTarget.dataset
    if (dataset.index != this.data.tabCur){
      this.setData({
        tabCur: dataset.index,
        ['parameter.status']: dataset.key
      })
      this.refresh()
    }
  },
  /**
   * 切换售后快捷筛选。
   *
   * @param {Object} e 点击事件
   */
  afterSaleFilterSelect(e) {
    const dataset = e.currentTarget.dataset || {}
    this.setData({
      ['parameter.afterSaleStatus']: dataset.key
    })
    this.refresh()
  },
  /**
   * 跳转售后中心，查看订单项维度的完整进度。
   */
  goAfterSaleCenter() {
    wx.navigateTo({
      url: `/pages/order/after-sale-list/index?afterSaleStatus=${this.data.parameter.afterSaleStatus || 'ALL'}`
    })
  },
  orderCancel(e) {
    let index = e.target.dataset.index
    let orderList = this.data.orderList
    app.api.orderGet(orderList[index].id)
      .then(res => {
        this.data.orderList[index] = this.decorateOrderList([res.data])[0]
        this.setData({
          orderList: this.data.orderList
        })
      })
  },
  orderDel(e) {
    let index = e.target.dataset.index
    this.data.orderList.splice(index, 1)
    this.setData({
      orderList: this.data.orderList
    })
  },
  unifiedOrder(e) {
    let index = e.target.dataset.index
    let orderList = this.data.orderList
    app.api.orderGet(orderList[index].id)
      .then(res => {
        this.data.orderList[index] = this.decorateOrderList([res.data])[0]
        this.setData({
          orderList: this.data.orderList
        })
      })
  }
})
