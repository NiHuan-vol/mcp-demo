package com.demo.core.vectorstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;

/**
 * Milvus向量数据库配置类
 */
@Configuration
public class MilvusVectorStoreConfig {

    @Value("${spring.ai.milvus.host:localhost}")
    private String host;

    @Value("${spring.ai.milvus.port:19530}")
    private Integer port;

    /**
     * 创建Milvus服务客户端
     */
    @Bean
    public MilvusServiceClient milvusServiceClient() {
        ConnectParam connectParam = ConnectParam.newBuilder()
                .withHost(host)
                .withPort(port)
                .build();
        return new MilvusServiceClient(connectParam);
    }
}