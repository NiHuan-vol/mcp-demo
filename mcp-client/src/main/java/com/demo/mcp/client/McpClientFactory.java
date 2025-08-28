package com.demo.mcp.client;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP客户端工厂类，负责创建和管理MCP客户端实例
 */
@Slf4j
public class McpClientFactory {

    private static final McpClientFactory INSTANCE = new McpClientFactory();
    private final Map<String, McpClient> clients = new ConcurrentHashMap<>();

    private McpClientFactory() {
        // 私有构造函数，防止外部实例化
    }

    /**
     * 获取工厂实例
     * @return 工厂实例
     */
    public static McpClientFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 创建默认的MCP客户端实例
     * @return 默认MCP客户端实例
     */
    public McpClient createDefaultClient() {
        return createClient("default", new DefaultMcpClientConfig());
    }

    /**
     * 创建指定配置的MCP客户端实例
     * @param config 客户端配置
     * @return 配置的MCP客户端实例
     */
    public McpClient createClient(McpClientConfig config) {
        return createClient("default", config);
    }

    /**
     * 创建指定ID和配置的MCP客户端实例
     * @param clientId 客户端ID
     * @param config 客户端配置
     * @return 配置的MCP客户端实例
     */
    public McpClient createClient(String clientId, McpClientConfig config) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID cannot be empty");
        }

        if (config == null) {
            config = new DefaultMcpClientConfig();
        }

        McpClient client = new DefaultMcpClient(config);
        clients.put(clientId, client);
        log.info("Created MCP client with ID: {}", clientId);
        return client;
    }

    /**
     * 获取指定ID的MCP客户端实例
     * @param clientId 客户端ID
     * @return MCP客户端实例，如果不存在则返回null
     */
    public McpClient getClient(String clientId) {
        return clients.get(clientId);
    }

    /**
     * 移除指定ID的MCP客户端实例
     * @param clientId 客户端ID
     * @return 被移除的MCP客户端实例，如果不存在则返回null
     */
    public McpClient removeClient(String clientId) {
        McpClient client = clients.remove(clientId);
        if (client != null) {
            client.shutdown();
            log.info("Removed MCP client with ID: {}", clientId);
        }
        return client;
    }

    /**
     * 关闭所有MCP客户端实例
     */
    public void shutdownAll() {
        for (McpClient client : clients.values()) {
            try {
                client.shutdown();
            } catch (Exception e) {
                log.error("Error shutting down MCP client: {}", e.getMessage());
            }
        }
        clients.clear();
        log.info("All MCP clients have been shut down");
    }

    /**
     * 获取客户端实例数量
     * @return 客户端实例数量
     */
    public int getClientCount() {
        return clients.size();
    }
}