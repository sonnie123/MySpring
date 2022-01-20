package com.sonnie.spring.context;

import com.sonnie.spring.beans.annotation.Component;
import com.sonnie.spring.beans.annotation.ComponentScan;
import com.sonnie.spring.beans.annotation.Lazy;
import com.sonnie.spring.beans.annotation.Scope;
import com.sonnie.spring.beans.config.BeanDefinition;
import com.sonnie.spring.beans.config.BeanPostProcessor;
import com.sonnie.spring.beans.enums.ScopeEnum;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author guozewen01-lhq
 * @version 1.0
 * @className ClassPathBeanDefinitionScanner
 * @description
 * @date 2022/1/9 16:30
 */
@SuppressWarnings("all")
public class ClassPathBeanDefinitionScanner {

    void scan(List<BeanPostProcessor> beanPostProcessorList, Map<String, BeanDefinition> beanDefinitionMap, Class clazz) {

        //是否被@ComponentScan标注
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            //获取bean的扫描路径
            ComponentScan componentScan = (ComponentScan) clazz.getAnnotation(ComponentScan.class);
            String[] path = componentScan.value();
            for (String p : path) {
                p = p.replace(".", "/");
                //获取类路径
                ClassLoader classLoader = SonnieApplicationContext.class.getClassLoader();
                File file = new File(classLoader.getResource(p).getFile());
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        String absolutePath = getAbsolutePath(f);
                        try {
                            //通过类路径生成类对象
                            Class<?> loadClass = classLoader.loadClass(absolutePath);
                            if (loadClass.isAnnotationPresent(Component.class)) {
                                encapBeanDefinition(beanDefinitionMap, loadClass, getBeanName(loadClass));
                                encapBeanPostProcessor(beanPostProcessorList, loadClass);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void encapBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList, Class<?> loadClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //BeanPostProcessor
        if (BeanPostProcessor.class.isAssignableFrom(loadClass))
            beanPostProcessorList.add((BeanPostProcessor) loadClass.getConstructor().newInstance());
    }

    private void encapBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, Class<?> loadClass, String beanName) {
        //封装beanDefinition
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setType(loadClass);
        if (loadClass.isAnnotationPresent(Lazy.class))
            beanDefinition.setLazy(true);
        if (loadClass.isAnnotationPresent(Scope.class)) {
            Scope scope = (Scope) loadClass.getAnnotation(Scope.class);
            beanDefinition.setScope(scope.value());
        } else
            beanDefinition.setScope(ScopeEnum.SINGLETON);

        beanDefinitionMap.put(beanName, beanDefinition);
    }

    private String getBeanName(Class<?> loadClass) {
        Component component = loadClass.getAnnotation(Component.class);
        //获取beanName
        String beanName = component.value();
        if ("".equals(beanName))
            beanName = Introspector.decapitalize(loadClass.getSimpleName());
        return beanName;
    }

    private String getAbsolutePath(File f) {
        String absolutePath = f.getAbsolutePath();
        absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
        //生成类路径
        absolutePath = absolutePath.replace("\\", ".");
        return absolutePath;
    }
}
