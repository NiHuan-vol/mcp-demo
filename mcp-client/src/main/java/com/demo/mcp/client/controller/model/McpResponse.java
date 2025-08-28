package com.demo.mcp.client.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP服务响应模型
 * 用于统一封装返回给小程序的响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class McpResponse<T> {
    
    /**
     * 响应状态码
     * SUCCESS: 成功
     * ERROR: 失败
     * PENDING: 处理中
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 响应时间戳
     */
    private Long timestamp;
    
    /**
     * 请求ID，与请求中的requestId对应
     */
    private String requestId;
    
    /**
     * 附加信息
     */
    private Map<String, Object> extra = new HashMap<>();
    
    /**
     * 创建成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应
     */
    public static <T> McpResponse<T> success(T data) {
        McpResponse<T> response = new McpResponse<>();
        response.setCode("SUCCESS");
        response.setMessage("请求成功");
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    
    /**
     * 创建失败响应
     * @param message 失败消息
     * @param <T> 数据类型
     * @return 失败响应
     */
    public static <T> McpResponse<T> error(String message) {
        McpResponse<T> response = new McpResponse<>();
        response.setCode("ERROR");
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    
    /**
     * 创建处理中响应
     * @param message 处理消息
     * @param <T> 数据类型
     * @return 处理中响应
     */
    public static <T> McpResponse<T> pending(String message) {
        McpResponse<T> response = new McpResponse<>();
        response.setCode("PENDING");
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}