package com.demo.mcp.client.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

/**
 * MCP客户端控制器配置类
 * 配置CORS、过滤器等Web相关设置
 */
@Configuration
public class McpControllerConfig implements WebFluxConfigurer {

    /**
     * 配置CORS跨域支持
     * 允许小程序从不同域名访问服务
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList(
                "Origin", "X-Requested-With", "Content-Type", "Accept", 
                "Authorization", "X-Token", "X-Signature"));
        corsConfig.setExposedHeaders(Arrays.asList("X-Token", "X-Timestamp"));
        corsConfig.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/mcp/mini-program/**", corsConfig);

        return new CorsWebFilter(source);
    }

    /**
     * 请求日志过滤器
     * 记录小程序请求的基本信息
     */
    @Bean
    public WebFilter loggingFilter() {
        return (exchange, chain) -> {
            ServerWebExchange serverWebExchange = exchange;
            String path = serverWebExchange.getRequest().getPath().toString();
            String method = serverWebExchange.getRequest().getMethod().name();
            String clientIp = serverWebExchange.getRequest().getRemoteAddress() != null 
                    ? serverWebExchange.getRequest().getRemoteAddress().getAddress().getHostAddress() 
                    : "unknown";

            // 记录请求开始时间
            long startTime = System.currentTimeMillis();

            // 处理请求并记录响应时间
            return chain.filter(serverWebExchange)
                    .doFinally(signalType -> {
                        long duration = System.currentTimeMillis() - startTime;
                        int statusCode = serverWebExchange.getResponse().getStatusCode() != null 
                                ? serverWebExchange.getResponse().getStatusCode().value() 
                                : 0;
                        // 这里可以添加日志记录逻辑，如使用SLF4J等日志框架
                        // logger.info("Request: {} {} from {} completed with status {} in {}ms", 
                        //         method, path, clientIp, statusCode, duration);
                    });
        };
    }

    /**
     * 错误处理过滤器
     * 统一处理小程序接口的异常情况
     */
    @Bean
    public WebFilter errorHandlingFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange)
                    .onErrorResume(error -> {
                        // 这里可以添加更详细的错误处理逻辑
                        // 记录错误信息并返回统一的错误响应格式
                        return Mono.error(error);
                    });
        };
    }
}