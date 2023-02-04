package com.example.demo1.service;

public interface ITokenService {
    // 获取token
    public String getToken();

    // 判断token是否过期
    public boolean isExpired();
}
