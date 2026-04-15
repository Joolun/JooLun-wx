/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import request from "@/utils/request";

/**
 * 查询商城数据看板。
 * 后台页面通过这一条接口拿到核心概览、趋势图、榜单和各类经营统计。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getDashboardOverview(query) {
  return request({
    url: "/dashboard/overview",
    method: "get",
    params: query,
  });
}
