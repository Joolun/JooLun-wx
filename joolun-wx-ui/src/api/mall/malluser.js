/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import request from "@/utils/request";
import { download } from "@/utils/request";

/**
 * 分页查询商城用户。
 * 列表、筛选、排序都通过该接口统一承载。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getPage(query) {
  return request({
    url: "/malluser/page",
    method: "get",
    params: query,
  });
}

/**
 * 查询商城用户概览统计。
 * 用于后台列表顶部概览卡片，统计口径与当前筛选条件保持一致。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getSummary(query) {
  return request({
    url: "/malluser/summary",
    method: "get",
    params: query,
  });
}

/**
 * 查询商城用户运营标签分布。
 * 统计口径与当前会员筛选条件一致，用于后台标签概览和快捷打标。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function getTagSummary(query) {
  return request({
    url: "/malluser/tag/summary",
    method: "get",
    params: query,
  });
}

/**
 * 查询启用中的会员标签选项。
 *
 * @returns {Promise}
 */
export function getTagOptions() {
  return request({
    url: "/malluser/tag/options",
    method: "get",
  });
}

/**
 * 查询会员标签库。
 *
 * @returns {Promise}
 */
export function getTagLibrary() {
  return request({
    url: "/malluser/tag/library",
    method: "get",
  });
}

/**
 * 新增会员标签。
 *
 * @param {Object} obj 标签数据
 * @returns {Promise}
 */
export function addTag(obj) {
  return request({
    url: "/malluser/tag",
    method: "post",
    data: obj,
  });
}

/**
 * 修改会员标签。
 *
 * @param {Object} obj 标签数据
 * @returns {Promise}
 */
export function putTag(obj) {
  return request({
    url: "/malluser/tag",
    method: "put",
    data: obj,
  });
}

/**
 * 导出商城用户。
 * 导出口径复用后台当前筛选条件，适合运营同学直接导出当前用户池。
 *
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function exportMallUser(query) {
  return download("/malluser/export", query, `商城用户_${Date.now()}.xlsx`);
}

/**
 * 查询商城用户详情。
 * 主要用于后台查看或打开编辑抽屉时回填最新数据。
 *
 * @param {string} id 商城用户主键
 * @returns {Promise}
 */
export function getObj(id) {
  return request({
    url: "/malluser/" + id,
    method: "get",
  });
}

/**
 * 新增会员跟进记录。
 * 用于后台客服或运营在会员详情页沉淀本次跟进动作。
 *
 * @param {string} id 商城用户主键
 * @param {Object} obj 跟进数据
 * @returns {Promise}
 */
export function postFollowRecord(id, obj) {
  return request({
    url: "/malluser/" + id + "/follow",
    method: "post",
    data: obj,
  });
}

/**
 * 更新商城用户基础资料。
 * 当前仅允许维护商城侧自己的资料字段和状态字段。
 *
 * @param {Object} obj 商城用户数据
 * @returns {Promise}
 */
export function putObj(obj) {
  return request({
    url: "/malluser",
    method: "put",
    data: obj,
  });
}

/**
 * 批量更新商城用户状态。
 * 用于后台批量启用、禁用会员账号。
 *
 * @param {Object} obj 批量参数
 * @returns {Promise}
 */
export function putBatchStatus(obj) {
  return request({
    url: "/malluser/batch/status",
    method: "put",
    data: obj,
  });
}

/**
 * 批量更新商城用户备注。
 * 用于后台给一批用户补充运营跟进说明。
 *
 * @param {Object} obj 批量参数
 * @returns {Promise}
 */
export function putBatchRemark(obj) {
  return request({
    url: "/malluser/batch/remark",
    method: "put",
    data: obj,
  });
}

/**
 * 批量更新商城用户运营标签。
 * 用于后台给一批会员统一打上运营分组，便于后续筛选、导出和人工跟进。
 *
 * @param {Object} obj 批量参数
 * @returns {Promise}
 */
export function putBatchTag(obj) {
  return request({
    url: "/malluser/batch/tag",
    method: "put",
    data: obj,
  });
}
