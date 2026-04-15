/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const util = require('../../utils/util.js')

Component({
  properties: {
    goodsList: {
      type: Object,
      value: [],
      observer(value) {
        this.setData({
          viewGoodsList: this.normalizeGoodsList(value)
        })
      }
    }
  },
  data: {
    viewGoodsList: []
  },
  methods: {
    normalizeGoodsList(goodsList) {
      return (goodsList || []).map((item) => {
        const picUrls = util.normalizePicUrlList(item.picUrls)
        return Object.assign({}, item, {
          picUrls,
          picUrl: util.getFirstPicUrl(picUrls)
        })
      })
    }
  }
})
