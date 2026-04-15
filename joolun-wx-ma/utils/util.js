/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
const validate = require('./validate.js')
const envModule = require('../config/env')

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

const getRuntimeConfig = () => {
  const app = typeof getApp === 'function' ? getApp() : null
  if (app && app.globalData && app.globalData.config) {
    return app.globalData.config
  }
  if (envModule && envModule.default) {
    return envModule.default
  }
  return envModule || {}
}

const resolveResourceUrl = (url, fallback = '') => {
  if (!url || typeof url !== 'string') {
    return fallback
  }
  if (url.indexOf('http://') === 0 || url.indexOf('https://') === 0) {
    return url
  }
  if (url.indexOf('/public/') === 0 || url.indexOf('data:') === 0 || url.indexOf('wxfile://') === 0) {
    return url
  }
  const runtimeConfig = getRuntimeConfig()
  const basePath = runtimeConfig && runtimeConfig.basePath ? runtimeConfig.basePath : ''
  const normalizedPath = url.indexOf('/') === 0 ? url : `/${url}`
  if (!basePath) {
    return normalizedPath
  }
  return `${basePath}${normalizedPath}`
}

const normalizePicUrlList = (picUrls) => {
  if (Array.isArray(picUrls)) {
    return picUrls.filter(Boolean).map((item) => resolveResourceUrl(item))
  }
  if (!picUrls || typeof picUrls !== 'string') {
    return []
  }
  const trimmedValue = picUrls.trim()
  if (!trimmedValue) {
    return []
  }
  if (trimmedValue.indexOf('[') === 0 && trimmedValue.lastIndexOf(']') === trimmedValue.length - 1) {
    try {
      const parsedValue = JSON.parse(trimmedValue)
      return Array.isArray(parsedValue)
        ? parsedValue.filter(Boolean).map((item) => resolveResourceUrl(item))
        : []
    } catch (error) {
    }
  }
  return trimmedValue
    .split(/[,\uFF0C]/)
    .map((item) => item.trim())
    .filter(Boolean)
    .map((item) => resolveResourceUrl(item))
}

const getFirstPicUrl = (picUrls, fallback = '/public/img/no_pic.png') => {
  const picUrlList = normalizePicUrlList(picUrls)
  if (picUrlList.length > 0) {
    return picUrlList[0]
  }
  return fallback
}

module.exports = {
  formatTime: formatTime,
  formatNumber: formatNumber,
  filterForm: filterForm,
  resolveResourceUrl: resolveResourceUrl,
  normalizePicUrlList: normalizePicUrlList,
  getFirstPicUrl: getFirstPicUrl
}
