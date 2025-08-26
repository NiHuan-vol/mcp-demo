package com.demo.core.registry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 模型信息类，用于存储AI模型的详细信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelInfo {
    // 模型唯一标识
    private String modelId;
    
    // 模型名称
    private String modelName;
    
    // 模型描述
    private String description;
    
    // 模型类型（如文本、图像等）
    private String modelType;
    
    // 模型提供者
    private String provider;
    
    // 模型版本
    private String version;
    
    // 注册时间
    private LocalDateTime registeredAt;
    
    // 模型配置参数
    private Map<String, Object> configuration;
    
    // 是否启用
    private boolean enabled;
}