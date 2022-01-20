package com.sonnie.spring.test.service;

import com.sonnie.spring.beans.annotation.Component;
import com.sonnie.spring.beans.annotation.Scope;
import com.sonnie.spring.beans.aware.BeanNameAware;
import com.sonnie.spring.beans.config.InitializtionBean;
import com.sonnie.spring.beans.enums.ScopeEnum;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className OrderServiceImpl
 * @description
 * @date 2022/1/9 16:14
 */
@Scope(ScopeEnum.PROTOTYPE)
@Component("orderService")
public class OrderServiceImpl implements OrderService, InitializtionBean, BeanNameAware {
    public OrderServiceImpl() {
        System.out.println("===执行OrderService构造函数===");
    }

    @Override
    public void test() {
        System.out.println("===OrderService===");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("===OrderService——>InitializtionBean===");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("======OrderService——>beanName：" + beanName);
    }
}
