/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

const util = require('../../utils/util.js')
const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    homeSections: [],
    hasServiceSection: false,
    serviceGuideList: [
      '选品推荐',
      '活动优惠',
      '订单进度',
      '售后帮助'
    ],
    serviceContactCard: {
      homeTitle: 'JooLun 商城咨询',
      chooseGoodsTitle: '首页选品咨询',
      orderTitle: '订单进度咨询',
      afterSaleTitle: '售后帮助咨询'
    },
    orderStats: {
      totalCount: 0
    },
    afterSaleStats: {
      totalCount: 0
    },
    page: {
      searchCount: false,
      current: 1,
      size: 10
    },
    loadmore: true,
    goodsList: []
  },
  onLoad() {
    app.initPage()
      .then(() => {
        return Promise.allSettled([this.loadData(), this.loadHomeServiceStatsSafely()])
      })
  },
  onShow() {
    app.setShoppingCartBadge(app.globalData.shoppingCartCount)
    this.loadHomeServiceStatsSafely()
  },
  onShareAppMessage() {
    return {
      title: 'JooLun 商城源码-小程序演示',
      path: 'pages/home/index'
    }
  },
  loadData() {
    return Promise.allSettled([this.loadHomeTemplate(), this.loadRecommendGoods()])
      .finally(() => {
        this.refreshServiceContactCard()
      })
  },
  loadHomeTemplate() {
    return app.api.homeTemplateGet()
      .then((res) => {
        const templateData = res.data || {}
        const homeSections = this.normalizeHomeSections(templateData.items || [])
        this.setData({
          homeSections: homeSections,
          hasServiceSection: homeSections.some((item) => item.itemType === 'service')
        })
        return templateData
      })
      .catch(() => {
        this.setData({
          homeSections: [],
          hasServiceSection: false
        })
        return {}
      })
  },
  loadRecommendGoods() {
    return app.api.goodsPage(this.data.page)
      .then((res) => {
        const goodsList = this.normalizeGoodsList(res.data.records)
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
  normalizeGoodsList(goodsList) {
    return (goodsList || []).map((item) => {
      return Object.assign({}, item, {
        picUrls: util.normalizePicUrlList(item.picUrls)
      })
    })
  },
  normalizeHomeSections(sectionList) {
    return (sectionList || []).map((section) => {
      return {
        id: section.id,
        itemType: section.itemType,
        itemName: section.itemName,
        content: this.normalizeSectionContent(section.itemType, section.content || {})
      }
    })
  },
  normalizeSectionContent(itemType, content) {
    if (itemType === 'banner' || itemType === 'image') {
      return {
        items: (content.items || []).map((item) => {
          return {
            imageUrl: util.resolveResourceUrl(item.imageUrl),
            linkType: item.linkType || 'none',
            linkValue: item.linkValue || ''
          }
        })
      }
    }
    if (itemType === 'nav') {
      return {
        items: (content.items || []).map((item) => {
          return {
            title: item.title || '',
            imageUrl: util.resolveResourceUrl(item.imageUrl, '/public/img/no_pic.png'),
            linkType: item.linkType || 'none',
            linkValue: item.linkValue || ''
          }
        })
      }
    }
    if (itemType === 'notice') {
      return {
        items: (content.items || []).map((item) => {
          return {
            tag: item.tag || '公告',
            text: item.text || ''
          }
        })
      }
    }
    if (itemType === 'service') {
      return {
        title: content.title || '首页咨询入口',
        desc: content.desc || '买前可咨询选品，买后可跟进订单和售后，统一走小程序原生客服更直接。',
        tags: Array.isArray(content.tags) && content.tags.length
          ? content.tags
          : this.data.serviceGuideList,
        contactButtonText: content.contactButtonText || '立即咨询',
        goodsButtonText: content.goodsButtonText || '选品咨询',
        orderButtonText: content.orderButtonText || '订单进度',
        afterSaleButtonText: content.afterSaleButtonText || '售后帮助'
      }
    }
    if (itemType === 'goods') {
      return {
        title: content.title || '商品分组',
        subTitle: content.subTitle || '',
        moreText: content.moreText || '更多',
        moreLinkType: content.moreLinkType || 'none',
        moreLinkValue: content.moreLinkValue || '',
        goodsList: this.normalizeGoodsList(content.goodsList || [])
      }
    }
    return content || {}
  },
  refresh() {
    this.setData({
      loadmore: true,
      ['page.current']: 1,
      goodsList: [],
      homeSections: [],
      hasServiceSection: false
    })
    return this.loadData()
  },
  onPullDownRefresh() {
    wx.showNavigationBarLoading()
    Promise.allSettled([this.refresh(), this.loadHomeServiceStatsSafely()])
      .finally(() => {
        wx.hideNavigationBarLoading()
        wx.stopPullDownRefresh()
      })
  },
  onReachBottom() {
    if (!this.data.loadmore) {
      return
    }
    this.setData({
      ['page.current']: this.data.page.current + 1
    })
    this.loadRecommendGoods()
  },
  handleTemplateLinkTap(e) {
    const linkType = e.currentTarget.dataset.linkType
    const linkValue = e.currentTarget.dataset.linkValue
    this.jumpByConfig(linkType, linkValue)
  },
  jumpByConfig(linkType, linkValue) {
    if (!linkType || linkType === 'none' || !linkValue) {
      return
    }
    if (linkType === 'goods') {
      wx.navigateTo({
        url: `/pages/goods/goods-detail/index?id=${linkValue}`
      })
      return
    }
    const targetUrl = linkValue.indexOf('/') === 0 ? linkValue : `/${linkValue}`
    if (linkType === 'tab') {
      wx.switchTab({
        url: targetUrl
      })
      return
    }
    wx.navigateTo({
      url: targetUrl
    })
  },
  handleGoodsContact(e) {
    console.log('home-goods-contact', e)
  },
  handleHomeContact(e) {
    console.log('home-contact', e)
  },
  loadHomeServiceStats() {
    return Promise.allSettled([this.loadOrderStats(), this.loadAfterSaleStats()])
      .finally(() => {
        this.refreshServiceContactCard()
      })
  },
  loadHomeServiceStatsSafely() {
    if (this.homeServiceStatsPromise) {
      return this.homeServiceStatsPromise
    }
    this.homeServiceStatsPromise = app.initPage()
      .then(() => {
        return this.loadHomeServiceStats()
      })
      .finally(() => {
        this.homeServiceStatsPromise = null
      })
    return this.homeServiceStatsPromise
  },
  loadOrderStats() {
    return app.api.orderCountAll()
      .then((res) => {
        const orderCountAll = res.data || {}
        const totalCount = Object.keys(orderCountAll).reduce((total, key) => {
          return total + Number(orderCountAll[key] || 0)
        }, 0)
        this.setData({
          orderStats: {
            totalCount: totalCount
          }
        })
      })
      .catch(() => {
        this.setData({
          orderStats: {
            totalCount: 0
          }
        })
      })
  },
  loadAfterSaleStats() {
    return app.api.orderPage({
      searchCount: false,
      current: 1,
      size: 1,
      descs: 'create_time',
      afterSaleStatus: 'ALL'
    })
      .then((res) => {
        this.setData({
          afterSaleStats: {
            totalCount: Number(res.data.total || 0)
          }
        })
      })
      .catch(() => {
        this.setData({
          afterSaleStats: {
            totalCount: 0
          }
        })
      })
  },
  goOrderProgressPage() {
    this.ensureMallUserReady('查看订单进度')
      .then(() => {
        if (Number(this.data.orderStats.totalCount || 0) <= 0) {
          wx.showModal({
            title: '暂无订单',
            content: '你当前还没有订单，先去逛逛商品；下单后可在这里随时查看订单进度。',
            confirmText: '去逛逛',
            cancelText: '知道了',
            success: (res) => {
              if (res.confirm) {
                wx.switchTab({
                  url: '/pages/goods/goods-category/index'
                })
              }
            }
          })
          return
        }
        wx.navigateTo({
          url: '/pages/order/order-list/index'
        })
      })
      .catch(() => {})
  },
  goAfterSaleHelpPage() {
    this.ensureMallUserReady('查看售后帮助')
      .then(() => {
        const afterSaleCount = Number(this.data.afterSaleStats.totalCount || 0)
        if (afterSaleCount > 0) {
          wx.navigateTo({
            url: '/pages/order/after-sale-list/index'
          })
          return
        }
        wx.showModal({
          title: '当前暂无售后',
          content: '你还没有售后记录。若商品有问题，可先进入订单详情发起申请，或直接联系客服说明情况。',
          confirmText: '查看订单',
          cancelText: '联系客服',
          success: (res) => {
            if (res.confirm) {
              this.goOrderProgressPage()
            } else {
              wx.showToast({
                title: '可点击咨询按钮联系商城客服',
                icon: 'none'
              })
            }
          }
        })
      })
      .catch(() => {})
  },
  ensureMallUserReady(actionText) {
    return app.initPage()
      .then(() => {
        return app.syncMallUser()
      })
      .then((mallUser) => {
        const user = mallUser || app.globalData.mallUser || {}
        app.globalData.mallUser = user
        return this.loadHomeServiceStatsSafely().then(() => user)
      })
      .catch((err) => {
        wx.showToast({
          title: `${actionText}失败，请稍后重试`,
          icon: 'none'
        })
        throw err
      })
  },
  refreshServiceContactCard() {
    const orderCount = Number(this.data.orderStats.totalCount || 0)
    const afterSaleCount = Number(this.data.afterSaleStats.totalCount || 0)
    this.setData({
      serviceContactCard: {
        homeTitle: orderCount > 0 ? `JooLun 商城咨询（订单 ${orderCount} 单）` : 'JooLun 商城咨询',
        chooseGoodsTitle: '首页选品咨询',
        orderTitle: orderCount > 0 ? `订单进度咨询（${orderCount} 单）` : '订单进度咨询',
        afterSaleTitle: afterSaleCount > 0 ? `售后帮助咨询（${afterSaleCount} 条）` : '售后帮助咨询'
      }
    })
  }
})
