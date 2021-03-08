/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    wxUser: null,
    userInfo: null,
    orderCountAll: []
  },
  onShow(){
    //更新tabbar购物车数量
    wx.setTabBarBadge({
      index: 2,
      text: app.globalData.shoppingCartCount + ''
    })
    
    let wxUser = app.globalData.wxUser
    this.setData({
      wxUser: wxUser
    })
    this.wxUserGet()
    this.orderCountAll()
  },
  onLoad(){
    if(this.data.config.adEnable){
      // 在页面中定义插屏广告
      let interstitialAd = null
      // 在页面onLoad回调事件中创建插屏广告实例
      if (wx.createInterstitialAd) {
        interstitialAd = wx.createInterstitialAd({
          adUnitId: this.data.config.adInsertScreenID
        })
        interstitialAd.onLoad(() => { })
        interstitialAd.onError((err) => { })
        interstitialAd.onClose(() => { })
      }
      // 在适合的场景显示插屏广告
      if (interstitialAd) {
        interstitialAd.show().catch((err) => {
          console.error(err)
        })
      }
    }
  },
  /**
   * 小程序设置
  */
  settings: function () {
    wx.openSetting({
      success: function (res) {
        console.log(res.authSetting)
      }
    })
  },
  agreeGetUser(e) {
    if (e.detail.errMsg == 'getUserInfo:ok') {
      app.api.wxUserSave(e.detail)
        .then(res => {
          let wxUser = res.data
          this.setData({
            wxUser: wxUser
          })
          app.globalData.wxUser = wxUser
          this.wxUserGet()
        })
    }
  },
  //获取商城用户信息
  wxUserGet(){
    app.api.wxUserGet()
      .then(res => {
        this.setData({
          userInfo: res.data
        })
      })
  },
  orderCountAll(){
    app.api.orderCountAll()
      .then(res => {
        this.setData({
          orderCountAll: res.data
        })
      })
  }
})
