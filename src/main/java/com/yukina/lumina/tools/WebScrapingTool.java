package com.yukina.lumina.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {


    @Tool(description = "Scrapes the HTML content of a webpage")
    public String scrapWebPage(@ToolParam(description = "URL of the webpage to be scraped") String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (IOException e) {
            return "Error occurred while scraping the webpage: " + e.getMessage();
        }
    }
}