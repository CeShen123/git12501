package com.yc.CglibProxy;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyTooll implements MethodInterceptor {
    private Object target;

    public CglibProxyTooll(Object target) {
        this.target = target;
    }

    //生成代理对象的方法
    public Object createProxy(){
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback( this);
        Object proxy =enhancer.create();
        return proxy;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().startsWith("add")){
            showHello();//加入前置增强
        }
        //         orderBizImpl.findOrder()
        Object returnValue=method.invoke(target,objects); //调用目标类的方法
        return returnValue;
    }
    public  void showHello(){
        System.out.println("hello");
    }
}
