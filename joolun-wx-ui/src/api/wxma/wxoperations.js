/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
import request from "@/utils/request";

const baseUrl = "/wxma/operations";

export const getUserOptions = (keyword = "") => request({ url: `${baseUrl}/users`, method: "get", params: { keyword } });
export const getTemplateCategories = () => request({ url: `${baseUrl}/templates/categories`, method: "get" });
export const getPublicTemplates = (params) => request({ url: `${baseUrl}/templates/public`, method: "get", params });
export const getPublicTemplateKeywords = (templateTitleId) => request({ url: `${baseUrl}/templates/public/${encodeURIComponent(templateTitleId)}/keywords`, method: "get" });
export const getTemplates = () => request({ url: `${baseUrl}/templates`, method: "get" });
export const addTemplate = (data) => request({ url: `${baseUrl}/templates`, method: "post", data });
export const deleteTemplate = (templateId) => request({ url: `${baseUrl}/templates/${encodeURIComponent(templateId)}`, method: "delete" });
export const sendSubscribeMessage = (data) => request({ url: `${baseUrl}/templates/send`, method: "post", data });

export async function createQrcode(data) {
  const blob = await request({
    url: `${baseUrl}/qrcode`,
    method: "post",
    data,
    responseType: "blob",
  });
  if (!(blob instanceof Blob) || blob.size === 0) {
    throw new Error("没有收到有效的小程序码图片");
  }

  // 二进制响应会绕过通用业务码拦截；微信错误仍可能以 JSON Blob 返回。
  const prefix = (await blob.slice(0, 128).text()).trimStart();
  if (blob.type.includes("json") || prefix.startsWith("{") || prefix.startsWith("[")) {
    try {
      const result = JSON.parse(await blob.text());
      throw new Error(result.msg || "小程序码生成失败");
    } catch (error) {
      if (error instanceof SyntaxError) throw new Error("小程序码接口返回了无法识别的数据");
      throw error;
    }
  }
  if (blob.type && !blob.type.startsWith("image/")) {
    throw new Error("小程序码接口没有返回图片");
  }
  return blob;
}
export const checkText = (data) => request({ url: `${baseUrl}/security/text`, method: "post", data });
export const checkMedia = (data) => request({ url: `${baseUrl}/security/media`, method: "post", data });
export const getRecentMediaResults = (limit = 20) => request({ url: `${baseUrl}/security/media-results`, method: "get", params: { limit } });
export const getMediaResult = (traceId) => request({ url: `${baseUrl}/security/media-results/${encodeURIComponent(traceId)}`, method: "get" });
export const getAnalytics = (params) => request({ url: `${baseUrl}/analytics`, method: "get", params });

export const getShippingEnabled = () => request({ url: `${baseUrl}/shipping/enabled`, method: "get" });
export const uploadShipping = (data) => request({ url: `${baseUrl}/shipping/upload`, method: "post", data });
export const queryShipping = (data) => request({ url: `${baseUrl}/shipping/query`, method: "post", data });
export const getShippingList = (data) => request({ url: `${baseUrl}/shipping/list`, method: "post", data });
export const notifyConfirm = (data) => request({ url: `${baseUrl}/shipping/notify-confirm`, method: "post", data });
export const setShippingJumpPath = (path) => request({ url: `${baseUrl}/shipping/jump-path`, method: "put", data: { path } });

// 与商城订单关联的微信发货同步中心。
export const getShippingOrderPage = (params) => request({ url: `${baseUrl}/shipping/orders`, method: "get", params });
export const getShippingOrderSummary = (params) => request({ url: `${baseUrl}/shipping/orders/summary`, method: "get", params });
export const syncShippingOrder = (id) => request({ url: `${baseUrl}/shipping/orders/${encodeURIComponent(id)}/sync`, method: "put" });
export const batchSyncShippingOrders = (orderIds) => request({ url: `${baseUrl}/shipping/orders/sync`, method: "put", data: { orderIds } });
export const getWechatShippingOrder = (id) => request({ url: `${baseUrl}/shipping/orders/${encodeURIComponent(id)}/wechat`, method: "get" });
