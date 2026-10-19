package com.yukina.lumina;

import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
public class LuminaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminaApplication.class, args);
    }

}
