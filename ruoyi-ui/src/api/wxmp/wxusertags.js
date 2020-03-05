/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/wxusertags/list',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/wxusertags',
    method: 'post',
    data: obj
  })
}

export function putObj(obj) {
  return request({
    url: '/wxusertags',
    method: 'put',
    data: obj
  })
}

export function delObj(query) {
  return request({
    url: '/wxusertags',
    method: 'delete',
    params: query
  })
}
