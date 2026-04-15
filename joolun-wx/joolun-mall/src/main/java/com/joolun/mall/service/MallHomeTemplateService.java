/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.MallHomeTemplate;

import java.util.Map;

/**
 * 商城首页装修模板服务。
 *
 * @author www.joolun.com
 */
public interface MallHomeTemplateService extends IService<MallHomeTemplate> {

	/**
	 * 查询当前模板详情。
	 *
	 * @return 当前模板
	 */
	MallHomeTemplate getCurrentTemplateDetail();

	/**
	 * 保存当前模板。
	 *
	 * @param mallHomeTemplate 模板信息
	 * @return 是否成功
	 */
	boolean saveCurrentTemplate(MallHomeTemplate mallHomeTemplate);

	/**
	 * 查询小程序首页当前模板。
	 * 返回结果会直接给小程序首页渲染使用。
	 *
	 * @return 小程序首页模板数据
	 */
	Map<String, Object> getMaCurrentTemplate();

	/**
	 * 刷新首页装修缓存。
	 */
	void refreshCurrentTemplateCache();
}
