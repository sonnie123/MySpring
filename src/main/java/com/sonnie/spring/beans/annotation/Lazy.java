package com.sonnie.spring.beans.annotation;

import java.lang.annotation.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className Lazy
 * @description
 * @date 2022/1/9 17:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Lazy {
}
