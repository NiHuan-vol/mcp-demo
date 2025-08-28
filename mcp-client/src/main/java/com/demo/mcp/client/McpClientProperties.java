package com.demo.mcp.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP客户端的属性配置类，用于从配置文件中读取配置项
 */
@Data
@ConfigurationProperties(prefix = "mcp.client")
public class McpClientProperties {

    // 基础URL
    private String baseUrl = "http://localhost:8080/api/v1";

    // 连接超时时间（毫秒）
    private int connectTimeout = 10000;

    // 读取超时时间（毫秒）
    private int readTimeout = 30000;

    // 写入超时时间（毫秒）
    private int writeTimeout = 30000;

    // 认证令牌
    private String authToken = "";

    // API密钥
    private String apiKey = "";

    // 应用ID
    private String appId = "default-app";

    // 是否启用SSL
    private boolean sslEnabled = false;

    // 最大重试次数
    private int maxRetries = 3;

    // 重试间隔（毫秒）
    private int retryInterval = 1000;

    // 自定义属性
    private Map<String, Object> customProperties = new HashMap<>();
}