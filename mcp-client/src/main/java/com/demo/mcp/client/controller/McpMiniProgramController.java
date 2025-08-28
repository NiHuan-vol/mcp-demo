package com.demo.mcp.client.controller;

import com.demo.mcp.client.DefaultMcpClient;
import com.demo.mcp.client.McpClientException;
import com.demo.mcp.client.controller.model.McpRequest;
import com.demo.mcp.client.controller.model.McpResponse;
import com.demo.mcp.client.security.MiniProgramSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MCP客户端小程序接口控制器
 * 提供HTTP接口供小程序调用MCP服务
 */
@RestController
@RequestMapping("/api/mcp/mini-program")
public class McpMiniProgramController {

    @Autowired
    private DefaultMcpClient defaultMcpClient;
    
    @Value("${mcp.client.mini-program.app-secret:default-secret}")
    private String appSecret;
    
    @Value("${mcp.client.mini-program.request-timeout:30000}")
    private long requestTimeout; // 默认30秒

    /**
     * 通用服务调用接口
     * 支持小程序调用MCP服务的通用入口
     *
     * @param serviceId 服务ID
     * @param methodName 方法名称
     * @param requestData 请求数据
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 响应结果
     */
    @PostMapping("/call/{serviceId}/{methodName}")
    public Mono<ResponseEntity<McpResponse<Object>>> callService(
            @PathVariable String serviceId,
            @PathVariable String methodName,
            @RequestBody Map<String, Object> requestData,
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) Long timestamp) {
        
        // 构建完整的请求参数，用于签名验证
        Map<String, Object> fullParams = new HashMap<>(requestData);
        fullParams.put("serviceId", serviceId);
        fullParams.put("methodName", methodName);
        if (timestamp != null) {
            fullParams.put("timestamp", timestamp);
        }
        
        // 验证请求签名和时间戳
        if (!verifyRequest(fullParams, signature, timestamp)) {
            McpResponse<Object> errorResponse = McpResponse.error("请求验证失败");
            return Mono.just(ResponseEntity.status(401).body(errorResponse));
        }
        
        try {
            // 异步调用MCP服务
            CompletableFuture<Object> resultFuture = defaultMcpClient.callServiceAsync(
                    serviceId, methodName, requestData);

            // 转换为Mono响应式类型
            return Mono.fromFuture(resultFuture)
                    .map(result -> ResponseEntity.ok(McpResponse.success(result)))
                    .onErrorResume(error -> {
                        // 处理异常情况
                        String errorMessage = error instanceof McpClientException 
                                ? error.getMessage() : "服务调用失败";
                        return Mono.just(ResponseEntity.ok(McpResponse.error(errorMessage)));
                    });
        } catch (Exception e) {
            return Mono.just(ResponseEntity.ok(McpResponse.error("请求处理异常: " + e.getMessage())));
        }
    }
    
    /**
     * 使用统一的请求模型调用服务
     * @param request MCP请求对象
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 响应结果
     */
    @PostMapping("/call")
    public Mono<ResponseEntity<McpResponse<Object>>> callServiceWithModel(
            @RequestBody McpRequest request,
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) Long timestamp) {
        
        // 验证请求参数
        if (request == null || request.getServiceId() == null || request.getMethodName() == null) {
            return Mono.just(ResponseEntity.ok(McpResponse.error("请求参数不完整")));
        }
        
        // 构建完整的请求参数，用于签名验证
        Map<String, Object> fullParams = new HashMap<>();
        fullParams.put("serviceId", request.getServiceId());
        fullParams.put("methodName", request.getMethodName());
        if (request.getParams() != null) {
            fullParams.putAll(request.getParams());
        }
        if (timestamp != null) {
            fullParams.put("timestamp", timestamp);
        }
        
        // 验证请求签名和时间戳
        if (!verifyRequest(fullParams, signature, timestamp)) {
            McpResponse<Object> errorResponse = McpResponse.error("请求验证失败");
            return Mono.just(ResponseEntity.status(401).body(errorResponse));
        }
        
        try {
            // 异步调用MCP服务
            CompletableFuture<Object> resultFuture = defaultMcpClient.callServiceAsync(
                    request.getServiceId(), request.getMethodName(), request.getParams());

            // 转换为Mono响应式类型
            return Mono.fromFuture(resultFuture)
                    .map(result -> {
                        McpResponse<Object> response = McpResponse.success(result);
                        response.setRequestId(request.getRequestId());
                        return ResponseEntity.ok(response);
                    })
                    .onErrorResume(error -> {
                        // 处理异常情况
                        String errorMessage = error instanceof McpClientException 
                                ? error.getMessage() : "服务调用失败";
                        McpResponse<Object> errorResponse = McpResponse.error(errorMessage);
                        errorResponse.setRequestId(request.getRequestId());
                        return Mono.just(ResponseEntity.ok(errorResponse));
                    });
        } catch (Exception e) {
            McpResponse<Object> errorResponse = McpResponse.error("请求处理异常: " + e.getMessage());
            errorResponse.setRequestId(request.getRequestId());
            return Mono.just(ResponseEntity.ok(errorResponse));
        }
    }

    /**
     * 模型调用接口
     * 专用于调用AI模型的接口
     *
     * @param modelId 模型ID
     * @param requestData 模型请求数据
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 模型响应结果
     */
    @PostMapping("/model/{modelId}")
    public Mono<ResponseEntity<McpResponse<Object>>> callModel(
            @PathVariable String modelId,
            @RequestBody Object requestData,
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) Long timestamp) {
        
        // 构建完整的请求参数，用于签名验证
        Map<String, Object> fullParams = new HashMap<>();
        fullParams.put("modelId", modelId);
        if (requestData instanceof Map) {
            fullParams.putAll((Map<? extends String, ?>) requestData);
        } else {
            fullParams.put("requestData", requestData);
        }
        if (timestamp != null) {
            fullParams.put("timestamp", timestamp);
        }
        
        // 验证请求签名和时间戳
        if (!verifyRequest(fullParams, signature, timestamp)) {
            McpResponse<Object> errorResponse = McpResponse.error("请求验证失败");
            return Mono.just(ResponseEntity.status(401).body(errorResponse));
        }
        
        try {
            // 异步调用AI模型
            CompletableFuture<Object> resultFuture = defaultMcpClient.callModelAsync(
                    modelId, requestData);

            return Mono.fromFuture(resultFuture)
                    .map(result -> ResponseEntity.ok(McpResponse.success(result)))
                    .onErrorResume(error -> {
                        String errorMessage = error instanceof McpClientException 
                                ? error.getMessage() : "模型调用失败";
                        return Mono.just(ResponseEntity.ok(McpResponse.error(errorMessage)));
                    });
        } catch (Exception e) {
            return Mono.just(ResponseEntity.ok(McpResponse.error("模型请求处理异常: " + e.getMessage())));
        }
    }

    /**
     * 发送事件接口
     * 用于小程序向MCP平台发送事件
     *
     * @param eventType 事件类型
     * @param eventData 事件数据
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 发送结果
     */
    @PostMapping("/event/{eventType}")
    public Mono<ResponseEntity<McpResponse<Object>>> sendEvent(
            @PathVariable String eventType,
            @RequestBody Map<String, Object> eventData,
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) Long timestamp) {
        
        // 构建完整的请求参数，用于签名验证
        Map<String, Object> fullParams = new HashMap<>();
        fullParams.put("eventType", eventType);
        if (eventData != null) {
            fullParams.putAll(eventData);
        }
        if (timestamp != null) {
            fullParams.put("timestamp", timestamp);
        }
        
        // 验证请求签名和时间戳
        if (!verifyRequest(fullParams, signature, timestamp)) {
            McpResponse<Object> errorResponse = McpResponse.error("请求验证失败");
            return Mono.just(ResponseEntity.status(401).body(errorResponse));
        }
        
        try {
            // 发送事件
            boolean success = defaultMcpClient.sendEvent(eventType, eventData);
            if (success) {
                return Mono.just(ResponseEntity.ok(McpResponse.success(null)));
            } else {
                return Mono.just(ResponseEntity.ok(McpResponse.error("事件发送失败")));
            }
        } catch (Exception e) {
            return Mono.just(ResponseEntity.ok(McpResponse.error("事件发送异常: " + e.getMessage())));
        }
    }

    /**
     * 服务健康检查接口
     * 用于小程序检查MCP服务是否可用
     *
     * @return 健康检查结果
     */
    @GetMapping("/health")
    public ResponseEntity<McpResponse<Map<String, Object>>> checkHealth() {
        try {
            boolean isAvailable = defaultMcpClient.isServiceAvailable();
            if (isAvailable) {
                Map<String, Object> data = new HashMap<>();
                data.put("available", true);
                return ResponseEntity.ok(McpResponse.success(data));
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("available", false);
                McpResponse<Map<String, Object>> errorResponse = McpResponse.error("服务暂不可用");
                errorResponse.setData(data);
                return ResponseEntity.ok(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> data = new HashMap<>();
            data.put("available", false);
            McpResponse<Map<String, Object>> errorResponse = McpResponse.error("健康检查异常: " + e.getMessage());
            errorResponse.setData(data);
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    /**
     * 验证请求的签名和时间戳
     * @param params 请求参数
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 是否验证通过
     */
    private boolean verifyRequest(Map<String, Object> params, String signature, Long timestamp) {
        // 在开发环境下，可以跳过签名验证
        if ("default-secret".equals(appSecret)) {
            return true;
        }
        
        // 验证时间戳是否在有效期内
        if (timestamp == null || !MiniProgramSecurityUtil.isRequestInTimeWindow(timestamp, requestTimeout)) {
            return false;
        }
        
        // 验证签名
        return MiniProgramSecurityUtil.verifySignature(params, signature, appSecret);
    }
}