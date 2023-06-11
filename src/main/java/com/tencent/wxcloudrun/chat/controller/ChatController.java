package com.tencent.wxcloudrun.chat.controller;

import com.tencent.wxcloudrun.chat.request.ReceiveMessageRequest;
import com.tencent.wxcloudrun.chat.service.WeChatService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private WeChatService weChatService;

    @RequestMapping(value = "receiveMessage")
    public Object receiveMessage(@RequestBody ReceiveMessageRequest request, HttpServletRequest servletRequest) {
        if (HttpMethod.GET.name().equals(servletRequest.getMethod())) {
            // 验证签名是否有效
          //  if (weChatService.)
        }

        return null;
    }
}
