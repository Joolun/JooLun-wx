/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import request from "@/utils/request";

/**
 * 分页查询订单。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getPage(query) {
  return request({
    url: "/orderinfo/page",
    method: "get",
    params: query,
  });
}

/**
 * 查询订单概览统计。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getSummary(query) {
  return request({
    url: "/orderinfo/summary",
    method: "get",
    params: query,
  });
}

/**
 * 查询订单数量。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getCount(query) {
  return request({
    url: "/orderinfo/count",
    method: "get",
    params: query,
  });
}

/**
 * 新增订单。
 *
 * @param {Object} obj 订单数据
 * @returns {Promise}
 */
export function addObj(obj) {
  return request({
    url: "/orderinfo",
    method: "post",
    data: obj,
  });
}

/**
 * 查询订单详情。
 *
 * @param {string} id 订单主键
 * @returns {Promise}
 */
export function getObj(id) {
  return request({
    url: "/orderinfo/" + id,
    method: "get",
  });
}

/**
 * 删除订单。
 *
 * @param {string} id 订单主键
 * @returns {Promise}
 */
export function delObj(id) {
  return request({
    url: "/orderinfo/" + id,
    method: "delete",
  });
}

/**
 * 更新订单。
 *
 * @param {Object} obj 订单数据
 * @returns {Promise}
 */
export function putObj(obj) {
  return request({
    url: "/orderinfo",
    method: "put",
    data: obj,
  });
}

/**
 * 修改订单价格。
 *
 * @param {Object} obj 调价参数
 * @returns {Promise}
 */
export function editPrice(obj) {
  return request({
    url: "/orderinfo/editPrice",
    method: "put",
    data: obj,
  });
}

/**
 * 取消订单。
 *
 * @param {string} id 订单主键
 * @returns {Promise}
 */
export function orderCancel(id) {
  return request({
    url: "/orderinfo/cancel/" + id,
    method: "put",
  });
}

/**
 * 确认收货。
 *
 * @param {string} id 订单主键
 * @returns {Promise}
 */
export function takeGoods(id) {
  return request({
    url: "/orderinfo/takegoods/" + id,
    method: "put",
  });
}

/**
 * 后台审核退款。
 *
 * @param {Object} obj 退款审核参数
 * @returns {Promise}
 */
export function doOrderRefunds(obj) {
  return request({
    url: "/orderinfo/doOrderRefunds",
    method: "put",
    data: obj,
  });
}
