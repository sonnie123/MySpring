package com.sonnie.spring.beans.config;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className BeanPostProcessor
 * @description
 * @date 2022/1/9 17:01
 */
public interface BeanPostProcessor {

    default Object postProcessorBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessorAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
