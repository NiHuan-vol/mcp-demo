package com.demo.mcp.client.proxy;

import com.demo.mcp.client.McpClientFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * MCP服务代理Bean，实现FactoryBean接口，用于在Spring容器中创建服务代理实例
 */
@Setter
@Slf4j
public class McpServiceProxyBean<T> implements FactoryBean<T>, InitializingBean {

    private String serviceInterface;
    private McpServiceProxyFactory proxyFactory;
    private T proxyObject;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (serviceInterface == null || serviceInterface.isEmpty()) {
            throw new IllegalArgumentException("Service interface must be specified");
        }

        if (proxyFactory == null) {
            // 默认使用工厂中的代理工厂
            proxyFactory = McpClientFactory.getInstance().getClient("default") != null ?
                    new McpServiceProxyFactory(McpClientFactory.getInstance().getClient("default")) :
                    new McpServiceProxyFactory(McpClientFactory.getInstance().createDefaultClient());
        }

        // 创建代理实例
        Class<?> interfaceClass = Class.forName(serviceInterface);
        this.proxyObject = createProxy(interfaceClass);
        log.debug("Created MCP service proxy for interface: {}", serviceInterface);
    }

    @Override
    public T getObject() throws Exception {
        return proxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        if (serviceInterface == null) {
            return null;
        }
        try {
            return Class.forName(serviceInterface);
        } catch (ClassNotFoundException e) {
            log.error("Failed to load service interface: {}", serviceInterface, e);
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private T createProxy(Class<?> interfaceClass) {
        return (T) proxyFactory.createProxy(interfaceClass);
    }
}