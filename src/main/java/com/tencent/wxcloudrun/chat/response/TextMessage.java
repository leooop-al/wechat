package com.tencent.wxcloudrun.chat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tencent.wxcloudrun.chat.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextMessage extends BaseMessage {

    @JacksonXmlCData
    @JsonProperty("Content")
    @JacksonXmlProperty(localName = "Content")
    private String content;

    public TextMessage() {
        setMsgType(MessageType.TEXT.getValue());
    }


    public TextMessage(String content) {
        this();
        this.content = content;
    }

    public TextMessage(String fromUserName, String toUserName, String content) {
        this(content);
        setFromUserName(fromUserName);
        setToUserName(toUserName);
        setCreateTime(System.currentTimeMillis());
    }
}
