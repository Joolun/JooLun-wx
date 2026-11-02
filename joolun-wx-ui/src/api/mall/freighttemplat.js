import request from '@/utils/request'

export const getFreightTemplatPage = (params) => request({ url: '/freighttemplat/page', method: 'get', params })
export const getFreightTemplatList = () => request({ url: '/freighttemplat/list', method: 'get' })
export const addFreightTemplat = (data) => request({ url: '/freighttemplat', method: 'post', data })
export const updateFreightTemplat = (data) => request({ url: '/freighttemplat', method: 'put', data })
export const delFreightTemplat = (id) => request({ url: '/freighttemplat/' + id, method: 'delete' })
