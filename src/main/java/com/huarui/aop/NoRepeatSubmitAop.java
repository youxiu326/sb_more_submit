package com.huarui.aop;

import javax.servlet.http.HttpServletRequest;
import com.huarui.util.NoRepeatSubmit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.google.common.cache.Cache;
import java.util.concurrent.TimeUnit;


/**
 * @功能描述 aop解析注解
 * @author www.gaozz.club
 * @date 2018-08-26
 */
@Aspect
@Component
public class NoRepeatSubmitAop {

    private Log logger = LogFactory.getLog(getClass());

    /**
     * 使用内存缓存
     */
    @Autowired
    private Cache<String, Integer> cache;

    /*
    @Around("execution(* com.huarui.controller.*Controller.*(..)) && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, NoRepeatSubmit nrs) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            HttpServletRequest request = attributes.getRequest();
            String key = sessionId + "-" + request.getServletPath();
            if (cache.getIfPresent(key) == null) {// 如果缓存中有这个url视为重复提交
                Object o = pjp.proceed();
                cache.put(key, 0);
                return o;
            } else {
                logger.error("重复提交");
                return null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("验证重复提交时出现未知异常!");
            return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
        }

    }
*/

    /**
     *
     * 以下为新版内容:解决了程序集群部署时请求可能会落到多台机器上的问题,把内存缓存换成了redis
     *
     * 使用redis当做缓存
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Around("execution(* com.huarui.controller.*Controller.*(..)) && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, NoRepeatSubmit nrs) {
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            HttpServletRequest request = attributes.getRequest();
            String key = sessionId + "-" + request.getServletPath();
            if (opsForValue.get(key) == null) {// 如果缓存中有这个url视为重复提交
                Object o = pjp.proceed();
                opsForValue.set(key, 0, 2, TimeUnit.SECONDS);
                return o;
            } else {
                logger.error("重复提交");
                return null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("验证重复提交时出现未知异常!");
            return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
        }

    }

}