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
            //获取扫描路径
            String[] path = componentScan.value();

            //遍历扫描路径
            for (String p : path) {
                p = p.replace(".", "/");
                //获取类加载器
                ClassLoader classLoader = SonnieApplicationContext.class.getClassLoader();
                //获取目录
                File file = new File(classLoader.getResource(p).getFile());
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        String classPath = getClassPath(f);
                        try {
                            //通过类路径获取类对象
                            Class<?> loadClass = classLoader.loadClass(classPath);

                            //通过@Component判断是否为spring bean
                            if (loadClass.isAnnotationPresent(Component.class)) {
                                registerBeanDefinition(beanDefinitionMap, loadClass, getBeanName(loadClass));
                                registerBeanPostProcessor(beanPostProcessorList, loadClass);
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

    /**
     * 注册beanPostProcessorList
     *
     * @param beanPostProcessorList
     * @param loadClass
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private void registerBeanPostProcessor(List<BeanPostProcessor> beanPostProcessorList, Class<?> loadClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (BeanPostProcessor.class.isAssignableFrom(loadClass))
            beanPostProcessorList.add((BeanPostProcessor) loadClass.getConstructor().newInstance());
    }

    /**
     * 注册beanDefinition
     *
     * @param beanDefinitionMap
     * @param loadClass
     * @param beanName
     */
    private void registerBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, Class<?> loadClass, String beanName) {

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

    /**
     * 获取bean名称
     *
     * @param loadClass
     * @return
     */
    private String getBeanName(Class<?> loadClass) {

        Component component = loadClass.getAnnotation(Component.class);
        String beanName = component.value();
        if ("".equals(beanName))
            beanName = Introspector.decapitalize(loadClass.getSimpleName());
        return beanName;
    }

    /**
     * 获取类路径
     *
     * @param f
     * @return
     */
    private String getClassPath(File f) {
        String absolutePath = f.getAbsolutePath();
        absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
        //生成类路径
        absolutePath = absolutePath.replace("\\", ".");
        return absolutePath;
    }
}
