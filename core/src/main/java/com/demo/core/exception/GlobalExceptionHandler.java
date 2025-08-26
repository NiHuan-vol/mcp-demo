package com.demo.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 全局异常处理器，用于处理应用程序中的异常情况
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理模型不存在异常
     */
    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> handleModelNotFound(ModelNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = createErrorResponse("MODEL_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }
    
    /**
     * 处理内容过滤失败异常
     */
    @ExceptionHandler(ContentFilterException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Mono<ResponseEntity<ErrorResponse>> handleContentFilterException(ContentFilterException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = createErrorResponse("CONTENT_FILTER_FAILED", ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse));
    }
    
    /**
     * 处理租户不存在异常
     */
    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> handleTenantNotFound(TenantNotFoundException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = createErrorResponse("TENANT_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }
    
    /**
     * 处理License验证异常
     */
    @ExceptionHandler(LicenseException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<ResponseEntity<Map<String, Object>>> handleLicenseException(LicenseException ex, ServerWebExchange exchange) {
        ErrorResponse errorResponse = createErrorResponse("LICENSE_VALIDATION_FAILED", "License验证失败: " + ex.getMessage(), HttpStatus.FORBIDDEN.value());
        
        // 创建一个扩展的错误响应，包含租户ID和模块ID
        Map<String, Object> extendedError = new HashMap<>();
        extendedError.put("errorId", errorResponse.getErrorId());
        extendedError.put("errorCode", errorResponse.getErrorCode());
        extendedError.put("message", errorResponse.getMessage());
        extendedError.put("statusCode", errorResponse.getStatusCode());
        extendedError.put("timestamp", errorResponse.getTimestamp());
        extendedError.put("tenantId", ex.getTenantId());
        extendedError.put("moduleId", ex.getModuleId());
        
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(extendedError));
    }
    
    /**
     * 处理服务器内部错误
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        // 记录异常日志
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();
        
        ErrorResponse errorResponse = createErrorResponse("INTERNAL_ERROR", "服务器内部错误", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
    
    /**
     * 创建错误响应对象
     */
    private ErrorResponse createErrorResponse(String errorCode, String message, int statusCode) {
        return ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode(errorCode)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * 错误响应类
     */
    @lombok.Data
    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class ErrorResponse {
        private String errorId;
        private String errorCode;
        private String message;
        private int statusCode;
        private LocalDateTime timestamp;
    }
}