/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import java.util.Map;

/**
 * 商城数据看板服务接口。
 * 对外统一输出后台经营分析所需的数据结构，控制器无需关心底层统计细节。
 *
 * @author www.joolun.com
 */
public interface MallDashboardService {

	/**
	 * 查询商城数据看板。
	 * dayRange 用于控制趋势图的时间跨度，默认按近 15 天输出。
	 *
	 * @param dayRange 趋势统计天数
	 * @return 看板数据
	 */
	Map<String, Object> getDashboardData(Integer dayRange);
}
