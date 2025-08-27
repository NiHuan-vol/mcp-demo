package com.demo.recommendation.mcp.assistant;

import reactor.core.publisher.Mono;

/**
 * 旅行助手接口，定义了智能旅行助手的核心功能
 */
public interface TravelAssistant {

    /**
     * 初始化旅行助手
     */
    void initialize();

    /**
     * 关闭旅行助手
     */
    void shutdown();

    /**
     * 处理用户请求
     * @param request 用户请求
     * @return 处理结果
     */
    Mono<AssistantResponse> processRequest(AssistantRequest request);

    /**
     * 获取助手ID
     * @return 助手唯一标识
     */
    String getAssistantId();

    /**
     * 获取助手名称
     * @return 助手名称
     */
    String getAssistantName();

    /**
     * 检查助手是否可用
     * @return 是否可用
     */
    boolean isAvailable();
}