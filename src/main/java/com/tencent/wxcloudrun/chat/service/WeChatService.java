package com.tencent.wxcloudrun.chat.service;

import ch.qos.logback.core.joran.spi.XMLUtil;
import com.tencent.wxcloudrun.chat.config.AppConfig;
import com.tencent.wxcloudrun.chat.domain.WeChatMessage;
import com.tencent.wxcloudrun.chat.enums.WeChatMsgType;
import com.tencent.wxcloudrun.chat.handler.WeChatMessageHandler;
import com.tencent.wxcloudrun.chat.response.BaseMessage;
import com.tencent.wxcloudrun.chat.response.TextMessage;
import com.tencent.wxcloudrun.chat.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeChatService {

    @Resource
    private AppConfig appConfig;

    @Resource
    private List<WeChatMessageHandler> handlerList;

    private Map<WeChatMsgType, WeChatMessageHandler> messageHandlerMap;

    @PostConstruct
    public void init() {
        messageHandlerMap = handlerList.stream().collect(Collectors.toMap(WeChatMessageHandler::getType, Function.identity()));
    }

    public boolean checkSignature(String signature, String timestamp, String nonce) {
        if (signature == null || timestamp == null || nonce == null) {
            return false;
        }

        String[] arr = new String[]{appConfig.getToken(), timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (String str : arr) {
            content.append(str);
        }

        String tmpStr;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes(StandardCharsets.UTF_8));
            tmpStr = byteToHex(digest);
            return tmpStr.equals(signature);
        } catch (Exception e) {
            log.error("校验签名异常", e);
        }
        return false;
    }

    public BaseMessage processReceived(InputStream inputStream) {
        BaseMessage resultMessage;
        WeChatMessage weChatMessage = XmlUtil.xmlToObject(inputStream, WeChatMessage.class);
        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        try {
            WeChatMsgType msgType = WeChatMsgType.findByValue(weChatMessage.getMsgType());
            WeChatMessageHandler handler = messageHandlerMap.get(msgType);
            resultMessage = handler.processMessage(weChatMessage);
        } catch (Exception e) {
            log.error("处理来至微信服务器的消息出现错误", e);
            resultMessage = new TextMessage(toUserName, fromUserName, "我不明白您的问题！");
        }
        return resultMessage;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
