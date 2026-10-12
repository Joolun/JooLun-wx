import request from '@/utils/request'

export const getReviewPage = (params) => request({ url: '/goodsreview/page', method: 'get', params })
export const replyReview = (id, data) => request({ url: `/goodsreview/${id}/reply`, method: 'put', data })
