package com.demo.core.license;

import com.demo.core.exception.LicenseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * License请求拦截器，用于在处理请求前进行License验证
 */
@Component
public class LicenseInterceptor implements GatewayFilter, Ordered {
    
    @Autowired
    private LicenseManager licenseManager;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从请求头中获取租户ID
        String tenantId = exchange.getRequest().getHeaders().getFirst("X-Tenant-Id");
        
        // 如果没有提供租户ID，使用默认值
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = "default-tenant";
        }
        
        // 从请求路径中获取模块ID
        String path = exchange.getRequest().getPath().value();
        String moduleId = extractModuleId(path);
        
        try {
            // 验证License
            LicenseManager.LicenseValidationResult validationResult = licenseManager.validateLicense(tenantId, moduleId);
            
            if (!validationResult.isValid()) {
                throw new LicenseException(validationResult.getMessage(), tenantId, moduleId);
            }
            
            // 减少剩余调用次数
            boolean decrementResult = licenseManager.decrementRemainingCalls(tenantId, moduleId);
            if (!decrementResult) {
                throw new LicenseException("License调用次数减少失败", tenantId, moduleId);
            }
            
            // 将租户ID和模块ID添加到请求属性中，以便后续处理使用
            exchange.getAttributes().put("TENANT_ID", tenantId);
            exchange.getAttributes().put("MODULE_ID", moduleId);
            
            // License验证通过，继续处理请求
            return chain.filter(exchange);
            
        } catch (LicenseException e) {
            // License验证失败，返回403 Forbidden
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            
            String errorBody = "{\"error\":\"License validation failed\",\"message\":\"" + 
                              e.getMessage() + "\",\"tenantId\":\"" + 
                              tenantId + "\",\"moduleId\":\"" + 
                              moduleId + "\"}";
            
            return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(errorBody.getBytes()))
            ).then(Mono.defer(exchange.getResponse()::setComplete));
        }
    }
    
    /**
     * 从请求路径中提取模块ID
     * @param path 请求路径
     * @return 模块ID
     */
    private String extractModuleId(String path) {
        // 假设路径格式为 /moduleId/api/...
        String[] parts = path.split("/");
        if (parts.length >= 2) {
            // 检查是否是有效的模块ID
            String potentialModuleId = parts[1];
            if (potentialModuleId.equals("core") || 
                potentialModuleId.equals("manufacturing") || 
                potentialModuleId.equals("recommendation")) {
                return potentialModuleId;
            }
        }
        // 默认返回core模块ID
        return "core";
    }
    
    @Override
    public int getOrder() {
        // 设置拦截器的执行顺序，确保在其他拦截器之前执行
        return -100;
    }
}