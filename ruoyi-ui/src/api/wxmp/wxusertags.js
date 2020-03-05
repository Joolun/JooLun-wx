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
