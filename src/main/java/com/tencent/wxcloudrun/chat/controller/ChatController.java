package com.tencent.wxcloudrun.chat.controller;

import com.tencent.wxcloudrun.chat.request.ReceiveMessageRequest;
import com.tencent.wxcloudrun.chat.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private WeChatService weChatService;

    @RequestMapping(value = "receiveMessage")
    public Object receiveMessage(@RequestBody ReceiveMessageRequest request, HttpServletRequest servletRequest) {
        String signature = request.getSignature();
        String timestamp = request.getTimestamp();
        String nonce = request.getNonce();

        Object result;
        if (HttpMethod.GET.name().equals(servletRequest.getMethod())) {
            // 验证签名是否有效
            if (weChatService.checkSignature(signature, timestamp, nonce)) {
                result = request.getEchostr();
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
