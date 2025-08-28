package com.demo.mcp.client.proxy;

import com.demo.mcp.client.McpClient;
import com.demo.mcp.client.McpClientException;
import com.demo.mcp.client.annotation.McpMethod;
import com.demo.mcp.client.annotation.McpService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MCP服务代理工厂，用于创建基于JDK动态代理的MCP服务代理
 */
@Slf4j
public class McpServiceProxyFactory {

    private final McpClient mcpClient;
    private final ExecutorService executorService;

    public McpServiceProxyFactory(McpClient mcpClient) {
        this.mcpClient = mcpClient;
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * 创建MCP服务代理实例
     * @param serviceInterface 服务接口
     * @param <T> 接口类型
     * @return 服务代理实例
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<T> serviceInterface) {
        if (!serviceInterface.isInterface()) {
            throw new IllegalArgumentException("Service must be an interface");
        }

        McpService mcpService = serviceInterface.getAnnotation(McpService.class);
        if (mcpService == null) {
            throw new IllegalArgumentException("Service interface must be annotated with @McpService");
        }

        String serviceId = mcpService.serviceId();
        if (serviceId.isEmpty()) {
            serviceId = mcpService.value();
            if (serviceId.isEmpty()) {
                serviceId = serviceInterface.getSimpleName();
                log.warn("Service ID is not specified, using interface name: {}", serviceId);
            }
        }

        InvocationHandler handler = new McpServiceInvocationHandler(mcpClient, serviceInterface, serviceId);
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, 
                handler
        );
    }

    /**
     * 关闭执行器服务
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * MCP服务调用处理器，实现InvocationHandler接口
     */
    private class McpServiceInvocationHandler implements InvocationHandler {

        private final McpClient mcpClient;
        private final Class<?> serviceInterface;
        private final String serviceId;

        public McpServiceInvocationHandler(McpClient mcpClient, Class<?> serviceInterface, String serviceId) {
            this.mcpClient = mcpClient;
            this.serviceInterface = serviceInterface;
            this.serviceId = serviceId;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 处理Object类的方法
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            McpMethod mcpMethod = method.getAnnotation(McpMethod.class);
            if (mcpMethod == null) {
                throw new McpClientException("Method must be annotated with @McpMethod");
            }

            String methodName = mcpMethod.methodName();
            if (methodName.isEmpty()) {
                methodName = mcpMethod.value();
                if (methodName.isEmpty()) {
                    methodName = method.getName();
                }
            }

            // 构建请求参数
            Map<String, Object> params = buildParams(method, args);

            // 判断返回类型
            Class<?> returnType = method.getReturnType();
            boolean isMono = Mono.class.isAssignableFrom(returnType);
            boolean isCompletableFuture = CompletableFuture.class.isAssignableFrom(returnType);
            boolean isAsync = mcpMethod.async() || isMono || isCompletableFuture;

            if (isAsync) {
                // 异步调用
                return invokeAsync(method, methodName, params, returnType, mcpMethod);
            } else {
                // 同步调用
                return invokeSync(methodName, params, returnType, mcpMethod);
            }
        }

        private Map<String, Object> buildParams(Method method, Object[] args) {
            Map<String, Object> params = new HashMap<>();
            if (args != null && args.length > 0) {
                String[] paramNames = getParamNames(method);
                for (int i = 0; i < args.length; i++) {
                    if (i < paramNames.length) {
                        params.put(paramNames[i], args[i]);
                    } else {
                        params.put("param" + (i + 1), args[i]);
                    }
                }
            }
            return params;
        }

        private String[] getParamNames(Method method) {
            // 注意：这里简化处理，实际应用中可能需要使用ParameterNameDiscoverer等工具获取参数名
            // 或者通过@McpMethod注解的paramMappings属性来配置参数映射
            return new String[0];
        }

        private Object invokeSync(String methodName, Map<String, Object> params, 
                                  Class<?> returnType, McpMethod mcpMethod) {
            try {
                long startTime = System.currentTimeMillis();
                Object result = mcpClient.invokeService(serviceId, methodName, params)
                        .block(java.time.Duration.ofMillis(mcpMethod.timeout()));
                long endTime = System.currentTimeMillis();
                log.debug("Sync call to {}#{} completed in {}ms", serviceId, methodName, (endTime - startTime));
                return result;
            } catch (Exception e) {
                log.error("Sync call to {}#{} failed: {}", serviceId, methodName, e.getMessage());
                throw new McpClientException("Failed to invoke service method synchronously", e);
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Object invokeAsync(Method method, String methodName, Map<String, Object> params, 
                                  Class<?> returnType, McpMethod mcpMethod) {
            Class<?> genericReturnType = getGenericReturnType(method);
            Mono monoResult = mcpClient.invokeService(serviceId, methodName, params)
                    .timeout(java.time.Duration.ofMillis(mcpMethod.timeout()))
                    .doOnSuccess(result -> log.debug("Async call to {}#{} completed successfully", serviceId, methodName))
                    .doOnError(error -> log.error("Async call to {}#{} failed: {}", serviceId, methodName, error.getMessage()));

            if (returnType == Mono.class) {
                // 返回Mono对象
                return monoResult;
            } else if (returnType == CompletableFuture.class) {
                // 返回CompletableFuture对象
                return monoResult.toFuture();
            } else {
                // 异步执行但返回具体类型（不推荐，因为会阻塞）
                CompletableFuture future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return monoResult.block(java.time.Duration.ofMillis(mcpMethod.timeout()));
                    } catch (Exception e) {
                        throw new McpClientException("Failed to invoke service method asynchronously", e);
                    }
                }, executorService);
                return future;
            }
        }

        private Class<?> getGenericReturnType(Method method) {
            // 注意：这里简化处理，实际应用中可能需要更复杂的泛型类型解析
            return Object.class;
        }
    }
}