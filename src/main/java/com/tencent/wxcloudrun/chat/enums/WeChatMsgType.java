package com.tencent.wxcloudrun.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeChatMsgType {

    EVENT("event"),

    TEXT("text");

    private final String value;

    public static WeChatMsgType findByValue(String value) {
        for (WeChatMsgType msgType : WeChatMsgType.values()) {
            if (msgType.getValue().equals(value)) {
                return msgType;
            }
        }
        return null;
    }
}
