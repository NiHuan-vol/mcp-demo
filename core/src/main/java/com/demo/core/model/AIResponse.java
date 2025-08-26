package com.demo.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.HashMap;

/**
 * AI响应模型类，用于封装各种AI模型的响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    
    // 响应ID
    private String responseId;
    
    // 模型ID
    private String modelId;
    
    // 响应状态：SUCCESS, FAILED, PARTIAL_SUCCESS
    private String status = "SUCCESS";
    
    // 响应数据
    private Object data;
    
    // 错误信息（如果有）
    private String errorMessage;
    
    // 响应元数据
    private Map<String, Object> metadata = new HashMap<>();
    
    // 处理时间（毫秒）
    private long processingTime;
}