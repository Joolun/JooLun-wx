/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
const app = getApp()

Page({
  data: {
    orderInfo: null,
    id: null,
    goodsAppraises: []
  },
  onShow() {
    
  },
  onLoad(options) {
    app.initPage()
      .then(res => {
        this.orderGet(options.orderId)
      })
  },
  orderGet(id) {
    let that = this
    app.api.orderGet(id)
      .then(res => {
        let orderInfo = res.data
        if (!orderInfo) {
          wx.showToast({
            title: '无效订单',
            icon: 'none',
            duration: 5000
          })
          return
        }
        let goodsAppraises = []
        orderInfo.listOrderItem.forEach(function (orderItem, index) {
          let appraisesObj = {}
          appraisesObj.orderId = orderInfo.id
          appraisesObj.orderItemId = orderItem.id
          appraisesObj.spuId = orderItem.spuId
          appraisesObj.skuId = orderItem.skuId
          appraisesObj.specInfo = orderItem.specInfo
          goodsAppraises.push(appraisesObj)
        })
        this.setData({
          orderInfo: orderInfo,
          goodsAppraises: goodsAppraises
        })
      })
  },
  radeOnChange(e){
    let goodsAppraises = this.data.goodsAppraises
    let dataset = e.currentTarget.dataset
    let index = dataset.index
    let type = dataset.type
    let value = e.detail
    if (type == 'goods'){
      goodsAppraises[index].goodsScore = value
    }
    if (type == 'service') {
      goodsAppraises[index].serviceScore = value
    }
    if (type == 'logistics') {
      goodsAppraises[index].logisticsScore = value
    }
    this.setData({
      goodsAppraises: goodsAppraises
    })
  },
  textareaInput(e) {
    let dataset = e.currentTarget.dataset
    let index = dataset.index
    let goodsAppraises = this.data.goodsAppraises
    goodsAppraises[index].content = e.detail.value
    this.setData({
      goodsAppraises: goodsAppraises
    })
  },
  subAppraises(){
    let that = this
    let b = true
    let goodsAppraises = that.data.goodsAppraises
    goodsAppraises.forEach(function (obj, index) {
      if (!obj.goodsScore || !obj.serviceScore || !obj.logisticsScore) {
        wx.showToast({
          title: '请给商品打分',
          icon: 'none',
          duration: 2000
        })
        b = false
        return
      }
    })
    if(b){
      wx.showModal({
        content: '确认提交评价吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success(res) {
          if (res.confirm) {
            app.api.goodsAppraisesAdd(goodsAppraises)
              .then(res => {
                wx.navigateBack()
              })
          }
        }
      })
    }
  }
})