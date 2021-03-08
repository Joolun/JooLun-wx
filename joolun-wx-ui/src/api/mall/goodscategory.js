/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/goodscategory/page',
    method: 'get',
    params: query
  })
}

export function fetchTree(query) {
  return request({
    url: '/goodscategory/tree',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/goodscategory',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/goodscategory/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/goodscategory/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/goodscategory',
    method: 'put',
    data: obj
  })
}
