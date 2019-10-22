/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
const app = getApp()

Page({
  data: {
    page: {
      searchCount: false,
      current: 1,
      size: 10
    },
    loadmore: true,
    goodsList: [],
    goodsListNew: [],
    goodsListHot: [],
    swiperList: [{
      id: 0,
      type: 'image',
      url: 'http://img14.360buyimg.com/cms/jfs/t1/41875/15/15234/177824/5d7e4bbdE9d92026d/a22352695fb54048.jpg',
      page: '/pages/goods/goods-detail/index?id=2835671ff031c18cb181b1a199f86b01'
    }, {
        id: 2,
        type: 'image',
        url: 'http://img10.360buyimg.com/cms/jfs/t1/63177/15/10387/240991/5d7f9dceEeeb37fc9/836c313d04150d0f.jpg',
        page: '/pages/goods/goods-list/index?categorySecond=862a74f109f7f14bcbfff1d5adf73cdc&title=%E5%8D%8E%E4%B8%BA'
    }, {
      id: 1,
      type: 'image',
      url: 'http://img12.360buyimg.com/cms/jfs/t1/52071/29/11410/442751/5d84357aE3604f88b/0da811f943ecd2d3.jpg',
      page: '/pages/goods/goods-detail/index?id=58c12341a226b641435b9aa435a1133c'
    }]
  },
  onLoad() {
    app.initPage()
      .then(res => {
        this.goodsNew()
        this.goodsHot()
        this.goodsPage()
      })
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
  scrollToLower() {
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
