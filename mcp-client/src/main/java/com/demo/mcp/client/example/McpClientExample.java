package com.demo.mcp.client.example;

import com.demo.mcp.client.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP客户端使用示例类
 * 展示如何初始化、配置和使用MCP客户端
 */
public class McpClientExample {

    public static void main(String[] args) {
        // 方式1：使用工厂模式创建客户端
        System.out.println("===== 使用工厂模式创建客户端 =====");
        useFactoryPattern();

        // 方式2：使用自定义配置创建客户端
        System.out.println("\n===== 使用自定义配置创建客户端 =====");
        useCustomConfig();

        // 方式3：异步调用示例
        System.out.println("\n===== 异步调用示例 =====");
        asyncCallExample();

        // 等待异步操作完成
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void useFactoryPattern() {
        try {
            // 获取工厂实例
            McpClientFactory factory = McpClientFactory.getInstance();

            // 创建默认客户端
            McpClient client = factory.createDefaultClient();

            // 检查客户端是否可用
            if (client.isAvailable()) {
                System.out.println("默认MCP客户端创建成功并可用");
            }

            // 在实际应用中，这里可以调用服务、模型等
            // 注意：在示例环境中，这些调用可能会失败，因为实际的MCP服务可能未启动
            try {
                // 调用服务示例
                Map<String, Object> params = new HashMap<>();
                params.put("param1", "value1");
                params.put("param2", 123);

                System.out.println("尝试调用服务示例（在实际环境中可能会失败）");
                // client.invokeService("test-service", "test-method", params)
                //     .subscribe(result -> System.out.println("服务调用结果: " + result));

            } catch (Exception e) {
                System.out.println("服务调用示例出错: " + e.getMessage());
            }

            // 最后关闭客户端
            factory.removeClient("default");
        } catch (Exception e) {
            System.out.println("工厂模式示例执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void useCustomConfig() {
        try {
            // 创建自定义配置
            DefaultMcpClientConfig config = new DefaultMcpClientConfig()
                    .setBaseUrl("http://localhost:8080/api/v1")
                    .setConnectTimeout(java.time.Duration.ofSeconds(5))
                    .setReadTimeout(java.time.Duration.ofSeconds(20))
                    .setWriteTimeout(java.time.Duration.ofSeconds(20))
                    .setAuthToken("your-auth-token")
                    .setApiKey("your-api-key")
                    .setAppId("example-app")
                    .setMaxRetries(3)
                    .setRetryInterval(java.time.Duration.ofSeconds(1))
                    .addCustomProperty("customKey1", "customValue1")
                    .addCustomProperty("customKey2", 456);

            // 创建客户端
            McpClient client = new DefaultMcpClient(config);
            client.initialize();

            if (client.isAvailable()) {
                System.out.println("自定义配置MCP客户端创建成功并可用");
                System.out.println("客户端基础URL: " + client.getConfig().getBaseUrl());
                System.out.println("客户端应用ID: " + client.getConfig().getAppId());
            }

            // 在实际应用中，这里可以调用服务、模型等
            // 注意：在示例环境中，这些调用可能会失败，因为实际的MCP服务可能未启动
            try {
                // 调用模型示例
                Map<String, Object> modelRequest = new HashMap<>();
                modelRequest.put("prompt", "Hello, world!");
                modelRequest.put("max_tokens", 100);

                System.out.println("尝试调用模型示例（在实际环境中可能会失败）");
                // client.invokeModel("gpt-3.5-turbo", modelRequest)
                //     .subscribe(result -> System.out.println("模型调用结果: " + result));

            } catch (Exception e) {
                System.out.println("模型调用示例出错: " + e.getMessage());
            }

            // 最后关闭客户端
            client.shutdown();
        } catch (Exception e) {
            System.out.println("自定义配置示例执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void asyncCallExample() {
        try {
            // 创建客户端
            McpClient client = McpClientFactory.getInstance().createDefaultClient();

            // 异步调用服务
            System.out.println("发起异步调用");

            // 在实际应用中，这里可以调用服务、模型等
            // 注意：在示例环境中，这些调用可能会失败，因为实际的MCP服务可能未启动
            Mono.fromRunnable(() -> {
                try {
                    // 模拟调用服务
                    System.out.println("异步任务开始执行");
                    Thread.sleep(2000); // 模拟耗时操作
                    
                    // 发布事件示例
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("eventSource", "example-app");
                    eventData.put("eventDetails", "Example event data");
                    
                    System.out.println("尝试发布事件示例（在实际环境中可能会失败）");
                    // client.publishEvent("example.event", eventData)
                    //     .subscribe(success -> System.out.println("事件发布结果: " + success));

                    System.out.println("异步任务执行完成");
                } catch (Exception e) {
                    System.out.println("异步任务执行出错: " + e.getMessage());
                }
            }).subscribeOn(Schedulers.boundedElastic()).subscribe();

            System.out.println("异步调用已发起，主线程继续执行其他任务");

        } catch (Exception e) {
            System.out.println("异步调用示例执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}