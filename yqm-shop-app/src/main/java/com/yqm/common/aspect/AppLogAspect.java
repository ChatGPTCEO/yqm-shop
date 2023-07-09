/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.common.aspect;

import com.yqm.common.bean.LocalUser;
import com.yqm.logging.domain.Log;
import com.yqm.logging.service.LogService;
import com.yqm.utils.RequestHolder;
import com.yqm.utils.StringUtils;
import com.yqm.utils.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weiximei
 * @date 2018-11-24
 */
@Component
@Aspect
@Slf4j
public class AppLogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public AppLogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.yqm.logging.aop.log.AppLog)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        Log log = new Log("INFO",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.saveApp(getUsername(),
                StringUtils.getIp(RequestHolder.getHttpServletRequest()),joinPoint,
                log,getUid());
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log("ERROR",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.save(getUsername(),
                StringUtils.getIp(RequestHolder.getHttpServletRequest()),
                (ProceedingJoinPoint)joinPoint, log,getUid());
    }

    public String getUsername() {
        try {
            return LocalUser.getUser().getUsername();
        }catch (Exception e){
            return "";
        }
    }

    public Long getUid(){
        try {
            return LocalUser.getUser().getUid();
        }catch (Exception e){
            return 0L;
        }
    }
}