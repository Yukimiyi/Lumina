package com.yukina.lumina.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class WebScrapingToolTest {

    @Test
    void scrapWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.baidu.com";
        String result = tool.scrapWebPage(url);
        assertNotNull(result);
    }
}