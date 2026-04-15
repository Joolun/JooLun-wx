/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import request from "@/utils/request";

/**
 * 分页查询售后工作台数据。
 *
 * @param {Object} query 查询条件
 * @returns {Promise}
 */
export function getAfterSalePage(query) {
  return request({
    url: "/orderitem/afterSalePage",
    method: "get",
    params: query,
  });
}

/**
 * 查询售后工作台概览统计。
 *
 * @param {Object} query 查询条件
 * @returns {Promise}
 */
export function getAfterSaleSummary(query) {
  return request({
    url: "/orderitem/afterSaleSummary",
    method: "get",
    params: query,
  });
}
