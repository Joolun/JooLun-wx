/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.common.constant.CacheConstants;
import com.joolun.common.core.redis.RedisCache;
import com.joolun.common.exception.ServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.entity.MallHomeTemplate;
import com.joolun.mall.entity.MallHomeTemplateItem;
import com.joolun.mall.mapper.MallHomeTemplateMapper;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.mall.service.MallHomeTemplateItemService;
import com.joolun.mall.service.MallHomeTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商城首页装修模板服务实现。
 * 第一版按“配置式首页”落地，不做拖拽低代码，优先满足可配置、可保存、可渲染。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class MallHomeTemplateServiceImpl extends ServiceImpl<MallHomeTemplateMapper, MallHomeTemplate>
		implements MallHomeTemplateService {

	private static final String TEMPLATE_STATUS_ENABLED = CommonConstants.YES;
	private static final String TEMPLATE_STATUS_DISABLED = CommonConstants.NO;
	private static final String ITEM_TYPE_BANNER = "banner";
	private static final String ITEM_TYPE_NOTICE = "notice";
	private static final String ITEM_TYPE_NAV = "nav";
	private static final String ITEM_TYPE_SERVICE = "service";
	private static final String ITEM_TYPE_IMAGE = "image";
	private static final String ITEM_TYPE_GOODS = "goods";
	private static final int DEFAULT_GOODS_SIZE = 6;
	private static final int MAX_GOODS_SIZE = 20;
	private static final int CACHE_TIMEOUT_MINUTES = 30;

	private final MallHomeTemplateItemService mallHomeTemplateItemService;
	private final GoodsSpuService goodsSpuService;
	private final ObjectMapper objectMapper;
	private final RedisCache redisCache;

	@Override
	public MallHomeTemplate getCurrentTemplateDetail() {
		String cacheKey = getAdminCacheKey();
		MallHomeTemplate cacheValue = redisCache.getCacheObject(cacheKey);
		if (cacheValue != null) {
			if (!hasServiceItem(cacheValue.getItemList())) {
				refreshCurrentTemplateCache();
				return getCurrentTemplateDetail();
			}
			return cacheValue;
		}
		MallHomeTemplate currentTemplate = ensureCurrentTemplate();
		currentTemplate.setItemList(loadTemplateItems(currentTemplate.getId(), false));
		cacheCurrentTemplateDetail(currentTemplate);
		return currentTemplate;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveCurrentTemplate(MallHomeTemplate mallHomeTemplate) {
		validateTemplateInput(mallHomeTemplate);
		MallHomeTemplate currentTemplate = ensureCurrentTemplate();
		LocalDateTime now = LocalDateTime.now();
		currentTemplate.setTemplateName(StrUtil.blankToDefault(trimValue(mallHomeTemplate.getTemplateName()), "默认首页模板"));
		currentTemplate.setTemplateCode(StrUtil.blankToDefault(trimValue(mallHomeTemplate.getTemplateCode()), "ma_home_default"));
		currentTemplate.setCurrentFlag(CommonConstants.YES);
		currentTemplate.setStatus(resolveTemplateStatus(mallHomeTemplate.getStatus()));
		currentTemplate.setRemark(trimValue(mallHomeTemplate.getRemark()));
		currentTemplate.setUpdateTime(now);
		if (currentTemplate.getCreateTime() == null) {
			currentTemplate.setCreateTime(now);
		}
		if (StrUtil.isBlank(currentTemplate.getDelFlag())) {
			currentTemplate.setDelFlag(CommonConstants.NO);
		}
		updateById(currentTemplate);
		update(Wrappers.<MallHomeTemplate>lambdaUpdate()
				.ne(MallHomeTemplate::getId, currentTemplate.getId())
				.set(MallHomeTemplate::getCurrentFlag, CommonConstants.NO));
		mallHomeTemplateItemService.remove(Wrappers.<MallHomeTemplateItem>lambdaQuery()
				.eq(MallHomeTemplateItem::getTemplateId, currentTemplate.getId()));
		List<MallHomeTemplateItem> saveItems = buildSaveItems(currentTemplate.getId(), mallHomeTemplate.getItemList(), now);
		if (!saveItems.isEmpty()) {
			mallHomeTemplateItemService.saveBatch(saveItems);
		}
		clearTemplateCache();
		cacheCurrentTemplateDetail(buildCurrentTemplateDetail(currentTemplate));
		cacheMiniProgramTemplate(buildMiniProgramTemplate(currentTemplate));
		return true;
	}

	@Override
	public Map<String, Object> getMaCurrentTemplate() {
		String cacheKey = getMiniProgramCacheKey();
		Map<String, Object> cacheValue = redisCache.getCacheObject(cacheKey);
		if (cacheValue != null && !cacheValue.isEmpty()) {
			if (!hasServiceItem(cacheValue.get("items"))) {
				refreshCurrentTemplateCache();
				return getMaCurrentTemplate();
			}
			return cacheValue;
		}
		MallHomeTemplate currentTemplate = ensureCurrentTemplate();
		Map<String, Object> result = buildMiniProgramTemplate(currentTemplate);
		cacheMiniProgramTemplate(result);
		return result;
	}

	@Override
	public void refreshCurrentTemplateCache() {
		clearTemplateCache();
		MallHomeTemplate currentTemplate = ensureCurrentTemplate();
		cacheCurrentTemplateDetail(buildCurrentTemplateDetail(currentTemplate));
		cacheMiniProgramTemplate(buildMiniProgramTemplate(currentTemplate));
	}

	private MallHomeTemplate ensureCurrentTemplate() {
		MallHomeTemplate currentTemplate = getOne(Wrappers.<MallHomeTemplate>lambdaQuery()
				.eq(MallHomeTemplate::getCurrentFlag, CommonConstants.YES)
				.eq(MallHomeTemplate::getDelFlag, CommonConstants.NO)
				.orderByDesc(MallHomeTemplate::getUpdateTime, MallHomeTemplate::getCreateTime), false);
		if (currentTemplate != null) {
			ensureServiceItemExists(currentTemplate.getId());
			return currentTemplate;
		}
		return initDefaultTemplate();
	}

	@Transactional(rollbackFor = Exception.class)
	protected MallHomeTemplate initDefaultTemplate() {
		LocalDateTime now = LocalDateTime.now();
		MallHomeTemplate template = new MallHomeTemplate();
		template.setTemplateName("默认首页模板");
		template.setTemplateCode("ma_home_default");
		template.setCurrentFlag(CommonConstants.YES);
		template.setStatus(TEMPLATE_STATUS_ENABLED);
		template.setRemark("系统初始化生成的首页模板");
		template.setCreateTime(now);
		template.setUpdateTime(now);
		template.setDelFlag(CommonConstants.NO);
		save(template);
		List<MallHomeTemplateItem> defaultItems = buildDefaultItems(template.getId(), now);
		if (!defaultItems.isEmpty()) {
			mallHomeTemplateItemService.saveBatch(defaultItems);
		}
		template.setItemList(defaultItems);
		clearTemplateCache();
		return template;
	}

	private MallHomeTemplate buildCurrentTemplateDetail(MallHomeTemplate currentTemplate) {
		currentTemplate.setItemList(loadTemplateItems(currentTemplate.getId(), false));
		return currentTemplate;
	}

	@Transactional(rollbackFor = Exception.class)
	protected void ensureServiceItemExists(String templateId) {
		long serviceCount = mallHomeTemplateItemService.count(Wrappers.<MallHomeTemplateItem>lambdaQuery()
				.eq(MallHomeTemplateItem::getTemplateId, templateId)
				.eq(MallHomeTemplateItem::getDelFlag, CommonConstants.NO)
				.eq(MallHomeTemplateItem::getItemType, ITEM_TYPE_SERVICE));
		if (serviceCount > 0) {
			return;
		}
		List<MallHomeTemplateItem> itemList = mallHomeTemplateItemService.list(Wrappers.<MallHomeTemplateItem>lambdaQuery()
				.eq(MallHomeTemplateItem::getTemplateId, templateId)
				.eq(MallHomeTemplateItem::getDelFlag, CommonConstants.NO)
				.orderByAsc(MallHomeTemplateItem::getItemSort, MallHomeTemplateItem::getCreateTime, MallHomeTemplateItem::getId));
		LocalDateTime now = LocalDateTime.now();
		List<MallHomeTemplateItem> updateItemList = new ArrayList<>();
		for (MallHomeTemplateItem item : itemList) {
			Integer currentSort = item.getItemSort() == null ? 0 : item.getItemSort();
			if (currentSort >= 4) {
				item.setItemSort(currentSort + 1);
				item.setUpdateTime(now);
				updateItemList.add(item);
			}
		}
		if (!updateItemList.isEmpty()) {
			mallHomeTemplateItemService.updateBatchById(updateItemList);
		}
		MallHomeTemplateItem serviceItem = buildDefaultItem(templateId, ITEM_TYPE_SERVICE, "首页咨询入口", 4, buildDefaultServiceContent(), now);
		mallHomeTemplateItemService.save(serviceItem);
	}

	private Map<String, Object> buildMiniProgramTemplate(MallHomeTemplate currentTemplate) {
		Map<String, Object> result = new LinkedHashMap<>(8);
		result.put("id", currentTemplate.getId());
		result.put("templateName", currentTemplate.getTemplateName());
		result.put("templateCode", currentTemplate.getTemplateCode());
		result.put("status", currentTemplate.getStatus());
		if (!TEMPLATE_STATUS_ENABLED.equals(currentTemplate.getStatus())) {
			result.put("items", Collections.emptyList());
			return result;
		}
		List<Map<String, Object>> items = loadTemplateItems(currentTemplate.getId(), true).stream()
				.map(this::buildMiniProgramItem)
				.collect(Collectors.toList());
		result.put("items", items);
		return result;
	}

	private void cacheCurrentTemplateDetail(MallHomeTemplate currentTemplate) {
		if (currentTemplate == null) {
			return;
		}
		redisCache.setCacheObject(getAdminCacheKey(), currentTemplate, CACHE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	private void cacheMiniProgramTemplate(Map<String, Object> templateData) {
		if (templateData == null || templateData.isEmpty()) {
			return;
		}
		redisCache.setCacheObject(getMiniProgramCacheKey(), templateData, CACHE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
	}

	private void clearTemplateCache() {
		redisCache.deleteObject(getAdminCacheKey());
		redisCache.deleteObject(getMiniProgramCacheKey());
	}

	private String getAdminCacheKey() {
		return CacheConstants.MALL_HOME_TEMPLATE_KEY + "admin:current";
	}

	private String getMiniProgramCacheKey() {
		return CacheConstants.MALL_HOME_TEMPLATE_KEY + "ma:current";
	}

	private List<MallHomeTemplateItem> loadTemplateItems(String templateId, boolean onlyEnabled) {
		List<MallHomeTemplateItem> items = mallHomeTemplateItemService.list(Wrappers.<MallHomeTemplateItem>lambdaQuery()
				.eq(MallHomeTemplateItem::getTemplateId, templateId)
				.eq(MallHomeTemplateItem::getDelFlag, CommonConstants.NO)
				.eq(onlyEnabled, MallHomeTemplateItem::getItemStatus, CommonConstants.YES)
				.orderByAsc(MallHomeTemplateItem::getItemSort, MallHomeTemplateItem::getCreateTime, MallHomeTemplateItem::getId));
		for (MallHomeTemplateItem item : items) {
			item.setContent(parseContentJson(item.getContentJson()));
		}
		return items;
	}

	private boolean hasServiceItem(List<MallHomeTemplateItem> itemList) {
		if (itemList == null || itemList.isEmpty()) {
			return false;
		}
		return itemList.stream().anyMatch(item -> item != null && ITEM_TYPE_SERVICE.equals(item.getItemType()));
	}

	@SuppressWarnings("unchecked")
	private boolean hasServiceItem(Object itemList) {
		if (!(itemList instanceof Collection)) {
			return false;
		}
		for (Object item : (Collection<Object>) itemList) {
			if (!(item instanceof Map)) {
				continue;
			}
			Object itemType = ((Map<String, Object>) item).get("itemType");
			if (ITEM_TYPE_SERVICE.equals(parseString(itemType))) {
				return true;
			}
		}
		return false;
	}

	private List<MallHomeTemplateItem> buildSaveItems(String templateId, List<MallHomeTemplateItem> itemList, LocalDateTime now) {
		if (itemList == null || itemList.isEmpty()) {
			return Collections.emptyList();
		}
		List<MallHomeTemplateItem> saveItems = new ArrayList<>();
		for (int index = 0; index < itemList.size(); index++) {
			MallHomeTemplateItem inputItem = itemList.get(index);
			if (inputItem == null || StrUtil.isBlank(inputItem.getItemType())) {
				continue;
			}
			MallHomeTemplateItem saveItem = new MallHomeTemplateItem();
			saveItem.setTemplateId(templateId);
			saveItem.setItemType(trimValue(inputItem.getItemType()));
			saveItem.setItemName(StrUtil.blankToDefault(trimValue(inputItem.getItemName()), defaultItemName(inputItem.getItemType(), index + 1)));
			saveItem.setItemSort(inputItem.getItemSort() == null ? index + 1 : inputItem.getItemSort());
			saveItem.setItemStatus(resolveItemStatus(inputItem.getItemStatus()));
			saveItem.setContent(normalizeContent(saveItem.getItemType(), inputItem.getContent()));
			saveItem.setContentJson(resolveContentJson(inputItem));
			saveItem.setCreateTime(now);
			saveItem.setUpdateTime(now);
			saveItem.setDelFlag(CommonConstants.NO);
			saveItems.add(saveItem);
		}
		return saveItems;
	}

	private void validateTemplateInput(MallHomeTemplate mallHomeTemplate) {
		if (mallHomeTemplate == null) {
			throw new ServiceException("首页模板数据不能为空");
		}
		if (StrUtil.isBlank(trimValue(mallHomeTemplate.getTemplateName()))) {
			throw new ServiceException("模板名称不能为空");
		}
		List<MallHomeTemplateItem> itemList = mallHomeTemplate.getItemList();
		if (itemList == null || itemList.isEmpty()) {
			return;
		}
		for (int index = 0; index < itemList.size(); index++) {
			validateTemplateItem(itemList.get(index), index);
		}
	}

	private void validateTemplateItem(MallHomeTemplateItem item, int index) {
		if (item == null || StrUtil.isBlank(trimValue(item.getItemType()))) {
			throw new ServiceException("第" + (index + 1) + "个首页组件类型不能为空");
		}
		String itemType = trimValue(item.getItemType());
		if (!isSupportedItemType(itemType)) {
			throw new ServiceException("第" + (index + 1) + "个首页组件类型不支持");
		}
		Map<String, Object> content = item.getContent() == null ? Collections.emptyMap() : item.getContent();
		if (ITEM_TYPE_BANNER.equals(itemType) || ITEM_TYPE_IMAGE.equals(itemType)) {
			validateMediaItems(content, index, false);
			return;
		}
		if (ITEM_TYPE_NAV.equals(itemType)) {
			validateMediaItems(content, index, true);
			return;
		}
		if (ITEM_TYPE_NOTICE.equals(itemType)) {
			validateNoticeItems(content, index);
			return;
		}
		if (ITEM_TYPE_SERVICE.equals(itemType)) {
			validateServiceSection(content, index);
			return;
		}
		validateGoodsSection(content, index);
	}

	private void validateMediaItems(Map<String, Object> content, int index, boolean requireTitle) {
		List<Map<String, Object>> items = parseMapList(content.get("items"));
		if (items.isEmpty()) {
			throw new ServiceException("第" + (index + 1) + "个首页组件至少需要配置一项内容");
		}
		for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
			Map<String, Object> item = items.get(itemIndex);
			if (StrUtil.isBlank(parseString(item.get("imageUrl")))) {
				throw new ServiceException("第" + (index + 1) + "个首页组件第" + (itemIndex + 1) + "项图片未配置");
			}
			if (requireTitle && StrUtil.isBlank(parseString(item.get("title")))) {
				throw new ServiceException("第" + (index + 1) + "个首页组件第" + (itemIndex + 1) + "项标题不能为空");
			}
			validateLinkValue(parseString(item.get("linkType")), parseString(item.get("linkValue")),
					"第" + (index + 1) + "个首页组件第" + (itemIndex + 1) + "项");
		}
	}

	private void validateNoticeItems(Map<String, Object> content, int index) {
		List<Map<String, Object>> items = parseMapList(content.get("items"));
		if (items.isEmpty()) {
			throw new ServiceException("第" + (index + 1) + "个首页组件至少需要配置一条公告");
		}
		for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
			if (StrUtil.isBlank(parseString(items.get(itemIndex).get("text")))) {
				throw new ServiceException("第" + (index + 1) + "个首页组件第" + (itemIndex + 1) + "条公告内容不能为空");
			}
		}
	}

	private void validateGoodsSection(Map<String, Object> content, int index) {
		if (StrUtil.isBlank(parseString(content.get("title")))) {
			throw new ServiceException("第" + (index + 1) + "个商品分组标题不能为空");
		}
		String sourceType = normalizeGoodsSourceType(parseString(content.get("sourceType")));
		if ("manual".equals(sourceType) && parseStringList(content.get("goodsIds")).isEmpty()) {
			throw new ServiceException("第" + (index + 1) + "个商品分组至少要选择一个商品");
		}
		validateLinkValue(parseString(content.get("moreLinkType")), parseString(content.get("moreLinkValue")),
				"第" + (index + 1) + "个商品分组更多链接");
	}

	private void validateServiceSection(Map<String, Object> content, int index) {
		if (StrUtil.isBlank(parseString(content.get("title")))) {
			throw new ServiceException("第" + (index + 1) + "个咨询入口标题不能为空");
		}
		if (StrUtil.isBlank(parseString(content.get("contactButtonText")))) {
			throw new ServiceException("第" + (index + 1) + "个咨询入口主按钮文案不能为空");
		}
		List<String> tagList = parseStringList(content.get("tags"));
		if (tagList.isEmpty()) {
			throw new ServiceException("第" + (index + 1) + "个咨询入口至少需要配置一个服务标签");
		}
	}

	private void validateLinkValue(String linkType, String linkValue, String itemLabel) {
		String finalLinkType = normalizeLinkType(linkType);
		if (!"none".equals(finalLinkType) && StrUtil.isBlank(linkValue)) {
			throw new ServiceException(itemLabel + "未配置跳转值");
		}
	}

	private Map<String, Object> normalizeContent(String itemType, Map<String, Object> content) {
		Map<String, Object> sourceContent = content == null ? Collections.emptyMap() : content;
		if (ITEM_TYPE_BANNER.equals(itemType) || ITEM_TYPE_IMAGE.equals(itemType)) {
			Map<String, Object> result = new LinkedHashMap<>(4);
			List<Map<String, Object>> items = parseMapList(sourceContent.get("items")).stream()
					.map(this::normalizeImageItem)
					.filter(item -> StrUtil.isNotBlank(parseString(item.get("imageUrl"))))
					.collect(Collectors.toList());
			result.put("items", items);
			return result;
		}
		if (ITEM_TYPE_NAV.equals(itemType)) {
			Map<String, Object> result = new LinkedHashMap<>(4);
			List<Map<String, Object>> items = parseMapList(sourceContent.get("items")).stream()
					.map(this::normalizeNavItem)
					.filter(item -> StrUtil.isNotBlank(parseString(item.get("imageUrl")))
							|| StrUtil.isNotBlank(parseString(item.get("title"))))
					.collect(Collectors.toList());
			result.put("items", items);
			return result;
		}
		if (ITEM_TYPE_NOTICE.equals(itemType)) {
			Map<String, Object> result = new LinkedHashMap<>(4);
			List<Map<String, Object>> items = parseMapList(sourceContent.get("items")).stream()
					.map(this::normalizeNoticeItem)
					.filter(item -> StrUtil.isNotBlank(parseString(item.get("text"))))
					.collect(Collectors.toList());
			result.put("items", items);
			return result;
		}
		if (ITEM_TYPE_SERVICE.equals(itemType)) {
			return normalizeServiceContent(sourceContent);
		}
		return normalizeGoodsContent(sourceContent);
	}

	private Map<String, Object> normalizeImageItem(Map<String, Object> item) {
		Map<String, Object> result = new LinkedHashMap<>(4);
		result.put("imageUrl", trimValue(parseString(item.get("imageUrl"))));
		result.put("linkType", normalizeLinkType(parseString(item.get("linkType"))));
		result.put("linkValue", normalizeLinkValue(parseString(item.get("linkType")), parseString(item.get("linkValue"))));
		return result;
	}

	private Map<String, Object> normalizeNavItem(Map<String, Object> item) {
		Map<String, Object> result = normalizeImageItem(item);
		result.put("title", trimValue(parseString(item.get("title"))));
		return result;
	}

	private Map<String, Object> normalizeNoticeItem(Map<String, Object> item) {
		Map<String, Object> result = new LinkedHashMap<>(4);
		result.put("tag", StrUtil.blankToDefault(trimValue(parseString(item.get("tag"))), "公告"));
		result.put("text", trimValue(parseString(item.get("text"))));
		return result;
	}

	private Map<String, Object> normalizeGoodsContent(Map<String, Object> content) {
		Map<String, Object> result = new LinkedHashMap<>(8);
		String sourceType = normalizeGoodsSourceType(parseString(content.get("sourceType")));
		result.put("title", StrUtil.blankToDefault(trimValue(parseString(content.get("title"))), "商品分组"));
		result.put("subTitle", trimValue(parseString(content.get("subTitle"))));
		result.put("sourceType", sourceType);
		result.put("size", Math.min(Math.max(parseInt(content.get("size"), DEFAULT_GOODS_SIZE), 1), MAX_GOODS_SIZE));
		result.put("sortField", normalizeGoodsSortField(parseString(content.get("sortField"))));
		result.put("sortOrder", "asc".equals(parseString(content.get("sortOrder"))) ? "asc" : "desc");
		result.put("moreText", StrUtil.blankToDefault(trimValue(parseString(content.get("moreText"))), "更多"));
		result.put("moreLinkType", normalizeLinkType(parseString(content.get("moreLinkType"))));
		result.put("moreLinkValue", normalizeLinkValue(parseString(content.get("moreLinkType")),
				parseString(content.get("moreLinkValue"))));
		result.put("goodsIds", "manual".equals(sourceType) ? parseStringList(content.get("goodsIds")) : Collections.emptyList());
		return result;
	}

	private Map<String, Object> normalizeServiceContent(Map<String, Object> content) {
		Map<String, Object> result = new LinkedHashMap<>(12);
		List<String> tagList = parseStringList(content.get("tags")).stream()
				.limit(6)
				.collect(Collectors.toList());
		result.put("title", StrUtil.blankToDefault(trimValue(parseString(content.get("title"))), "首页咨询入口"));
		result.put("desc", StrUtil.blankToDefault(trimValue(parseString(content.get("desc"))),
				"买前可咨询选品，买后可追踪订单和售后，统一走小程序原生客服更直接。"));
		result.put("tags", tagList.isEmpty()
				? new ArrayList<>(java.util.Arrays.asList("选品推荐", "活动优惠", "订单进度", "售后帮助"))
				: tagList);
		result.put("contactButtonText", StrUtil.blankToDefault(trimValue(parseString(content.get("contactButtonText"))), "立即咨询"));
		result.put("goodsButtonText", StrUtil.blankToDefault(trimValue(parseString(content.get("goodsButtonText"))), "选品咨询"));
		result.put("orderButtonText", StrUtil.blankToDefault(trimValue(parseString(content.get("orderButtonText"))), "订单进度"));
		result.put("afterSaleButtonText", StrUtil.blankToDefault(trimValue(parseString(content.get("afterSaleButtonText"))), "售后帮助"));
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> parseMapList(Object value) {
		if (!(value instanceof Collection)) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> result = new ArrayList<>();
		for (Object item : (Collection<?>) value) {
			if (item instanceof Map) {
				result.add(new LinkedHashMap<>((Map<String, Object>) item));
			}
		}
		return result;
	}

	private String resolveContentJson(MallHomeTemplateItem item) {
		if (item.getContent() != null && !item.getContent().isEmpty()) {
			return writeContentJson(item.getContent());
		}
		if (StrUtil.isNotBlank(item.getContentJson())) {
			return item.getContentJson().trim();
		}
		return "{}";
	}

	private Map<String, Object> buildMiniProgramItem(MallHomeTemplateItem item) {
		Map<String, Object> result = new LinkedHashMap<>(8);
		result.put("id", item.getId());
		result.put("itemType", item.getItemType());
		result.put("itemName", item.getItemName());
		result.put("itemSort", item.getItemSort());
		Map<String, Object> content = new LinkedHashMap<>(item.getContent() == null ? Collections.emptyMap() : item.getContent());
		if (ITEM_TYPE_GOODS.equals(item.getItemType())) {
			content.put("goodsList", queryHomeGoodsList(content));
		}
		result.put("content", content);
		return result;
	}

	private List<GoodsSpu> queryHomeGoodsList(Map<String, Object> content) {
		int size = Math.min(Math.max(parseInt(content.get("size"), DEFAULT_GOODS_SIZE), 1), MAX_GOODS_SIZE);
		String sourceType = StrUtil.blankToDefault(parseString(content.get("sourceType")), "hot");
		if ("manual".equals(sourceType)) {
			return loadGoodsByIds(parseStringList(content.get("goodsIds")), size);
		}
		return loadGoodsByIds(queryGoodsIdsBySource(sourceType, size, content), size);
	}

	private List<String> queryGoodsIdsBySource(String sourceType, int size, Map<String, Object> content) {
		List<GoodsSpu> goodsIdList = goodsSpuService.list(Wrappers.<GoodsSpu>lambdaQuery()
				.select(GoodsSpu::getId)
				.eq(GoodsSpu::getShelf, CommonConstants.YES)
				.eq(GoodsSpu::getDelFlag, CommonConstants.NO)
				.orderByAsc(Objects.equals(parseString(content.get("sortOrder")), "asc"), resolveOrderColumn(sourceType, content))
				.orderByDesc(!Objects.equals(parseString(content.get("sortOrder")), "asc"), resolveOrderColumn(sourceType, content))
				.last("limit " + size));
		return goodsIdList.stream().map(GoodsSpu::getId).filter(StrUtil::isNotBlank).collect(Collectors.toList());
	}

	private com.baomidou.mybatisplus.core.toolkit.support.SFunction<GoodsSpu, ?> resolveOrderColumn(String sourceType,
			Map<String, Object> content) {
		String sortField = parseString(content.get("sortField"));
		if ("createTime".equals(sortField) || "new".equals(sourceType)) {
			return GoodsSpu::getCreateTime;
		}
		if ("sort".equals(sortField)) {
			return GoodsSpu::getSort;
		}
		if ("updateTime".equals(sortField)) {
			return GoodsSpu::getUpdateTime;
		}
		return GoodsSpu::getSaleNum;
	}

	private List<GoodsSpu> loadGoodsByIds(List<String> goodsIds, int size) {
		if (goodsIds == null || goodsIds.isEmpty()) {
			return Collections.emptyList();
		}
		List<GoodsSpu> goodsList = new ArrayList<>();
		for (String goodsId : goodsIds) {
			if (goodsList.size() >= size) {
				break;
			}
			GoodsSpu goodsSpu = goodsSpuService.getById2(goodsId);
			if (goodsSpu == null) {
				continue;
			}
			if (!CommonConstants.YES.equals(goodsSpu.getShelf())) {
				continue;
			}
			if (CommonConstants.YES.equals(goodsSpu.getDelFlag())) {
				continue;
			}
			goodsList.add(goodsSpu);
		}
		return goodsList;
	}

	private List<String> parseStringList(Object value) {
		if (value == null) {
			return Collections.emptyList();
		}
		if (value instanceof Collection) {
			return ((Collection<?>) value).stream()
					.map(this::parseString)
					.filter(StrUtil::isNotBlank)
					.collect(Collectors.toList());
		}
		String text = parseString(value);
		if (StrUtil.isBlank(text)) {
			return Collections.emptyList();
		}
		String[] splitArray = text.split("[,，]");
		List<String> result = new ArrayList<>();
		for (String item : splitArray) {
			if (StrUtil.isNotBlank(item)) {
				result.add(item.trim());
			}
		}
		return result;
	}

	private Map<String, Object> parseContentJson(String contentJson) {
		if (StrUtil.isBlank(contentJson)) {
			return new LinkedHashMap<>(4);
		}
		try {
			return objectMapper.readValue(contentJson, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			return new LinkedHashMap<>(4);
		}
	}

	private String writeContentJson(Map<String, Object> content) {
		try {
			return objectMapper.writeValueAsString(content == null ? Collections.emptyMap() : content);
		} catch (Exception e) {
			return "{}";
		}
	}

	private List<MallHomeTemplateItem> buildDefaultItems(String templateId, LocalDateTime now) {
		List<MallHomeTemplateItem> items = new ArrayList<>();
		items.add(buildDefaultItem(templateId, ITEM_TYPE_BANNER, "首页轮播", 1, buildDefaultBannerContent(), now));
		items.add(buildDefaultItem(templateId, ITEM_TYPE_NAV, "快捷导航", 2, buildDefaultNavContent(), now));
		items.add(buildDefaultItem(templateId, ITEM_TYPE_NOTICE, "首页公告", 3, buildDefaultNoticeContent(), now));
		items.add(buildDefaultItem(templateId, ITEM_TYPE_SERVICE, "首页咨询入口", 4, buildDefaultServiceContent(), now));
		items.add(buildDefaultItem(templateId, ITEM_TYPE_IMAGE, "活动图片", 5, buildDefaultImageContent(), now));
		items.add(buildDefaultItem(templateId, ITEM_TYPE_GOODS, "热销单品", 6, buildDefaultGoodsContent(), now));
		return items;
	}

	private MallHomeTemplateItem buildDefaultItem(String templateId, String itemType, String itemName, int sort,
			Map<String, Object> content, LocalDateTime now) {
		MallHomeTemplateItem item = new MallHomeTemplateItem();
		item.setTemplateId(templateId);
		item.setItemType(itemType);
		item.setItemName(itemName);
		item.setItemSort(sort);
		item.setItemStatus(CommonConstants.YES);
		item.setContent(content);
		item.setContentJson(writeContentJson(content));
		item.setCreateTime(now);
		item.setUpdateTime(now);
		item.setDelFlag(CommonConstants.NO);
		return item;
	}

	private Map<String, Object> buildDefaultBannerContent() {
		Map<String, Object> content = new LinkedHashMap<>(4);
		List<Map<String, Object>> items = new ArrayList<>();
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/c888e1d3-f542-4b4e-aa43-be9d50cc0696.jpg",
				"goods",
				"2043982437111697409"));
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/a5e3b9f4-f1fe-4bb2-b487-13f4395ef187.jpg",
				"goods",
				"2043982437111697409"));
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/c8fd87ff-ca5d-4f95-8f81-e99cae48b0f7.jpg",
				"goods",
				"2043982437111697409"));
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/bf176bd5-3487-4b61-8d30-1cc2ad95b8ac.jpg",
				"goods",
				"2043982437111697409"));
		content.put("items", items);
		return content;
	}

	private Map<String, Object> buildDefaultNavContent() {
		Map<String, Object> content = new LinkedHashMap<>(4);
		List<Map<String, Object>> items = new ArrayList<>();
		items.add(buildNavItem("iPhone", "/public/img/6-1.png", "goods", "2043982437111697409"));
		items.add(buildNavItem("iPad", "/public/img/6-2.png", "goods", "2043982437111697409"));
		items.add(buildNavItem("Mac", "/public/img/6-3.png", "goods", "2043982437111697409"));
		items.add(buildNavItem("Watch", "/public/img/6-4.png", "goods", "1442513594978988034"));
		items.add(buildNavItem("AirPods", "/public/img/6-5.png", "goods", "1442514202062548994"));
		content.put("items", items);
		return content;
	}

	private Map<String, Object> buildDefaultNoticeContent() {
		Map<String, Object> content = new LinkedHashMap<>(4);
		List<Map<String, Object>> items = new ArrayList<>();
		items.add(buildNoticeItem("公告", "更多源码请到 www.joolun.com"));
		items.add(buildNoticeItem("注意", "演示商城不发货不退款"));
		content.put("items", items);
		return content;
	}

	private Map<String, Object> buildDefaultImageContent() {
		Map<String, Object> content = new LinkedHashMap<>(4);
		List<Map<String, Object>> items = new ArrayList<>();
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/055f3304-13d1-43c8-8547-326cc3efc7fc.jpg",
				"goods",
				"2043982437111697409"));
		items.add(buildImageLinkItem(
				"https://joolun-base-test.oss-cn-zhangjiakou.aliyuncs.com/1/material/2f290591-8351-4be8-a9ab-6277d007b8c7.jpg",
				"goods",
				"1442512050032275457"));
		content.put("items", items);
		return content;
	}

	private Map<String, Object> buildDefaultServiceContent() {
		Map<String, Object> content = new LinkedHashMap<>(12);
		content.put("title", "首页咨询入口");
		content.put("desc", "买前可咨询选品，买后可追踪订单和售后，统一走小程序原生客服更直接。");
		content.put("tags", new ArrayList<>(java.util.Arrays.asList("选品推荐", "活动优惠", "订单进度", "售后帮助")));
		content.put("contactButtonText", "立即咨询");
		content.put("goodsButtonText", "选品咨询");
		content.put("orderButtonText", "订单进度");
		content.put("afterSaleButtonText", "售后帮助");
		return content;
	}

	private Map<String, Object> buildDefaultGoodsContent() {
		Map<String, Object> content = new LinkedHashMap<>(8);
		content.put("title", "热销单品");
		content.put("subTitle", "按销量排序展示当前热销商品");
		content.put("sourceType", "hot");
		content.put("size", 5);
		content.put("sortField", "saleNum");
		content.put("sortOrder", "desc");
		content.put("moreText", "更多");
		content.put("moreLinkType", "page");
		content.put("moreLinkValue", "/pages/goods/goods-list/index?type=2");
		content.put("goodsIds", Collections.emptyList());
		return content;
	}

	private Map<String, Object> buildImageLinkItem(String imageUrl, String linkType, String linkValue) {
		Map<String, Object> item = new LinkedHashMap<>(4);
		item.put("imageUrl", imageUrl);
		item.put("linkType", linkType);
		item.put("linkValue", linkValue);
		return item;
	}

	private Map<String, Object> buildNavItem(String title, String imageUrl, String linkType, String linkValue) {
		Map<String, Object> item = buildImageLinkItem(imageUrl, linkType, linkValue);
		item.put("title", title);
		return item;
	}

	private Map<String, Object> buildNoticeItem(String tag, String text) {
		Map<String, Object> item = new LinkedHashMap<>(4);
		item.put("tag", tag);
		item.put("text", text);
		return item;
	}

	private String resolveTemplateStatus(String status) {
		return TEMPLATE_STATUS_DISABLED.equals(status) ? TEMPLATE_STATUS_DISABLED : TEMPLATE_STATUS_ENABLED;
	}

	private String resolveItemStatus(String status) {
		return TEMPLATE_STATUS_DISABLED.equals(status) ? TEMPLATE_STATUS_DISABLED : TEMPLATE_STATUS_ENABLED;
	}

	private String defaultItemName(String itemType, int index) {
		if (ITEM_TYPE_BANNER.equals(itemType)) {
			return "轮播图" + index;
		}
		if (ITEM_TYPE_NOTICE.equals(itemType)) {
			return "公告栏" + index;
		}
		if (ITEM_TYPE_NAV.equals(itemType)) {
			return "快捷导航" + index;
		}
		if (ITEM_TYPE_SERVICE.equals(itemType)) {
			return "首页咨询入口" + index;
		}
		if (ITEM_TYPE_IMAGE.equals(itemType)) {
			return "活动图片" + index;
		}
		if (ITEM_TYPE_GOODS.equals(itemType)) {
			return "商品分组" + index;
		}
		return "首页组件" + index;
	}

	private String trimValue(String value) {
		return value == null ? null : value.trim();
	}

	private boolean isSupportedItemType(String itemType) {
		return ITEM_TYPE_BANNER.equals(itemType)
				|| ITEM_TYPE_NOTICE.equals(itemType)
				|| ITEM_TYPE_NAV.equals(itemType)
				|| ITEM_TYPE_SERVICE.equals(itemType)
				|| ITEM_TYPE_IMAGE.equals(itemType)
				|| ITEM_TYPE_GOODS.equals(itemType);
	}

	private String normalizeLinkType(String linkType) {
		if ("goods".equals(linkType) || "page".equals(linkType) || "tab".equals(linkType)) {
			return linkType;
		}
		return "none";
	}

	private String normalizeLinkValue(String linkType, String linkValue) {
		return "none".equals(normalizeLinkType(linkType)) ? "" : trimValue(linkValue);
	}

	private String normalizeGoodsSourceType(String sourceType) {
		if ("new".equals(sourceType) || "manual".equals(sourceType)) {
			return sourceType;
		}
		return "hot";
	}

	private String normalizeGoodsSortField(String sortField) {
		if ("createTime".equals(sortField) || "sort".equals(sortField) || "updateTime".equals(sortField)) {
			return sortField;
		}
		return "saleNum";
	}

	private String parseString(Object value) {
		return value == null ? null : String.valueOf(value).trim();
	}

	private int parseInt(Object value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		try {
			return Integer.parseInt(String.valueOf(value).trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
