import request from '@/utils/request'

// 查询微信账号配置
export function getWxAccountConfig() {
  return request({
    url: '/system/app-config/wx',
    method: 'get'
  })
}

// 保存微信账号配置
export function updateWxAccountConfig(data) {
  return request({
    url: '/system/app-config/wx',
    method: 'put',
    data
  })
}

// 上传微信支付商户 API 私钥
export function uploadWxPayMerchantPrivateKey(file) {
  const data = new FormData()
  data.append('file', file)
  return request({
    url: '/system/app-config/wx/merchant-private-key',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data
  })
}

// 上传微信支付公钥
export function uploadWechatPayPublicKey(file) {
  const data = new FormData()
  data.append('file', file)
  return request({
    url: '/system/app-config/wx/wechat-pay-public-key',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data
  })
}

// 查询商城配置
export function getMallConfig() {
  return request({
    url: '/system/app-config/mall',
    method: 'get'
  })
}

// 保存商城配置
export function updateMallConfig(data) {
  return request({
    url: '/system/app-config/mall',
    method: 'put',
    data
  })
}
