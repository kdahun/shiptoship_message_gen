package com.all4land.generator.system.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	//
	private static ApplicationContext applicationContext;
	private static DefaultListableBeanFactory defaultListableBeanFactory;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		// TODO Auto-generated method stub
		applicationContext = ctx;
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
	}
	
	public static ApplicationContext getApplicationContext() {
		//
		return applicationContext;
	}

	public static DefaultListableBeanFactory getDefaultListableBeanFactory() {
		//
		return defaultListableBeanFactory;
	}
}
