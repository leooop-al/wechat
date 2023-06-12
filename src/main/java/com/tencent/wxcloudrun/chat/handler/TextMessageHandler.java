package com.tencent.wxcloudrun.chat.handler;

import com.tencent.wxcloudrun.chat.domain.WeChatMessage;
import com.tencent.wxcloudrun.chat.enums.WeChatMsgType;
import com.tencent.wxcloudrun.chat.response.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TextMessageHandler implements WeChatMessageHandler {

    @Override
    public WeChatMsgType getType() {
        return WeChatMsgType.TEXT;
    }

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {
        log.info("收到用户文本信息:{}", weChatMessage);

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();

        BaseMessage message = null;


        return message;
    }

}
