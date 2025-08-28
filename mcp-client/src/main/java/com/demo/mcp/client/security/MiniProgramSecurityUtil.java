package com.demo.mcp.client.security;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 小程序安全工具类
 * 提供签名验证、数据加密等安全相关功能
 */
public class MiniProgramSecurityUtil {
    
    /**
     * 默认的签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5";
    
    /**
     * 验证小程序请求的签名
     * @param params 请求参数
     * @param signature 客户端提供的签名
     * @param appSecret 小程序的密钥
     * @return 签名是否有效
     */
    public static boolean verifySignature(Map<String, Object> params, String signature, String appSecret) {
        if (params == null || !StringUtils.hasText(signature) || !StringUtils.hasText(appSecret)) {
            return false;
        }
        
        // 生成待签名的字符串
        String signString = generateSignString(params, appSecret);
        
        // 计算签名
        String generatedSignature = generateSignature(signString);
        
        // 比较签名
        return signature.equalsIgnoreCase(generatedSignature);
    }
    
    /**
     * 生成签名字符串
     * @param params 参数Map
     * @param appSecret 应用密钥
     * @return 待签名的字符串
     */
    private static String generateSignString(Map<String, Object> params, String appSecret) {
        // 使用TreeMap保证参数排序一致
        TreeMap<String, Object> sortedParams = new TreeMap<>(params);
        
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            // 跳过签名参数本身
            if ("signature".equals(entry.getKey())) {
                continue;
            }
            
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && StringUtils.hasText(value.toString())) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        
        // 添加应用密钥
        sb.append("appSecret=").append(appSecret);
        
        return sb.toString();
    }
    
    /**
     * 根据签名算法生成签名
     * @param signString 待签名的字符串
     * @return 生成的签名
     */
    private static String generateSignature(String signString) {
        try {
            MessageDigest md = MessageDigest.getInstance(SIGNATURE_ALGORITHM);
            byte[] bytes = md.digest(signString.getBytes());
            
            // 转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
    
    /**
     * 生成随机的安全令牌
     * @param length 令牌长度
     * @return 生成的令牌
     */
    public static String generateToken(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            token.append(chars.charAt(index));
        }
        
        return token.toString();
    }
    
    /**
     * 检查请求是否在有效期内
     * @param timestamp 请求时间戳
     * @param validDuration 有效时长（毫秒）
     * @return 是否在有效期内
     */
    public static boolean isRequestInTimeWindow(long timestamp, long validDuration) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - timestamp;
        
        return timeDiff >= 0 && timeDiff <= validDuration;
    }
}