package com.sonnie.spring.beans.aware;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className BeanFactoryAware
 * @description
 * @date 2022/1/10 9:42
 */
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}
