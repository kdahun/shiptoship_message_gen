package com.all4land.generator.system.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import com.all4land.generator.system.component.ApplicationContextProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanUtils {
	//
	public static Object getBean(String beanName) {
		//
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		return applicationContext.getBean(beanName);
	}
	
	public static void invoke(String beanName, Map<String, String> map) {
		//
		Object targetBean = getBean(beanName);
		Class<? extends Object> findClass = targetBean.getClass();
		Method method;
		try {
			method = findClass.getMethod("create", Map.class);
			method.invoke(targetBean, map);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
	}
	
	public static void registerBean(String beanName, Class<?> clz) {
		//
		DefaultListableBeanFactory defaultListableBeanFactory = ApplicationContextProvider.getDefaultListableBeanFactory();
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clz);
		defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
	}
	
	public static void getAllBeans() {
		//
		String[] allBeanNames = ApplicationContextProvider.getApplicationContext().getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
        	//
            log.info(beanName);
        }
	}
	
	public static void removeBean(String beanName) {
		//
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory();

	    if (beanFactory.containsBeanDefinition(beanName)) {
	        beanFactory.removeBeanDefinition(beanName);
	    } else {
	        log.warn("ApplicationContext에서 이름이 '{}'인 빈을 찾을 수 없습니다.", beanName);
	    }
	}
	
	public static boolean containsBean(String beanName) {
		//
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory();
		return beanFactory.containsBeanDefinition(beanName);
	}
}
