/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.support;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.GoodsSku;
import com.joolun.mall.entity.GoodsSkuSpecValue;
import com.joolun.mall.entity.GoodsSpec;
import com.joolun.mall.entity.GoodsSpecValue;
import com.joolun.mall.service.GoodsSkuService;
import com.joolun.mall.service.GoodsSkuSpecValueService;
import com.joolun.mall.service.GoodsSpecService;
import com.joolun.mall.service.GoodsSpecValueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
/**
 * SKU 公共辅助类，供购物车、订单、商品详情等流程复用。
 *
 * 统一处理：
 * 1. SKU 解析规则。
 * 2. 可读规格文案生成。
 * 3. sku / spec / specValue 之间的基础关系回填。
 *
 * @author www.joolun.com
 */
public class MallGoodsSkuSupport {

	private final GoodsSkuService goodsSkuService;
	private final GoodsSkuSpecValueService goodsSkuSpecValueService;
	private final GoodsSpecValueService goodsSpecValueService;
	private final GoodsSpecService goodsSpecService;

	public GoodsSku resolveSku(String spuId, String skuId, boolean enabledOnly) {
		// 优先使用明确传入的 skuId。
		// 如果没传 skuId，则回退到该 SPU 下的第一条 SKU。
		GoodsSku goodsSku;
		if (StrUtil.isNotBlank(skuId)) {
			goodsSku = goodsSkuService.getById(skuId);
			if (goodsSku == null) {
				return null;
			}
			if (StrUtil.isNotBlank(spuId) && !spuId.equals(goodsSku.getSpuId())) {
				return null;
			}
			if (enabledOnly && !CommonConstants.YES.equals(goodsSku.getEnable())) {
				return null;
			}
			return goodsSku;
		}
		if (StrUtil.isBlank(spuId)) {
			return null;
		}
		List<GoodsSku> list = goodsSkuService.list(Wrappers.<GoodsSku>lambdaQuery()
				.eq(GoodsSku::getSpuId, spuId)
				.eq(enabledOnly, GoodsSku::getEnable, CommonConstants.YES)
				.orderByAsc(GoodsSku::getCreateTime, GoodsSku::getId));
		return list.stream().findFirst().orElse(null);
	}

	public String buildSpecInfo(String skuId) {
		Map<String, String> map = buildSpecInfoMap(Collections.singletonList(skuId));
		return map.get(skuId);
	}

	public Map<String, String> buildSpecInfoMap(Collection<String> skuIds) {
		// 批量模式用于订单页和购物车页，避免一条条 SKU 重复查规格字典。
		Set<String> validSkuIds = skuIds == null ? Collections.emptySet() : skuIds.stream()
				.filter(StrUtil::isNotBlank)
				.collect(Collectors.toSet());
		if (validSkuIds.isEmpty()) {
			return Collections.emptyMap();
		}
		List<GoodsSkuSpecValue> relations = goodsSkuSpecValueService.list(Wrappers.<GoodsSkuSpecValue>lambdaQuery()
				.in(GoodsSkuSpecValue::getSkuId, validSkuIds)
				.orderByAsc(GoodsSkuSpecValue::getSort, GoodsSkuSpecValue::getCreateTime, GoodsSkuSpecValue::getId));
		if (relations.isEmpty()) {
			return Collections.emptyMap();
		}
		Set<String> specValueIds = relations.stream().map(GoodsSkuSpecValue::getSpecValueId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		Map<String, GoodsSpecValue> specValueMap = goodsSpecValueService.listByIds(specValueIds).stream()
				.collect(Collectors.toMap(GoodsSpecValue::getId, item -> item, (left, right) -> left));
		Set<String> specIds = specValueMap.values().stream().map(GoodsSpecValue::getSpecId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
		Map<String, GoodsSpec> specMap = goodsSpecService.listByIds(specIds).stream()
				.collect(Collectors.toMap(GoodsSpec::getId, item -> item, (left, right) -> left));
		Map<String, List<GoodsSkuSpecValue>> grouped = relations.stream()
				// 把规格名和规格值名补回到关系对象上，后续调用方可以直接拼展示文案。
				.peek(item -> {
					GoodsSpecValue specValue = specValueMap.get(item.getSpecValueId());
					if (specValue != null) {
						item.setSpecId(specValue.getSpecId());
						item.setSpecValueName(specValue.getName());
						GoodsSpec goodsSpec = specMap.get(specValue.getSpecId());
						item.setSpecName(goodsSpec == null ? null : goodsSpec.getName());
					}
				})
				.collect(Collectors.groupingBy(GoodsSkuSpecValue::getSkuId, LinkedHashMap::new, Collectors.toList()));
		Map<String, String> result = new LinkedHashMap<>();
		grouped.forEach((key, value) -> {
			value.sort(Comparator.comparing(item -> item.getSort() == null ? Integer.MAX_VALUE : item.getSort()));
			String specInfo = value.stream()
					.filter(item -> StrUtil.isNotBlank(item.getSpecValueName()))
					.map(item -> StrUtil.isNotBlank(item.getSpecName())
							? item.getSpecName() + ":" + item.getSpecValueName()
							: item.getSpecValueName())
					.collect(Collectors.joining("; "));
			result.put(key, specInfo);
		});
		return result;
	}
}
