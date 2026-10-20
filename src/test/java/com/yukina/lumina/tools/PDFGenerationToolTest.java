package com.yukina.lumina.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "文件.pdf";
        String content = "Yukina 的 github 主页： https://github.com/Yukimiyi";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}