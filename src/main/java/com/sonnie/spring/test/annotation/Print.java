package com.sonnie.spring.test.annotation;

import java.lang.annotation.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className Print
 * @description
 * @date 2022/1/9 20:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Print {
}
