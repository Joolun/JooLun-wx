/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Component({
  properties: {
    couponInfo: {
      type: Object,
      value: {}
    },
    toUse: {
      type: Boolean,
      value: true
    }
  },
  data: {
    
  },
  methods: {
    couponUserSave(){
      app.api.couponUserSave({
        couponId: this.data.couponInfo.id
      })
        .then(res => {
          wx.showToast({
            title: '领取成功',
            icon: 'success',
            duration: 2000
          })
          let couponInfo = this.data.couponInfo
          couponInfo.couponUser = res.data
          this.setData({
            couponInfo: couponInfo
          })
          this.triggerEvent('receiveCoupon', couponInfo)
        })
    }
  }
})