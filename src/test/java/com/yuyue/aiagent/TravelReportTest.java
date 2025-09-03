package com.yuyue.aiagent;


import com.yuyue.aiagent.app.TravelApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
@SpringBootTest
public class TravelReportTest {
    @Resource
    private TravelApp travelApp;
    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是游客Rinne，我想去九寨沟旅游。";
        TravelApp.TravelReport travelReport = travelApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(travelReport);
    }
}