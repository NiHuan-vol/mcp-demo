package com.demo.mcp.client;

import lombok.Data;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP客户端配置的默认实现
 */
@Data
public class DefaultMcpClientConfig implements McpClientConfig {

    private String baseUrl = "http://localhost:8080/api/v1";
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration readTimeout = Duration.ofSeconds(30);
    private Duration writeTimeout = Duration.ofSeconds(30);
    private String authToken = "";
    private String apiKey = "";
    private String appId = "default-app";
    private boolean sslEnabled = false;
    private int maxRetries = 3;
    private Duration retryInterval = Duration.ofSeconds(1);
    private Map<String, Object> customProperties = new HashMap<>();

    @Override
    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    // 方便链式调用的setter方法
    public DefaultMcpClientConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public DefaultMcpClientConfig setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public DefaultMcpClientConfig setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public DefaultMcpClientConfig setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public DefaultMcpClientConfig setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }

    public DefaultMcpClientConfig setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public DefaultMcpClientConfig setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public DefaultMcpClientConfig setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
        return this;
    }

    public DefaultMcpClientConfig setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public DefaultMcpClientConfig setRetryInterval(Duration retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }

    public DefaultMcpClientConfig addCustomProperty(String key, Object value) {
        this.customProperties.put(key, value);
        return this;
    }
}