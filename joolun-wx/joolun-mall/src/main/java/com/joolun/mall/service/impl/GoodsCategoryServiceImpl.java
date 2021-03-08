/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.GoodsCategory;
import com.joolun.mall.entity.GoodsCategoryTree;
import com.joolun.mall.mapper.GoodsCategoryMapper;
import com.joolun.mall.service.GoodsCategoryService;
import com.joolun.mall.util.TreeUtil;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品类目
 *
 * @author www.joolun.com
 * @date 2019-08-12 11:46:28
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {

	@Override
	public List<GoodsCategoryTree> selectTree(GoodsCategory goodsCategory) {
		return getTree(this.list(Wrappers.lambdaQuery(goodsCategory)));
	}

	/**
	 * 构建树
	 *
	 * @param entitys
	 * @return
	 */
	private List<GoodsCategoryTree> getTree(List<GoodsCategory> entitys) {
		List<GoodsCategoryTree> treeList = entitys.stream()
				.filter(entity -> !entity.getId().equals(entity.getParentId()))
				.sorted(Comparator.comparingInt(GoodsCategory::getSort))
				.map(entity -> {
					GoodsCategoryTree node = new GoodsCategoryTree();
					BeanUtil.copyProperties(entity,node);
					return node;
				}).collect(Collectors.toList());
		return TreeUtil.build(treeList, CommonConstants.PARENT_ID);
	}

	@Override
	public boolean removeById(Serializable id) {
		super.removeById(id);
		remove(Wrappers.<GoodsCategory>query()
				.lambda().eq(GoodsCategory::getParentId, id));
		return true;
	}
}
