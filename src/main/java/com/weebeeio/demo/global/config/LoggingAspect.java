package com.weebeeio.demo.global.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.weebeeio.demo.domain.login.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Configuration
public class LoggingAspect {
    
    @Around("execution(* com.weebeeio.demo.domain.*.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String requestId = UUID.randomUUID().toString();
        
        // MDC에 요청 ID 설정
        MDC.put("requestId", requestId);
        
        // 현재 요청 정보 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            MDC.put("method", request.getMethod());
            MDC.put("uri", request.getRequestURI());
            MDC.put("ip", request.getRemoteAddr());
        }
        
        // 인증된 사용자 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            MDC.put("userId", String.valueOf(user.getUserId()));
        }
        
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        // 메소드 파라미터 로깅 (민감정보 제외)
        logger.info("Request [{}]: Calling {}.{} with arguments: {}", 
                requestId, joinPoint.getTarget().getClass().getSimpleName(), 
                methodName, Arrays.toString(args));
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 실제 메소드 실행
            Object result = joinPoint.proceed();
            
            // 응답 시간 계산
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.info("Response [{}]: {}.{} completed in {}ms", 
                    requestId, joinPoint.getTarget().getClass().getSimpleName(), 
                    methodName, executionTime);
            
            return result;
            
        } catch (Exception e) {
            // 예외 발생 시 로깅
            logger.error("Exception [{}]: {}.{} threw exception: {}", 
                    requestId, joinPoint.getTarget().getClass().getSimpleName(), 
                    methodName, e.getMessage(), e);
            throw e;
        } finally {
            // MDC 정리
            MDC.clear();
        }
    }
    
    @Around("execution(* com.weebeeio.demo.domain.*.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String methodName = joinPoint.getSignature().getName();
        
        // 서비스 메소드 호출 로깅
        logger.debug("Service: Calling {}.{}", 
                joinPoint.getTarget().getClass().getSimpleName(), methodName);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            
            // 응답 시간 계산
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.debug("Service: {}.{} completed in {}ms", 
                    joinPoint.getTarget().getClass().getSimpleName(), methodName, executionTime);
            
            return result;
        } catch (Exception e) {
            logger.error("Service Exception: {}.{} threw exception: {}", 
                    joinPoint.getTarget().getClass().getSimpleName(), methodName, e.getMessage(), e);
            throw e;
        }
    }
}