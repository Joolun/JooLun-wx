/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Component({
  properties: {
    orderInfo: {
      type: Object,
      value: {}
    },
    callPay: {
      type: Boolean,
      value: false
    },
    contact: {
      type: Boolean,
      value: false
    }
  },
  data: {
    loading: false
  },
  ready() {
    let that = this
    setTimeout(function () {
      if (that.data.callPay) {
        that.unifiedOrder()
      }
    }, 1000)
  },
  methods: {
    orderReceive(){
      let that = this
      wx.showModal({
        content: '是否确认收货吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success(res) {
          if (res.confirm) {
            let id = that.data.orderInfo.id
            app.api.orderReceive(id)
              .then(res => {
                that.triggerEvent('orderReceive', res)
              })
          }
        }
      })
    },
    orderCancel() {
      let that = this
      wx.showModal({
        content: '确认取消该订单吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success(res) {
          if (res.confirm) {
            let id = that.data.orderInfo.id
            app.api.orderCancel(id)
              .then(res => {
                that.triggerEvent('orderCancel', res)
              })
          }
        }
      })
    },
    orderDel() {
      let that = this
      wx.showModal({
        content: '确认删除该订单吗？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success(res) {
          if (res.confirm) {
            let id = that.data.orderInfo.id
            app.api.orderDel(id)
              .then(res => {
                that.triggerEvent('orderDel', res)
              })
          }
        }
      })
    },
    unifiedOrder() {
      this.setData({
        loading: true
      })
      var that = this
      let orderInfo = this.data.orderInfo
      app.api.unifiedOrder({
        id: orderInfo.id
      })
        .then(res => {
          this.setData({
            loading: false
          })
          if (orderInfo.paymentPrice <= 0){//0元付款
            that.triggerEvent('unifiedOrder', res)
          }else{
            let payData = res.data
            wx.requestPayment({
              'timeStamp': payData.timeStamp,
              'nonceStr': payData.nonceStr,
              'package': payData.packageValue,
              'signType': payData.signType,
              'paySign': payData.paySign,
              'success': function (res) {
                that.triggerEvent('unifiedOrder', res)
              },
              'fail': function (res) {

              },
              'complete': function (res) {
                console.log(res)
              }
            })
          }
        }).catch(() => {
          this.setData({
            loading: false
          })
        })
    },
    urgeOrder() {
      wx.showToast({
        title: '已提醒卖家发货',
        icon: 'success',
        duration: 2000
      })
    },
    orderLogistics(){
      wx.navigateTo({
        url: '/pages/order/order-logistics/index?id=' + this.data.orderInfo.orderLogistics.id
      })
    },
    orderAppraise(){
      wx.navigateTo({
        url: '/pages/appraises/form/index?orderId=' + this.data.orderInfo.id
      })
    }
  }
})