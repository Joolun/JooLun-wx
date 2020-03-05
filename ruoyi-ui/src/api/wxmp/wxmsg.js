import request from '@/utils/request'

export function getPage(query) {
  return request({
    url: '/wxmsg/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return request({
    url: '/wxmsg',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return request({
    url: '/wxmsg/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return request({
    url: '/wxmsg/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return request({
    url: '/wxmsg',
    method: 'put',
    data: obj
  })
}

