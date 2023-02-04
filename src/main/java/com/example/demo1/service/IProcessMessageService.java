package com.example.demo1.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IProcessMessageService {

    /**
     * 将自动返回的消息转化为xml返回
     * @param request
     * @return
     */
    String returnRspXml(HttpServletRequest request) throws IOException;

    void processMessage(HttpServletRequest request, HttpServletResponse response);
}
