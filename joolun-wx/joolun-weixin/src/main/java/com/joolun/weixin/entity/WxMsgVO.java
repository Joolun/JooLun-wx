/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.entity;

import lombok.Data;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
@Data
public class WxMsgVO extends WxMsg {
private static final long serialVersionUID = 1L;

    /**
   * 数量
   */
    private Integer countMsg;

	/**
	 * repType not in筛选
	 */
	private String notInRepType;

}
