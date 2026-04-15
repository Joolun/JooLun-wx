/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.weixin.entity.WxOpenDataDTO;
import com.joolun.weixin.entity.WxPhoneNumberDTO;
import com.joolun.weixin.entity.WxUser;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信用户
 *
 * @author www.joolun.com
 * @date 2019-03-25 15:39:39
 */
public interface WxUserService extends IService<WxUser> {

	/**
	 * 同步微信用户
	 */
	void synchroWxUser() throws WxErrorException;

	/**
	 * 修改用户备注
	 * @param entity
	 * @return
	 */
	boolean updateRemark(WxUser entity) throws WxErrorException;

	/**
	 * 认识标签
	 * @param taggingType
	 * @param tagId
	 * @param openIds
	 * @throws WxErrorException
	 */
	void tagging(String taggingType, Long tagId, String[] openIds) throws WxErrorException;

	/**
	 * 根据openId获取用户
	 * @param openId
	 * @return
	 */
	WxUser getByOpenId(String openId);

	/**
	 * 小程序登录
	 * @param appId
	 * @param jsCode
	 * @return
	 */
	WxUser loginMa(String appId, String jsCode) throws WxErrorException;

	/**
	 * 新增、更新微信用户
	 * @param wxOpenDataDTO
	 * @return
	 */
	WxUser saveOrUptateWxUser(WxOpenDataDTO wxOpenDataDTO);

	/**
	 * 绑定小程序手机号。
	 *
	 * @param wxPhoneNumberDTO 手机号绑定参数
	 * @return 更新后的微信用户
	 */
	WxUser bindPhoneNumber(WxPhoneNumberDTO wxPhoneNumberDTO);
}
