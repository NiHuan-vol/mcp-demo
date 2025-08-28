package com.demo.mcp.client;

import java.time.Duration;
import java.util.Map;

/**
 * MCP客户端配置接口，定义了客户端的配置参数
 */
public interface McpClientConfig {

    /**
     * 获取MCP服务的基础URL
     * @return 基础URL
     */
    String getBaseUrl();

    /**
     * 获取连接超时时间
     * @return 连接超时时间
     */
    Duration getConnectTimeout();

    /**
     * 获取读取超时时间
     * @return 读取超时时间
     */
    Duration getReadTimeout();

    /**
     * 获取写入超时时间
     * @return 写入超时时间
     */
    Duration getWriteTimeout();

    /**
     * 获取认证令牌
     * @return 认证令牌
     */
    String getAuthToken();

    /**
     * 获取API密钥
     * @return API密钥
     */
    String getApiKey();

    /**
     * 获取应用ID
     * @return 应用ID
     */
    String getAppId();

    /**
     * 获取自定义配置参数
     * @return 自定义配置参数
     */
    Map<String, Object> getCustomProperties();

    /**
     * 是否启用SSL
     * @return 是否启用SSL
     */
    boolean isSslEnabled();

    /**
     * 获取最大重试次数
     * @return 最大重试次数
     */
    int getMaxRetries();

    /**
     * 获取重试间隔
     * @return 重试间隔
     */
    Duration getRetryInterval();
}