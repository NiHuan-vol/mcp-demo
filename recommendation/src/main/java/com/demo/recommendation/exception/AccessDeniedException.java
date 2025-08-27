package com.demo.recommendation.exception;

/**
 * 权限异常类
 * 用于表示用户没有足够权限访问资源或执行操作时抛出的异常
 */
public class AccessDeniedException extends RuntimeException {
    
    private String featureCode;
    private String userId;
    
    public AccessDeniedException() {
        super();
    }
    
    public AccessDeniedException(String message) {
        super(message);
    }
    
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AccessDeniedException(String message, String featureCode) {
        super(message);
        this.featureCode = featureCode;
    }
    
    public AccessDeniedException(String message, String featureCode, String userId) {
        super(message);
        this.featureCode = featureCode;
        this.userId = userId;
    }
    
    public AccessDeniedException(String message, String featureCode, Throwable cause) {
        super(message, cause);
        this.featureCode = featureCode;
    }
    
    public String getFeatureCode() {
        return featureCode;
    }
    
    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}