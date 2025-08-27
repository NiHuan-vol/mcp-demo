package com.demo.recommendation.mcp.gateway;

/**
 * MCP网关基础接口，定义了所有网关组件的标准操作
 */
public interface McpGateway {

    /**
     * 初始化网关
     */
    void initialize();

    /**
     * 关闭网关
     */
    void shutdown();

    /**
     * 检查网关是否可用
     * @return 是否可用
     */
    boolean isAvailable();

    /**
     * 获取网关ID
     * @return 网关唯一标识
     */
    String getGatewayId();

    /**
     * 获取网关名称
     * @return 网关名称
     */
    String getGatewayName();
}