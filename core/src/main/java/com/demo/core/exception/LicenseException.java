package com.demo.core.exception;

/**
 * License异常，用于在License验证失败时抛出
 */
public class LicenseException extends RuntimeException {
    
    private final String tenantId;
    private final String moduleId;
    
    public LicenseException(String message, String tenantId, String moduleId) {
        super(message);
        this.tenantId = tenantId;
        this.moduleId = moduleId;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getModuleId() {
        return moduleId;
    }
}