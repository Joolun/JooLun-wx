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