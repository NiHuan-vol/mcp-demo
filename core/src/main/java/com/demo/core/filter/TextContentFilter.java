package com.demo.core.filter;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文本内容过滤器实现，用于过滤文本中的不安全内容
 */
@Component("textContentFilter")
public class TextContentFilter implements ContentFilter {
    
    private static final String FILTER_ID = "text_filter";
    private static final String FILTER_NAME = "文本内容过滤器";
    
    // 这里可以配置敏感词列表
    private List<String> sensitiveWords = new ArrayList<>();
    
    // 这里可以配置敏感正则表达式
    private List<Pattern> sensitivePatterns = new ArrayList<>();
    
    @Override
    public void initialize() {
        // 初始化敏感词和正则表达式
        // 实际应用中，这些配置可能会从数据库或配置文件中加载
        initSensitiveWords();
        initSensitivePatterns();
    }
    
    @Override
    public Mono<FilterResult> filter(String content) {
        if (content == null || content.isEmpty()) {
            return Mono.just(FilterResult.builder()
                    .passed(true)
                    .filteredContent(content)
                    .build());
        }
        
        List<FilterResult.RiskItem> riskItems = new ArrayList<>();
        String filteredContent = content;
        
        // 检测敏感词
        for (String sensitiveWord : sensitiveWords) {
            if (content.contains(sensitiveWord)) {
                riskItems.add(FilterResult.RiskItem.builder()
                        .riskType("sensitive_word")
                        .riskLevel(FilterResult.RiskItem.RiskLevel.MEDIUM)
                        .content(sensitiveWord)
                        .recommendation("移除敏感词")
                        .build());
                // 替换敏感词（实际应用中可能需要更复杂的替换策略）
                filteredContent = filteredContent.replace(sensitiveWord, "*".repeat(sensitiveWord.length()));
            }
        }
        
        // 检测敏感模式
        for (Pattern pattern : sensitivePatterns) {
            java.util.regex.Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String matchedContent = matcher.group();
                riskItems.add(FilterResult.RiskItem.builder()
                        .riskType("sensitive_pattern")
                        .riskLevel(FilterResult.RiskItem.RiskLevel.HIGH)
                        .content(matchedContent)
                        .recommendation("移除敏感模式")
                        .build());
                // 替换敏感模式（实际应用中可能需要更复杂的替换策略）
                filteredContent = filteredContent.replaceAll(pattern.pattern(), "***");
            }
        }
        
        boolean passed = riskItems.isEmpty() || riskItems.stream().allMatch(item -> 
                item.getRiskLevel() != FilterResult.RiskItem.RiskLevel.HIGH);
        
        return Mono.just(FilterResult.builder()
                .passed(passed)
                .reason(passed ? "内容安全" : "包含不安全内容")
                .riskItems(riskItems)
                .filteredContent(filteredContent)
                .build());
    }
    
    @Override
    public String getFilterId() {
        return FILTER_ID;
    }
    
    @Override
    public String getFilterName() {
        return FILTER_NAME;
    }
    
    @Override
    public Mono<FilterResult> filterAsync(String content) {
        // 简单实现：直接调用同步的filter方法
        return filter(content);
    }
    
    @Override
    public void shutdown() {
        // 清理资源
        sensitiveWords.clear();
        sensitivePatterns.clear();
    }
    
    /**
     * 初始化敏感词列表
     */
    private void initSensitiveWords() {
        // 示例敏感词，实际应用中可能会从配置中加载
        sensitiveWords.addAll(Arrays.asList(
                "敏感词1",
                "敏感词2"
                // 可以添加更多敏感词
        ));
    }
    
    /**
     * 初始化敏感正则表达式
     */
    private void initSensitivePatterns() {
        // 示例正则表达式，实际应用中可能会从配置中加载
        sensitivePatterns.add(Pattern.compile("[0-9]{17}[0-9Xx]")); // 身份证号
        sensitivePatterns.add(Pattern.compile("[0-9]{16,19}")); // 银行卡号
        // 可以添加更多正则表达式
    }
}