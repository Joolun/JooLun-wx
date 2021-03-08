/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
Component({
  properties: {
    max: {
      type: Number,
      value: 0
    },
    min: {
      type: Number,
      value: 0
    },
    stNum: {
      type: Number,
      value: 0
    },
    customClass:{
      type: String,
      value: ''
    }
  },
  data: {

  },
  methods: {
    stNumMinus() {
      this.setData({
        stNum: this.data.stNum - 1
      })
      this.triggerEvent('numChange', this.data.stNum)
    },
    stNumAdd() {
      this.setData({
        stNum: this.data.stNum + 1
      })
      this.triggerEvent('numChange', this.data.stNum)
    },
    numChange(e){
      this.triggerEvent('numChange', val)
    }
  }
})