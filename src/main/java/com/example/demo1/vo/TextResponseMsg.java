package com.example.demo1.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias(value = "xml")
public class TextResponseMsg extends BaseResponseMsg{

    // 私有的属性
    // 回复的消息内容（换行：在 content 中能够换行，微信客户端就支持换行显示）
    private String Content;
    public TextResponseMsg() {
        super();
    }
    public TextResponseMsg(Map<?, ?> requestMsgMap) {
        super(requestMsgMap);
        this.Content = "asd";
    }
    public String getContent() {

        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
        return "TextResponseMsg{" +
                "Content='" + Content + '\'' +
                ", ToUserName='" + ToUserName + '\'' +
                ", FromUserName='" + FromUserName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", MsgType='" + MsgType + '\'' +
                '}';
    }
}
