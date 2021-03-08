/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()
Component({
  properties: {
    switchOn: {
      type: Boolean,
      value: true
    },
  },
  data: {
    wxUser: null
  },
  lifetimes: {
    // 组件所在页面的生命周期函数
    ready: function () {
      this.setData({
        wxUser: app.globalData.wxUser
      })
    }
  },
  methods: {
    agreeGetUser(e) {
      if (e.detail.errMsg == 'getUserInfo:ok') {
        app.api.wxUserSave(e.detail)
          .then(res => {
            let wxUser = res.data
            this.setData({
              wxUser: wxUser
            })
            app.globalData.wxUser = wxUser
          })
      }
    },
  }
})