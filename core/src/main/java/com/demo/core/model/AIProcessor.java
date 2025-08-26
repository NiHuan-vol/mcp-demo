package com.demo.core.model;

import reactor.core.publisher.Mono;

/**
 * AI处理器接口，定义了AI模型的标准操作
 */
public interface AIProcessor {
    
    /**
     * 处理请求
     * @param request 请求数据
     * @param <T> 请求类型
     * @param <R> 响应类型
     * @return 处理结果
     */
    <T, R> Mono<R> process(T request);
    
    /**
     * 获取处理器ID
     * @return 处理器唯一标识
     */
    String getProcessorId();
    
    /**
     * 获取处理器名称
     * @return 处理器名称
     */
    String getProcessorName();
    
    /**
     * 初始化处理器
     */
    void initialize();
    
    /**
     * 关闭处理器
     */
    void shutdown();
    
    /**
     * 检查处理器是否可用
     * @return 是否可用
     */
    boolean isAvailable();
}