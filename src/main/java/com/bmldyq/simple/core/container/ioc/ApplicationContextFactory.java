/**
 * package name：ApplicationContextFactory.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.core.container.ioc;


import com.bmldyq.simple.annotation.Component;

/**
 *
 * description： 系统容器工厂类，从容器中获取指定的Bean对象
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public class ApplicationContextFactory extends IOCFactory {

	private static ApplicationContextFactory applicationContextFactory;
	
	private ApplicationContextFactory() {}
	
	public static ApplicationContextFactory getApplicationContextFactoryInstance() {
		if(applicationContextFactory == null) {
			applicationContextFactory = new ApplicationContextFactory();
		}
		applicationContextFactory.containInit();
		return applicationContextFactory;
	}
	
	private void containInit() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public  <T> T getBean(String beanName) throws Exception {
		T result =  null;
		FactoryBean fbean= beanFactory.get(beanName);
		if(fbean != null) {
			result = getBean(fbean);
		}
		return result;
	}
	public  <T> T getBean(Class<? extends T> clzz) throws Exception {
		String clzzSimpleName = clzz.getSimpleName();
		String beanName = clzzSimpleName.substring(0,1).toLowerCase() + clzzSimpleName.substring(1);
		return getBean(beanName);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getBean(FactoryBean fbean) throws Exception{
		T result = null;
		Object instance = fbean.getBeanInstance();
		if(instance != null) {
			result = (T) instance;
		}else {
			Component component = fbean.getComponent();
			Component.BEAN_TYPE bType = component.beanType();
			switch(bType) {
			case prototype:
				Class<? extends Object> clzz = fbean.getBeanClzz();
				result = (T) getInstanceByAspect(clzz);
				autoSetByPropto(result);
				break;
			case singleton:
				result = (T) instance;
				break;
			}
		}
		return result;
	}
}
