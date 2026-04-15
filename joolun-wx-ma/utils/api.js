/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import __config from '../config/env'

const request = (url, method, data, showLoading) => {
  const requestUrl = __config.basePath + url
  return new Promise((resolve, reject) => {
    if (showLoading) {
      wx.showLoading({
        title: '加载中...'
      })
    }
    wx.request({
      url: requestUrl,
      method: method,
      data: data,
      header: {
        'app-id': wx.getAccountInfoSync().miniProgram.appId,
        'third-session': getApp().globalData.thirdSession != null ? getApp().globalData.thirdSession : ''
      },
      success(res) {
        if (res.statusCode === 200) {
          if (res.data.code !== 200) {
            wx.showModal({
              title: '提示',
              content: res.data.msg ? res.data.msg : '没有数据',
              complete() {
                if (res.data.code === 60001) {
                  getApp().globalData.thirdSession = null
                  wx.reLaunch({
                    url: getApp().getCurrentPageUrlWithArgs()
                  })
                }
              }
            })
            reject(res.data.msg)
            return
          }
          resolve(res.data)
          return
        }
        if (res.statusCode === 404) {
          wx.showModal({
            title: '提示',
            content: '接口请求出错，请检查手机网络'
          })
          reject(res)
          return
        }
        wx.showModal({
          title: '提示',
          content: `${res.errMsg}:${res.data.message}:${res.data.msg}`
        })
        reject(res)
      },
      fail(error) {
        wx.showModal({
          title: '提示',
          content: '接口请求出错：' + error.errMsg
        })
        reject(error)
      },
      complete() {
        wx.hideLoading()
      }
    })
  })
}

module.exports = {
  request,
  login: (data) => {
    return request('/weixin/api/ma/malluser/login', 'post', data, false)
  },
  // 获取当前商城用户资料。
  mallUserGet: () => {
    return request('/weixin/api/ma/malluser', 'get', null, false)
  },
  // 保存微信授权资料，并同步商城用户快照。
  mallUserSave: (data) => {
    return request('/weixin/api/ma/malluser', 'post', data, true)
  },
  // 更新商城用户基础资料。
  mallUserUpdate: (data) => {
    return request('/weixin/api/ma/malluser', 'put', data, true)
  },
  // 绑定小程序手机号，并刷新商城会员身份。
  mallUserBindPhone: (data) => {
    return request('/weixin/api/ma/malluser/phone', 'post', data, true)
  },
  mallConfigGet: () => {
    return request('/weixin/api/ma/mall/config', 'get', null, false)
  },
  homeTemplateGet: () => {
    return request('/weixin/api/ma/home/template/current', 'get', null, false)
  },
  goodsCategoryGet: (data) => {
    return request('/weixin/api/ma/goodscategory/tree', 'get', data, true)
  },
  goodsPage: (data) => {
    return request('/weixin/api/ma/goodsspu/page', 'get', data, false)
  },
  goodsGet: (id) => {
    return request('/weixin/api/ma/goodsspu/' + id, 'get', null, false)
  },
  shoppingCartPage: (data) => {
    return request('/weixin/api/ma/shoppingcart/page', 'get', data, false)
  },
  shoppingCartAdd: (data) => {
    return request('/weixin/api/ma/shoppingcart', 'post', data, true)
  },
  shoppingCartEdit: (data) => {
    return request('/weixin/api/ma/shoppingcart', 'put', data, true)
  },
  shoppingCartDel: (data) => {
    return request('/weixin/api/ma/shoppingcart/del', 'post', data, false)
  },
  shoppingCartCount: (data) => {
    return request('/weixin/api/ma/shoppingcart/count', 'get', data, false)
  },
  orderSub: (data) => {
    return request('/weixin/api/ma/orderinfo', 'post', data, true)
  },
  orderPage: (data) => {
    return request('/weixin/api/ma/orderinfo/page', 'get', data, false)
  },
  orderGet: (id) => {
    return request('/weixin/api/ma/orderinfo/' + id, 'get', null, false)
  },
  orderCancel: (id) => {
    return request('/weixin/api/ma/orderinfo/cancel/' + id, 'put', null, true)
  },
  orderRefunds: (data) => {
    return request('/weixin/api/ma/orderinfo/refunds', 'post', data, true)
  },
  orderReceive: (id) => {
    return request('/weixin/api/ma/orderinfo/receive/' + id, 'put', null, true)
  },
  orderDel: (id) => {
    return request('/weixin/api/ma/orderinfo/' + id, 'delete', null, false)
  },
  orderCountAll: (data) => {
    return request('/weixin/api/ma/orderinfo/countAll', 'get', data, false)
  },
  unifiedOrder: (data) => {
    return request('/weixin/api/ma/orderinfo/unifiedOrder', 'post', data, true)
  },
  userAddressPage: (data) => {
    return request('/weixin/api/ma/useraddress/page', 'get', data, false)
  },
  userAddressSave: (data) => {
    return request('/weixin/api/ma/useraddress', 'post', data, true)
  },
  userAddressDel: (id) => {
    return request('/weixin/api/ma/useraddress/' + id, 'delete', null, false)
  }
}
