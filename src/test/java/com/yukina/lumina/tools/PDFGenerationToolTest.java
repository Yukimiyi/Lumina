package com.yukina.lumina.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "文件.pdf";
        String content = "1 + 1 = 2";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}