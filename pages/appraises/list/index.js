/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
const util = require('../../../utils/util.js')
const app = getApp()

Page({
  data: {
    page: {
      searchCount: false,
      current: 1,
      size: 10,
      ascs: '',//升序字段
      descs: 'create_time'
    },
    parameter: {
      
    },
    loadmore: true,
    goodsAppraises: []
  },
  onLoad(options) {
    let spuId = options.spuId
    this.setData({
      ['parameter.spuId']: spuId
    })
    app.initPage()
      .then(res => {
        this.goodsAppraisesPage()
      })
  },
  goodsAppraisesPage() {
    app.api.goodsAppraisesPage(Object.assign(
      {},
      this.data.page,
      util.filterForm(this.data.parameter)
    ))
      .then(res => {
        let goodsAppraises = res.data.records
        this.setData({
          goodsAppraises: [...this.data.goodsAppraises, ...goodsAppraises]
        })
        if (goodsAppraises.length < this.data.page.size) {
          this.setData({
            loadmore: false
          })
        }
      })
  },
  scrollToLower() {
    if (this.data.loadmore) {
      this.setData({
        ['page.current']: this.data.page.current + 1
      })
      this.goodsAppraisesPage()
    }
  }
})
