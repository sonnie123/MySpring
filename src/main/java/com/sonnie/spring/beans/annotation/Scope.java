package com.sonnie.spring.beans.annotation;

import com.sonnie.spring.beans.enums.ScopeEnum;

import java.lang.annotation.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className Scope
 * @description
 * @date 2022/1/9 17:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    ScopeEnum value() default ScopeEnum.SINGLETON;
}
