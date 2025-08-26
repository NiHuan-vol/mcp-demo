package com.demo.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;
import java.util.HashMap;

/**
 * AI请求模型类，用于封装各种AI模型的请求数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequest {
    
    // 模型ID
    private String modelId;
    
    // 请求参数
    private Map<String, Object> parameters = new HashMap<>();
    
    // 请求数据
    private Object data;
    
    // 租户ID
    private String tenantId;
    
    // 模块ID
    private String moduleId;
    
    // 请求上下文
    private Map<String, Object> context = new HashMap<>();
}