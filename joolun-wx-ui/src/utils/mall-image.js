/**
 * 商城图片地址处理工具。
 * 约定数据库和接口只保存相对路径，页面展示时统一补全访问前缀。
 */

const baseUrl = import.meta.env.VITE_APP_BASE_API;

export function parseMallImageList(picUrls) {
  if (Array.isArray(picUrls)) {
    return picUrls.filter(Boolean);
  }
  if (!picUrls || typeof picUrls !== "string") {
    return [];
  }
  const trimmedValue = picUrls.trim();
  if (!trimmedValue) {
    return [];
  }
  if (trimmedValue.startsWith("[") && trimmedValue.endsWith("]")) {
    try {
      const parsedValue = JSON.parse(trimmedValue);
      return Array.isArray(parsedValue) ? parsedValue.filter(Boolean) : [];
    } catch (error) {
    }
  }
  return trimmedValue
    .split(/[,\uFF0C]/)
    .map((item) => item.trim())
    .filter(Boolean);
}

export function resolveMallImageUrl(picUrl, fallback = "") {
  if (!picUrl || typeof picUrl !== "string") {
    return fallback;
  }
  if (picUrl.startsWith("http://") || picUrl.startsWith("https://")) {
    return picUrl;
  }
  if (baseUrl && picUrl.startsWith(baseUrl)) {
    return picUrl;
  }
  const normalizedPicUrl = picUrl.startsWith("/") ? picUrl : `/${picUrl}`;
  if (!baseUrl) {
    return normalizedPicUrl;
  }
  return `${baseUrl}${normalizedPicUrl}`;
}

export function normalizeMallImageList(picUrls) {
  return parseMallImageList(picUrls).map((item) => resolveMallImageUrl(item));
}

export function getMallFirstImageValue(picUrls, fallback = "") {
  const picUrlList = parseMallImageList(picUrls);
  return picUrlList.length > 0 ? picUrlList[0] : fallback;
}

export function getMallFirstImageUrl(picUrls, fallback = "") {
  const picUrlList = normalizeMallImageList(picUrls);
  return picUrlList.length > 0 ? picUrlList[0] : fallback;
}
