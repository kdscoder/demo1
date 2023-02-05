package com.example.demo1.controller;

import com.example.demo1.service.impl.IMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
public class StaticScheduleTask {

    @Autowired
    IMessagePushService messagePushService;

    @Scheduled(cron = "0 0 12 * * ?")
    private void configureTasks() {
        System.out.println("执行静态定时任务时间：" + LocalDateTime.now());
        // todo 每日中午12点进行消息推送
        this.messagePushService.pushMessage();
    }
}
