package com.sonnie.spring.test;

import com.sonnie.spring.test.config.BeanConfig;
import com.sonnie.spring.test.service.OrderService;
import com.sonnie.spring.test.service.UserService;
import com.sonnie.spring.context.SonnieApplicationContext;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className SpringTest
 * @description
 * @date 2022/1/9 16:01
 */
public class SpringTest {

    public static void main(String[] args) {
        SonnieApplicationContext context = new SonnieApplicationContext(BeanConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        userService.test();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        OrderService orderService1 = (OrderService) context.getBean("orderService");
        System.out.println(orderService1);
        OrderService orderService2 = (OrderService) context.getBean("orderService");
        System.out.println(orderService2);
    }
}
