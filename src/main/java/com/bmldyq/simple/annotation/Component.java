/**
 * package name：Compent.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public @interface Component {
	
	String name() default "";
	
	enum BEAN_TYPE{
		/**
		 * 单例,容器初始化时，创建实例放入容器，在系统的生命周期之内只存在一个单例对象
		 */
		singleton,
		/**
		 * 容器初始化时无需创建对象，在每次调用时创建
		 */
		prototype 
	}
	BEAN_TYPE beanType() default BEAN_TYPE.singleton;
	

}
