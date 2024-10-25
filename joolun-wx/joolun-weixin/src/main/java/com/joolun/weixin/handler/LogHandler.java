/*
MIT License

Copyright (c) 2020 www.joolun.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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
 * @author Binary Wang(https://github.com/binarywang)
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
                    String sceneStr = StrUtil.split(wxMessage.getEventKey(), ":")[1];
                    String rs = HttpUtils.sendPost("http://127.0.0.1:8083/joolun-open/user",StrUtil.format("openId={}&sceneStr={}", openId, sceneStr));
                }
            }
        }
        return null;
    }

}
