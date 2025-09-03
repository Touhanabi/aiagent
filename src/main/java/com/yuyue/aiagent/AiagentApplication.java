package com.yuyue.aiagent;

import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiagentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiagentApplication.class, args);
    }

}
