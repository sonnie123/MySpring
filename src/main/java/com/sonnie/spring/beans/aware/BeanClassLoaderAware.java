package com.sonnie.spring.beans.aware;


/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className BeanClassLoaderAware
 * @description
 * @date 2022/1/10 9:40
 */
public interface BeanClassLoaderAware extends Aware {
    void setBeanClassLoader(ClassLoader var1);
}
