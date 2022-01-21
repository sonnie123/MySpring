package com.sonnie.spring.context;

import com.sonnie.spring.beans.annotation.Autowired;
import com.sonnie.spring.beans.aware.BeanNameAware;
import com.sonnie.spring.beans.config.BeanDefinition;
import com.sonnie.spring.beans.config.BeanPostProcessor;
import com.sonnie.spring.beans.config.InitializtionBean;
import com.sonnie.spring.beans.enums.ScopeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className SonnieApplicationContext
 * @description
 * @date 2022/1/9 16:19
 */
@SuppressWarnings("all")
public class SonnieApplicationContext {

    List<BeanPostProcessor> beanPostProcessorList = new ArrayList();
    Map<String, BeanDefinition> beanDefinitionMap = new HashMap();
    Map<String, Object> singletonObjects = new HashMap();

    private ClassPathBeanDefinitionScanner scanner;

    public SonnieApplicationContext(Class clazz) {
        scanner = new ClassPathBeanDefinitionScanner();
        this.scan(clazz);
        registrySingletonObjects();
    }

    /**
     * 调用扫描逻辑
     */
    void scan(Class clazz) {
        if ((clazz != null)) {
            this.scanner.scan(beanPostProcessorList, beanDefinitionMap, clazz);
            //打印beanName和beanDefinition
            beanDefinitionMap.forEach((key, value) -> System.out.println(key + " : " + value));
        } else {
            throw new RuntimeException("class不能为空");
        }
    }

    /**
     * 注册单例bean
     * 如果作用域为单例则创建bean
     * 并将bean添加到单例池中
     */
    private void registrySingletonObjects() {
        beanDefinitionMap.entrySet().stream()
                .filter(entry -> ScopeEnum.SINGLETON.equals(entry.getValue().getScope()))
                .forEach(entry -> singletonObjects.put(entry.getKey(), createBean(entry.getKey(), entry.getValue())));
    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName))
            throw new NullPointerException();

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        return getBean(beanName, beanDefinition);
    }

    private Object getBean(String beanName, BeanDefinition beanDefinition) {
        if (ScopeEnum.SINGLETON.equals(beanDefinition.getScope())) {
            Object bean = singletonObjects.get(beanName);

            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
            return bean;
        } else
            return createBean(beanName, beanDefinition);
    }

    /**
     * 创建bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    Object createBean(String beanName, BeanDefinition beanDefinition) {

        Object instance = null;
        Class clazz = beanDefinition.getType();
        try {
            //实例化对象
            instance = clazz.getConstructor().newInstance();

            //初始化之前
            instance = resolveBeforeInitialization(beanName, instance, clazz);

            //初始化时
            if (InitializtionBean.class.isAssignableFrom(clazz))
                ((InitializtionBean) instance).afterPropertiesSet();

            //初始化之后
            instance = resolveAfterInitialization(beanName, instance);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 初始化之前
     *
     * @param beanName
     * @param instance
     * @param clazz
     * @return
     * @throws IllegalAccessException
     */
    private Object resolveBeforeInitialization(String beanName, Object instance, Class clazz) throws IllegalAccessException {
        //依赖注入
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Autowired.class)) {
                f.setAccessible(true);
                f.set(instance, getBean(f.getName()));
            }
        }

        //执行Aware回调
        if (BeanNameAware.class.isAssignableFrom(clazz))
            ((BeanNameAware) instance).setBeanName(beanName);

        for (BeanPostProcessor bp : beanPostProcessorList) {
            instance = bp.postProcessorBeforeInitialization(instance, beanName);
        }
        return instance;
    }

    /**
     * 初始化之后
     *
     * @param beanName
     * @param instance
     * @return
     */
    private Object resolveAfterInitialization(String beanName, Object instance) {
        for (BeanPostProcessor bp : beanPostProcessorList) {
            instance = bp.postProcessorAfterInitialization(instance, beanName);
        }
        return instance;
    }
}
