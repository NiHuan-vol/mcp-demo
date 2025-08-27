package com.demo.recommendation.mcp.assistant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.HashMap;

/**
 * 助手请求模型，用于封装用户向旅行助手发送的请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssistantRequest {
    // 请求ID
    private String requestId;
    
    // 用户ID
    private String userId;
    
    // 请求类型
    private String requestType;
    
    // 请求内容
    private String content;
    
    // 请求参数
    private Map<String, Object> parameters = new HashMap<>();
    
    // 上下文信息
    private Map<String, Object> context = new HashMap<>();
    
    // 请求时间
    private long timestamp;
    
    // 用户偏好
    private Map<String, Object> userPreferences = new HashMap<>();
}