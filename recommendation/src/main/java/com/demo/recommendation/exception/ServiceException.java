package com.demo.recommendation.exception;

/**
 * 服务层异常类
 * 用于表示服务层处理过程中发生的异常
 */
public class ServiceException extends RuntimeException {
    
    private String errorCode;
    
    public ServiceException() {
        super();
    }
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ServiceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}