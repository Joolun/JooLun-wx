/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
const app = getApp()

Component({
  properties: {
    goodsDetail: {
      type: Object,
      value: []
    },
    goodsSpecData: {
      type: Object,
      value: []
    },
    skuData: {
      type: Object,
      value: []
    },
    modalSku: {
      type: Boolean,
      value: false
    } ,
    modalSkuType: {
      type: String,
      value: ''
    },
    cartNum: {
      type: Number,
      value: 1
    },
    shoppingCartId: {
      type: String,
      value: null
    }
  },
  data: {
    
  },
  methods: {
    touchMove(){
      return
    },
    hideModalSku() {
      this.setData({
        modalSku: false
      })
    },
    cartNumChang(e){
      this.setData({
        cartNum: e.detail
      })
    },
    //选择规格
    tapSpec(e){
      let index = e.currentTarget.dataset.index
      let index2 = e.currentTarget.dataset.index2
      let goodsSpecData = this.data.goodsSpecData
      let spec = goodsSpecData[index].leaf[index2]
      goodsSpecData[index].checked = spec.id
      this.setData({
        goodsSpecData: goodsSpecData
      })
      let that = this
      let skus = this.data.goodsDetail.skus
      skus.forEach(function (sku) {
        let i = 0
        sku.specs.forEach(function (spec2) {
          goodsSpecData.forEach(function (spec) {
            if (spec.id == spec2.specId && spec.checked == spec2.specValueId){
              i = i+1
            }
          })
        })
        if (i == goodsSpecData.length){
          that.setData({
            skuData: sku
          })
          let detail = {
            goodsSpecData: goodsSpecData
          }
          that.triggerEvent('changeSpec', detail)
        }
      })
    },
    //提交
    toDo(e){
      let canDo = true
      try {
        this.data.goodsSpecData.forEach(function (spec) {
          if (!spec.checked) {
            canDo = false
            wx.showToast({
              title: '请选择' + spec.value,
              icon: 'none',
              duration: 2000
            })
            throw new Error()
          }
        })
      } catch (e) {}
      if (canDo){
        let skuData = this.data.skuData
        let goodsDetail = this.data.goodsDetail
        let specInfo = ''
        let goodsSpecData = this.data.goodsSpecData
        goodsSpecData.forEach(function (spec, index) {
          spec.leaf.forEach(function (specItem) {
            if (spec.checked == specItem.id) {
              specInfo = specInfo + specItem.value
              if (goodsSpecData.length != index + 1) {
                specInfo = specInfo + ';'
              }
            }
          })
        })
        if (e.currentTarget.dataset.type == '1'){//加购物车
          if (this.data.shoppingCartId){
            app.api.shoppingCartEdit({
              id: this.data.shoppingCartId,
              spuId: goodsDetail.id,
              skuId: skuData.id,
              quantity: this.data.cartNum,
              addPrice: skuData.salesPrice,
              spuName: goodsDetail.name,
              specInfo: specInfo,
              picUrl: skuData.picUrl ? skuData.picUrl : goodsDetail.picUrls[0]
            })
              .then(res => {
                wx.showToast({
                  title: '修改成功',
                  duration: 5000
                })
                this.setData({
                  modalSku: false
                })
                this.triggerEvent('operateCartEvent')
              })
          }else{
            app.api.shoppingCartAdd({
              spuId: goodsDetail.id,
              skuId: skuData.id,
              quantity: this.data.cartNum,
              addPrice: skuData.salesPrice,
              spuName: goodsDetail.name,
              specInfo: specInfo,
              picUrl: skuData.picUrl ? skuData.picUrl : goodsDetail.picUrls[0]
            })
              .then(res => {
                wx.showToast({
                  title: '添加成功',
                  duration: 5000
                })
                this.setData({
                  modalSku: false
                })
                this.triggerEvent('operateCartEvent')
              })
          }
        }else{//立即购买，前去确认订单
          /* 把参数信息异步存储到缓存当中 */
          wx.setStorage({
            key: 'param-orderConfirm',
            data: [
              {
              spuId: goodsDetail.id,
              skuId: skuData.id,
              quantity: this.data.cartNum,
              salesPrice: skuData.salesPrice,
              spuName: goodsDetail.name,
              specInfo: specInfo,
              picUrl: skuData.picUrl ? skuData.picUrl : goodsDetail.picUrls[0]
            }]
          })
          wx.navigateTo({
            url: '/pages/order/order-confirm/index'
          })
        }
      }
    }
  }
})