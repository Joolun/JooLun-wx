/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
import request from '@/utils/request'

export function save(obj) {
  return request({
    url: '/wxmenu',
    method: 'post',
    data: obj
  })
}

export function saveAndRelease(obj) {
  return request({
    url: '/wxmenu/release',
    method: 'post',
    data: obj
  })
}

export function getList(query) {
  return request({
    url: '/wxmenu/list',
    method: 'get',
    params: query
  })
}

export function delByRuleId(ruleId) {
  return request({
    url: '/wxmenu/' + ruleId,
    method: 'delete'
  })
}