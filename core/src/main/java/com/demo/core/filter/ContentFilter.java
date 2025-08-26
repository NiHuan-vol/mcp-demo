package com.demo.core.filter;

import reactor.core.publisher.Mono;

/**
 * 内容过滤器接口，定义了内容过滤的基本操作
 */
public interface ContentFilter {
    
    /**
     * 初始化过滤器
     */
    void initialize();
    
    /**
     * 过滤文本内容
     * @param content 要过滤的文本内容
     * @return 包含过滤结果的Mono对象
     */
    Mono<FilterResult> filter(String content);
    
    /**
     * 异步过滤文本内容
     * @param content 要过滤的文本内容
     * @return 包含过滤结果的Mono对象
     */
    Mono<FilterResult> filterAsync(String content);
    
    /**
     * 获取过滤器ID
     * @return 过滤器ID
     */
    String getFilterId();
    
    /**
     * 获取过滤器名称
     * @return 过滤器名称
     */
    String getFilterName();
    
    /**
     * 关闭过滤器，清理资源
     */
    void shutdown();
}