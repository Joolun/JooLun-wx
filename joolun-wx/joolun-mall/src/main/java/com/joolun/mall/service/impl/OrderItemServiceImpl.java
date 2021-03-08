/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.mapper.OrderItemMapper;
import com.joolun.mall.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * 商城订单详情
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:31:40
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
