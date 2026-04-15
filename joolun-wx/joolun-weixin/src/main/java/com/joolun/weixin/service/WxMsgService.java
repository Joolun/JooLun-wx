/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.weixin.entity.WxMsg;
import com.joolun.weixin.entity.WxMsgVO;

import java.util.List;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
public interface WxMsgService extends IService<WxMsg> {

	/**
	 * 获取分组后的消息列表
	 * @param page
	 * @param wxMsgVO
	 * @return
	 */
	IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, WxMsgVO wxMsgVO);
}
