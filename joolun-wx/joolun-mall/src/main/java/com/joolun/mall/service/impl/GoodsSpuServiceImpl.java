/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.dto.SpuSpecDTO;
import com.joolun.mall.entity.GoodsSku;
import com.joolun.mall.entity.GoodsSkuSpecValue;
import com.joolun.mall.entity.GoodsSpec;
import com.joolun.mall.entity.GoodsSpecValue;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.entity.GoodsSpuSpec;
import com.joolun.mall.mapper.GoodsSpuMapper;
import com.joolun.mall.service.GoodsSkuService;
import com.joolun.mall.service.GoodsSkuSpecValueService;
import com.joolun.mall.service.GoodsSpecService;
import com.joolun.mall.service.GoodsSpecValueService;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.mall.service.GoodsSpuSpecService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
/**
 * 商品 SPU 服务，多规格持久化逻辑都集中在这里。
 *
 * 主要职责：
 * 1. 把前端编辑态转换为 SPU、SKU、规格关系表数据。
 * 2. 更新时先清空再重建规格关系，降低维护复杂度。
 * 3. 查询详情时回填 SKU、规格组和规格文案，供管理端和小程序使用。
 *
 * @author www.joolun.com
 */
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements GoodsSpuService {

	/** 单规格模式，只保留一条有效 SKU。 */
	private static final String SPEC_TYPE_SINGLE = "0";
	/** 多规格模式，SKU 由规格组合生成。 */
	private static final String SPEC_TYPE_MULTI = "1";

	private final GoodsSkuService goodsSkuService;
	private final GoodsSpecService goodsSpecService;
	private final GoodsSpecValueService goodsSpecValueService;
	private final GoodsSpuSpecService goodsSpuSpecService;
	private final GoodsSkuSpecValueService goodsSkuSpecValueService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		clearSkuAndSpecRelations(String.valueOf(id));
		super.removeById(id);
		return true;
	}

	@Override
	public IPage<GoodsSpu> page1(IPage<GoodsSpu> page, GoodsSpu goodsSpu) {
		return baseMapper.selectPage1(page, goodsSpu);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save1(GoodsSpu goodsSpu) {
		// 先把前端传入结构整理成统一格式，后续落库逻辑就不需要区分单规格和多规格。
		normalizeIncomingSpu(goodsSpu);
		baseMapper.insert(goodsSpu);
		saveSkuAndSpecRelations(goodsSpu);
		refreshPriceRange(goodsSpu.getId(), goodsSpu.getSpecType());
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById1(GoodsSpu goodsSpu) {
		// 更新时采用“先清空、再重建”的方式，而不是做复杂差异比对。
		// 本项目不要求兼容旧数据，这种方案更直接也更稳定。
		normalizeIncomingSpu(goodsSpu);
		baseMapper.updateById(goodsSpu);
		clearSkuAndSpecRelations(goodsSpu.getId());
		saveSkuAndSpecRelations(goodsSpu);
		refreshPriceRange(goodsSpu.getId(), goodsSpu.getSpecType());
		return true;
	}

	@Override
	public GoodsSpu getById1(String id) {
		GoodsSpu goodsSpu = baseMapper.selectById1(id);
		fillSkuAndSpecData(goodsSpu);
		return goodsSpu;
	}

	@Override
	public GoodsSpu getById2(String id) {
		GoodsSpu goodsSpu = baseMapper.selectById2(id);
		fillSkuAndSpecData(goodsSpu);
		return goodsSpu;
	}

	private void normalizeIncomingSpu(GoodsSpu goodsSpu) {
		// 保证至少存在一条 SKU，避免保存时再区分“旧单规格结构”和“新多规格结构”。
		List<GoodsSku> skus = new ArrayList<>(safeList(goodsSpu.getSkus()));
		if (skus.isEmpty()) {
			skus.add(buildDefaultSku(goodsSpu));
		}
		for (GoodsSku sku : skus) {
			if (StrUtil.isBlank(sku.getEnable())) {
				sku.setEnable(CommonConstants.YES);
			}
			if (StrUtil.isBlank(sku.getPicUrl())) {
				sku.setPicUrl(firstPic(goodsSpu.getPicUrls()));
			}
			if (sku.getStock() == null) {
				sku.setStock(0);
			}
		}
		goodsSpu.setSkus(skus);
		if (safeList(goodsSpu.getSpuSpec()).isEmpty()) {
			goodsSpu.setSpecType(skus.size() > 1 ? SPEC_TYPE_MULTI : SPEC_TYPE_SINGLE);
		} else {
			goodsSpu.setSpecType(SPEC_TYPE_MULTI);
		}
	}

	private GoodsSku buildDefaultSku(GoodsSpu goodsSpu) {
		// 用 SPU 聚合字段构造默认 SKU。
		// 这样单规格商品也能直接落到新的 SKU 模型里。
		GoodsSku goodsSku = new GoodsSku();
		goodsSku.setSkuCode(goodsSpu.getSpuCode());
		goodsSku.setPicUrl(firstPic(goodsSpu.getPicUrls()));
		goodsSku.setSalesPrice(defaultDecimal(goodsSpu.getSalesPrice(), goodsSpu.getPriceDown()));
		goodsSku.setMarketPrice(defaultDecimal(goodsSpu.getMarketPrice(), goodsSpu.getPriceUp(), goodsSpu.getSalesPrice(), goodsSpu.getPriceDown()));
		goodsSku.setCostPrice(defaultDecimal(goodsSpu.getCostPrice(), goodsSpu.getSalesPrice(), goodsSpu.getPriceDown()));
		goodsSku.setStock(goodsSpu.getStock() == null ? 0 : goodsSpu.getStock());
		goodsSku.setEnable(CommonConstants.YES);
		return goodsSku;
	}

	private void saveSkuAndSpecRelations(GoodsSpu goodsSpu) {
		// 保存顺序：
		// 1. 先保存 SKU，确保每条记录拿到 skuId。
		// 2. 多规格模式下再保存 SPU-规格关系和 SKU-规格值关系。
		PreparedSpecData preparedSpecData = prepareSpecData(goodsSpu);
		List<GoodsSku> skus = safeList(goodsSpu.getSkus());
		for (GoodsSku goodsSku : skus) {
			goodsSku.setSpuId(goodsSpu.getId());
			goodsSkuService.save(goodsSku);
		}
		if (!SPEC_TYPE_MULTI.equals(goodsSpu.getSpecType())) {
			return;
		}
		if (!preparedSpecData.spuSpecs.isEmpty()) {
			goodsSpuSpecService.saveBatch(preparedSpecData.spuSpecs);
		}
		List<GoodsSkuSpecValue> skuSpecValues = new ArrayList<>();
		for (GoodsSku goodsSku : skus) {
			List<GoodsSkuSpecValue> specs = safeList(goodsSku.getSpecs());
			for (int index = 0; index < specs.size(); index++) {
				GoodsSkuSpecValue item = specs.get(index);
				String resolvedSpecValueId = resolveSpecValueId(item, preparedSpecData);
				if (StrUtil.isBlank(resolvedSpecValueId)) {
					throw new IllegalArgumentException("SKU spec value is missing: " + goodsSku.getSkuCode());
				}
				item.setSpuId(goodsSpu.getId());
				item.setSkuId(goodsSku.getId());
				item.setSpecValueId(resolvedSpecValueId);
				if (item.getSort() == null) {
					item.setSort(index);
				}
				skuSpecValues.add(item);
			}
		}
		if (!skuSpecValues.isEmpty()) {
			goodsSkuSpecValueService.saveBatch(skuSpecValues);
		}
	}

	private PreparedSpecData prepareSpecData(GoodsSpu goodsSpu) {
		// 生成规格主数据和规格值主数据。
		// 同时记录别名映射，便于把前端临时 id 转成新入库的真实 id。
		PreparedSpecData preparedSpecData = new PreparedSpecData();
		if (!SPEC_TYPE_MULTI.equals(goodsSpu.getSpecType())) {
			return preparedSpecData;
		}
		List<SpuSpecDTO> spuSpecs = safeList(goodsSpu.getSpuSpec());
		for (int specIndex = 0; specIndex < spuSpecs.size(); specIndex++) {
			SpuSpecDTO spuSpec = spuSpecs.get(specIndex);
			if (StrUtil.isBlank(spuSpec.getName())) {
				continue;
			}
			GoodsSpec goodsSpec = new GoodsSpec();
			goodsSpec.setName(spuSpec.getName());
			goodsSpecService.save(goodsSpec);
			if (StrUtil.isNotBlank(spuSpec.getId())) {
				preparedSpecData.specIdAliasMap.put(spuSpec.getId(), goodsSpec.getId());
			}
			preparedSpecData.specNameIdMap.put(spuSpec.getName(), goodsSpec.getId());
			GoodsSpuSpec goodsSpuSpec = new GoodsSpuSpec();
			goodsSpuSpec.setSpuId(goodsSpu.getId());
			goodsSpuSpec.setSpecId(goodsSpec.getId());
			goodsSpuSpec.setSpecName(spuSpec.getName());
			goodsSpuSpec.setSort(spuSpec.getSort() == null ? specIndex : spuSpec.getSort());
			preparedSpecData.spuSpecs.add(goodsSpuSpec);
			List<GoodsSpecValue> values = normalizeSpecValues(spuSpec);
			for (GoodsSpecValue value : values) {
				if (StrUtil.isBlank(value.getName())) {
					continue;
				}
				String originalId = value.getId();
				value.setId(null);
				value.setSpecId(goodsSpec.getId());
				goodsSpecValueService.save(value);
				if (StrUtil.isNotBlank(originalId)) {
					preparedSpecData.specValueIdAliasMap.put(originalId, value.getId());
				}
				preparedSpecData.specValueNameIdMap.put(composeSpecValueKey(goodsSpec.getId(), value.getName()), value.getId());
				preparedSpecData.specValueNameIdMap.put(composeSpecValueKey(spuSpec.getName(), value.getName()), value.getId());
			}
		}
		return preparedSpecData;
	}

	private String resolveSpecValueId(GoodsSkuSpecValue item, PreparedSpecData preparedSpecData) {
		// 规格值 id 的解析优先级：
		// 1. 直接使用前端传入的 specValueId
		// 2. 使用 specId + specValueName 匹配
		// 3. 使用 specName + specValueName 匹配
		if (StrUtil.isNotBlank(item.getSpecValueId())) {
			return preparedSpecData.specValueIdAliasMap.getOrDefault(item.getSpecValueId(), item.getSpecValueId());
		}
		String resolvedSpecId = item.getSpecId();
		if (StrUtil.isBlank(resolvedSpecId) && StrUtil.isNotBlank(item.getSpecName())) {
			resolvedSpecId = preparedSpecData.specNameIdMap.get(item.getSpecName());
		}
		if (StrUtil.isNotBlank(resolvedSpecId)) {
			resolvedSpecId = preparedSpecData.specIdAliasMap.getOrDefault(resolvedSpecId, resolvedSpecId);
		}
		if (StrUtil.isNotBlank(resolvedSpecId) && StrUtil.isNotBlank(item.getSpecValueName())) {
			String valueId = preparedSpecData.specValueNameIdMap.get(composeSpecValueKey(resolvedSpecId, item.getSpecValueName()));
			if (StrUtil.isNotBlank(valueId)) {
				return valueId;
			}
		}
		if (StrUtil.isNotBlank(item.getSpecName()) && StrUtil.isNotBlank(item.getSpecValueName())) {
			return preparedSpecData.specValueNameIdMap.get(composeSpecValueKey(item.getSpecName(), item.getSpecValueName()));
		}
		return null;
	}

	private List<GoodsSpecValue> normalizeSpecValues(SpuSpecDTO spuSpec) {
		// 管理端可能传 values 数组，也可能传逗号分隔字符串。
		// 这里统一转换成 GoodsSpecValue 列表。
		List<GoodsSpecValue> values = new ArrayList<>(safeList(spuSpec.getValues()));
		if (!values.isEmpty()) {
			return values;
		}
		if (StrUtil.isBlank(spuSpec.getValue())) {
			return Collections.emptyList();
		}
		String[] splitValues = spuSpec.getValue().split(",");
		List<GoodsSpecValue> result = new ArrayList<>();
		for (String splitValue : splitValues) {
			if (StrUtil.isBlank(splitValue)) {
				continue;
			}
			GoodsSpecValue specValue = new GoodsSpecValue();
			specValue.setName(splitValue.trim());
			result.add(specValue);
		}
		return result;
	}

	private void clearSkuAndSpecRelations(String spuId) {
		// 本项目里规格关系完全从属于一个 SPU。
		// 因此直接删除后重建，比做局部更新更安全、更容易维护。
		List<GoodsSpuSpec> spuSpecs = goodsSpuSpecService.list(Wrappers.<GoodsSpuSpec>lambdaQuery()
				.eq(GoodsSpuSpec::getSpuId, spuId));
		Set<String> specIds = spuSpecs.stream().map(GoodsSpuSpec::getSpecId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		goodsSkuSpecValueService.remove(Wrappers.<GoodsSkuSpecValue>lambdaQuery()
				.eq(GoodsSkuSpecValue::getSpuId, spuId));
		goodsSpuSpecService.remove(Wrappers.<GoodsSpuSpec>lambdaQuery()
				.eq(GoodsSpuSpec::getSpuId, spuId));
		goodsSkuService.remove(Wrappers.<GoodsSku>lambdaQuery()
				.eq(GoodsSku::getSpuId, spuId));
		if (!specIds.isEmpty()) {
			goodsSpecValueService.remove(Wrappers.<GoodsSpecValue>lambdaQuery()
					.in(GoodsSpecValue::getSpecId, specIds));
			goodsSpecService.removeByIds(specIds);
		}
	}

	private void refreshPriceRange(String spuId, String specType) {
		// SPU 的价格区间由启用的 SKU 销售价汇总而来。
		// 小程序列表页仍然依赖这些聚合字段。
		List<GoodsSku> listGoodsSku = goodsSkuService.list(Wrappers.<GoodsSku>lambdaQuery()
				.eq(GoodsSku::getSpuId, spuId)
				.orderByAsc(GoodsSku::getCreateTime, GoodsSku::getId));
		BigDecimal priceDown = null;
		BigDecimal priceUp = null;
		for (GoodsSku goodsSku : listGoodsSku) {
			if (!CommonConstants.YES.equals(goodsSku.getEnable())) {
				continue;
			}
			if (goodsSku.getSalesPrice() == null) {
				continue;
			}
			if (priceDown == null || goodsSku.getSalesPrice().compareTo(priceDown) < 0) {
				priceDown = goodsSku.getSalesPrice();
			}
			if (priceUp == null || goodsSku.getSalesPrice().compareTo(priceUp) > 0) {
				priceUp = goodsSku.getSalesPrice();
			}
		}
		if (priceDown == null || priceUp == null) {
			GoodsSku firstSku = listGoodsSku.stream().findFirst().orElse(null);
			if (firstSku != null) {
				priceDown = firstSku.getSalesPrice();
				priceUp = firstSku.getSalesPrice();
			}
		}
		GoodsSpu update = new GoodsSpu();
		update.setId(spuId);
		update.setPriceDown(defaultDecimal(priceDown, BigDecimal.ZERO));
		update.setPriceUp(defaultDecimal(priceUp, update.getPriceDown()));
		update.setSpecType(StrUtil.isBlank(specType) ? SPEC_TYPE_SINGLE : specType);
		baseMapper.updateById(update);
	}

	private void fillSkuAndSpecData(GoodsSpu goodsSpu) {
		// 回填管理端编辑页和详情接口需要的临时字段：
		// skus、spuSpec、sku.specs 以及可读规格文案。
		if (goodsSpu == null) {
			return;
		}
		List<GoodsSku> skus = goodsSkuService.list(Wrappers.<GoodsSku>lambdaQuery()
				.eq(GoodsSku::getSpuId, goodsSpu.getId())
				.orderByAsc(GoodsSku::getCreateTime, GoodsSku::getId));
		List<GoodsSpuSpec> spuSpecs = goodsSpuSpecService.list(Wrappers.<GoodsSpuSpec>lambdaQuery()
				.eq(GoodsSpuSpec::getSpuId, goodsSpu.getId())
				.orderByAsc(GoodsSpuSpec::getSort, GoodsSpuSpec::getCreateTime, GoodsSpuSpec::getId));
		List<GoodsSkuSpecValue> skuSpecValues = goodsSkuSpecValueService.list(Wrappers.<GoodsSkuSpecValue>lambdaQuery()
				.eq(GoodsSkuSpecValue::getSpuId, goodsSpu.getId())
				.orderByAsc(GoodsSkuSpecValue::getSort, GoodsSkuSpecValue::getCreateTime, GoodsSkuSpecValue::getId));
		Set<String> specIds = spuSpecs.stream().map(GoodsSpuSpec::getSpecId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		Set<String> specValueIds = skuSpecValues.stream().map(GoodsSkuSpecValue::getSpecValueId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		Map<String, GoodsSpec> specMap = specIds.isEmpty()
				? Collections.emptyMap()
				: goodsSpecService.listByIds(specIds).stream()
				.collect(Collectors.toMap(GoodsSpec::getId, item -> item, (left, right) -> left));
		Map<String, GoodsSpecValue> specValueMap = specValueIds.isEmpty()
				? Collections.emptyMap()
				: goodsSpecValueService.listByIds(specValueIds).stream()
				.collect(Collectors.toMap(GoodsSpecValue::getId, item -> item, (left, right) -> left));
		Map<String, List<GoodsSpecValue>> specIdValueMap = specValueMap.values().stream()
				.filter(item -> StrUtil.isNotBlank(item.getSpecId()))
				.sorted(Comparator.comparing(item -> defaultString(item.getName())))
				.collect(Collectors.groupingBy(GoodsSpecValue::getSpecId, LinkedHashMap::new, Collectors.toList()));
		Map<String, List<GoodsSkuSpecValue>> skuIdSpecMap = skuSpecValues.stream()
				.collect(Collectors.groupingBy(GoodsSkuSpecValue::getSkuId, LinkedHashMap::new, Collectors.toList()));
		List<SpuSpecDTO> spuSpecDTOList = new ArrayList<>();
		for (GoodsSpuSpec spuSpec : spuSpecs) {
			SpuSpecDTO spuSpecDTO = new SpuSpecDTO();
			spuSpecDTO.setId(spuSpec.getSpecId());
			spuSpecDTO.setName(StrUtil.blankToDefault(spuSpec.getSpecName(),
					specMap.containsKey(spuSpec.getSpecId()) ? specMap.get(spuSpec.getSpecId()).getName() : null));
			spuSpecDTO.setSort(spuSpec.getSort());
			spuSpecDTO.setValues(specIdValueMap.getOrDefault(spuSpec.getSpecId(), Collections.emptyList()));
			spuSpecDTOList.add(spuSpecDTO);
		}
		for (GoodsSku goodsSku : skus) {
			List<GoodsSkuSpecValue> currentSpecs = new ArrayList<>(skuIdSpecMap.getOrDefault(goodsSku.getId(), Collections.emptyList()));
			for (GoodsSkuSpecValue item : currentSpecs) {
				GoodsSpecValue specValue = specValueMap.get(item.getSpecValueId());
				if (specValue == null) {
					continue;
				}
				item.setSpecId(specValue.getSpecId());
				item.setSpecValueName(specValue.getName());
				GoodsSpec goodsSpec = specMap.get(specValue.getSpecId());
				item.setSpecName(goodsSpec == null ? null : goodsSpec.getName());
			}
			currentSpecs.sort(Comparator.comparing(item -> item.getSort() == null ? Integer.MAX_VALUE : item.getSort()));
			goodsSku.setSpecs(currentSpecs);
			goodsSku.setName(buildSpecInfo(currentSpecs));
		}
		goodsSpu.setSkus(skus);
		goodsSpu.setSpuSpec(spuSpecDTOList);
		if (goodsSpu.getPriceDown() != null && goodsSpu.getSalesPrice() == null) {
			goodsSpu.setSalesPrice(goodsSpu.getPriceDown());
		}
	}

	private String buildSpecInfo(List<GoodsSkuSpecValue> specs) {
		// 统一生成“颜色:红色; 尺码:M”这种可读规格文案。
		return specs.stream()
				.filter(item -> StrUtil.isNotBlank(item.getSpecValueName()))
				.map(item -> StrUtil.isNotBlank(item.getSpecName())
						? item.getSpecName() + ":" + item.getSpecValueName()
						: item.getSpecValueName())
				.collect(Collectors.joining("; "));
	}

	private <T> List<T> safeList(Collection<T> items) {
		if (items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		return new ArrayList<>(items);
	}

	private BigDecimal defaultDecimal(BigDecimal... values) {
		for (BigDecimal value : values) {
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	private String firstPic(String[] picUrls) {
		if (picUrls == null || picUrls.length == 0) {
			return null;
		}
		return picUrls[0];
	}

	private String composeSpecValueKey(String key, String valueName) {
		return defaultString(key) + "||" + defaultString(valueName);
	}

	private String defaultString(String value) {
		return Objects.toString(value, "");
	}

	private static class PreparedSpecData {
		/** 待入库的 SPU 与规格关系数据。 */
		private final List<GoodsSpuSpec> spuSpecs = new ArrayList<>();
		/** 旧规格 id 到新规格 id 的映射。 */
		private final Map<String, String> specIdAliasMap = new LinkedHashMap<>();
		/** 规格名到规格 id 的映射，兼容只传名称的前端结构。 */
		private final Map<String, String> specNameIdMap = new LinkedHashMap<>();
		/** 旧规格值 id 到新规格值 id 的映射。 */
		private final Map<String, String> specValueIdAliasMap = new LinkedHashMap<>();
		/** 组合键到规格值 id 的映射，便于按名称反查。 */
		private final Map<String, String> specValueNameIdMap = new LinkedHashMap<>();
	}
}
