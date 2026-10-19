package com.yukina.lumina.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "测试";
        String result = fileOperationTool.readFile(filename);
        assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "测试";
        String result = fileOperationTool.writeFile(filename, "测试");
        assertNotNull(result);
    }
}