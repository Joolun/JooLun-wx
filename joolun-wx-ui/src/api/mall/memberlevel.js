import request from '@/utils/request'

export const getMemberLevels = () => request({ url: '/memberlevel/list', method: 'get' })
export const addMemberLevel = (data) => request({ url: '/memberlevel', method: 'post', data })
export const updateMemberLevel = (data) => request({ url: '/memberlevel', method: 'put', data })
