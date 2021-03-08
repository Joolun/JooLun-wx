/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Page({
  data: {
    page: {
      searchCount: false,
      current: 1,
      size: 10,
      ascs: '',//升序字段
      descs: ''
    },
    parameter: {},
    loadmore: true,
    userAddress: [],
    select: false
  },
  onLoad(options) {
    if (options.select){
      this.setData({
        select: true
      })
    }
  },
  onShow() {
    app.initPage()
      .then(res => {
        this.userAddressPage()
      })
  },
  userAddressPage() {
    app.api.userAddressPage(this.data.page)
      .then(res => {
        let userAddress = res.data.records
        this.setData({
          userAddress: userAddress,
          loadmore: false
        })
      })
  },
  toAdd(){
    wx.setStorage({
      key: 'param-userAddressForm',
      data: []
    })
    wx.navigateTo({
      url: '/pages/user/user-address/form/index'
    })
  },
  toEdit(e){
    let index = e.currentTarget.dataset.index
    let userAddressForm = this.data.userAddress[index]
    /* 把参数信息异步存储到缓存当中 */
    wx.setStorage({
      key: 'param-userAddressForm',
      data: userAddressForm
    })
    wx.navigateTo({
      url: '/pages/user/user-address/form/index'
    })
  },
  selectUserAddress(e){
    if (this.data.select){
      let index = e.currentTarget.dataset.index
      let userAddressForm = this.data.userAddress[index]
      var pages = getCurrentPages(); // 获取页面栈
      var currPage = pages[pages.length - 1]; // 当前页面
      var prevPage = pages[pages.length - 2]; // 上一个页面
      prevPage.setData({
        userAddress: userAddressForm
      })
      wx.navigateBack()
    }
  }
})
