/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.joolun.mall.entity.FreightTemplat;
import com.joolun.mall.support.FreightCalculator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 运费模板服务。
 *
 * @author www.joolun.com
 */
public interface FreightTemplatService extends IService<FreightTemplat> {

	/**
	 * 查询启用中的运费模板，按排序字段升序返回。
	 *
	 * @return 启用中的模板列表
	 */
	List<FreightTemplat> listEnabled();

	/**
	 * 批量加载运费模板并转换成计价规则。
	 * 已删除或已停用的模板不会出现在结果中，调用方按"无规则即不计运费"处理。
	 *
	 * @param ids 模板 ID 集合
	 * @return 模板 ID -> 计价规则
	 */
	Map<String, FreightCalculator.FreightRule> getRuleMap(Collection<String> ids);
}
