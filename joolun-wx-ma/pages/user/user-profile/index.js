/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

const app = getApp()

Page({
  data: {
    form: {
      realName: '',
      sex: '0',
      birthday: ''
    },
    mallUser: {},
    memberIdentityText: '未入会',
    memberTimeText: '未入会',
    sexOptions: [
      { label: '未知', value: '0' },
      { label: '男', value: '1' },
      { label: '女', value: '2' }
    ],
    sexIndex: 0
  },
  onShow() {
    this.loadMallUser()
  },
  /**
   * 拉取商城用户资料。
   */
  loadMallUser() {
    const cachedUser = app.globalData.mallUser || {}
    this.applyMallUser(cachedUser)
    app.initPage()
      .then(() => {
        return app.api.mallUserGet()
      })
      .then((res) => {
        const mallUser = res.data || {}
        app.globalData.mallUser = mallUser
        this.applyMallUser(mallUser)
      })
  },
  /**
   * 更新微信头像和昵称。
   */
  getUserProfile() {
    wx.getUserProfile({
      desc: '用于完善会员资料',
      success: (detail) => {
        app.api.mallUserSave(detail).then((res) => {
          const mallUser = res.data || {}
          app.globalData.mallUser = mallUser
          this.applyMallUser(mallUser)
        })
      }
    })
  },
  /**
   * 绑定手机号。
   * 绑定成功后会刷新手机号、会员身份和入会时间。
   *
   * @param {Object} e 微信手机号授权回调
   */
  bindPhoneNumber(e) {
    const detail = e.detail || {}
    if (!detail.code && (!detail.encryptedData || !detail.iv)) {
      if (detail.errMsg && detail.errMsg.indexOf(':fail') > -1) {
        wx.showToast({
          title: '你已取消授权',
          icon: 'none'
        })
      }
      return
    }
    app.api.mallUserBindPhone({
      code: detail.code,
      encryptedData: detail.encryptedData,
      iv: detail.iv,
      errMsg: detail.errMsg
    }).then((res) => {
      const mallUser = res.data || {}
      app.globalData.mallUser = mallUser
      this.applyMallUser(mallUser)
      wx.showToast({
        title: '绑定成功',
        icon: 'success'
      })
    })
  },
  /**
   * 输入真实姓名。
   */
  inputRealName(e) {
    this.setData({
      'form.realName': e.detail.value
    })
  },
  /**
   * 切换性别。
   */
  changeSex(e) {
    const sexIndex = Number(e.detail.value || 0)
    this.setData({
      sexIndex: sexIndex,
      'form.sex': this.data.sexOptions[sexIndex].value
    })
  },
  /**
   * 选择生日。
   */
  changeBirthday(e) {
    this.setData({
      'form.birthday': e.detail.value
    })
  },
  /**
   * 保存商城用户资料。
   */
  saveProfile() {
    const form = this.data.form
    app.api.mallUserUpdate({
      realName: form.realName,
      sex: form.sex,
      birthday: form.birthday
    }).then((res) => {
      app.globalData.mallUser = res.data || {}
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })
      setTimeout(() => {
        wx.navigateBack()
      }, 600)
    })
  },
  /**
   * 应用商城用户数据到页面表单。
   *
   * @param {Object} mallUser 商城用户
   */
  applyMallUser(mallUser) {
    const sexIndex = this.findSexIndex(mallUser.sex)
    this.setData({
      mallUser: mallUser,
      memberIdentityText: this.formatMemberIdentity(mallUser.memberFlag),
      memberTimeText: this.formatMemberTime(mallUser),
      sexIndex: sexIndex,
      form: {
        realName: mallUser.realName || '',
        sex: mallUser.sex || '0',
        birthday: mallUser.birthday || ''
      }
    })
  },
  /**
   * 格式化注册来源。
   */
  formatRegisterSource(registerSource) {
    if (registerSource === '1') {
      return '小程序'
    }
    if (registerSource === '2') {
      return '公众号'
    }
    if (registerSource === '3') {
      return 'H5'
    }
    if (registerSource === '4') {
      return 'APP'
    }
    return '其他'
  },
  /**
   * 拼接地区文案。
   */
  buildRegion() {
    const mallUser = this.data.mallUser || {}
    const region = [mallUser.country, mallUser.province, mallUser.city].filter(item => item)
    return region.length > 0 ? region.join(' / ') : '未同步'
  },
  /**
   * 手机号脱敏。
   */
  maskMobile(mobile) {
    if (!mobile) {
      return '未绑定'
    }
    if (mobile.length < 7) {
      return mobile
    }
    return `${mobile.substring(0, 3)}****${mobile.substring(mobile.length - 4)}`
  },
  /**
   * 格式化会员身份。
   *
   * @param {string} memberFlag 会员标记
   * @returns {string} 展示文本
   */
  formatMemberIdentity(memberFlag) {
    return memberFlag === '1' ? '已入会' : '未入会'
  },
  /**
   * 格式化入会时间。
   *
   * @param {Object} mallUser 商城用户
   * @returns {string} 展示文本
   */
  formatMemberTime(mallUser) {
    if (!mallUser || mallUser.memberFlag !== '1' || !mallUser.memberTime) {
      return '未入会'
    }
    return String(mallUser.memberTime).replace('T', ' ').substring(0, 16)
  },
  /**
   * 根据性别值反查索引。
   */
  findSexIndex(sex) {
    const index = this.data.sexOptions.findIndex(item => item.value === sex)
    return index >= 0 ? index : 0
  }
})
