package com.demo.mcp.client.example;

import com.demo.mcp.client.annotation.McpMethod;
import com.demo.mcp.client.annotation.McpService;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MCP服务示例接口，展示如何使用@McpService和@McpMethod注解
 */
@McpService(serviceId = "test-service", version = "1.0.0", description = "测试服务接口")
public interface TestService {

    /**
     * 测试同步方法调用
     * @param param1 参数1
     * @param param2 参数2
     * @return 测试结果
     */
    @McpMethod(methodName = "syncTestMethod", timeout = 5000)
    Map<String, Object> syncTestMethod(String param1, Integer param2);

    /**
     * 测试异步方法调用（返回Mono）
     * @param data 测试数据
     * @return 异步结果
     */
    @McpMethod(methodName = "asyncTestMethod", timeout = 10000, retryCount = 2)
    Mono<Map<String, Object>> asyncTestMethod(Map<String, Object> data);

    /**
     * 测试CompletableFuture异步调用
     * @param id 测试ID
     * @return CompletableFuture包装的结果
     */
    @McpMethod(methodName = "futureTestMethod", timeout = 8000, async = true)
    CompletableFuture<Map<String, Object>> futureTestMethod(String id);

    /**
     * 测试复杂对象参数
     * @param request 复杂请求对象
     * @return 响应结果
     */
    @McpMethod(methodName = "complexObjectMethod", timeout = 15000)
    TestResponse complexObjectMethod(TestRequest request);
}

/**
 * 测试请求对象
 */
class TestRequest {
    private String id;
    private String name;
    private Map<String, Object> properties;

    // 构造函数、getter和setter省略
    // 实际应用中应使用lombok注解

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}

/**
 * 测试响应对象
 */
class TestResponse {
    private String code;
    private String message;
    private Object data;

    // 构造函数、getter和setter省略
    // 实际应用中应使用lombok注解

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}