package com.demo.mcp.client.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * MCP服务请求模型
 * 用于封装小程序发送给MCP客户端的请求数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class McpRequest {
    
    /**
     * 服务ID
     */
    private String serviceId;
    
    /**
     * 方法名称
     */
    private String methodName;
    
    /**
     * 请求参数
     */
    private Map<String, Object> params;
    
    /**
     * 请求ID，用于追踪请求
     */
    private String requestId;
    
    /**
     * 客户端ID，标识请求来源
     */
    private String clientId;
    
    /**
     * 请求时间戳
     */
    private Long timestamp;
}