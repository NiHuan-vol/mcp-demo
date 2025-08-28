package com.demo.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * MyBatis-Plus配置类
 */
@Configuration
@MapperScan("com.demo.core.repository")
public class MyBatisPlusConfig {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 配置SqlSessionFactory以解决Spring Boot 3.x兼容性问题
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(resolveMapperLocations());
        factoryBean.setTypeAliasesPackage("com.demo.core.entity");
        // factoryBean.setPlugins(mybatisPlusInterceptor());
        return factoryBean.getObject();
    }
    
    /**
     * 配置SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 解析Mapper XML文件位置
     */
    private Resource[] resolveMapperLocations() throws IOException {
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        return resourceResolver.getResources("classpath:mapper/**/*.xml");
    }
}