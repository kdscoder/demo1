package com.example.demo1.vo;

import java.text.ParseException;
import java.util.Map;

/**
 * 回复消息的基类
 */
public abstract class BaseResponseMsg {

    /*定义一些固有属性*/
    // 接收方帐号（收到的OpenID）
    public String ToUserName;
    // 开发者微信号(公众号)
    public String FromUserName;
    // 消息创建时间戳 （整型） 10位
    public String CreateTime;
    //消息类型
    public String MsgType;

    public BaseResponseMsg() {
    }

    public BaseResponseMsg(Map<?, ?>  requestMsgMap) {
        FromUserName = (String) requestMsgMap.get("ToUserName");
        ToUserName = (String) requestMsgMap.get("FromUserName");
        CreateTime = String.valueOf(System.currentTimeMillis() / 1000);
        MsgType = (String) requestMsgMap.get("MsgType");
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }
}
