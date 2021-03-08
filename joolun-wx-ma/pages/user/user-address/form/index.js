/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const app = getApp()

Page({
  data: {
    region: [],
    userAddress: []
  },
  onLoad(options) {
    // 本地获取参数信息
    let that = this
    wx.getStorage({
      key: 'param-userAddressForm',
      success: function (res) {
        let userAddress = res.data
        that.setData({
          [`region[0]`]: userAddress.provinceName ? userAddress.provinceName : '选择省',
          [`region[1]`]: userAddress.cityName ? userAddress.cityName : '选择市',
          [`region[2]`]: userAddress.countyName ? userAddress.countyName : '选择区',
          userAddress: userAddress
        })
      }
    })
  },
  regionChange(e) {
    this.setData({
      region: e.detail.value
    })
  },
  isDefaultChange(e){
    if(e.detail.value){
      this.setData({
        [`userAddress.isDefault`]: '1'
      })
    }else{
      this.setData({
        [`userAddress.isDefault`]: '0'
      })
    }
  },
  userAddressSave(e) {
    let value = e.detail.value
    let region = this.data.region
    if (!value.userName){
      wx.showToast({
        title: '请填写收货人姓名',
        icon: 'none',
        duration: 3000
      })
      return
    }
    if (!value.telNum) {
      wx.showToast({
        title: '请填写联系电话',
        icon: 'none',
        duration: 3000
      })
      return
    }
    if (!/^1(3|4|5|7|8|9|6)\d{9}$/i.test(value.telNum)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none',
        duration: 3000
      })
      return
    }
    if (region[0] == '选择省') {
      wx.showToast({
        title: '请选择所在地区',
        icon: 'none',
        duration: 3000
      })
      return
    }
    if (!value.detailInfo) {
      wx.showToast({
        title: '请填写详细地址',
        icon: 'none',
        duration: 3000
      })
      return
    }
    app.api.userAddressSave({
      id: this.data.userAddress.id,
      userName: value.userName,
      telNum: value.telNum,
      provinceName: region[0],
      cityName: region[1],
      countyName: region[2],
      detailInfo: value.detailInfo,
      isDefault: this.data.userAddress.isDefault
    })
      .then(res => {
        wx.navigateBack()
      })
  },
  userAddressDelete(){
    let that = this
    wx.showModal({
      content: '确认将这个地址删除吗？',
      cancelText: '我再想想',
      confirmColor: '#ff0000',
      success(res) {
        if (res.confirm) {
          app.api.userAddressDel(that.data.userAddress.id)
            .then(res => {
              wx.navigateBack()
            })
        }
      }
    })
  },
  /*
  * 导入微信地址
  */
  getWxAddress: function () {
    let that = this
    wx.authorize({
      scope: 'scope.address',
      success: function (res) {
        wx.chooseAddress({
          success: function (res) {
            that.setData({
              [`region[0]`]: res.provinceName,
              [`region[1]`]: res.cityName,
              [`region[2]`]: res.countyName,
              [`userAddress.userName`]: res.userName,
              [`userAddress.telNum`]: res.telNumber,
              [`userAddress.detailInfo`]: res.detailInfo,
            })
          },
          fail: function (res) {
            if (res.errMsg == 'chooseAddress:cancel') {
              wx.showToast({
                title: '取消选择',
                icon: 'none',
                duration: 3000
              })
            }
          },
        })
      },
      fail: function (res) {
        console.log(res);
      },
    })
  },
})
