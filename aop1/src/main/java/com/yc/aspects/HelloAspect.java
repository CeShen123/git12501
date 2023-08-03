package com.yc.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(value = 100)
public class HelloAspect {
@Pointcut("execution(* com.yc.biz.*.findPid(..))")
    private void a(){}

    @Around("a()")
    public Object show(ProceedingJoinPoint jp){
        System.out.println("HelloAspect的show的前面......");
        Object obj =null;
        try {
            obj=jp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("HelloAsepect的show的后面===========");
        return  obj;
    }
}
