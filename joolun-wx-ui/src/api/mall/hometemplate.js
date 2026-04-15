/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
import request from "@/utils/request";

export function getCurrentTemplate() {
  return request({
    url: "/mallhometemplate/current",
    method: "get",
  });
}

export function saveCurrentTemplate(data) {
  return request({
    url: "/mallhometemplate/current",
    method: "put",
    data,
  });
}

export function refreshCurrentTemplateCache() {
  return request({
    url: "/mallhometemplate/current/cache",
    method: "put",
  });
}
