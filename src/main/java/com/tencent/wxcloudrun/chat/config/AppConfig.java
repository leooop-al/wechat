package com.tencent.wxcloudrun.chat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ziyou.cxf
 * @version : AppConfig.java, v 0.1 2023年06月12日 22:24 ziyou.cxf Exp $
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 令牌
     */
    public String token;
}
