/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.weixin.constant.ConfigConstant;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.mapper.WxUserMapper;
import com.joolun.weixin.service.WxMsgService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author www.joolun.com
 */
@Slf4j
@Component
@AllArgsConstructor
public class UnsubscribeHandler extends AbstractHandler {

    private final WxMsgService wxMsgService;
    private final WxUserMapper wxUserMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        log.info("取消关注用户 OPENID: " + openId);
        WxUser wxUser = wxUserMapper.selectOne(Wrappers.<WxUser>lambdaQuery()
                .eq(WxUser::getOpenId,openId));
        if(wxUser!=null){
            wxUser.setSubscribe(ConfigConstant.SUBSCRIBE_TYPE_NO);
            wxUser.setCancelSubscribeTime(LocalDateTime.now());
            wxUserMapper.updateById(wxUser);
            //消息记录
            MsgHandler.getWxMpXmlOutMessage(wxMessage,null,wxUser,wxMsgService);
        }
        return null;
    }

}
