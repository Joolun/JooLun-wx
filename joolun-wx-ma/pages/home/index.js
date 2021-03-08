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
    page: {
      searchCount: false,
      current: 1,
      size: 10
    },
    loadmore: true,
    goodsList: [],
    goodsListNew: [],
    goodsListHot: [],
    swiperData: [
      'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/banner1.png',
      'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/banner2.png',
      'http://joolun-open.oss-cn-zhangjiakou.aliyuncs.com/banner3.png'
    ],
    cardCur: 0,
    noticeData: []
  },
  onLoad() {
    app.initPage()
      .then(res => {
        this.loadData()
      })
  },
  onShow(){
    //更新tabbar购物车数量
    wx.setTabBarBadge({
      index: 2,
      text: app.globalData.shoppingCartCount + ''
    })
  },
  loadData(){
    this.goodsNew()
    this.goodsHot()
    this.goodsPage()
  },
  onShareAppMessage: function () {
    let title = 'JooLun商城源码-小程序演示'
    let path = 'pages/home/index'
    return {
      title: title,
      path: path,
      success: function (res) {
        if (res.errMsg == 'shareAppMessage:ok') {
          console.log(res.errMsg)
        }
      },
      fail: function (res) {
        // 转发失败
      }
    }
  },
  //新品首发
  goodsNew() {
    app.api.goodsPage({
      searchCount: false,
      current: 1,
      size: 5,
      descs: 'create_time'
    })
      .then(res => {
        let goodsListNew = res.data.records
        this.setData({
          goodsListNew: goodsListNew
        })
      })
  },
  //热销单品
  goodsHot() {
    app.api.goodsPage({
      searchCount: false,
      current: 1,
      size: 5,
      descs: 'sale_num'
    })
      .then(res => {
        let goodsListHot = res.data.records
        this.setData({
          goodsListHot: goodsListHot
        })
      })
  },
  goodsPage(e) {
    app.api.goodsPage(this.data.page)
      .then(res => {
        let goodsList = res.data.records
        this.setData({
          goodsList: [...this.data.goodsList, ...goodsList]
        })
        if (goodsList.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  refresh(){
    this.setData({
      loadmore: true,
      ['page.current']: 1,
      goodsList: [],
      goodsListNew: [],
      goodsListHot: []
    })
    this.loadData()
  },
  onPullDownRefresh(){
    // 显示顶部刷新图标
    wx.showNavigationBarLoading()
    this.refresh()
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading()
    // 停止下拉动作
    wx.stopPullDownRefresh()
  },
  onReachBottom() {
    if (this.data.loadmore) {
      this.setData({
        ['page.current']: this.data.page.current + 1
      })
      this.goodsPage()
    }
  },
  jumpPage(e){
    let page = e.currentTarget.dataset.page
    if (page){
      wx.navigateTo({
        url: page
      })
    }
  }
})
