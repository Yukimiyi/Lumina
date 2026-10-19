package com.yukina.lumina.tools;

import cn.hutool.core.io.FileUtil;
import com.yukina.lumina.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class FileOperationTool {

    private final String File_DIR = FileConstant.FILE_SAVE_PATH + "/file";

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of the file to read") String filename) {
        String filePath = File_DIR + "/" + filename;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file")
    public String writeFile(@ToolParam(description = "Name of the file to write") String filename,
                            @ToolParam(description = "Content to write to the file") String content) {
        String filePath = File_DIR + "/" + filename;
        try {
            FileUtil.mkdir(File_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully.";
        } catch (Exception e) {
            return "Error writing file: " + e.getMessage();
        }
    }
}
