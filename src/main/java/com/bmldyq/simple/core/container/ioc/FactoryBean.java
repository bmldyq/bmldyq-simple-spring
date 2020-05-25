/**
 * package name：FactoryBean.java
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
 * description： 工厂BEAN
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public class FactoryBean {
	/**
	 * Bean名称
	 */
	private String beanName;
	/**
	 * Bean对象
	 */
	private Object beanInstance;
	/**
	 * Bean所在类
	 */
	private Class<? extends Object> beanClzz;
	/**
	 * Bean的注解
	 */
	private Component component;
	
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public Object getBeanInstance() {
		return beanInstance;
	}
	public void setBeanInstance(Object beanInstance) {
		this.beanInstance = beanInstance;
	}
	public Class<? extends Object> getBeanClzz() {
		return beanClzz;
	}
	public void setBeanClzz(Class<? extends Object> beanClzz) {
		this.beanClzz = beanClzz;
	}
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	@Override
	public String toString() {
		return "FactoryBean [beanName=" + beanName + ", beanInstance=" + beanInstance + ", beanClzz=" + beanClzz
				+ ", component=" + component + "]";
	}
	
	

}
