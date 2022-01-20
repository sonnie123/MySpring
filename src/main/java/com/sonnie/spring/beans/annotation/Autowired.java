package com.sonnie.spring.beans.annotation;

import java.lang.annotation.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className Autowired
 * @description
 * @date 2022/1/9 16:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Autowired {
    String value() default "";
}
