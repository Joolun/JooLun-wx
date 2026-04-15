/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */

const app = getApp()

Page({
  data: {
    config: app.globalData.config,
    mallUser: {},
    memberIdentityText: '未入会',
    memberCards: [],
    profileMetas: [],
    orderMenus: [],
    afterSaleStats: {
      totalCount: 0,
      processingCount: 0
    },
    serviceEntry: {
      title: '联系商城客服',
      desc: '订单、退款、物流问题都可以直接咨询',
      tips: [
        '建议进入对应订单或售后页再咨询，客服能更快定位问题',
        '会员、下单、退款规则问题也可直接发起客服咨询'
      ]
    },
    quickMenus: []
  },
  onShow() {
    this.syncShoppingCartBadge()
    this.refreshPanels(app.globalData.mallUser || {})
    this.refreshUserCenterSafely()
  },
  onPullDownRefresh() {
    this.refreshUserCenterSafely().finally(() => {
      wx.stopPullDownRefresh()
    })
  },
  refreshUserCenterSafely() {
    if (this.userCenterRefreshPromise) {
      return this.userCenterRefreshPromise
    }
    this.userCenterRefreshPromise = app.initPage()
      .then(() => {
        return Promise.allSettled([
          this.loadMallUser(),
          this.orderCountAll(),
          this.loadAfterSaleStats()
        ])
      })
      .finally(() => {
        this.userCenterRefreshPromise = null
      })
    return this.userCenterRefreshPromise
  },
  /**
   * 打开小程序授权设置页。
   */
  settings() {
    wx.openSetting({})
  },
  /**
   * 获取用户头像和昵称，并同步到商城用户档案。
   */
  getUserProfile() {
    wx.getUserProfile({
      desc: '用于完善会员资料',
      success: (detail) => {
        app.api.mallUserSave(detail).then((res) => {
          const mallUser = res.data || {}
          app.globalData.mallUser = mallUser
          this.refreshPanels(mallUser)
        })
      }
    })
  },
  /**
   * 绑定手机号。
   * 绑定成功后自动刷新商城用户档案，未入会用户会同步转成会员。
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
      this.refreshPanels(mallUser)
      wx.showToast({
        title: '绑定成功',
        icon: 'success'
      })
    })
  },
  /**
   * 个人中心客服入口回调。
   *
   * @param {Object} e 客服回调
   */
  handleContact(e) {
    console.log('user-center-contact', e)
  },
  /**
   * 跳转资料编辑页。
   */
  goProfilePage() {
    wx.navigateTo({
      url: '/pages/user/user-profile/index'
    })
  },
  /**
   * 拉取商城用户信息，并同步到全局缓存。
   */
  loadMallUser() {
    return app.api.mallUserGet().then((res) => {
      const mallUser = res.data || {}
      app.globalData.mallUser = mallUser
      this.refreshPanels(mallUser)
      return mallUser
    })
  },
  /**
   * 获取订单状态数量，并驱动订单入口角标。
   */
  orderCountAll() {
    return app.api.orderCountAll().then((res) => {
      const orderCountAll = res.data || {}
      this.setData({
        orderMenus: this.buildOrderMenus(orderCountAll)
      })
      return orderCountAll
    })
  },
  /**
   * 统计售后数量。
   * 复用现有订单分页接口读取 total，避免为了个人中心角标再增加后端接口。
   */
  loadAfterSaleStats() {
    const basePage = {
      searchCount: false,
      current: 1,
      size: 1,
      descs: 'create_time'
    }
    return Promise.all([
      app.api.orderPage(Object.assign({}, basePage, { afterSaleStatus: 'ALL' })),
      app.api.orderPage(Object.assign({}, basePage, { afterSaleStatus: '1' })),
      app.api.orderPage(Object.assign({}, basePage, { afterSaleStatus: '3' }))
    ]).then((responses) => {
      const totalCount = Number(responses[0]?.data?.total || 0)
      const processingCount = Number(responses[1]?.data?.total || 0) + Number(responses[2]?.data?.total || 0)
      const nextStats = {
        totalCount: totalCount,
        processingCount: processingCount
      }
      this.setData({
        afterSaleStats: nextStats,
        quickMenus: this.buildQuickMenus(nextStats)
      })
      return nextStats
    }).catch(() => {
      const nextStats = {
        totalCount: 0,
        processingCount: 0
      }
      this.setData({
        afterSaleStats: nextStats,
        quickMenus: this.buildQuickMenus(nextStats)
      })
      return nextStats
    })
  },
  /**
   * 统一刷新页面展示面板。
   *
   * @param {Object} mallUser 商城用户
   */
  refreshPanels(mallUser) {
    const user = mallUser || {}
    this.setData({
      mallUser: user,
      memberIdentityText: this.formatMemberIdentity(user.memberFlag),
      memberCards: this.buildMemberCards(user),
      profileMetas: this.buildProfileMetas(user),
      quickMenus: this.buildQuickMenus(this.data.afterSaleStats)
    })
  },
  /**
   * 构建会员卡片。
   *
   * @param {Object} mallUser 商城用户
   * @returns {Array} 卡片数据
   */
  buildMemberCards(mallUser) {
    const user = mallUser || {}
    return [
      {
        label: '累计下单',
        value: `${this.safeNumber(user.orderCount)} 单`
      },
      {
        label: '累计支付',
        value: `${this.safeNumber(user.consumeCount)} 单`
      },
      {
        label: '累计退款',
        value: `${this.safeNumber(user.refundCount)} 单`
      },
      {
        label: '累计消费',
        value: this.formatAmount(user.consumeAmount)
      }
    ]
  },
  /**
   * 构建用户资料摘要。
   *
   * @param {Object} mallUser 商城用户
   * @returns {Array} 摘要数据
   */
  buildProfileMetas(mallUser) {
    const user = mallUser || {}
    return [
      {
        label: '会员身份',
        value: this.formatMemberIdentity(user.memberFlag)
      },
      {
        label: '入会时间',
        value: this.formatMemberTime(user)
      },
      {
        label: '会员编号',
        value: this.safeText(user.userNo, '系统生成中')
      },
      {
        label: '手机号',
        value: this.maskMobile(user.mobile)
      },
      {
        label: '性别',
        value: this.formatSex(user.sex)
      },
      {
        label: '注册来源',
        value: this.formatRegisterSource(user.registerSource)
      },
      {
        label: '所在地区',
        value: this.buildRegion(user)
      },
      {
        label: '最近登录',
        value: this.formatDateTime(user.lastLoginTime)
      }
    ]
  },
  /**
   * 构建订单菜单数据。
   *
   * @param {Object} orderCountAll 订单统计
   * @returns {Array} 订单菜单
   */
  buildOrderMenus(orderCountAll) {
    return [
      {
        label: '待付款',
        icon: '/public/img/5-1.png',
        url: '/pages/order/order-list/index?status=0',
        badge: this.safeNumber(orderCountAll['0'])
      },
      {
        label: '待发货',
        icon: '/public/img/5-2.png',
        url: '/pages/order/order-list/index?status=1',
        badge: this.safeNumber(orderCountAll['1'])
      },
      {
        label: '待收货',
        icon: '/public/img/5-3.png',
        url: '/pages/order/order-list/index?status=2',
        badge: this.safeNumber(orderCountAll['2'])
      },
      {
        label: '已完成',
        icon: '/public/img/5-4.png',
        url: '/pages/order/order-list/index?status=4',
        badge: this.safeNumber(orderCountAll['3'])
      }
    ]
  },
  /**
   * 构建快捷入口。
   *
   * @returns {Array} 快捷入口
   */
  buildQuickMenus(afterSaleStats) {
    const stats = afterSaleStats || {}
    const processingCount = this.safeNumber(stats.processingCount)
    const totalCount = this.safeNumber(stats.totalCount)
    return [
      {
        label: '资料编辑',
        desc: '完善真实姓名、性别和生日',
        url: '/pages/user/user-profile/index',
        icon: 'cuIcon-profile'
      },
      {
        label: '售后中心',
        desc: this.buildAfterSaleQuickDesc(stats),
        url: '/pages/order/after-sale-list/index',
        icon: 'cuIcon-servicefill',
        badgeText: processingCount > 0 ? `${processingCount > 99 ? '99+' : processingCount}` : '',
        extraText: totalCount > 0 ? `累计 ${totalCount} 条` : '暂无记录'
      },
      {
        label: '收货地址',
        desc: '管理常用收货地址',
        url: '/pages/user/user-address/list/index',
        icon: 'cuIcon-locationfill'
      }
    ]
  },
  /**
   * 构建售后中心快捷入口文案。
   *
   * @param {Object} afterSaleStats 售后统计
   * @returns {string}
   */
  buildAfterSaleQuickDesc(afterSaleStats) {
    const processingCount = this.safeNumber(afterSaleStats.processingCount)
    const totalCount = this.safeNumber(afterSaleStats.totalCount)
    if (processingCount > 0) {
      return `有 ${processingCount} 条处理中，请及时查看进度`
    }
    if (totalCount > 0) {
      return `累计 ${totalCount} 条售后记录，可回看退款结果`
    }
    return '集中查看退款进度和审核结果'
  },
  /**
   * 同步购物车徽标。
   */
  syncShoppingCartBadge() {
    const count = Number(app.globalData.shoppingCartCount || 0)
    if (count > 0) {
      wx.setTabBarBadge({
        index: 2,
        text: `${count}`
      })
    } else {
      wx.removeTabBarBadge({
        index: 2
      })
    }
  },
  /**
   * 展示插屏广告。
   */
  /**
   * 统一处理金额显示。
   *
   * @param {number|string} value 金额
   * @returns {string} 格式化后的金额
   */
  formatAmount(value) {
    const amount = Number(value || 0)
    return `¥${amount.toFixed(2)}`
  },
  /**
   * 手机号脱敏。
   *
   * @param {string} mobile 手机号
   * @returns {string} 脱敏手机号
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
   * 格式化日期时间。
   *
   * @param {string} dateTime 日期时间
   * @returns {string} 展示文本
   */
  formatDateTime(dateTime) {
    if (!dateTime) {
      return '刚刚加入'
    }
    return String(dateTime).replace('T', ' ').substring(0, 16)
  },
  /**
   * 格式化会员身份。
   * 当前规则：绑定手机号后即成为会员。
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
   * 格式化性别。
   *
   * @param {string} sex 性别
   * @returns {string} 展示文本
   */
  formatSex(sex) {
    if (sex === '1') {
      return '男'
    }
    if (sex === '2') {
      return '女'
    }
    return '未知'
  },
  /**
   * 格式化注册来源。
   *
   * @param {string} registerSource 注册来源
   * @returns {string} 展示文本
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
   *
   * @param {Object} mallUser 商城用户
   * @returns {string} 地区文案
   */
  buildRegion(mallUser) {
    const region = [mallUser.country, mallUser.province, mallUser.city].filter(item => item)
    return region.length > 0 ? region.join(' / ') : '未同步'
  },
  /**
   * 兜底数字。
   *
   * @param {number|string} value 原始值
   * @returns {number} 数字结果
   */
  safeNumber(value) {
    const numberValue = Number(value || 0)
    return Number.isNaN(numberValue) ? 0 : numberValue
  },
  /**
   * 兜底文本。
   *
   * @param {string} value 原始文本
   * @param {string} defaultValue 默认文本
   * @returns {string} 最终文本
   */
  safeText(value, defaultValue) {
    return value ? value : defaultValue
  }
})
