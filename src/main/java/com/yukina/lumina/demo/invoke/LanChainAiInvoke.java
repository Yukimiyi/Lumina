package com.yukina.lumina.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LanChainAiInvoke {

    public static void main(String[] args) {
        QwenChatModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat("你是谁");
        System.out.println(answer);
    }
}
