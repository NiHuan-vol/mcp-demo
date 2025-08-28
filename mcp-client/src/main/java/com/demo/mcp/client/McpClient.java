package com.demo.mcp.client;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MCP客户端核心接口，定义了与MCP服务交互的基本操作
 * 基于服务代理模式，提供统一的API调用能力
 */
public interface McpClient {

    /**
     * 初始化MCP客户端
     */
    void initialize();

    /**
     * 关闭MCP客户端
     */
    void shutdown();

    /**
     * 检查MCP客户端是否可用
     * @return 是否可用
     */
    boolean isAvailable();

    /**
     * 检查MCP服务是否可用
     * @return 服务是否可用
     */
    boolean isServiceAvailable();

    /**
     * 调用MCP服务的通用方法
     * @param serviceId 服务ID
     * @param methodName 方法名称
     * @param params 请求参数
     * @param <T> 返回类型
     * @return 响应结果的Mono包装
     */
    <T> Mono<T> invokeService(String serviceId, String methodName, Map<String, Object> params);

    /**
     * 异步调用MCP服务
     * @param serviceId 服务ID
     * @param methodName 方法名称
     * @param params 请求参数
     * @return 响应结果的CompletableFuture包装
     */
    CompletableFuture<Object> callServiceAsync(String serviceId, String methodName, Map<String, Object> params);

    /**
     * 调用模型服务
     * @param modelId 模型ID
     * @param request 请求数据
     * @param <T> 返回类型
     * @return 模型响应的Mono包装
     */
    <T> Mono<T> invokeModel(String modelId, Object request);

    /**
     * 异步调用模型服务
     * @param modelId 模型ID
     * @param request 请求数据
     * @return 模型响应的CompletableFuture包装
     */
    CompletableFuture<Object> callModelAsync(String modelId, Object request);

    /**
     * 发送异步事件
     * @param eventType 事件类型
     * @param eventData 事件数据
     * @return 事件发送结果的Mono包装
     */
    Mono<Boolean> publishEvent(String eventType, Map<String, Object> eventData);

    /**
     * 发送事件（同步方式）
     * @param eventType 事件类型
     * @param eventData 事件数据
     * @return 是否发送成功
     */
    boolean sendEvent(String eventType, Map<String, Object> eventData);

    /**
     * 获取客户端配置
     * @return 客户端配置
     */
    McpClientConfig getConfig();
}