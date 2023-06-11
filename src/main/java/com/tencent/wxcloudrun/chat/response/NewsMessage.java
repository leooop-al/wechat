package com.tencent.wxcloudrun.chat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tencent.wxcloudrun.chat.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 响应消息 -> 图文消息
 */
@Getter
@Setter

public class NewsMessage extends BaseMessage {

    /**
     * 文章数量，限制为10条以内
     */
    @JsonProperty("ArticleCount")
    @JacksonXmlProperty(localName = "ArticleCount")
    private int articleCount;

    /**
     * 文章列表默认第一个item为大图
     */
    @JsonProperty("Articles")
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(localName = "Articles")
    private List<Article> articles;

    public NewsMessage() {
        setMsgType(MessageType.NEWS.getValue());
    }

    public NewsMessage(List<Article> articles) {
        this();
        this.articles = articles;
        this.articleCount = articles.size();
    }
}
