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
    initialized: false,
    tabCur: 0,
    summaryCards: [],
    currentFilterTip: '',
    afterSaleTabs: [
      {
        label: '全部',
        key: 'ALL'
      },
      {
        label: '申请中',
        key: '1'
      },
      {
        label: '已拒绝',
        key: '2'
      },
      {
        label: '待退款',
        key: '3'
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
      ascs: '',
      descs: 'create_time'
    },
    parameter: {
      afterSaleStatus: 'ALL'
    },
    loadmore: true,
    afterSaleList: []
  },
  onLoad(options) {
    this.applyTabByStatus(options.status || options.afterSaleStatus || 'ALL')
    app.initPage()
      .then(() => {
        return this.loadAfterSalePage()
      })
      .finally(() => {
        this.setData({
          initialized: true
        })
      })
  },
  onShow() {
    if (!this.data.initialized) {
      return
    }
    this.refresh()
  },
  /**
   * 根据售后状态同步顶部 Tab。
   *
   * @param {string} status 售后状态
   */
  applyTabByStatus(status) {
    let tabCur = 0
    this.data.afterSaleTabs.forEach((item, index) => {
      if (item.key === status) {
        tabCur = index
      }
    })
    this.setData({
      tabCur: tabCur,
      ['parameter.afterSaleStatus']: status
    })
  },
  /**
   * 拉取售后中心分页数据。
   * 后端当前仍按订单分页，这里在前端展开成订单项维度，便于用户查看每个商品的售后进度。
   */
  loadAfterSalePage() {
    return app.api.orderPage(Object.assign(
      {},
      this.data.page,
      util.filterForm(this.data.parameter)
    ))
      .then((res) => {
        const orderList = res.data.records || []
        const afterSaleList = this.buildAfterSaleList(orderList)
        const mergedList = [...this.data.afterSaleList, ...afterSaleList]
        this.setData({
          afterSaleList: mergedList,
          summaryCards: this.buildSummaryCards(mergedList),
          currentFilterTip: this.buildCurrentFilterTip(this.data.parameter.afterSaleStatus)
        })
        if (orderList.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
      .catch(() => {})
  },
  /**
   * 将订单列表展开成售后订单项列表。
   *
   * @param {Array} orderList 订单列表
   * @returns {Array}
   */
  buildAfterSaleList(orderList) {
    const currentStatus = this.data.parameter.afterSaleStatus
    const result = []
    ;(orderList || []).forEach((order) => {
      ;(order.listOrderItem || []).forEach((item) => {
        if (!this.isMatchAfterSaleItem(item, currentStatus)) {
          return
        }
        result.push({
          id: item.id,
          orderId: order.id,
          orderNo: order.orderNo,
          orderStatusDesc: order.statusDesc,
          orderIsPay: order.isPay,
          picUrl: util.resolveResourceUrl(item.picUrl, '/public/img/no_pic.png'),
          spuName: item.spuName,
          specInfo: item.specInfo,
          quantity: item.quantity,
          paymentPrice: item.paymentPrice,
          status: item.status,
          isRefund: item.isRefund,
          refundReason: item.refundReason,
          refundAuditRemark: item.refundAuditRemark,
          refundApplyTime: item.refundApplyTime,
          refundAuditTime: item.refundAuditTime,
          refundSuccessTime: item.refundSuccessTime
        })
      })
    })
    return result.map((record) => this.decorateAfterSaleRecord(record))
  },
  /**
   * 补齐售后记录展示字段。
   *
   * @param {Object} record 售后记录
   * @returns {Object}
   */
  decorateAfterSaleRecord(record) {
    const nextRecord = Object.assign({}, record)
    nextRecord.statusText = this.buildAfterSaleStatusDesc(record)
    nextRecord.statusClass = this.buildAfterSaleStatusClass(record)
    nextRecord.stageTitle = this.buildStageTitle(record)
    nextRecord.stageDesc = this.buildStageDesc(record)
    nextRecord.nextActionText = this.buildNextActionText(record)
    nextRecord.timeline = this.buildAfterSaleTimeline(record)
    return nextRecord
  },
  /**
   * 构建顶部概览卡片。
   *
   * @param {Array} recordList 售后记录列表
   * @returns {Array}
   */
  buildSummaryCards(recordList) {
    const list = recordList || []
    const processingCount = list.filter((item) => item.status === '1' || item.status === '3').length
    const rejectedCount = list.filter((item) => item.status === '2' && item.isRefund !== '1').length
    const refundedCount = list.filter((item) => item.isRefund === '1').length
    const refundAmount = list.reduce((total, item) => {
      return total + Number(item.paymentPrice || 0)
    }, 0)
    return [
      {
        label: '售后单数',
        value: `${list.length}`,
        tip: '当前筛选结果下的商品售后记录',
        emphasisClass: 'is-primary'
      },
      {
        label: '处理中',
        value: `${processingCount}`,
        tip: '包含申请中和退款处理中',
        emphasisClass: 'is-warning'
      },
      {
        label: '已退款',
        value: `${refundedCount}`,
        tip: '退款已完成回写到账',
        emphasisClass: 'is-success'
      },
      {
        label: '售后金额',
        value: `¥${refundAmount.toFixed(2)}`,
        tip: rejectedCount > 0 ? `另有 ${rejectedCount} 笔被拒绝` : '按当前结果累计统计',
        emphasisClass: 'is-danger'
      }
    ]
  },
  /**
   * 构建当前筛选说明。
   *
   * @param {string} status 售后状态
   * @returns {string}
   */
  buildCurrentFilterTip(status) {
    if (status === '1') {
      return '当前仅展示用户已提交、等待商家审核的退款申请。'
    }
    if (status === '2') {
      return '当前仅展示商家已拒绝的退款申请，可回订单详情重新发起退款。'
    }
    if (status === '3') {
      return '当前仅展示商家已同意、等待退款结果回写的记录。'
    }
    if (status === '4') {
      return '当前仅展示退款已完成的记录，请留意原支付账户到账通知。'
    }
    return '这里集中展示全部售后记录，可直接查看申请原因、审核结果和退款时间。'
  },
  /**
   * 判断订单项是否属于当前筛选状态。
   *
   * @param {Object} orderItem 订单项
   * @param {string} status 售后状态筛选
   * @returns {boolean}
   */
  isMatchAfterSaleItem(orderItem, status) {
    if (!orderItem) {
      return false
    }
    if (status === '4') {
      return orderItem.isRefund === '1'
    }
    if (status === 'ALL') {
      return orderItem.isRefund === '1' || orderItem.status === '1' || orderItem.status === '2' || orderItem.status === '3'
    }
    return orderItem.status === status && orderItem.isRefund !== '1'
  },
  /**
   * 售后 Tab 切换。
   *
   * @param {Object} e 点击事件
   */
  tabSelect(e) {
    const dataset = e.currentTarget.dataset || {}
    if (dataset.index === this.data.tabCur) {
      return
    }
    this.setData({
      tabCur: dataset.index,
      ['parameter.afterSaleStatus']: dataset.key,
      currentFilterTip: this.buildCurrentFilterTip(dataset.key)
    })
    this.refresh()
  },
  onReachBottom() {
    if (!this.data.loadmore) {
      return
    }
    this.setData({
      ['page.current']: this.data.page.current + 1
    })
    this.loadAfterSalePage()
  },
  refresh() {
    this.setData({
      loadmore: true,
      afterSaleList: [],
      summaryCards: [],
      currentFilterTip: this.buildCurrentFilterTip(this.data.parameter.afterSaleStatus),
      ['page.current']: 1
    })
    return this.loadAfterSalePage()
  },
  onPullDownRefresh() {
    wx.showNavigationBarLoading()
    this.refresh()
    wx.hideNavigationBarLoading()
    wx.stopPullDownRefresh()
  },
  /**
   * 跳转订单详情，继续查看订单物流、支付和售后完整上下文。
   *
   * @param {Object} e 点击事件
   */
  goOrderDetail(e) {
    const orderId = e.currentTarget.dataset.orderId
    if (!orderId) {
      return
    }
    wx.navigateTo({
      url: `/pages/order/order-detail/index?id=${orderId}`
    })
  },
  /**
   * 售后场景客服入口。
   * 这里同样走微信原生客服，便于用户带着售后上下文直接咨询。
   *
   * @param {Object} e 客服回调
   */
  handleContact(e) {
    console.log('after-sale-contact', e)
  },
  /**
   * 格式化售后状态文案。
   *
   * @param {Object} record 售后记录
   * @returns {string}
   */
  buildAfterSaleStatusDesc(record) {
    if (record.isRefund === '1') {
      return '已退款'
    }
    if (record.status === '1') {
      return '申请退款中'
    }
    if (record.status === '2') {
      return '退款被拒绝'
    }
    if (record.status === '3') {
      return '等待退款回调'
    }
    return '无售后'
  },
  /**
   * 构建状态标签样式类。
   *
   * @param {Object} record 售后记录
   * @returns {string}
   */
  buildAfterSaleStatusClass(record) {
    if (record.isRefund === '1') {
      return 'is-success'
    }
    if (record.status === '1') {
      return 'is-warning'
    }
    if (record.status === '2') {
      return 'is-danger'
    }
    if (record.status === '3') {
      return 'is-info'
    }
    return ''
  },
  /**
   * 构建当前阶段标题。
   *
   * @param {Object} record 售后记录
   * @returns {string}
   */
  buildStageTitle(record) {
    if (record.isRefund === '1') {
      return '退款已完成'
    }
    if (record.status === '1') {
      return '等待商家审核'
    }
    if (record.status === '2') {
      return '本次申请被拒绝'
    }
    if (record.status === '3') {
      return '退款处理中'
    }
    return '暂无售后进度'
  },
  /**
   * 构建当前阶段说明。
   *
   * @param {Object} record 售后记录
   * @returns {string}
   */
  buildStageDesc(record) {
    if (record.isRefund === '1') {
      return '退款结果已经回写完成，后续可在账单或原支付渠道查看到账。'
    }
    if (record.status === '1') {
      return '商家尚未处理本次退款申请，建议保留电话畅通以便必要时沟通。'
    }
    if (record.status === '2') {
      return '商家已拒绝退款申请，可根据审核备注补充说明后重新提交。'
    }
    if (record.status === '3') {
      return '商家已同意退款，系统正在等待退款结果回写。'
    }
    return '当前记录尚未进入售后处理流程。'
  },
  /**
   * 构建下一步提示。
   *
   * @param {Object} record 售后记录
   * @returns {string}
   */
  buildNextActionText(record) {
    if (record.isRefund === '1') {
      return '下一步：关注到账通知，如有问题可联系商家核对退款状态。'
    }
    if (record.status === '1') {
      return '下一步：等待商家审核处理。'
    }
    if (record.status === '2') {
      return '下一步：查看拒绝原因后，可回订单详情重新申请退款。'
    }
    if (record.status === '3') {
      return '下一步：等待系统完成退款回调。'
    }
    return '下一步：暂无。'
  },
  /**
   * 构建售后时间线。
   *
   * @param {Object} record 售后记录
   * @returns {Array}
   */
  buildAfterSaleTimeline(record) {
    const timeline = []
    if (record.refundApplyTime) {
      timeline.push({
        title: '提交申请',
        time: this.formatDateTime(record.refundApplyTime),
        desc: `申请原因：${record.refundReason || '未填写'}`
      })
    }
    if (record.refundAuditTime) {
      timeline.push({
        title: record.status === '2' ? '商家拒绝退款' : '商家处理退款',
        time: this.formatDateTime(record.refundAuditTime),
        desc: `审核备注：${record.refundAuditRemark || '暂无审核备注'}`
      })
    }
    if (record.refundSuccessTime || record.isRefund === '1') {
      timeline.push({
        title: '退款完成',
        time: this.formatDateTime(record.refundSuccessTime),
        desc: '系统已完成退款结果回写'
      })
    }
    return timeline
  },
  /**
   * 统一格式化时间。
   *
   * @param {string} value 时间值
   * @returns {string}
   */
  formatDateTime(value) {
    if (!value) {
      return '暂无'
    }
    return String(value).replace('T', ' ').substring(0, 19)
  }
})
