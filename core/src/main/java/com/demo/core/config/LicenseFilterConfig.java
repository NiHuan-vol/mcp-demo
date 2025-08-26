package com.demo.core.config;

import com.demo.core.license.LicenseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * License过滤器配置类，用于注册License拦截器
 */
@Configuration
public class LicenseFilterConfig {
    
    @Autowired
    private LicenseInterceptor licenseInterceptor;
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 为所有路由添加License拦截器
                .route("global-license-filter", r -> r
                        .path("/**")
                        .filters(f -> f.filter(licenseInterceptor))
                        .uri("forward:/")
                )
                .build();
    }
}