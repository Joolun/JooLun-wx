/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const util = require('../../../utils/util.js')
const app = getApp()

Page({
  data: {
    tabCur: 0,
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
    app.initPage()
      .then(res => {
        this.orderPage()
      })
  },
  orderPage() {
    app.api.orderPage(Object.assign(
      {},
      this.data.page,
      util.filterForm(this.data.parameter)
    ))
      .then(res => {
        let orderList = res.data.records
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
    this.orderPage()
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
  orderCancel(e) {
    let index = e.target.dataset.index
    let orderList = this.data.orderList
    app.api.orderGet(orderList[index].id)
      .then(res => {
        this.data.orderList[index] = res.data
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
        this.data.orderList[index] = res.data
        this.setData({
          orderList: this.data.orderList
        })
      })
  }
})