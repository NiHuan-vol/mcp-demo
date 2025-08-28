package com.demo.mcp.client;

/**
 * MCP客户端异常类，用于表示与MCP服务交互过程中发生的错误
 */
public class McpClientException extends RuntimeException {

    private String errorCode;
    private Object errorDetails;

    public McpClientException(String message) {
        super(message);
    }

    public McpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public McpClientException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public McpClientException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public McpClientException(String message, String errorCode, Object errorDetails) {
        super(message);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public McpClientException(String message, String errorCode, Object errorDetails, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(Object errorDetails) {
        this.errorDetails = errorDetails;
    }
}