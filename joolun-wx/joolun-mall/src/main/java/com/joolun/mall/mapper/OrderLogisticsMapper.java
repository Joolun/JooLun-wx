/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joolun.mall.entity.OrderLogistics;

import java.io.Serializable;

/**
 * 订单物流
 *
 * @author www.joolun.com
 * @date 2019-09-16 09:53:17
 */
public interface OrderLogisticsMapper extends BaseMapper<OrderLogistics> {

	OrderLogistics selectById2(Serializable id);
}
