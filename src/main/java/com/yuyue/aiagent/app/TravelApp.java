package com.yuyue.aiagent.app;

import com.yuyue.aiagent.advisor.MyLoggerAdvisor;
import com.yuyue.aiagent.chatmemory.FileBasedChatMemory;
import com.yuyue.aiagent.rag.QueryRewriter;
import com.yuyue.aiagent.rag.TravelAppRagCustomAdvisorFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class TravelApp {
    @Resource
    @Qualifier("travelAppVectorStore")
    private VectorStore travelAppVectorStore;
    @Resource
    private Advisor travelAppRagCloudAdvisor;
    @Resource
    private QueryRewriter queryRewriter;

    @Resource
    private VectorStore pgVectorVectorStore;


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
            "语言风格亲切自然、专业务实。" +
            "有可能要求你结合知识库内容进行回答。如果发现用户提问的问题有旅行团推荐，给出旅游团链接。";

    public TravelApp(ChatModel dashscopeChatModel) {
//        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
//        chatClient = ChatClient.builder(dashscopeChatModel)
//                .defaultSystem(SYSTEM_PROMPT)
//                .defaultAdvisors(
//                        new MessageChatMemoryAdvisor(chatMemory),
//                        new MyLoggerAdvisor()
//                )
//                .build();
        //基于文件读写的自定义chatMemory
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
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

    public record TravelReport(String title, List<String> suggestions) {
    }

    public TravelReport doChatWithReport(String message, String chatId) {
        TravelReport travelReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成旅游建议报告，标题为{用户名}的旅游建议报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(TravelReport.class);
        log.info("travelReport: {}", travelReport);
        return travelReport;
    }

    public String doChatWithRag(String message, String chatId) {
        log.info("original query: {}", message);

        // 查询重写
        String rewrittenMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(rewrittenMessage)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
                //.advisors(new QuestionAnswerAdvisor(travelAppVectorStore))
                // 应用云知识库
                //.advisors(travelAppRagCloudAdvisor)
                // 应用pg向量数据库
                //.advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义RAG检索增强
                .advisors(TravelAppRagCustomAdvisorFactory.createTravelAppRagCustomAdvisor(
                                travelAppVectorStore, "国内"
                        )
                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
