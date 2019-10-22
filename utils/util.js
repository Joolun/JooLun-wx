/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制
 */
const validate = require('./validate.js')

const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

//空值过滤器
const filterForm = (form) => {
  let obj = {};
  Object.keys(form).forEach(ele => {
    if (!validate.validatenull(form[ele])) {
      obj[ele] = form[ele]
    }
  });
  return obj;
}

module.exports = {
  formatTime: formatTime,
  filterForm: filterForm
}
