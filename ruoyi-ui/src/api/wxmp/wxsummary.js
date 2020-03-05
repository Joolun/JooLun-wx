import request from '@/utils/request'

export function getUserSummary(query) {
  return request({
    url: '/wxsummary/usersummary',
    method: 'get',
    params: query
  })
}

export function getUserCumulate(query) {
  return request({
    url: '/wxsummary/usercumulate',
    method: 'get',
    params: query
  })
}

export function getInterfaceSummary(query) {
  return request({
    url: '/wxsummary/interfacesummary',
    method: 'get',
    params: query
  })
}

