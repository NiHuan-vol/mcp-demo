package com.demo.mcp.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * MCP客户端的默认实现，基于WebClient实现服务代理功能
 */
@Slf4j
public class DefaultMcpClient implements McpClient {

    private final McpClientConfig config;
    private WebClient webClient;
    private boolean initialized = false;

    public DefaultMcpClient(McpClientConfig config) {
        this.config = config;
    }

    @Override
    public synchronized void initialize() {
        if (initialized) {
            log.warn("MCP client is already initialized");
            return;
        }

        // 构建WebClient实例
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse());

        // 配置认证信息
        if (config.getAuthToken() != null && !config.getAuthToken().isEmpty()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAuthToken());
        }

        if (config.getApiKey() != null && !config.getApiKey().isEmpty()) {
            builder.defaultHeader("X-API-Key", config.getApiKey());
        }

        // 配置超时时间
        builder.clientConnector(new reactor.netty.http.client.HttpClient()
                .tcpConfiguration(tcpClient -> tcpClient
                        .option(reactor.netty.tcp.TcpClient.OPTION_CONNECT_TIMEOUT_MILLIS, 
                                Math.toIntExact(config.getConnectTimeout().toMillis()))
                        .responseTimeout(config.getReadTimeout())));

        this.webClient = builder.build();
        this.initialized = true;

        log.info("MCP client initialized successfully with base URL: {}", config.getBaseUrl());
    }

    @Override
    public synchronized void shutdown() {
        if (!initialized) {
            log.warn("MCP client is not initialized");
            return;
        }

        // WebClient不需要显式关闭
        this.initialized = false;
        log.info("MCP client shutdown successfully");
    }

    @Override
    public boolean isAvailable() {
        return initialized && webClient != null;
    }

    @Override
    public boolean isServiceAvailable() {
        if (!isAvailable()) {
            return false;
        }

        try {
            // 发送轻量级请求检查服务是否可用
            String healthCheckUrl = "/health";
            Boolean result = webClient.get()
                    .uri(healthCheckUrl)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return result != null && result;
        } catch (Exception e) {
            log.error("Service health check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public CompletableFuture<Object> callServiceAsync(String serviceId, String methodName, Map<String, Object> params) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        
        try {
            // 使用现有的invokeService方法并转换为CompletableFuture
            invokeService(serviceId, methodName, params)
                    .subscribe(
                            result -> future.complete(result),
                            error -> future.completeExceptionally(error)
                    );
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }

    @Override
    public CompletableFuture<Object> callModelAsync(String modelId, Object request) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        
        try {
            // 使用现有的invokeModel方法并转换为CompletableFuture
            invokeModel(modelId, request)
                    .subscribe(
                            result -> future.complete(result),
                            error -> future.completeExceptionally(error)
                    );
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }

    @Override
    public boolean sendEvent(String eventType, Map<String, Object> eventData) {
        try {
            // 同步发送事件并等待结果
            Boolean result = publishEvent(eventType, eventData)
                    .timeout(Duration.ofSeconds(10))
                    .block();
            return result != null && result;
        } catch (Exception e) {
            log.error("Failed to send event {}: {}", eventType, e.getMessage());
            return false;
        }
    }

    @Override
    public <T> Mono<T> invokeService(String serviceId, String methodName, Map<String, Object> params) {
        ensureInitialized();

        if (serviceId == null || serviceId.isEmpty() || methodName == null || methodName.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Service ID and method name cannot be empty"));
        }

        String url = "/services/" + serviceId + "/methods/" + methodName;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("serviceId", serviceId);
        requestBody.put("methodName", methodName);
        requestBody.put("params", params != null ? params : new HashMap<>());
        requestBody.put("requestId", UUID.randomUUID().toString());

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> {
                    // 简单的类型转换，实际应用中可能需要更复杂的处理
                    @SuppressWarnings("unchecked")
                    T typedResponse = (T) response;
                    return typedResponse;
                })
                .retryWhen(retryStrategy())
                .onErrorResume(e -> {
                    log.error("Error invoking service {} method {}: {}", serviceId, methodName, e.getMessage());
                    return Mono.error(new McpClientException("Failed to invoke service", e));
                });
    }

    @Override
    public <T> Mono<T> invokeModel(String modelId, Object request) {
        ensureInitialized();

        if (modelId == null || modelId.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Model ID cannot be empty"));
        }

        String url = "/models/" + modelId + "/invoke";

        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class)
                .map(response -> {
                    @SuppressWarnings("unchecked")
                    T typedResponse = (T) response;
                    return typedResponse;
                })
                .retryWhen(retryStrategy())
                .onErrorResume(e -> {
                    log.error("Error invoking model {}: {}", modelId, e.getMessage());
                    return Mono.error(new McpClientException("Failed to invoke model", e));
                });
    }

    @Override
    public Mono<Boolean> publishEvent(String eventType, Map<String, Object> eventData) {
        ensureInitialized();

        if (eventType == null || eventType.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Event type cannot be empty"));
        }

        String url = "/events/publish";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventType", eventType);
        requestBody.put("eventData", eventData != null ? eventData : new HashMap<>());
        requestBody.put("eventId", UUID.randomUUID().toString());
        requestBody.put("timestamp", System.currentTimeMillis());

        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(retryStrategy())
                .onErrorResume(e -> {
                    log.error("Error publishing event {}: {}", eventType, e.getMessage());
                    return Mono.error(new McpClientException("Failed to publish event", e));
                });
    }

    @Override
    public McpClientConfig getConfig() {
        return config;
    }

    // 确保客户端已初始化
    private void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }

    // 重试策略
    private Retry retryStrategy() {
        return Retry.backoff(config.getMaxRetries(), config.getRetryInterval())
                .filter(throwable -> throwable instanceof McpClientException || 
                        throwable instanceof java.io.IOException)
                .onRetryExhaustedThrow((spec, signal) -> 
                        new McpClientException("Retry exhausted", signal.failure()));
    }

    // 请求日志过滤器
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers().forEach((name, values) -> 
                        values.forEach(value -> log.debug("Header: {}={}", name, value)));
            }
            return Mono.just(clientRequest);
        });
    }

    // 响应日志过滤器
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                log.debug("Response status code: {}", clientResponse.statusCode());
                clientResponse.headers().asHttpHeaders().forEach((name, values) -> 
                        values.forEach(value -> log.debug("Response header: {}={}", name, value)));
            }
            return Mono.just(clientResponse);
        });
    }
}