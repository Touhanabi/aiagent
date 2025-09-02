package com.yuyue.aiagent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 Re2 Advisor
 * 可提高大型语言模型的推理能力
 */
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    private static final String MODIFICATION_MARKER = "Read the question again:";

    private AdvisedRequest before(AdvisedRequest advisedRequest) {
        // 记录原始用户查询
        String originalQuery = advisedRequest.userText();

        // 仅在第一次调用时附加重写文本，避免重复附加
        if (!originalQuery.contains(MODIFICATION_MARKER)) {
            String rewrittenText = originalQuery+ MODIFICATION_MARKER + " " + originalQuery;

            // 打印原始和重写后的查询
            System.out.println("Original Query: " + originalQuery);
            System.out.println("Rewritten Query: " + rewrittenText);

            return AdvisedRequest.from(advisedRequest)
                    .userText(rewrittenText)
                    .build();
        } else {
            // 打印当查询已经被重写过的情况
            System.out.println("Original Query: " + originalQuery);
            System.out.println("Rewritten Query (unchanged): " + originalQuery);

            return advisedRequest;
        }
    }


    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        return chain.nextAroundCall(this.before(advisedRequest));
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        return chain.nextAroundStream(this.before(advisedRequest));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
