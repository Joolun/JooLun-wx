/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 订单相关枚举
 *
 * @author www.joolun.com
 */
public enum OrderInfoEnum implements IEnum<String> {

	STATUS_0("0","待付款"),
	STATUS_1("1","待发货"),
	STATUS_2("2","待收货"),
	STATUS_3("3","已完成"),
	STATUS_5("5","已取消");

	OrderInfoEnum(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}
	public static String STATUS_PREFIX = "STATUS";
	private String value;
	private String desc;

	@Override
	public String getValue() {
		return this.value;
	}

	@JsonValue
	public String getDesc(){
		return this.desc;
	}
}
