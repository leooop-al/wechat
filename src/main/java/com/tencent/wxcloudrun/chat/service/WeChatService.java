package com.tencent.wxcloudrun.chat.service;

import com.tencent.wxcloudrun.chat.handler.WeChatMessageHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WeChatService {

    @Resource
    private List<WeChatMessageHandler> handlerList;


}
