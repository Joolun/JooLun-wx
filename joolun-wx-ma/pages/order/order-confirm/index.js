/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
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
    userInfo: null,
    spuIds: [],
  },
  onShow() {
    
  },
  onLoad: function () {
    this.userAddressPage()
    this.userInfoGet()
    this.orderConfirmDo()
  },
  deliveryWayChange(e){
    this.setData({
      [`orderSubParm.deliveryWay`]: e.detail.value,
      freightMap: null
    })
    this.orderConfirmDo()
  },
  orderConfirmDo(){
    // 本地获取参数信息
    let that = this
    wx.getStorage({
      key: 'param-orderConfirm',
      success: function (res) {
        let orderConfirmData = res.data
        let salesPrice = 0 //订单金额
        let freightPrice = 0 //运费
        let spuIds = null
        orderConfirmData.forEach((orderConfirm, index) => {
          if (spuIds) {
            spuIds = spuIds + ',' + orderConfirm.spuId
          } else {
            spuIds = orderConfirm.spuId
          }
          salesPrice = (Number(salesPrice) + orderConfirm.salesPrice * orderConfirm.quantity).toFixed(2)
          orderConfirm.paymentPrice = (orderConfirm.salesPrice * orderConfirm.quantity).toFixed(2)
          //计算运费
          orderConfirm.freightPrice = 0
          freightPrice = (Number(freightPrice) + Number(orderConfirm.freightPrice)).toFixed(2)
        })
        that.setData({
          orderConfirmData: orderConfirmData,
          salesPrice: salesPrice,
          freightPrice: freightPrice,
          paymentPrice: salesPrice,
          spuIds: spuIds
        })
        
      }
    })
  },
  //计算运费
  countFreight(orderConfirm, freightTemplat, quantity){
    
  },
  //获取默认收货地址
  userAddressPage() {
    app.api.userAddressPage(
      {
        searchCount: false,
        current: 1,
        size: 1,
        isDefault: '1'
    })
      .then(res => {
        let records = res.data.records
        if (records && records.length > 0){
          this.setData({
            userAddress: records[0]
          })
        }
      })
  },
  //获取商城用户信息
  userInfoGet() {
    app.api.wxUserGet()
      .then(res => {
        this.setData({
          userInfo: res.data
        })
      })
  },
  userMessageInput(e){
    this.setData({
      [`orderSubParm.userMessage`]: e.detail.value
    })
  },
  //提交订单
  orderSub(){
    let that = this
    let userAddress = that.data.userAddress
    if (that.data.orderSubParm.deliveryWay == '1' && userAddress == null){
      wx.showToast({
        title: '请选择收货地址',
        icon: 'none',
        duration: 2000
      })
      return
    }
    that.setData({
      loading: true
    })
    let orderSubParm = that.data.orderSubParm
    orderSubParm.skus = that.data.orderConfirmData
    app.api.orderSub(Object.assign(
      {},
      { userAddressId: that.data.orderSubParm.deliveryWay == '1' ? userAddress.id : null},
      orderSubParm
    ))
      .then(res => {
        wx.redirectTo({
          url: '/pages/order/order-detail/index?callPay=true&id=' + res.data.id
        })
      }).catch(() => {
        that.setData({
          loading: false
        })
      })
  }
})