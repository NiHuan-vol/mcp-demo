package com.demo.recommendation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 * 统一处理所有控制器抛出的异常，提供友好的错误响应
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理服务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleServiceException(ServiceException ex, ServerWebExchange exchange) {
        logger.error("Service exception occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "服务异常",
                ex.getMessage());
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgumentException(IllegalArgumentException ex, ServerWebExchange exchange) {
        logger.error("Illegal argument exception occurred: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "参数错误",
                ex.getMessage());
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleNullPointerException(NullPointerException ex, ServerWebExchange exchange) {
        logger.error("Null pointer exception occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "系统错误",
                "系统内部发生错误，请稍后再试");
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * 处理网络请求异常
     */
    @ExceptionHandler(NetworkException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleNetworkException(NetworkException ex, ServerWebExchange exchange) {
        logger.error("Network exception occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "网络异常",
                ex.getMessage());
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE));
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleAccessDeniedException(AccessDeniedException ex, ServerWebExchange exchange) {
        logger.error("Access denied exception occurred: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "权限不足",
                ex.getMessage());
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN));
    }

    /**
     * 处理未找到资源异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleResourceNotFoundException(ResourceNotFoundException ex, ServerWebExchange exchange) {
        logger.error("Resource not found exception occurred: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "资源未找到",
                ex.getMessage());
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND));
    }

    /**
     * 处理所有其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        logger.error("Unexpected exception occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "系统错误",
                "系统内部发生未预期的错误，请联系管理员");
        
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * 创建统一的错误响应格式
     */
    private Map<String, Object> createErrorResponse(int status, String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", ""); // WebFlux中获取path需要特殊处理
        return errorResponse;
    }
}