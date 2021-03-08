/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
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
        'app-id': wx.getAccountInfoSync().miniProgram.appId,
        'third-session': getApp().globalData.thirdSession != null ? getApp().globalData.thirdSession : ''
      },
      success(res) {
        if (res.statusCode == 200) {
          if (res.data.code != 200) {
            console.log(res.data)
            wx.showModal({
              title: '提示',
              content: res.data.msg ? res.data.msg : '没有数据' + '',
              success() {
                
              },
              complete(){
                if(res.data.code == 60001){
                  //session过期，则清除过期session，并重新加载当前页
                  getApp().globalData.thirdSession = null
                  wx.reLaunch({
                    url: getApp().getCurrentPageUrlWithArgs()
                  })
                }
              }
            })
            reject(res.data.msg)
          }
          resolve(res.data)
        } else if (res.statusCode == 404) {
          wx.showModal({
            title: '提示',
            content: '接口请求出错，请检查手机网络',
            success(res) {

            }
          })
          reject()
        } else {
          console.log(res)
          wx.showModal({
            title: '提示',
            content: res.errMsg + ':' + res.data.message + ':' + res.data.msg,
            success(res) {

            }
          })
          reject()
        }
      },
      fail(error) {
        console.log(error)
        wx.showModal({
          title: '提示',
          content: '接口请求出错：' + error.errMsg,
          success(res) {

          }
        })
        reject(error)
      },
      complete(res) {
        wx.hideLoading()
      }
    })
  })
}

module.exports = {
  request,
  login: (data) => {//小程序登录接口
    return request('/weixin/api/ma/wxuser/login', 'post', data, false)
  },
  wxUserGet: (data) => {//微信用户查询
    return request('/weixin/api/ma/wxuser', 'get', null, false)
  },
  wxUserSave: (data) => {//微信用户新增
    return request('/weixin/api/ma/wxuser', 'post', data, true)
  },
  goodsCategoryGet: (data) => {//商品分类查询
    return request('/weixin/api/ma/goodscategory/tree' , 'get', data, true)
  },
  goodsPage: (data) => {//商品列表
    return request('/weixin/api/ma/goodsspu/page', 'get', data, false)
  },
  goodsGet: (id) => {//商品查询
    return request('/weixin/api/ma/goodsspu/' + id, 'get', null, false)
  },
  shoppingCartPage: (data) => {//购物车列表
    return request('/weixin/api/ma/shoppingcart/page', 'get', data, false)
  },
  shoppingCartAdd: (data) => {//购物车新增
    return request('/weixin/api/ma/shoppingcart', 'post', data, true)
  },
  shoppingCartEdit: (data) => {//购物车修改
    return request('/weixin/api/ma/shoppingcart', 'put', data, true)
  },
  shoppingCartDel: (data) => {//购物车删除
    return request('/weixin/api/ma/shoppingcart/del', 'post', data, false)
  },
  shoppingCartCount: (data) => {//购物车数量
    return request('/weixin/api/ma/shoppingcart/count', 'get', data, false)
  },
  orderSub: (data) => {//订单提交
    return request('/weixin/api/ma/orderinfo', 'post', data, true)
  },
  orderPage: (data) => {//订单列表
    return request('/weixin/api/ma/orderinfo/page', 'get', data, false)
  },
  orderGet: (id) => {//订单详情查询
    return request('/weixin/api/ma/orderinfo/' + id, 'get', null, false)
  },
  orderCancel: (id) => {//订单确认取消
    return request('/weixin/api/ma/orderinfo/cancel/' + id, 'put', null, true)
  },
  orderReceive: (id) => {//订单确认收货
    return request('/weixin/api/ma/orderinfo/receive/' + id, 'put', null, true)
  },
  orderDel: (id) => {//订单删除
    return request('/weixin/api/ma/orderinfo/' + id, 'delete', null, false)
  },
  orderCountAll: (data) => {//订单计数
    return request('/weixin/api/ma/orderinfo/countAll', 'get', data, false)
  },
  unifiedOrder: (data) => {//下单接口
    return request('/weixin/api/ma/orderinfo/unifiedOrder', 'post', data, true)
  },
  userAddressPage: (data) => {//用户收货地址列表
    return request('/weixin/api/ma/useraddress/page', 'get', data, false)
  },
  userAddressSave: (data) => {//用户收货地址新增
    return request('/weixin/api/ma/useraddress', 'post', data, true)
  },
  userAddressDel: (id) => {//用户收货地址删除
    return request('/weixin/api/ma/useraddress/' + id, 'delete', null, false)
  }
}