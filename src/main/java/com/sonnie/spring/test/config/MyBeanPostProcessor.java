package com.sonnie.spring.test.config;

import com.sonnie.spring.beans.annotation.Component;
import com.sonnie.spring.beans.config.BeanPostProcessor;
import com.sonnie.spring.test.annotation.Aop;
import com.sonnie.spring.test.annotation.Print;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className MyBeanPostProcessor
 * @description
 * @date 2022/1/9 20:30
 */
@SuppressWarnings("all")
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) {
        if (bean.getClass().isAnnotationPresent(Print.class))
            System.out.println("======postProcessorBeforeInitializtion——》print——》" + beanName);
        return BeanPostProcessor.super.postProcessorBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) {
        if (bean.getClass().isAnnotationPresent(Aop.class)) {
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("===postProcessorAfterInitialization===》" + beanName);
                    return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        }
        return BeanPostProcessor.super.postProcessorAfterInitialization(bean, beanName);
    }
}
