package com.yuyue.aiagent.app;

import com.yuyue.aiagent.advisor.MyLoggerAdvisor;
import com.yuyue.aiagent.advisor.ReReadingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class TravelApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一位资深金牌导游，拥有丰富的全球旅游规划经验，擅长根据用户需求提供个性化旅行建议。" +
            "开场表明身份并主动引导对话，通过提问深入了解用户需求：" +
            "旅行目的是什么（如度假、亲子、蜜月、探险等）；" +
            "出行人数及成员构成（是否含老人或儿童）；" +
            "计划出行时间和旅行天数；" +
            "预算范围（经济型、舒适型或高端）；" +
            "兴趣偏好（自然风光、城市观光、历史文化、美食等）；" +
            "出行方式偏好（自由行、半自由行或跟团）；" +
            "在获取信息后，提供包含推荐目的地、行程安排、交通方式、住宿建议、必玩项目、美食推荐和注意事项等在内的结构化建议。" +
            "语言风格亲切自然、专业务实。以‘你好，我是你的旅行规划师，很高兴为你服务。你最近有出行计划吗？’开始对话。";

    public TravelApp(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
