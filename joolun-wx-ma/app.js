/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
/**
 * <version>3.3.2</version>
 */
import api from './utils/api'
import __config from './config/env'

App({
  api: api,
  globalData: {
    thirdSession: null,
    channelUser: null,
    mallUser: null,
    mallConfig: null,
    shoppingCartCount: '0',
    config: __config,
    initPagePromise: null,
    mallUserSyncPromise: null
  },
  onLaunch: function () {
    // 检测并提示小程序新版本。
    this.updateManager()
    wx.getSystemInfo({
      success: e => {
        this.globalData.StatusBar = e.statusBarHeight
        let custom = wx.getMenuButtonBoundingClientRect()
        this.globalData.Custom = custom
        this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight
      }
    })
    // 进入小程序后先预热登录态，后续页面只需要等待 initPage 即可。
    this.initPage().catch(() => {})
  },
  updateManager () {
    const updateManager = wx.getUpdateManager()
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success (res) {
          if (res.confirm) {
            updateManager.applyUpdate()
          }
        }
      })
    })
  },
  // 同步购物车数量到全局缓存和 tabBar 徽标。
  shoppingCartCount () {
    return this.api.shoppingCartCount()
      .then(res => {
        const shoppingCartCount = Number(res.data || 0)
        this.setShoppingCartBadge(shoppingCartCount)
        return shoppingCartCount
      })
  },
  setShoppingCartBadge (count) {
    const shoppingCartCount = Number(count || 0)
    this.globalData.shoppingCartCount = `${shoppingCartCount}`
    if (shoppingCartCount > 0) {
      wx.setTabBarBadge({
        index: 2,
        text: this.globalData.shoppingCartCount
      })
      return
    }
    wx.removeTabBarBadge({
      index: 2
    })
  },
  // 初始化页面公共登录态。
  initPage: function () {
    if (this.globalData.initPagePromise) {
      return this.globalData.initPagePromise
    }
    let that = this
    this.globalData.initPagePromise = new Promise((resolve, reject) => {
      if (!that.globalData.thirdSession) {
        that.doLogin()
          .then(() => {
            resolve('success')
          })
          .catch(reject)
        return
      }
      wx.checkSession({
        success () {
          console.log('session_key 未过期')
          resolve('success')
        },
        fail () {
          console.log('session_key 已失效')
          that.doLogin()
            .then(() => {
              resolve('success')
            })
            .catch(reject)
        }
      })
    })
      .finally(() => {
        that.globalData.initPagePromise = null
      })
    return this.globalData.initPagePromise
  },
  // 登录成功后顺带刷新商城用户资料，后续页面统一读取 mallUser。
  syncMallUser () {
    if (this.globalData.mallUserSyncPromise) {
      return this.globalData.mallUserSyncPromise
    }
    this.globalData.mallUserSyncPromise = this.api.mallUserGet()
      .then(res => {
        this.globalData.mallUser = res.data
        return res.data
      })
      .catch(() => {
        this.globalData.mallUser = null
        return null
      })
      .finally(() => {
        this.globalData.mallUserSyncPromise = null
      })
    return this.globalData.mallUserSyncPromise
  },
  doLogin () {
    wx.showLoading({
      title: '登录中'
    })
    let that = this
    return new Promise((resolve, reject) => {
      wx.login({
        success: function (res) {
          if (res.code) {
            api.login({
              jsCode: res.code
            })
              .then(res => {
                let channelUser = res.data
                that.globalData.thirdSession = channelUser.sessionKey
                that.globalData.channelUser = channelUser
                that.syncMallUser()
                  .then(() => {
                    wx.hideLoading()
                    resolve('success')
                    that.shoppingCartCount()
                  })
                  .catch(() => {
                    wx.hideLoading()
                    resolve('success')
                    that.shoppingCartCount()
                  })
              })
              .catch(err => {
                wx.hideLoading()
                reject(err)
              })
          } else {
            wx.hideLoading()
            reject(new Error('微信登录失败'))
          }
        },
        fail (err) {
          wx.hideLoading()
          reject(err)
        }
      })
    })
  },
  // 获取当前页面带参数的 url。
  getCurrentPageUrlWithArgs () {
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    const url = currentPage.route
    const options = currentPage.options
    let urlWithArgs = `/${url}?`
    for (let key in options) {
      const value = options[key]
      urlWithArgs += `${key}=${value}&`
    }
    urlWithArgs = urlWithArgs.substring(0, urlWithArgs.length - 1)
    return urlWithArgs
  }
})
