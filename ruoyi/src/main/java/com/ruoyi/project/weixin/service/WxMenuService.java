/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.ruoyi.project.weixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.weixin.entity.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;

import java.io.Serializable;

/**
 * 自定义菜单
 *
 * @author JL
 * @date 2019-03-27 16:52:10
 */
public interface WxMenuService extends IService<WxMenu> {

	/***
	 * 获取WxApp下的菜单
	 * @return
	 */
	String getWxMenuButton();

	/**
	 * 保存并发布菜单
	 * @param
	 */
	void saveAndRelease( String strWxMenu) throws WxErrorException;

}
