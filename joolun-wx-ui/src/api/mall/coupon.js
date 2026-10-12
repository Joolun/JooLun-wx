import request from '@/utils/request'

export const getCouponPage = (params) => request({ url: '/coupon/page', method: 'get', params })
export const addCoupon = (data) => request({ url: '/coupon', method: 'post', data })
export const updateCoupon = (data) => request({ url: '/coupon', method: 'put', data })
