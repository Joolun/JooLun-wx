/**
 * Copyright (C) 2026
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
    }
  },
  data: {
    mallUser: null
  },
  lifetimes: {
    // 组件准备完成后，优先读取全局缓存，避免重复请求。
    ready () {
      const mallUser = app.globalData.mallUser
      if (mallUser) {
        this.applyMallUser(mallUser)
        return
      }
      app.initPage()
        .then(() => {
          return app.api.mallUserGet()
        })
        .then((res) => {
          this.applyMallUser((res && res.data) || null)
        })
    }
  },
  methods: {
    agreeGetUser (e) {
      if (e.detail.errMsg === 'getUserInfo:ok') {
        app.api.mallUserSave(e.detail).then((res) => {
          this.applyMallUser(res.data || null)
        })
      }
    },
    /**
     * 绑定手机号。
     * 订单确认等场景下可直接在公共授权弹层里完成会员转化，并把最新 mallUser 同步给宿主页面。
     *
     * @param {Object} e 微信手机号授权回调
     */
    bindPhoneNumber (e) {
      const detail = e.detail || {}
      if (!detail.code && (!detail.encryptedData || !detail.iv)) {
        return
      }
      app.api.mallUserBindPhone({
        code: detail.code,
        encryptedData: detail.encryptedData,
        iv: detail.iv,
        errMsg: detail.errMsg
      }).then((res) => {
        this.applyMallUser(res.data || null)
      })
    },
    applyMallUser (mallUser) {
      this.setData({
        mallUser
      })
      app.globalData.mallUser = mallUser
      this.triggerEvent('change', { mallUser })
    }
  }
})
