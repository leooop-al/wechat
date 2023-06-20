package com.tencent.wxcloudrun.chat.controller;

import com.tencent.wxcloudrun.chat.request.ReceiveMessageRequest;
import com.tencent.wxcloudrun.chat.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class ChatController {

    @Resource
    private WeChatService weChatService;

    @RequestMapping(value = "/chat/receiveMessage")
    public Object receiveMessage(String signature, String timestamp, String nonce, String echoStr,
                                 HttpServletRequest servletRequest) {

        log.info("[ChatController]receiveMessage | start | signature:{} timestamp:{} nonce:{} echoStr:{}",
                signature, timestamp, nonce, echoStr);
        Object result;
        if (HttpMethod.GET.name().equals(servletRequest.getMethod())) {
            // 验证签名是否有效
            if (weChatService.checkSignature(signature, timestamp, nonce)) {
                result = echoStr;
            } else {
                result = "你是谁？你想干嘛";
            }
        } else {
            try {
                result = weChatService.processReceived(servletRequest.getInputStream());
            } catch (Exception e) {
                log.error("获取来自微信的消息异常", e);
                result = StringUtils.EMPTY;
            }
        }

        return result;
    }
}
