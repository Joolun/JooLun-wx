/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.weixin.mapper.WxMsgMapper;
import com.joolun.weixin.service.WxMsgService;
import com.joolun.weixin.entity.WxMsg;
import com.joolun.weixin.entity.WxMsgVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
@Service
public class WxMsgServiceImpl extends ServiceImpl<WxMsgMapper, WxMsg> implements WxMsgService {

	@Override
	public IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, WxMsgVO wxMsgVO) {
		return baseMapper.listWxMsgMapGroup(page, wxMsgVO);
	}
}
