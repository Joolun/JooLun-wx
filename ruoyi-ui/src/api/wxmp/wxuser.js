/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/wxuser/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/wxuser',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/wxuser/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/wxuser/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/wxuser',
    method: 'put',
    data: obj
  })
}

export function synchroWxUser(obj) {
  return request({
    url: '/wxuser/synchron',
    method: 'post',
    data: obj
  })
}

export function updateRemark(obj) {
  return request({
    url: '/wxuser/remark',
    method: 'put',
    data: obj
  })
}

export function tagging(obj) {
  return request({
    url: '/wxuser/tagid-list',
    method: 'put',
    data: obj
  })
}
