package com.demo.core.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 过滤结果类，存储内容过滤的结果信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterResult {
    // 是否通过过滤
    private boolean passed;
    
    // 过滤原因
    private String reason;
    
    // 过滤出的风险项列表
    private List<RiskItem> riskItems;
    
    // 过滤后的内容（如果有修改）
    private String filteredContent;
    
    /**
     * 风险项类，存储具体的风险信息
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RiskItem {
        // 风险类型
        private String riskType;
        
        // 风险级别
        private RiskLevel riskLevel;
        
        // 风险内容
        private String content;
        
        // 建议处理方式
        private String recommendation;
        
        /**
         * 风险级别枚举
         */
        public enum RiskLevel {
            LOW,    // 低风险
            MEDIUM, // 中风险
            HIGH    // 高风险
        }
    }
}