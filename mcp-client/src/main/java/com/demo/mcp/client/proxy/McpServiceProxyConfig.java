package com.demo.mcp.client.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP服务代理配置类，用于配置服务代理相关的Bean
 */
@Configuration
public class McpServiceProxyConfig {

    @Autowired(required = false)
    private McpServiceScannerConfigurer scannerConfigurer;

    @Bean
    @ConditionalOnMissingBean
    public McpServiceProxyFactory mcpServiceProxyFactory(McpClientFactory clientFactory) {
        // 默认使用工厂中的默认客户端
        McpClient client = clientFactory.getClient("default");
        if (client == null) {
            client = clientFactory.createDefaultClient();
        }
        return new McpServiceProxyFactory(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public McpServiceScannerConfigurer mcpServiceScannerConfigurer() {
        McpServiceScannerConfigurer configurer = new McpServiceScannerConfigurer();
        // 默认不指定基础包，需要用户在使用时配置
        return configurer;
    }
}