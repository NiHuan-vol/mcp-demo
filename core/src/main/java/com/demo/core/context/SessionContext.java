package com.demo.core.context;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话上下文类，存储特定会话的上下文信息
 */
@Data
public class SessionContext {
    // 会话唯一标识
    private final String sessionId;
    
    // 会话创建时间
    private final LocalDateTime createdAt;
    
    // 最后访问时间
    private LocalDateTime lastAccessedAt;
    
    // 会话属性存储
    private final Map<String, Object> attributes;
    
    /**
     * 构造函数
     * @param sessionId 会话ID
     */
    public SessionContext(String sessionId) {
        this.sessionId = sessionId;
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = LocalDateTime.now();
        this.attributes = new ConcurrentHashMap<>();
    }
    
    /**
     * 设置会话属性
     * @param key 属性键
     * @param value 属性值
     * @return 之前的值（如果存在）
     */
    public Object setAttribute(String key, Object value) {
        updateLastAccessedAt();
        return attributes.put(key, value);
    }
    
    /**
     * 获取会话属性
     * @param key 属性键
     * @return 属性值，如果不存在则返回null
     */
    public Object getAttribute(String key) {
        updateLastAccessedAt();
        return attributes.get(key);
    }
    
    /**
     * 移除会话属性
     * @param key 属性键
     * @return 被移除的值
     */
    public Object removeAttribute(String key) {
        updateLastAccessedAt();
        return attributes.remove(key);
    }
    
    /**
     * 检查属性是否存在
     * @param key 属性键
     * @return 是否存在
     */
    public boolean containsAttribute(String key) {
        updateLastAccessedAt();
        return attributes.containsKey(key);
    }
    
    /**
     * 清空所有属性
     */
    public void clearAttributes() {
        updateLastAccessedAt();
        attributes.clear();
    }
    
    /**
     * 更新最后访问时间
     */
    private void updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now();
    }
}