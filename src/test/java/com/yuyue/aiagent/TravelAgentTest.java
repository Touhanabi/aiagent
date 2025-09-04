package com.yuyue.aiagent;

import com.yuyue.aiagent.agent.TravelAgent;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TravelAgentTest {
    @Resource
    private TravelAgent travelAgent;

    @Test
    void run(){
        String userPrompt = """
                我想去成都的武侯祠旅游，顺便打卡附近的好吃的餐馆。
                请你帮我找到武侯祠附近10公里的餐馆和其他景点，
                并搜索一些网络图片用于展示，
                制定一份详细的一日游计划，包括参观日程安排和午餐晚餐地点，并以PDF格式输出。
                注意，参观景点必须包括武侯祠。
                """;
        String answer = travelAgent.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}
