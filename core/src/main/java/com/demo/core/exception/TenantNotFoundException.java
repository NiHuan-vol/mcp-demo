package com.demo.core.exception;

/**
 * 租户不存在异常，当请求的租户不存在时抛出
 */
public class TenantNotFoundException extends RuntimeException {
    
    private final String tenantId;
    
    /**
     * 构造函数
     * @param tenantId 不存在的租户ID
     */
    public TenantNotFoundException(String tenantId) {
        super("Tenant not found: " + tenantId);
        this.tenantId = tenantId;
    }
    
    /**
     * 获取不存在的租户ID
     * @return 租户ID
     */
    public String getTenantId() {
        return tenantId;
    }
}