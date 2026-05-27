package com.yukina.lumina.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class HttpAiInvoke {

    public static void main(String[] args) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 从环境变量中读取 API Key
        String apiKey = TestApiKey.API_KEY;

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("环境变量 DASHSCOPE_API_KEY 未设置");
        }

        // 构造 messages
        JSONArray messages = new JSONArray();

        messages.add(new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));

        messages.add(new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？"));

        // 构造 input
        JSONObject input = new JSONObject()
                .set("messages", messages);

        // 构造 parameters
        JSONObject parameters = new JSONObject()
                .set("result_format", "message");

        // 构造完整请求体
        JSONObject body = new JSONObject()
                .set("model", "qwen-plus")
                .set("input", input)
                .set("parameters", parameters);

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .execute();

        int httpCode = response.getStatus();
        String result = response.body();

        if (httpCode == 200) {
            System.out.println("Response: " + result);
        } else {
            System.out.println("Error: " + httpCode + " - " + result);
        }
    }
}