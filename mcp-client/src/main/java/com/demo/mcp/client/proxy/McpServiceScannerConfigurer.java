package com.demo.mcp.client.proxy;

import com.demo.mcp.client.annotation.McpService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * MCP服务扫描配置器，用于扫描带有@McpService注解的接口并注册为Spring Bean
 */
@Setter
@Slf4j
public class McpServiceScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private ResourceLoader resourceLoader;
    private String[] basePackages;
    private boolean registerBasePackageClasses = false;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (basePackages == null || basePackages.length == 0) {
            log.warn("No base packages specified for MCP service scanning. Scanning is disabled.");
            return;
        }

        try {
            // 扫描所有带@McpService注解的接口
            Set<Class<?>> serviceInterfaces = scanServiceInterfaces();
            
            // 为每个接口创建代理Bean定义
            for (Class<?> serviceInterface : serviceInterfaces) {
                registerServiceProxyBean(registry, serviceInterface);
            }
        } catch (Exception e) {
            log.error("Failed to scan MCP services: {}", e.getMessage(), e);
            throw new BeansException("Failed to scan MCP services", e) {
            };
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不需要实现
    }

    private Set<Class<?>> scanServiceInterfaces() throws IOException, ClassNotFoundException {
        Set<Class<?>> serviceInterfaces = new HashSet<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);

        for (String basePackage : basePackages) {
            if (!StringUtils.hasText(basePackage)) {
                continue;
            }

            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = resolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    
                    // 跳过内部类和非接口
                    if (metadataReader.getClassMetadata().isInterface() && !metadataReader.getClassMetadata().isAnnotation()) {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(McpService.class)) {
                            serviceInterfaces.add(clazz);
                            log.debug("Found MCP service interface: {}", className);
                        }
                    }
                }
            }
        }

        log.info("Scanned {} MCP service interfaces", serviceInterfaces.size());
        return serviceInterfaces;
    }

    private void registerServiceProxyBean(BeanDefinitionRegistry registry, Class<?> serviceInterface) {
        String beanName = generateBeanName(serviceInterface);
        if (registry.containsBeanDefinition(beanName)) {
            log.warn("Bean with name '{}' already exists. Skipping registration.", beanName);
            return;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(McpServiceProxyBean.class);
        builder.addPropertyValue("serviceInterface", serviceInterface.getName());
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        log.debug("Registered MCP service proxy bean: {} for interface: {}", beanName, serviceInterface.getName());
    }

    private String generateBeanName(Class<?> serviceInterface) {
        McpService annotation = serviceInterface.getAnnotation(McpService.class);
        String serviceId = annotation.serviceId();
        if (StringUtils.hasText(serviceId)) {
            return serviceId;
        }
        return StringUtils.uncapitalize(serviceInterface.getSimpleName());
    }
}