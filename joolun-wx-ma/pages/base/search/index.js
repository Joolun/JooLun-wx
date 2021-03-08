/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Page({
  data: {
    searchHistory: [],
    goodsList: []
  },
  onShow() {
    this.setData({
      searchHistory: wx.getStorageSync('searchHistory') ? wx.getStorageSync('searchHistory') : []
    })
  },
  onLoad(options) {
    app.initPage()
      .then(res => {
        this.goodsPage()
      })
  },
  searchHandle(e) {
    let value
    if (e.detail.value) {
      value = e.detail.value
    } else if (e.currentTarget.dataset.name){
      value = e.currentTarget.dataset.name
    }
    let searchHistory = this.data.searchHistory
    searchHistory.forEach(function (item, index) {
      let i = 9 //最多缓存10条
      if (item.name == value){
        searchHistory.splice(index, 1)
        i++
      }
      if (index >= i){
        searchHistory.splice(index, 1)
      }
    })
    searchHistory.unshift({
      name: value
    })
    wx.setStorageSync('searchHistory', searchHistory)
    wx.navigateTo({
      url: '/pages/goods/goods-list/index?name=' + value
    })
  },
  clearSearchHistory(){
    let that = this
    wx.showModal({
      content: '确认删除全部历史记录？',
      cancelText: '我再想想',
      confirmColor: '#ff0000',
      success(res) {
        if (res.confirm) {
          that.setData({
            searchHistory: []
          })
          wx.setStorageSync('searchHistory', [])
        }
      }
    })
  },
  goodsPage() {
    app.api.goodsPage({
      searchCount: false,
      current: 1,
      size: 10,
      ascs: '',//升序字段
      descs: 'sale_num'
    })
      .then(res => {
        let goodsList = res.data.records
        this.setData({
          goodsList: goodsList
        })
      })
  },
})