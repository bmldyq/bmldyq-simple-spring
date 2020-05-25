/**
 * package name：Bean.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
/**
 *
 * description： 本注解用于方法上，方法返回一个对象，是为了告诉容器，它是一个bean，进行初始化
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public @interface Bean {
	/**
	 * 一般使用方法的名字作为Bean，当设置了name属性过后，以设置的为准
	 * method name：propertys
	 * description：
	 * create time：2020年5月19日
	 */
	String name() default "";
	/**
	 * 初始化BEAN初始化的属性
	 * method name：propertys
	 * description：
	 * create time：2020年5月19日
	 */
	Property [] propertys() default {};
	
	
	
	

}
