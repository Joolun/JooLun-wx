/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.MallUserTag;

import java.util.List;

/**
 * 商城用户标签库服务接口。
 *
 * @author www.joolun.com
 */
public interface MallUserTagService extends IService<MallUserTag> {

	/**
	 * 查询启用状态的标签选项。
	 *
	 * @return 标签选项
	 */
	List<MallUserTag> listActiveTags();

	/**
	 * 查询标签库列表。
	 * 返回时会补齐每个标签的使用人数，方便后台运营维护。
	 *
	 * @return 标签库列表
	 */
	List<MallUserTag> listTagLibrary();

	/**
	 * 保存或补齐标签库。
	 * 用于后台维护标签，以及用户打标签时自动沉淀新标签。
	 *
	 * @param tagNames 标签名称集合
	 */
	void saveIfAbsent(List<String> tagNames);
}
