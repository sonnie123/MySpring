package com.sonnie.spring.beans.annotation;

import java.lang.annotation.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className PostConstruct
 * @description
 * @date 2022/1/10 9:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface PostConstruct {
}
