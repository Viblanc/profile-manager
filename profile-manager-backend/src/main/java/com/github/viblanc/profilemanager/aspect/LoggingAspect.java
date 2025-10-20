package com.github.viblanc.profilemanager.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	@Before("execution(* com.github.viblanc.profilemanager.service.*.*(..))")
	public void logBeforeMethod(JoinPoint joinPoint) {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Entering method: {}", joinPoint.getSignature().getName());
	}
	
	@AfterReturning(pointcut = "execution(* com.github.viblanc.profilemanager.service.*.*(..))", returning = "result")
	public void logAfterMethod(JoinPoint joinPoint, Object result) {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.info("Method {} executed with result: {}", joinPoint.getSignature().getName(), result);
	}
	
	@AfterThrowing(pointcut = "execution(* com.github.viblanc.profilemanager.service.*.*(..))", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		logger.warn("Exception in method {} with message: {}", joinPoint.getSignature().getName(), ex.getMessage());
	}
	
}
