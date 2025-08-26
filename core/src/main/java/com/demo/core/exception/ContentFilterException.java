package com.demo.core.exception;

import com.demo.core.filter.FilterResult;

/**
 * 内容过滤异常，当内容过滤失败时抛出
 */
public class ContentFilterException extends RuntimeException {
    
    private final FilterResult filterResult;
    
    /**
     * 构造函数
     * @param message 错误消息
     * @param filterResult 过滤结果
     */
    public ContentFilterException(String message, FilterResult filterResult) {
        super(message);
        this.filterResult = filterResult;
    }
    
    /**
     * 获取过滤结果
     * @return 过滤结果
     */
    public FilterResult getFilterResult() {
        return filterResult;
    }
}