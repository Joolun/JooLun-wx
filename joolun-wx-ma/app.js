/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
/**
 * <version>3.3.1</version>
 */
import api from './utils/api'
import __config from './config/env'

App({
  api: api,
  globalData: {
    thirdSession: null,
    wxUser: null,
    config: __config
  },
  onLaunch: function () {
    //检测新版本
    this.updateManager()
    wx.getSystemInfo({
      success: e => {
        this.globalData.StatusBar = e.statusBarHeight;
        let custom = wx.getMenuButtonBoundingClientRect();
        this.globalData.Custom = custom;
        this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
      }
    })
  },
  updateManager(){
    const updateManager = wx.getUpdateManager()
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success(res) {
          if (res.confirm) {
            updateManager.applyUpdate()
          }
        }
      })
    })
  },
  //获取购物车数量
  shoppingCartCount() {
    this.api.shoppingCartCount()
      .then(res => {
        let shoppingCartCount = res.data
        this.globalData.shoppingCartCount = shoppingCartCount + ''
        wx.setTabBarBadge({
          index: 2,
          text: this.globalData.shoppingCartCount + ''
        })
      })
  },
  //初始化，供每个页面调用 
  initPage: function () {
    let that = this
    return new Promise((resolve, reject) => {
      if (!that.globalData.thirdSession) {//无thirdSession，进行登录
        that.doLogin()
          .then(res => {
            resolve("success")
          })
      } else {//有thirdSession，说明已登录，返回初始化成功
        wx.checkSession({//检查登录态是否过期
          success () {
            //session_key 未过期，并且在本生命周期一直有效
            console.log('session_key 未过期')
            resolve("success")
          },
          fail () {
            // session_key 已经失效，需要重新执行登录流程
            console.log('session_key 已经失效')
            that.doLogin()
              .then(res => {
                resolve("success")
              })
          }
        })
       
      }
    })
  },
  doLogin(){
    wx.showLoading({
      title: '登录中',
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
                wx.hideLoading()
                let wxUser = res.data
                that.globalData.thirdSession = wxUser.sessionKey
                that.globalData.wxUser = wxUser
                resolve("success")
                //获取购物车数量
                that.shoppingCartCount()
              })
          }
        }
      })
    })
  },
  //获取当前页面带参数的url
  getCurrentPageUrlWithArgs(){
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