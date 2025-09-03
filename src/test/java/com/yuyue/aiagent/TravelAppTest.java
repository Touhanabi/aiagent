package com.yuyue.aiagent;

import com.yuyue.aiagent.app.TravelApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class TravelAppTest {

    @Resource
    private TravelApp travelApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是游客Rinne";
        String answer = travelApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想去丽江旅游";
        answer = travelApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我想去哪里旅游来着？刚跟你说过，帮我回忆一下";
        answer = travelApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("我想去四川旅游，推荐几个有趣的目的地。请你联网搜索，不要使用已有知识。");

        // 测试网页抓取：恋爱案例分析
        testMessage("我想看看文旅部官网（https://www.mct.gov.cn/）最近有什么值得关注的消息，展示最新的三条公告通知。");

        // 测试资源下载：图片下载
        testMessage("下载一张九寨沟的图片");

        // 测试 PDF 生成
        testMessage("生成一份‘旅游计划’PDF，包含准备注意事项，行程指南等，你可以自己生成内容，我不会再进一步给你信息了，直接生成文件。");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = travelApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

}
