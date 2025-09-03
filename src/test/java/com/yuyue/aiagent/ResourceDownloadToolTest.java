package com.yuyue.aiagent;

import com.yuyue.aiagent.tools.ResourceDownloadTool;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://www.evoote.com/wp-content/uploads/2023/06/20230610090245-64843c351f8e1.jpg";
        String fileName = "resort.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}