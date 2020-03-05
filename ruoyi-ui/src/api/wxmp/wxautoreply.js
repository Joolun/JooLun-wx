import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/wxautoreply/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/wxautoreply',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/wxautoreply/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/wxautoreply/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/wxautoreply',
    method: 'put',
    data: obj
  })
}
