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

/**
 * 上传小程序本地文件。
 * wx.uploadFile 不走 wx.request，这里统一补齐小程序登录态和错误提示。
 */
const upload = (url, filePath) => {
  const basePath = String(__config.basePath || '').replace(/\/$/, '')
  return new Promise((resolve, reject) => {
    wx.showLoading({
      title: '上传中...'
    })
    wx.uploadFile({
      url: basePath + url,
      filePath: filePath,
      name: 'file',
      header: {
        'app-id': wx.getAccountInfoSync().miniProgram.appId,
        'third-session': getApp().globalData.thirdSession != null ? getApp().globalData.thirdSession : ''
      },
      success(res) {
        let responseData
        try {
          responseData = JSON.parse(res.data)
        } catch (error) {
          wx.showModal({
            title: '提示',
            content: '头像上传响应格式不正确'
          })
          reject(error)
          return
        }
        if (res.statusCode === 200 && responseData.code === 200) {
          resolve(responseData)
          return
        }
        const message = responseData.msg || '头像上传失败'
        wx.showModal({
          title: '提示',
          content: message
        })
        reject(new Error(message))
      },
      fail(error) {
        wx.showModal({
          title: '提示',
          content: '头像上传失败：' + error.errMsg
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
  // 上传微信官方头像组件返回的本地临时文件。
  mallUserUploadAvatar: (filePath) => {
    return upload('/weixin/api/ma/malluser/avatar', filePath)
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
  couponCenter: () => {
    return request('/weixin/api/ma/coupon/center', 'get', null, false)
  },
  couponMine: (data) => {
    return request('/weixin/api/ma/coupon/mine', 'get', data, false)
  },
  couponClaim: (id) => {
    return request('/weixin/api/ma/coupon/' + id + '/claim', 'post', null, true)
  },
  memberProfile: () => {
    return request('/weixin/api/ma/member/profile', 'get', null, false)
  },
  memberPoints: (data) => {
    return request('/weixin/api/ma/member/points', 'get', data, false)
  },
  goodsReviewPage: (data) => {
    return request('/weixin/api/ma/goodsreview/page', 'get', data, false)
  },
  goodsReviewSummary: (spuId) => {
    return request('/weixin/api/ma/goodsreview/summary/' + spuId, 'get', null, false)
  },
  goodsReviewSubmit: (data) => {
    return request('/weixin/api/ma/goodsreview', 'post', data, true)
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
