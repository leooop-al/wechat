package com.tencent.wxcloudrun.chat.request;

import lombok.Data;

@Data
public class ReceiveMessageRequest {

    /**
     * 签名
     */
    private String signature;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * ？？
     */
    private String nonce;

    /**
     * 回声
     */
    private String echostr;
}
