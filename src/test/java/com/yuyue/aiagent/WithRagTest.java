package com.yuyue.aiagent;

import com.yuyue.aiagent.app.TravelApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class WithRagTest {
    @Resource
    private TravelApp travelApp;
    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "亲近自然的旅游地点";
        String answer =  travelApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}
