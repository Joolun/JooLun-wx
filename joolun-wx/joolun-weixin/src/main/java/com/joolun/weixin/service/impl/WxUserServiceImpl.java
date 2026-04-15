/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.weixin.config.WxMaConfiguration;
import com.joolun.weixin.constant.WxMaConstants;
import com.joolun.weixin.entity.ThirdSession;
import com.joolun.weixin.entity.WxOpenDataDTO;
import com.joolun.weixin.entity.WxPhoneNumberDTO;
import com.joolun.weixin.handler.SubscribeHandler;
import com.joolun.weixin.mapper.WxUserMapper;
import com.joolun.weixin.service.WxUserService;
import com.joolun.weixin.constant.ConfigConstant;
import com.joolun.weixin.entity.WxUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 微信用户
 *
 * @author www.joolun.com
 * @date 2019-03-25 15:39:39
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {

	private final WxMpService wxService;
	private final RedisTemplate redisTemplate;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateRemark(WxUser entity) throws WxErrorException {
		String id = entity.getId();
		String remark = entity.getRemark();
		String openId = entity.getOpenId();
		entity = new WxUser();
		entity.setId(id);
		entity.setRemark(remark);
		super.updateById(entity);
		WxMpUserService wxMpUserService = wxService.getUserService();
		wxMpUserService.userUpdateRemark(openId,remark);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void tagging(String taggingType,Long tagId,String[] openIds) throws WxErrorException {
		WxMpUserTagService wxMpUserTagService = wxService.getUserTagService();
		WxUser wxUser;
		if("tagging".equals(taggingType)){
			for(String openId : openIds){
				wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
						.eq(WxUser::getOpenId,openId));
				Long[] tagidList = wxUser.getTagidList();
				List<Long> list = Arrays.asList(tagidList);
				list = new ArrayList<>(list);
				if(!list.contains(tagId)){
					list.add(tagId);
					tagidList = list.toArray(new Long[list.size()]);
					wxUser.setTagidList(tagidList);
					this.updateById(wxUser);
				}
			}
			wxMpUserTagService.batchTagging(tagId,openIds);
		}
		if("unTagging".equals(taggingType)){
			for(String openId : openIds){
				wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
						.eq(WxUser::getOpenId,openId));
				Long[] tagidList = wxUser.getTagidList();
				List<Long> list = Arrays.asList(tagidList);
				list = new ArrayList<>(list);
				if(list.contains(tagId)){
					list.remove(tagId);
					tagidList = list.toArray(new Long[list.size()]);
					wxUser.setTagidList(tagidList);
					this.updateById(wxUser);
				}
			}
			wxMpUserTagService.batchUntagging(tagId,openIds);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void synchroWxUser() throws WxErrorException {
		//先将已关注的用户取关
		WxUser wxUser = new WxUser();
		wxUser.setSubscribe(ConfigConstant.SUBSCRIBE_TYPE_NO);
		this.baseMapper.update(wxUser, Wrappers.<WxUser>lambdaQuery()
				.eq(WxUser::getSubscribe, ConfigConstant.SUBSCRIBE_TYPE_YES));
		WxMpUserService wxMpUserService = wxService.getUserService();
		this.recursionGet(wxMpUserService,null);
	}

	/**
	 * 递归获取
	 * @param nextOpenid
	 */
	void recursionGet(WxMpUserService wxMpUserService,String nextOpenid) throws WxErrorException {
		WxMpUserList userList = wxMpUserService.userList(nextOpenid);
		List<WxUser> listWxUser = new ArrayList<>();
		List<WxMpUser> listWxMpUser = getWxMpUserList(wxMpUserService,userList.getOpenids());
		listWxMpUser.forEach(wxMpUser -> {
			WxUser wxUser = baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
					.eq(WxUser::getOpenId,wxMpUser.getOpenId()));
			if(wxUser == null){//用户未存在
				wxUser = new WxUser();
				wxUser.setSubscribeNum(1);
			}
			SubscribeHandler.setWxUserValue(wxUser,wxMpUser);
			listWxUser.add(wxUser);
		});
		this.saveOrUpdateBatch(listWxUser);
		if(userList.getCount() >= 10000){
			this.recursionGet(wxMpUserService,userList.getNextOpenid());
		}
	}

	/**
	 * 分批次获取微信粉丝信息 每批100条
	 * @param wxMpUserService
	 * @param openidsList
	 * @return
	 * @throws WxErrorException
	 * @author www.joolun.com
	 */
	private List<WxMpUser> getWxMpUserList(WxMpUserService wxMpUserService, List<String> openidsList) throws WxErrorException {
		// 粉丝openid数量
		int count = openidsList.size();
		if (count <= 0) {
			return new ArrayList<>();
		}
		List<WxMpUser> list = Lists.newArrayList();
		List<WxMpUser> followersInfoList;
		int a = count % 100 > 0 ? count / 100 + 1 : count / 100;
		for (int i = 0; i < a; i++) {
			if (i + 1 < a) {
				log.debug("i:{},from:{},to:{}", i, i * 100, (i + 1) * 100);
				followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, ((i + 1) * 100)));
				if (null != followersInfoList && !followersInfoList.isEmpty()) {
					list.addAll(followersInfoList);
				}
			}
			else {
				log.debug("i:{},from:{},to:{}", i, i * 100, count - i * 100);
				followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, count));
				if (null != followersInfoList && !followersInfoList.isEmpty()) {
					list.addAll(followersInfoList);
				}
			}
		}
		log.debug("本批次获取微信粉丝数：",list.size());
		return list;
	}

	@Override
	public WxUser getByOpenId(String openId){
		return this.baseMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
				.eq(WxUser::getOpenId,openId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WxUser loginMa(String appId, String jsCode) throws WxErrorException {
		WxMaJscode2SessionResult jscode2session = WxMaConfiguration.getMaService(appId).jsCode2SessionInfo(jsCode);
		WxUser wxUser = this.getByOpenId(jscode2session.getOpenid());
		if(wxUser==null) {
			//新增微信用户
			wxUser = new WxUser();
			wxUser.setAppType(ConfigConstant.WX_APP_TYPE_1);
			wxUser.setOpenId(jscode2session.getOpenid());
			wxUser.setSessionKey(jscode2session.getSessionKey());
			wxUser.setUnionId(jscode2session.getUnionid());
			try{
				this.save(wxUser);
			}catch (DuplicateKeyException e){
				if(e.getMessage().contains("uk_appid_openid")){
					wxUser = this.getByOpenId(wxUser.getOpenId());
				}
			}
		}else {
			//更新SessionKey
			wxUser.setAppType(ConfigConstant.WX_APP_TYPE_1);
			wxUser.setOpenId(jscode2session.getOpenid());
			wxUser.setSessionKey(jscode2session.getSessionKey());
			wxUser.setUnionId(jscode2session.getUnionid());
			this.updateById(wxUser);
		}

		String thirdSessionKey = UUID.randomUUID().toString();
		ThirdSession thirdSession = new ThirdSession();
		thirdSession.setAppId(appId);
		thirdSession.setSessionKey(wxUser.getSessionKey());
		thirdSession.setWxUserId(wxUser.getId());
		thirdSession.setOpenId(wxUser.getOpenId());
		//将3rd_session和用户信息存入redis，并设置过期时间
		String key = WxMaConstants.THIRD_SESSION_BEGIN + ":" + thirdSessionKey;
		redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(thirdSession) , WxMaConstants.TIME_OUT_SESSION, TimeUnit.HOURS);
		wxUser.setSessionKey(thirdSessionKey);
		return wxUser;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WxUser saveOrUptateWxUser(WxOpenDataDTO wxOpenDataDTO) {
		WxMaUserService wxMaUserService = WxMaConfiguration.getMaService(wxOpenDataDTO.getAppId()).getUserService();
		WxMaUserInfo wxMaUserInfo = wxMaUserService.getUserInfo(wxOpenDataDTO.getSessionKey(), wxOpenDataDTO.getEncryptedData(), wxOpenDataDTO.getIv());
		WxUser wxUser = new WxUser();
		BeanUtil.copyProperties(wxMaUserInfo,wxUser);
		wxUser.setId(wxOpenDataDTO.getUserId());
		wxUser.setSex(wxMaUserInfo.getGender());
		wxUser.setHeadimgUrl(wxMaUserInfo.getAvatarUrl());
		baseMapper.updateById(wxUser);
		wxUser = baseMapper.selectById(wxUser.getId());
		return wxUser;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WxUser bindPhoneNumber(WxPhoneNumberDTO wxPhoneNumberDTO) {
		WxMaUserService wxMaUserService = WxMaConfiguration.getMaService(wxPhoneNumberDTO.getAppId()).getUserService();
		WxMaPhoneNumberInfo phoneNumberInfo = resolvePhoneNumberInfo(wxPhoneNumberDTO, wxMaUserService);
		if (phoneNumberInfo == null || StrUtil.isBlank(phoneNumberInfo.getPhoneNumber())) {
			throw new IllegalArgumentException("获取手机号失败");
		}
		WxUser wxUser = new WxUser();
		wxUser.setId(wxPhoneNumberDTO.getUserId());
		wxUser.setPhone(phoneNumberInfo.getPhoneNumber());
		baseMapper.updateById(wxUser);
		return baseMapper.selectById(wxPhoneNumberDTO.getUserId());
	}

	/**
	 * 解析微信手机号信息。
	 * 新版基础库优先使用 code 换取手机号，旧版设备仍兼容 encryptedData + iv 解密方式。
	 *
	 * @param wxPhoneNumberDTO 手机号绑定参数
	 * @param wxMaUserService 小程序用户服务
	 * @return 手机号信息
	 */
	private WxMaPhoneNumberInfo resolvePhoneNumberInfo(WxPhoneNumberDTO wxPhoneNumberDTO, WxMaUserService wxMaUserService) {
		if (StrUtil.isNotBlank(wxPhoneNumberDTO.getCode())) {
			return getPhoneByCode(wxPhoneNumberDTO, wxMaUserService);
		}
		if (StrUtil.isNotBlank(wxPhoneNumberDTO.getEncryptedData()) && StrUtil.isNotBlank(wxPhoneNumberDTO.getIv())) {
			return wxMaUserService.getPhoneNoInfo(wxPhoneNumberDTO.getSessionKey(), wxPhoneNumberDTO.getEncryptedData(), wxPhoneNumberDTO.getIv());
		}
		throw new IllegalArgumentException("手机号授权参数不完整");
	}

	/**
	 * 通过新版手机号授权 code 获取手机号。
	 *
	 * @param wxPhoneNumberDTO 手机号绑定参数
	 * @param wxMaUserService 小程序用户服务
	 * @return 手机号信息
	 */
	private WxMaPhoneNumberInfo getPhoneByCode(WxPhoneNumberDTO wxPhoneNumberDTO, WxMaUserService wxMaUserService) {
		try {
			return wxMaUserService.getPhoneNumber(wxPhoneNumberDTO.getCode());
		} catch (WxErrorException ex) {
			if (StrUtil.isNotBlank(wxPhoneNumberDTO.getEncryptedData()) && StrUtil.isNotBlank(wxPhoneNumberDTO.getIv())) {
				return wxMaUserService.getPhoneNoInfo(wxPhoneNumberDTO.getSessionKey(), wxPhoneNumberDTO.getEncryptedData(), wxPhoneNumberDTO.getIv());
			}
			throw new IllegalStateException("获取手机号失败", ex);
		}
	}
}
