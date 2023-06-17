package com.tencent.wxcloudrun.chat.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.tencent.wxcloudrun.chat.enums.MessageType;
import com.tencent.wxcloudrun.chat.response.BaseMessage;
import com.tencent.wxcloudrun.chat.response.NewsMessage;
import com.tencent.wxcloudrun.chat.response.TextMessage;
import com.tencent.wxcloudrun.chat.util.HttpUtil;
import com.tencent.wxcloudrun.chat.util.JsonUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConfigurationProperties("keyword.location")
@ConditionalOnProperty(prefix = "keyword.location", name = "version")
public class KeywordConfig implements ApplicationContextAware, DisposableBean {

    /**
     * 当前配置的版本
     */
    private static String currentConfigVersion;

    /**
     * 关键字回复内容配置
     */
    private static Map<String, JsonNode> keywordMessageMap = Maps.newHashMap();

    private ApplicationContext applicationContext;

    @Setter
    private String version;

    @Setter
    private String message;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("reload-keyword-task");
        return thread;
    });

    @Override
    public void destroy() throws Exception {
        scheduledExecutorService.shutdown();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void startReloadThread() {
        scheduledExecutorService
                .scheduleAtFixedRate(new ReloadKeyWorkTask(applicationContext, version, message),
                        0, 1, TimeUnit.MINUTES);
    }

    public BaseMessage getMessageByKeyword(String keyword) {
        JsonNode messageJsonNode = keywordMessageMap.get(keyword);
        if (messageJsonNode == null) {
            return null;
        }

        String type = messageJsonNode.get("type").asText();
        BaseMessage baseMessage = null;
        if (MessageType.TEXT.getValue().equals(type)) {
            baseMessage = JSONObject.parseObject(messageJsonNode.toString(), TextMessage.class);
        } else if (MessageType.NEWS.getValue().equals(type)) {
            baseMessage = JSONObject.parseObject(messageJsonNode.toString(), NewsMessage.class);
        }

        return baseMessage;
    }

    private static class ReloadKeyWorkTask extends TimerTask {

        private final ApplicationContext applicationContext;

        private final String versionLocation;

        private final String messageLocation;

        public ReloadKeyWorkTask(ApplicationContext applicationContext, String versionLocation, String messageLocation) {
            this.applicationContext = applicationContext;
            this.versionLocation = versionLocation;
            this.messageLocation = messageLocation;
        }

        @Override
        public void run() {
            if (versionLocation.startsWith("classpath:")) {
                reloadByResource(applicationContext, versionLocation, messageLocation);
            } else if (versionLocation.startsWith("http")) {
                reloadByHttp(versionLocation, messageLocation);
            }
        }

        private static void reloadByResource(ApplicationContext applicationContext, String versionLocation, String messageLocation) {
            Resource versionResource = applicationContext.getResource(versionLocation);
            String newVersion = null;
            try (InputStream inputStream = versionResource.getInputStream()) {
                newVersion = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                if (Objects.equals(currentConfigVersion, newVersion)) {
                    return;
                }
            } catch (Exception e) {
                log.error("检查关键字版本异常", e);
            }

            Resource messageResource = applicationContext.getResource(messageLocation);
            try (InputStream inputStream = messageResource.getInputStream()) {
                String messageConfigStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                replaceKeywordMessageMap(newVersion, messageConfigStr);
            } catch (Exception e) {
                log.error("检查关键字版本异常", e);
            }
        }

        private static void reloadByHttp(String versionLocation, String messageLocation) {
            try {
                String newVersion = HttpUtil.get(versionLocation);

                if (Objects.equals(currentConfigVersion, newVersion)) {
                    return;
                }

                String messageConfigStr = HttpUtil.get(messageLocation);
                replaceKeywordMessageMap(newVersion, messageConfigStr);
            } catch (Exception e) {
                log.error("根据http请求获取关键字配置信息异常", e);
            }
        }

        private static void replaceKeywordMessageMap(String newVersion, String messageConfigStr) {
            Map<String, JsonNode> newKeywordMessageMap = Maps.newHashMap();
            JsonNode messageConfigJson = JsonUtil.jsonToObj(messageConfigStr, JsonNode.class);
            Iterator<Map.Entry<String, JsonNode>> iterator = messageConfigJson.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> next = iterator.next();
                String keyword = next.getKey();
                JsonNode messageJsonNode = next.getValue();
                newKeywordMessageMap.put(keyword, messageJsonNode);
            }

            currentConfigVersion = newVersion;
            keywordMessageMap = newKeywordMessageMap;
        }
    }

}
