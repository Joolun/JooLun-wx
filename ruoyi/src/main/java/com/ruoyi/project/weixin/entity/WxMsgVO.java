/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.ruoyi.project.weixin.entity;

import lombok.Data;

/**
 * 微信消息
 *
 * @author JL
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
