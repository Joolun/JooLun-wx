/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

/**
 * 版权所有 (C) 2018-2019 www.joolun.com
 * 保留所有权利
 * 注意：
 * 本软件为 www.joolun.com 开发研制，项目使用请保留此说明
 *
 * 多规格改造说明：
 * 1. 购物车记录现在以 skuId 为准，不再只依赖 spuId。
 * 2. 价格和库存校验统一读取当前选中 SKU 的快照数据。
 * 3. “修改规格”会把 skuId 和 shoppingCartId 一起带回详情页。
 */
const util = require('../../utils/util.js')
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    page: {
      current: 1,
      size: 50,
      ascs: '',
      descs: 'create_time'
    },
    parameter: {},
    loadmore: true,
    operation: true,
    shoppingCartData: [],
    shoppingCartDataInvalid: [],
    isAllSelect: false,
    selectValue: [],
    settlePrice: 0,
    goodsSpu: [],
    shoppingCartSelect: []
  },
  onShow() {
    // 每次切回购物车页都重新拉取列表，保证价格、库存和勾选态及时刷新。
    wx.setTabBarBadge({
      index: 2,
      text: app.globalData.shoppingCartCount + ''
    })
    app.initPage()
      .then(() => {
        this.shoppingCartPage()
      })
  },
  onLoad: function () {
    app.initPage()
      .then(() => {
        this.goodsRecom()
      })
  },
  operation() {
    this.setData({
      operation: !this.data.operation
    })
    this.checkboxHandle(this.data.selectValue)
  },
  shoppingCartPage() {
    // 将购物车记录拆成有效和失效两组，结算时只处理有效商品，逻辑更清晰。
    app.api.shoppingCartPage(this.data.page)
      .then(res => {
        app.globalData.shoppingCartCount = res.data.total + ''
        wx.setTabBarBadge({
          index: 2,
          text: app.globalData.shoppingCartCount + ''
        })

        const shoppingCartData = []
        const shoppingCartDataInvalid = []
        res.data.records.forEach((record) => {
          const shoppingCart = this.normalizeShoppingCart(record)
          if (!shoppingCart.goodsSpu || shoppingCart.goodsSpu.shelf == '0') {
            shoppingCartDataInvalid.push(shoppingCart)
          } else {
            shoppingCartData.push(shoppingCart)
          }
        })
        this.setData({
          shoppingCartData: shoppingCartData,
          shoppingCartDataInvalid: shoppingCartDataInvalid,
          loadmore: false
        })
        if (this.data.selectValue.length > 0) {
          this.checkboxHandle(this.data.selectValue)
        }
      })
  },
  goodsRecom() {
    app.api.goodsPage({
      searchCount: false,
      current: 1,
      size: 4,
      descs: 'create_time'
    })
      .then(res => {
        this.setData({
          goodsListRecom: res.data.records
        })
      })
  },
  normalizeShoppingCart(shoppingCart) {
    const nextGoodsSpu = shoppingCart.goodsSpu
      ? Object.assign({}, shoppingCart.goodsSpu, {
        picUrls: util.normalizePicUrlList(shoppingCart.goodsSpu.picUrls)
      })
      : null
    return Object.assign({}, shoppingCart, {
      picUrl: util.resolveResourceUrl(shoppingCart.picUrl),
      goodsSpu: nextGoodsSpu
    })
  },
  cartNumChang(e) {
    // 数量变更时必须同时携带 spuId 和 skuId，后端才能准确定位到当前购物车行。
    const index = e.target.dataset.index
    const shoppingCart = this.data.shoppingCartData[index]
    const quantity = Number(e.detail)
    this.setData({
      [`shoppingCartData[${index}].quantity`]: quantity
    })
    this.shoppingCartEdit({
      id: shoppingCart.id,
      spuId: shoppingCart.spuId,
      skuId: shoppingCart.skuId,
      quantity: quantity
    })
    this.countSelect()
  },
  shoppingCartEdit(parm) {
    app.api.shoppingCartEdit(parm)
  },
  userCollectAdd() {
  },
  shoppingCartDel() {
    const selectValue = this.data.selectValue
    if (selectValue.length > 0) {
      wx.showModal({
        content: '确认将这' + selectValue.length + '个宝贝删除？',
        cancelText: '我再想想',
        confirmColor: '#ff0000',
        success: (res) => {
          if (res.confirm) {
            app.api.shoppingCartDel(selectValue)
              .then(() => {
                this.setData({
                  selectValue: [],
                  isAllSelect: false,
                  settlePrice: 0
                })
                this.shoppingCartPage()
              })
          }
        }
      })
    }
  },
  clearInvalid() {
    const selectValue = this.data.shoppingCartDataInvalid.map(item => item.id)
    wx.showModal({
      content: '确认清空失效的宝贝吗',
      cancelText: '我再想想',
      confirmColor: '#ff0000',
      success: (res) => {
        if (res.confirm) {
          app.api.shoppingCartDel(selectValue)
            .then(() => {
              this.setData({
                shoppingCartDataInvalid: []
              })
            })
        }
      }
    })
  },
  checkboxHandle(selectValue) {
    // 结算模式下需要过滤库存不足的商品，避免勾选后到了下单页再失败。
    const shoppingCartData = this.data.shoppingCartData
    const nextSelectValue = Array.isArray(selectValue) ? selectValue.slice() : []
    let isAllSelect = false
    if (shoppingCartData.length == nextSelectValue.length) { isAllSelect = true }
    if (nextSelectValue.length > 0) {
      const shoppingCartIds = []
      shoppingCartData.forEach((shoppingCart) => {
        shoppingCartIds.push(shoppingCart.id)
        const selectValueIndex = nextSelectValue.indexOf(shoppingCart.id)
        if (selectValueIndex > -1) {
          if (!this.data.operation) {
            shoppingCart.checked = true
          } else {
            if (shoppingCart.goodsSpu && shoppingCart.quantity <= shoppingCart.goodsSpu.stock) {
              shoppingCart.checked = true
            } else {
              shoppingCart.checked = false
              nextSelectValue.splice(selectValueIndex, 1)
            }
          }
        } else {
          shoppingCart.checked = false
        }
      })
      nextSelectValue.slice().forEach((obj) => {
        if (shoppingCartIds.indexOf(obj) <= -1) {
          const removeIndex = nextSelectValue.indexOf(obj)
          if (removeIndex > -1) {
            nextSelectValue.splice(removeIndex, 1)
          }
        }
      })
    } else {
      shoppingCartData.forEach((shoppingCart) => {
        shoppingCart.checked = false
      })
    }
    this.setData({
      shoppingCartData: shoppingCartData,
      isAllSelect: isAllSelect,
      selectValue: nextSelectValue
    })
    this.countSelect()
  },
  checkboxChange: function (e) {
    this.checkboxHandle(e.detail.value)
  },
  checkboxAllChange(e) {
    const value = e.detail.value
    if (value.length > 0) {
      this.setAllSelectValue(true)
    } else {
      this.setAllSelectValue(false)
    }
  },
  setAllSelectValue(status) {
    const shoppingCartData = this.data.shoppingCartData
    const selectValue = []
    if (shoppingCartData.length > 0) {
      if (status) {
        shoppingCartData.forEach((shoppingCart) => {
          if (!this.data.operation) {
            selectValue.push(shoppingCart.id)
          } else if (shoppingCart.goodsSpu && shoppingCart.quantity <= shoppingCart.goodsSpu.stock) {
            selectValue.push(shoppingCart.id)
          }
        })
      }
      this.checkboxHandle(selectValue)
    }
  },
  countSelect() {
    // 结算金额按当前 SKU 价格快照计算，不再直接使用 SPU 单规格价格。
    const selectValue = this.data.selectValue
    let settlePrice = 0
    if (selectValue.length <= 0) {
      this.setData({
        settlePrice: settlePrice
      })
    } else {
      this.data.shoppingCartData.forEach((shoppingCart) => {
        if (
          selectValue.indexOf(shoppingCart.id) > -1 &&
          shoppingCart.goodsSpu &&
          shoppingCart.quantity <= shoppingCart.goodsSpu.stock
        ) {
          const currentPrice = shoppingCart.goodsSpu.salesPrice || shoppingCart.addPrice
          settlePrice = Number(settlePrice) + Number(shoppingCart.quantity) * Number(currentPrice)
        }
      })
      this.setData({
        settlePrice: settlePrice.toFixed(2)
      })
    }
  },
  changeSpecs(e) {
    // 跳回商品详情页时带上当前购物车行 id，改规格后仍然覆盖原购物车记录。
    const index = e.currentTarget.dataset.index
    const shoppingCart = this.data.shoppingCartData[index]
    wx.navigateTo({
      url: '/pages/goods/goods-detail/index?id=' + shoppingCart.spuId + '&skuId=' + shoppingCart.skuId + '&shoppingCartId=' + shoppingCart.id
    })
  },
  goodsGet(id) {
    app.api.goodsGet(id)
      .then(res => {
        this.setData({
          goodsSpu: res.data
        })
      })
  },
  operateCartEvent() {
    this.shoppingCartPage()
  },
  orderConfirm() {
    // 结算前把选中的 skuId 和规格文案一并缓存，下单确认页直接复用这份快照。
    const params = []
    this.data.shoppingCartData.forEach((shoppingCart) => {
      if (
        shoppingCart.checked &&
        shoppingCart.goodsSpu &&
        shoppingCart.goodsSpu.shelf == '1' &&
        shoppingCart.quantity <= shoppingCart.goodsSpu.stock
      ) {
        params.push({
          spuId: shoppingCart.spuId,
          skuId: shoppingCart.skuId,
          quantity: shoppingCart.quantity,
          salesPrice: shoppingCart.goodsSpu.salesPrice || shoppingCart.addPrice,
          spuName: shoppingCart.goodsSpu.name,
          picUrl: shoppingCart.picUrl || (shoppingCart.goodsSpu.picUrls ? shoppingCart.goodsSpu.picUrls[0] : ''),
          specInfo: shoppingCart.specInfo || ''
        })
      }
    })
    wx.setStorage({
      key: 'param-orderConfirm',
      data: params
    })
    wx.navigateTo({
      url: '/pages/order/order-confirm/index'
    })
  }
})
