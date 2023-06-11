package com.tencent.wxcloudrun.chat.handler;

import com.tencent.wxcloudrun.chat.domain.WeChatMessage;
import com.tencent.wxcloudrun.chat.enums.WeChatMsgType;
import com.tencent.wxcloudrun.chat.response.BaseMessage;

/**
 * 处理微信消息接口类
 */
public interface WeChatMessageHandler {

    WeChatMsgType getType();

    BaseMessage processMessage(WeChatMessage weChatMessage);
}
