package com.tencent.wxcloudrun.chat.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ziyou.cxf
 * @version : XmlUtil.java, v 0.1 2023年06月12日 22:36 ziyou.cxf Exp $
 */
@Slf4j
public class XmlUtil {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static <T> T xmlToObject(InputStream inputStream, Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("xml格式花异常", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
