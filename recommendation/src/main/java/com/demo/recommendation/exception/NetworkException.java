package com.demo.recommendation.exception;

/**
 * 网络异常类
 * 用于表示与外部服务通信时发生的网络相关异常
 */
public class NetworkException extends RuntimeException {
    
    private String serviceName;
    private int timeout;
    
    public NetworkException() {
        super();
    }
    
    public NetworkException(String message) {
        super(message);
    }
    
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NetworkException(String message, String serviceName) {
        super(message);
        this.serviceName = serviceName;
    }
    
    public NetworkException(String message, String serviceName, int timeout) {
        super(message);
        this.serviceName = serviceName;
        this.timeout = timeout;
    }
    
    public NetworkException(String message, String serviceName, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}