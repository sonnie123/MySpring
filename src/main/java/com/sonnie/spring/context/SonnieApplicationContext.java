package com.sonnie.spring.context;

import com.sonnie.spring.beans.annotation.Autowired;
import com.sonnie.spring.beans.aware.BeanClassLoaderAware;
import com.sonnie.spring.beans.aware.BeanFactoryAware;
import com.sonnie.spring.beans.aware.BeanNameAware;
import com.sonnie.spring.beans.config.BeanDefinition;
import com.sonnie.spring.beans.config.BeanPostProcessor;
import com.sonnie.spring.beans.config.InitializtionBean;
import com.sonnie.spring.beans.enums.ScopeEnum;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<String, Object> singletonMap = new HashMap();
    private ClassPathBeanDefinitionScanner scanner;

    private List<BeanPostProcessor> getBeanPostProcessorList() {
        return beanPostProcessorList;
    }

    private Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    private Map<String, Object> getSingletonMap() {
        return singletonMap;
    }

    public SonnieApplicationContext(Class clazz) {
        scanner = new ClassPathBeanDefinitionScanner();
        this.scan(clazz);

        registry();
    }

    private void registry() {
        //遍历beanDefinitionMap
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            //获取beanName
            String beanName = entry.getKey();
            //获取BeanDefinition
            BeanDefinition beanDefinition = entry.getValue();

            //如果作用域为单例则将bean添加到单例bean集合中
            if (ScopeEnum.SINGLETON.equals(beanDefinition.getScope())) {
                //创建bean
                Object bean = createBean(beanName, beanDefinition);
                singletonMap.put(beanName, bean);
            }
        }
    }

    void scan(Class clazz) {
        Assert.notNull(clazz, "class不能为空");
        this.scanner.scan(beanPostProcessorList, beanDefinitionMap, clazz);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    public Object getBean(String beanName) {

        if (!beanDefinitionMap.containsKey(beanName))
            throw new NullPointerException();

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        return getBean(beanName, beanDefinition);
    }

    private Object getBean(String beanName, BeanDefinition beanDefinition) {
        if (ScopeEnum.SINGLETON.equals(beanDefinition.getScope())) {
            Object bean = singletonMap.get(beanName);

            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
                singletonMap.put(beanName, bean);
            }
            return bean;
        } else
            return createBean(beanName, beanDefinition);
    }

    Object createBean(String beanName, BeanDefinition beanDefinition) {

        Object instance = null;
        Class clazz = beanDefinition.getType();
        try {
            //实例化对象
            instance = clazz.getConstructor().newInstance();

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

            if (BeanClassLoaderAware.class.isAssignableFrom(clazz))
                ((BeanClassLoaderAware) instance).setBeanClassLoader(clazz.getClassLoader());

            if (BeanFactoryAware.class.isAssignableFrom(clazz))
                ((BeanFactoryAware) instance).setBeanFactory(null);

            for (BeanPostProcessor bp : beanPostProcessorList) {
                instance = bp.postProcessorBeforeInitialization(instance, beanName);
            }

            if (InitializtionBean.class.isAssignableFrom(clazz))
                ((InitializtionBean) instance).afterPropertiesSet();

            for (BeanPostProcessor bp : beanPostProcessorList) {
                instance = bp.postProcessorAfterInitialization(instance, beanName);
            }
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
}
