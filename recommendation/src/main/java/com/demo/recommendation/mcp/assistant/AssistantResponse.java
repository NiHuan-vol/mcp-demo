package com.demo.recommendation.mcp.assistant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.HashMap;

/**
 * 助手响应模型，用于封装旅行助手返回的响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssistantResponse {
    // 响应ID
    private String responseId;
    
    // 对应的请求ID
    private String requestId;
    
    // 响应状态：SUCCESS, FAILED, PARTIAL_SUCCESS
    private String status;
    
    // 响应内容
    private String content;
    
    // 响应数据
    private Object data;
    
    // 错误信息（如果有）
    private String errorMessage;
    
    // 响应元数据
    private Map<String, Object> metadata = new HashMap<>();
    
    // 处理时间（毫秒）
    private long processingTime;
    
    // 建议的下一步操作
    private Map<String, Object> suggestedActions = new HashMap<>();
}