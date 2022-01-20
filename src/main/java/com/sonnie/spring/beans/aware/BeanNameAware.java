package com.sonnie.spring.beans.aware;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className BeanNameAware
 * @description
 * @date 2022/1/9 18:39
 */
public interface BeanNameAware extends Aware{
    void setBeanName(String beanName);
}
