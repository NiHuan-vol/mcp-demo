package com.demo.core.config;

import com.demo.core.filter.TextContentFilter;
import com.demo.core.registry.ModelInfo;
import com.demo.core.registry.ModelRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用程序初始化器，用于在应用启动时初始化各个组件
 */
@Component
public class ApplicationInitializer implements ApplicationRunner {
    
    private final ModelRegistry modelRegistry;
    private final TextContentFilter textContentFilter;
    
    public ApplicationInitializer(ModelRegistry modelRegistry, TextContentFilter textContentFilter) {
        this.modelRegistry = modelRegistry;
        this.textContentFilter = textContentFilter;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化内容过滤器
        initializeContentFilters();
        
        // 注册默认模型
        registerDefaultModels();
    }
    
    /**
     * 初始化内容过滤器
     */
    private void initializeContentFilters() {
        textContentFilter.initialize();
        // 可以初始化更多过滤器
    }
    
    /**
     * 注册默认模型
     */
    private void registerDefaultModels() {
        // 示例：注册一个文本生成模型
        registerTextGenerationModel();
        
        // 示例：注册一个图像生成模型
        registerImageGenerationModel();
        
        // 示例：注册一个对话模型
        registerChatModel();
        
        // 示例：注册一个向量搜索模型
        registerVectorSearchModel();
    }
    
    /**
     * 注册文本生成模型
     */
    private void registerTextGenerationModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxTokens", 1000);
        config.put("temperature", 0.7);
        config.put("topP", 0.95);
        
        ModelInfo textModel = ModelInfo.builder()
                .modelId("text-gen-001")
                .modelName("文本生成模型")
                .description("用于生成文本内容的AI模型")
                .modelType("text")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(textModel.getModelId(), textModel);
    }
    
    /**
     * 注册图像生成模型
     */
    private void registerImageGenerationModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("size", "1024x1024");
        config.put("quality", "standard");
        
        ModelInfo imageModel = ModelInfo.builder()
                .modelId("image-gen-001")
                .modelName("图像生成模型")
                .description("用于生成图像内容的AI模型")
                .modelType("image")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(imageModel.getModelId(), imageModel);
    }
    
    /**
     * 注册对话模型
     */
    private void registerChatModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxContextSize", 4096);
        config.put("temperature", 0.8);
        
        ModelInfo chatModel = ModelInfo.builder()
                .modelId("chat-001")
                .modelName("对话模型")
                .description("用于进行对话交互的AI模型")
                .modelType("chat")
                .provider("demo-provider")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(chatModel.getModelId(), chatModel);
    }
    
    /**
     * 注册向量搜索模型
     */
    private void registerVectorSearchModel() {
        Map<String, Object> config = new HashMap<>();
        config.put("defaultCollection", "default_collection");
        config.put("topK", 5);
        config.put("dimension", 768);
        
        ModelInfo vectorModel = ModelInfo.builder()
                .modelId("vector-search-001")
                .modelName("向量搜索模型")
                .description("用于在向量数据库中搜索相似内容的模型")
                .modelType("vector")
                .provider("milvus")
                .version("1.0")
                .registeredAt(LocalDateTime.now())
                .configuration(config)
                .enabled(true)
                .build();
        
        modelRegistry.registerModel(vectorModel.getModelId(), vectorModel);
    }
}