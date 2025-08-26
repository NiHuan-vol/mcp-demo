package com.demo.core.license;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * License管理器，用于管理和验证各模块的License
 */
@Component
public class LicenseManager {
    
    // 存储租户-模块的License信息，使用ConcurrentHashMap保证线程安全
    private final Map<String, LicenseInfo> licenseStore = new ConcurrentHashMap<>();
    
    /**
     * 注册License
     * @param licenseInfo License信息
     */
    public void registerLicense(LicenseInfo licenseInfo) {
        // 构建复合键：租户ID + 模块ID
        String key = buildKey(licenseInfo.getTenantId(), licenseInfo.getModuleId());
        licenseStore.put(key, licenseInfo);
    }
    
    /**
     * 验证License是否有效
     * @param tenantId 租户ID
     * @param moduleId 模块ID
     * @return License验证结果
     */
    public LicenseValidationResult validateLicense(String tenantId, String moduleId) {
        String key = buildKey(tenantId, moduleId);
        LicenseInfo licenseInfo = licenseStore.get(key);
        
        if (licenseInfo == null) {
            return new LicenseValidationResult(false, "未找到对应的License");
        }
        
        // 检查License是否有效
        if (!licenseInfo.isValid()) {
            return new LicenseValidationResult(false, "License无效");
        }
        
        // 检查License是否过期
        if (LocalDateTime.now().isAfter(licenseInfo.getExpiryDate())) {
            return new LicenseValidationResult(false, "License已过期");
        }
        
        // 检查是否超过调用次数限制
        if (licenseInfo.getRemainingCalls() <= 0) {
            return new LicenseValidationResult(false, "License调用次数已用完");
        }
        
        // License验证通过
        return new LicenseValidationResult(true, "License验证通过");
    }
    
    /**
     * 减少License剩余调用次数
     * @param tenantId 租户ID
     * @param moduleId 模块ID
     * @return 是否减少成功
     */
    public boolean decrementRemainingCalls(String tenantId, String moduleId) {
        String key = buildKey(tenantId, moduleId);
        LicenseInfo licenseInfo = licenseStore.get(key);
        
        if (licenseInfo == null || !licenseInfo.isValid() || licenseInfo.getRemainingCalls() <= 0) {
            return false;
        }
        
        // 减少剩余调用次数
        licenseInfo.setRemainingCalls(licenseInfo.getRemainingCalls() - 1);
        return true;
    }
    
    /**
     * 获取License信息
     * @param tenantId 租户ID
     * @param moduleId 模块ID
     * @return License信息，如果不存在则返回空
     */
    public Optional<LicenseInfo> getLicenseInfo(String tenantId, String moduleId) {
        String key = buildKey(tenantId, moduleId);
        return Optional.ofNullable(licenseStore.get(key));
    }
    
    /**
     * 构建复合键
     * @param tenantId 租户ID
     * @param moduleId 模块ID
     * @return 复合键
     */
    private String buildKey(String tenantId, String moduleId) {
        return tenantId + ":" + moduleId;
    }
    
    /**
     * License验证结果类
     */
    public static class LicenseValidationResult {
        private final boolean valid;
        private final String message;
        
        public LicenseValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}