package com.demo.core.context;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户上下文类，存储特定租户的上下文信息
 */
@Data
public class TenantContext {
    // 租户唯一标识
    private final String tenantId;
    
    // 上下文创建时间
    private final LocalDateTime createdAt;
    
    // 最后访问时间
    private LocalDateTime lastAccessedAt;
    
    // 会话上下文存储
    private final Map<String, SessionContext> sessions;
    
    // 租户级配置
    private final Map<String, Object> configuration;
    
    /**
     * 构造函数
     * @param tenantId 租户唯一标识
     */
    public TenantContext(String tenantId) {
        this.tenantId = tenantId;
        this.createdAt = LocalDateTime.now();
        this.lastAccessedAt = LocalDateTime.now();
        this.sessions = new ConcurrentHashMap<>();
        this.configuration = new ConcurrentHashMap<>();
    }
    
    /**
     * 创建或获取会话上下文
     * @param sessionId 会话ID，如果为null则生成新的会话ID
     * @return 会话上下文
     */
    public SessionContext getOrCreateSession(String sessionId) {
        updateLastAccessedAt();
        String effectiveSessionId = sessionId != null ? sessionId : UUID.randomUUID().toString();
        return sessions.computeIfAbsent(effectiveSessionId, id -> new SessionContext(id));
    }
    
    /**
     * 获取会话上下文
     * @param sessionId 会话ID
     * @return 会话上下文，如果不存在则返回null
     */
    public SessionContext getSession(String sessionId) {
        updateLastAccessedAt();
        return sessions.get(sessionId);
    }
    
    /**
     * 移除会话上下文
     * @param sessionId 会话ID
     * @return 是否成功移除
     */
    public boolean removeSession(String sessionId) {
        updateLastAccessedAt();
        return sessions.remove(sessionId) != null;
    }
    
    /**
     * 清理过期会话
     * @param maxInactiveTimeMinutes 最大不活动时间（分钟）
     */
    public void cleanupExpiredSessions(int maxInactiveTimeMinutes) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(maxInactiveTimeMinutes);
        sessions.entrySet().removeIf(entry -> entry.getValue().getLastAccessedAt().isBefore(cutoffTime));
    }
    
    /**
     * 更新最后访问时间
     */
    private void updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now();
    }
}