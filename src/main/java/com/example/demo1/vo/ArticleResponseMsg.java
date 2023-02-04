package com.example.demo1.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;
import java.util.Map;

@XStreamAlias(value = "xml")
public class ArticleResponseMsg extends BaseResponseMsg{
    private int ArticleCount;
    private List<Article> Articles;
    public ArticleResponseMsg() {
        super();
    }
    public ArticleResponseMsg(Map<?, ?> requestMsgMap) {
        super(requestMsgMap);
        this.MsgType = "news";
    }
    public int getArticleCount() {
        return ArticleCount;
    }
    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }
}
