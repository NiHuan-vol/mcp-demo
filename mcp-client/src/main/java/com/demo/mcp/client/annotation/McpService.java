package com.demo.mcp.client.annotation;

import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

/**
 * MCP服务注解，用于标记需要作为MCP服务代理的接口
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface McpService {

    /**
     * 服务ID，用于唯一标识MCP服务
     */
    @AliasFor("serviceId")
    String value() default "";

    /**
     * 服务ID，用于唯一标识MCP服务
     */
    @AliasFor("value")
    String serviceId() default "";

    /**
     * 服务版本
     */
    String version() default "1.0.0";

    /**
     * 服务描述
     */
    String description() default "";

    /**
     * 是否启用熔断
     */
    boolean fallbackEnabled() default false;

    /**
     * 熔断实现类
     */
    Class<?> fallbackClass() default void.class;
}