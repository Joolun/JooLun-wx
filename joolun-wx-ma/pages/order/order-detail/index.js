/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Page({
  data: {
    orderInfo: null,
    id: null,
    callPay: false//是否直接调起支付
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
  //复制内容
  copyData(e) {
    wx.setClipboardData({
      data: e.currentTarget.dataset.data
    })
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