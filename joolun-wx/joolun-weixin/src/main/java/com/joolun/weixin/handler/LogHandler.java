/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.weixin.handler;

import cn.hutool.core.util.StrUtil;
import com.joolun.common.utils.http.HttpUtils;
import com.joolun.weixin.utils.JsonUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author www.joolun.com
 */
@Component
public class LogHandler extends AbstractHandler {
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        this.logger.info("\n接收到请求消息，内容：{}", JsonUtils.toJson(wxMessage));
        //以下为测试代码，自行删除不影响系统功能
        if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            if(wxMessage.getEvent().equals(WxConsts.EventType.SUBSCRIBE) ||
                    wxMessage.getEvent().equals(WxConsts.EventType.SCAN)){
                if(wxMessage.getEventKey().contains("jl-wiki")){
                    String openId = wxMessage.getFromUser();
                    String sceneStr = StrUtil.split(wxMessage.getEventKey(), ":").get(1);
                    String rs = HttpUtils.sendPost("http://127.0.0.1:8083/joolun-open/user",StrUtil.format("openId={}&sceneStr={}", openId, sceneStr));
                }
            }
        }
        return null;
    }

}
