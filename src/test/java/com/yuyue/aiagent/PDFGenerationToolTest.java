package com.yuyue.aiagent;

import com.yuyue.aiagent.tools.PDFGenerationTool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "旅游指南.pdf";
        String content = "去旅游吧！";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}