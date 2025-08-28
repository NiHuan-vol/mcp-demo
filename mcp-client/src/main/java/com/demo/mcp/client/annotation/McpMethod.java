package com.demo.mcp.client.annotation;

import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

/**
 * MCP方法注解，用于标记需要作为MCP服务方法代理的接口方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface McpMethod {

    /**
     * 方法名称，用于映射到MCP服务的具体方法
     */
    @AliasFor("methodName")
    String value() default "";

    /**
     * 方法名称，用于映射到MCP服务的具体方法
     */
    @AliasFor("value")
    String methodName() default "";

    /**
     * 请求超时时间（毫秒），默认30000毫秒（30秒）
     */
    int timeout() default 30000;

    /**
     * 重试次数，默认0次（不重试）
     */
    int retryCount() default 0;

    /**
     * 重试间隔时间（毫秒），默认1000毫秒（1秒）
     */
    int retryInterval() default 1000;

    /**
     * 是否异步执行
     */
    boolean async() default false;

    /**
     * 自定义请求映射，将方法参数映射到请求参数
     */
    String[] paramMappings() default {};
}