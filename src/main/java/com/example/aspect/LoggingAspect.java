package com.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String endpoint = request.getRequestURI();
        String method = request.getMethod();

        logger.info("Request received", 
            "method", method,
            "endpoint", endpoint,
            "class", className,
            "method", methodName,
            "args", Arrays.toString(joinPoint.getArgs())
        );

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            logger.info("Request completed",
                "method", method,
                "endpoint", endpoint,
                "duration", duration,
                "status", "success"
            );

            return result;
        } catch (Exception e) {
            logger.error("Request failed",
                "method", method,
                "endpoint", endpoint,
                "error", e.getMessage(),
                "exception", e.getClass().getName()
            );
            throw e;
        }
    }
} 