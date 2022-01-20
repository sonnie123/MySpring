package com.sonnie.spring.test.service;

import com.sonnie.spring.beans.annotation.Autowired;
import com.sonnie.spring.beans.annotation.Component;
import com.sonnie.spring.beans.aware.BeanNameAware;
import com.sonnie.spring.beans.config.InitializtionBean;
import com.sonnie.spring.test.annotation.Aop;
import com.sonnie.spring.test.annotation.Print;

import javax.annotation.PostConstruct;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className UserServiceImpl
 * @description
 * @date 2022/1/9 16:12
 */
@Aop
@Print
@Component("userService")
public class UserServiceImpl implements UserService, InitializtionBean, BeanNameAware {

    public UserServiceImpl() {
        System.out.println("===执行UserService构造函数===");
    }

    @Autowired("orderService")
    OrderServiceImpl orderService;

    @Override
    public void test() {
        System.out.println("========================");
        System.out.println("===UserService===");
        orderService.test();
        System.out.println("========================");
    }

    @PostConstruct
    public void postConstructTest(){
        System.out.println("===postConstruct===");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("===UserService——>InitializtionBean===");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("======UserService——>beanName：" + beanName);
    }
}
