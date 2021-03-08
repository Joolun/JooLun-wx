/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const WxParse = require('../../../public/wxParse/wxParse.js')
import Poster from '../../../components/wxa-plugin-canvas/poster/poster'
const { base64src } = require('../../../utils/base64src.js')
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    goodsSpu: null,
    currents: 1,
    cartNum: 1,
    goodsSpecData: [],
    shoppingCartCount: 0,
    shareShow: '',
    modalService: '',
  },
  onLoad(options) {
    let id
    if (options.scene){//接受二维码中参数
      id = decodeURIComponent(options.scene)
    }else{
      id = options.id
    }
    this.setData({
      id: id
    })
    app.initPage()
      .then(res => {
        this.goodsGet(id)
        this.shoppingCartCount()
      })
  },
  onShareAppMessage: function () {
    let goodsSpu = this.data.goodsSpu
    let title = goodsSpu.name
    let imageUrl = goodsSpu.picUrls[0]
    let path = 'pages/goods/goods-detail/index?id=' + goodsSpu.id
    return {
      title: title,
      path: path,
      imageUrl: imageUrl,
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
  goodsGet(id) {
    app.api.goodsGet(id)
      .then(res => {
        let goodsSpu = res.data
        this.setData({
          goodsSpu: goodsSpu
        })
        //html转wxml
        WxParse.wxParse('description', 'html', goodsSpu.description, this, 0)
      })
  },
  
  change: function (e) {
    this.setData({
      currents: e.detail.current + 1
    })
  },
  // 购买或加入购物车
  toDo(e) {
    let canDo = true
    if (canDo) {
      let goodsSpu = this.data.goodsSpu
      if (e.currentTarget.dataset.type == '1') {//加购物车
        if (this.data.shoppingCartId) {
          app.api.shoppingCartEdit({
            id: this.data.shoppingCartId,
            spuId: goodsSpu.id,
            quantity: this.data.cartNum,
            addPrice: goodsSpu.salesPrice,
            spuName: goodsSpu.name,
            picUrl: goodsSpu.picUrls ? goodsSpu.picUrls[0]:''
          })
            .then(res => {
              wx.showToast({
                title: '修改成功',
                duration: 5000
              })
              this.shoppingCartCount();
            })
        } else {
          app.api.shoppingCartAdd({
            spuId: goodsSpu.id,
            quantity: this.data.cartNum,
            addPrice: goodsSpu.salesPrice,
            spuName: goodsSpu.name,
            picUrl: goodsSpu.picUrls ? goodsSpu.picUrls[0] : ''
          })
            .then(res => {
              wx.showToast({
                title: '添加成功',
                duration: 5000
              })
              this.setData({
                modalSku: false
              })
              this.shoppingCartCount()
            })
        }
      } else {//立即购买，前去确认订单
        if (this.data.goodsSpu.stock <= 0) {
          wx.showToast({
            title: '抱歉，库存不足暂时无法购买',
            icon: 'none',
            duration: 2000
          })
          return;
        }
        /* 把参数信息异步存储到缓存当中 */
        wx.setStorage({
          key: 'param-orderConfirm',
          data: [{
            spuId: goodsSpu.id,
            quantity: this.data.cartNum,
            salesPrice: goodsSpu.salesPrice,
            spuName: goodsSpu.name,
            picUrl: goodsSpu.picUrls ? goodsSpu.picUrls[0] : ''
          }]
        })
        wx.navigateTo({
          url: '/pages/order/order-confirm/index'
        })
      }
    }
  },
  shoppingCartCount(){
    app.api.shoppingCartCount()
      .then(res => {
        let shoppingCartCount = res.data
        this.setData({
          shoppingCartCount: shoppingCartCount
        })
        //设置TabBar购物车数量
        app.globalData.shoppingCartCount = shoppingCartCount + ''
      })
  },
  operateCartEvent(){
    this.shoppingCartCount()
  },
  shareShow(){
    this.setData({
      shareShow: 'show'
    })
  },
  shareHide(){
    this.setData({
      shareShow: ''
    })
  },
  onPosterSuccess(e) {
    const { detail } = e
    this.setData({
      posterUrl: detail
    })
  },
  onPosterFail(err) {
    console.error(err);
  },
  hidePosterShow(){
    this.setData({
      posterShow: false,
      shareShow: ''
    })
  },
  /**
   * 异步生成海报
   */
  onCreatePoster() {
    
  },
  //点击保存到相册
  savePoster: function () {
    
  },
  handleContact(e) {
    console.log(e)
  }
})
