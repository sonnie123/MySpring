package com.sonnie.spring.beans.config;

import com.sonnie.spring.beans.enums.ScopeEnum;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className BeanDefinition
 * @description
 * @date 2022/1/9 17:21
 */
public class BeanDefinition {

    private Class type;

    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    private ScopeEnum scope;
    private boolean isLazy;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
