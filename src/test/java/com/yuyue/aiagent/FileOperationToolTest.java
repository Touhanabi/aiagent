package com.yuyue.aiagent;
import com.yuyue.aiagent.tools.FileOperationTool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FileOperationToolTest {

    @Test
    public void testReadFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "旅行指南.txt";
        String result = tool.readFile(fileName);
        assertNotNull(result);
    }

    @Test
    public void testWriteFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "旅行指南.txt";
        String content = "https://cn.bing.com 用搜索引擎查询心仪的目的地吧";
        String result = tool.writeFile(fileName, content);
        assertNotNull(result);
    }
}
