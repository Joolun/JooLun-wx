/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
import __config from '../config/env'

const request = (url, method, data, showLoading) => {
  let _url = __config.basePath + url
  return new Promise((resolve, reject) => {
    if (showLoading){
      wx.showLoading({
        title: '加载中',
      })
    }
    wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'tenant-id' : __config.tenantId,
        'thirdSession': getApp().globalData.thirdSession
      },
      success(res) {
        if (res.statusCode == 200) {
          if (res.data.code != 0) {
            wx.showToast({
              title: res.data.msg,
              icon: 'none',
              duration: 5000
            })
            reject()
          }
          resolve(res.data)
        } else if (res.statusCode == 404) {
          wx.showToast({
            title: '接口请求出错，请检查手机网络',
            icon: 'none',
            duration: 5000
          })
          reject()
        } else {
          wx.showToast({
            title: res.data.msg,
            icon: 'none',
            duration: 5000
          })
          reject()
        }
      },
      fail(error) {
        wx.showToast({
          title: '接口请求出错',
          icon: 'none',
          duration: 2000
        })
        reject(error)
      },
      complete(res) {
        // 加载完成
        if (showLoading) {
          wx.hideLoading()
        }
      }
    })
  })
}

/**
 * 小程序的promise没有finally方法，自己扩展下
 */
Promise.prototype.finally = function (callback) {
  var Promise = this.constructor;
  return this.then(
    function (value) {
      Promise.resolve(callback()).then(
        function () {
          return value;
        }
      );
    },
    function (reason) {
      Promise.resolve(callback()).then(
        function () {
          throw reason;
        }
      );
    }
  );
}

module.exports = {
  request,
  login: (data) => {
    return request('/weixin/api/ma/wxuser/login', 'post', data, false)
  },
  userInfoGet: (data) => {
    return request('/mall/api/ma/wxuser', 'get', null, false)
  },
  userInfoSave: (data) => {
    return request('/mall/api/ma/wxuser', 'post', data, true)
  },
  goodsCategoryGet: (data) => {
    return request('/mall/api/ma/goodscategory/tree' , 'get', data, true)
  },
  goodsPage: (data) => {
    return request('/mall/api/ma/goodsspu/page', 'get', data, false)
  },
  goodsGet: (id) => {
    return request('/mall/api/ma/goodsspu/' + id, 'get', null, false)
  },
  goodsSpecGet: (data) => {
    return request('/mall/api/ma/goodsspuspec/tree', 'get', data, false)
  },
  shoppingCartPage: (data) => {
    return request('/mall/api/ma/shoppingcart/page', 'get', data, false)
  },
  shoppingCartAdd: (data) => {
    return request('/mall/api/ma/shoppingcart', 'post', data, true)
  },
  shoppingCartEdit: (data) => {
    return request('/mall/api/ma/shoppingcart', 'put', data, true)
  },
  shoppingCartDel: (data) => {
    return request('/mall/api/ma/shoppingcart/del', 'post', data, false)
  },
  shoppingCartCount: (data) => {
    return request('/mall/api/ma/shoppingcart/count', 'get', data, false)
  },
  orderSub: (data) => {
    return request('/mall/api/ma/orderinfo', 'post', data, true)
  },
  orderPage: (data) => {
    return request('/mall/api/ma/orderinfo/page', 'get', data, false)
  },
  orderGet: (id) => {
    return request('/mall/api/ma/orderinfo/' + id, 'get', null, false)
  },
  orderCancel: (id) => {
    return request('/mall/api/ma/orderinfo/cancel/' + id, 'put', null, true)
  },
  orderReceive: (id) => {
    return request('/mall/api/ma/orderinfo/receive/' + id, 'put', null, true)
  },
  orderDel: (id) => {
    return request('/mall/api/ma/orderinfo/' + id, 'delete', null, false)
  },
  orderLogisticsGet: (logisticsId) => {
    return request('/mall/api/ma/orderinfo/orderlogistics/' + logisticsId, 'get', null, false)
  },
  unifiedOrder: (data) => {
    return request('/mall/api/ma/orderinfo/unifiedOrder', 'post', data, true)
  },
  userAddressPage: (data) => {
    return request('/mall/api/ma/useraddress/page', 'get', data, false)
  },
  userAddressSave: (data) => {
    return request('/mall/api/ma/useraddress', 'post', data, true)
  },
  userCollectAdd: (data) => {
    return request('/mall/api/ma/usercollect', 'post', data, true)
  },
  userCollectDel: (id) => {
    return request('/mall/api/ma/usercollect/' + id, 'delete', null, false)
  },
  userCollectPage: (data) => {
    return request('/mall/api/ma/usercollect/page', 'get', data, false)
  },
  goodsAppraisesAdd: (data) => {
    return request('/mall/api/ma/goodsappraises', 'post', data, true)
  },
  goodsAppraisesPage: (data) => {
    return request('/mall/api/ma/goodsappraises/page', 'get', data, false)
  },
  qrCodeUnlimited: (data) => {
    return request('/weixin/api/ma/wxqrcode/unlimited', 'post', data, false)
  },
}