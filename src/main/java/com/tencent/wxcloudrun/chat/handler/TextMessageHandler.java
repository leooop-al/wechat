package com.tencent.wxcloudrun.chat.handler;

import com.tencent.wxcloudrun.chat.config.KeywordConfig;
import com.tencent.wxcloudrun.chat.domain.WeChatMessage;
import com.tencent.wxcloudrun.chat.enums.WeChatMsgType;
import com.tencent.wxcloudrun.chat.response.BaseMessage;
import com.tencent.wxcloudrun.chat.response.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class TextMessageHandler implements WeChatMessageHandler {

    @Autowired
    private KeywordConfig keywordConfig;

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
        if (Objects.nonNull(keywordConfig)) {
            message = keywordConfig.getMessageByKeyword(content);
        }

        if (message == null) {
            message = new TextMessage(toUserName, fromUserName, content);
        } else {
            message.setFromUserName(toUserName);
            message.setFromUserName(fromUserName);
            message.setCreateTime(System.currentTimeMillis());
        }

        return message;
    }

}
