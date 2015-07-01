package org.jaeyo.webscripter.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SpringBeans implements ApplicationContextAware{
	private static ApplicationContext context;
	
	public static <T> T getBean(Class<T> clazz){
		return context.getBean(clazz);
	} //getBean

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	} //setApplicationContext
} // class