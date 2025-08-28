package com.demo.mcp.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * MCP客户端的Spring Boot自动配置类
 */
@Configuration
@EnableConfigurationProperties(McpClientProperties.class)
public class McpClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public McpClientConfig mcpClientConfig(McpClientProperties properties) {
        DefaultMcpClientConfig config = new DefaultMcpClientConfig();
        config.setBaseUrl(properties.getBaseUrl());
        config.setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()));
        config.setReadTimeout(Duration.ofMillis(properties.getReadTimeout()));
        config.setWriteTimeout(Duration.ofMillis(properties.getWriteTimeout()));
        config.setAuthToken(properties.getAuthToken());
        config.setApiKey(properties.getApiKey());
        config.setAppId(properties.getAppId());
        config.setSslEnabled(properties.isSslEnabled());
        config.setMaxRetries(properties.getMaxRetries());
        config.setRetryInterval(Duration.ofMillis(properties.getRetryInterval()));
        
        // 添加自定义属性
        if (properties.getCustomProperties() != null) {
            properties.getCustomProperties().forEach(config::addCustomProperty);
        }
        
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public McpClient mcpClient(McpClientConfig config) {
        McpClient client = new DefaultMcpClient(config);
        client.initialize();
        return client;
    }

    @Bean
    public McpClientFactory mcpClientFactory() {
        return McpClientFactory.getInstance();
    }
}