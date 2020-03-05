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
