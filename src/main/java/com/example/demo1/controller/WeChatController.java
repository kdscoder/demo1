package com.example.demo1.controller;

import com.example.demo1.service.ICheckService;
import com.example.demo1.service.IProcessMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信公众号控制器进行交易的命中
 */
@RestController
public class WeChatController {
    @Autowired
    private ICheckService checkService;
    @Autowired
    private IProcessMessageService processMessageService;
    /**
     * 用于进行接入
     * @param request
     * @return
     */
    @GetMapping("/CheckSignature")
    private String checkTheSignature(HttpServletRequest request) {
        // 这里相当于是action的位置了，可以写一些业务代码
        if (checkService.checkTheSignature(request)) {
            System.out.println("接入成功！");
            return request.getParameter("echostr");
        } else {
            System.out.println("接入失败！");
        }
        return "config fail";
    }

    @PostMapping("/CheckSignature")
    private void processMessage(HttpServletRequest request, HttpServletResponse response) {
        // 进行消息的回复
        processMessageService.processMessage(request, response);
    }

}
