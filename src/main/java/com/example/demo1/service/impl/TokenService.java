package com.example.demo1.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo1.service.ITokenService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService implements ITokenService {
    private static final String APP_ID = "wx6d135662b8d67d93";
    private static final String APP_SECRET = "876f80efa6bbdfc935b3f7ab319d4a14";
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi" +
            "-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";

    private String Token;
    private long ExpireTime;

    @Override
    public String getToken() {
        if (null == this.Token || this.isExpired()) {
            // 重新获取token
            getTokenInner();
        }
        return this.Token;
    }


    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() > this.ExpireTime;
    }

    private void getTokenInner() {
        String true_url = TOKEN_URL.
                replace("{APPID}", APP_ID).
                replace("{APPSECRET}", APP_SECRET);
        JSONObject jsonObject = new RestTemplate().getForObject(true_url, JSONObject.class);
        System.out.println("返回：" + jsonObject);
        String token = jsonObject.getString("access_token");
        this.Token = token;
        this.ExpireTime = System.currentTimeMillis() + Long.parseLong(jsonObject.getString("expires_in"));
    }
}
