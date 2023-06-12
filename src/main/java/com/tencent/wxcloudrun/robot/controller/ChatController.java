package com.tencent.wxcloudrun.robot.controller;

import com.tencent.wxcloudrun.robot.request.ChatRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * @author ziyou.cxf
 * @version : ChatController.java, v 0.1 2023年06月08日 9:06 ziyou.cxf Exp $
 */
@RequestMapping("weChat")
public class ChatController {

//    @Resource
//    private

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    public Object receiveMessage(@RequestBody ChatRequest request) {


        return
    }
}
