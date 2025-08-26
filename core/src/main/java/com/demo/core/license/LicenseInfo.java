package com.demo.core.license;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * License信息类，用于存储各模块的License信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LicenseInfo {
    // 租户ID
    private String tenantId;
    
    // 模块ID（如core、manufacturing、recommendation）
    private String moduleId;
    
    // 剩余调用次数
    private int remainingCalls;
    
    // 总调用次数限制
    private int totalCallsLimit;
    
    // License过期时间
    private LocalDateTime expiryDate;
    
    // License状态（有效/无效）
    private boolean valid;
    
    // License类型
    private String licenseType;
    
    // 颁发时间
    private LocalDateTime issuedAt;
}