/**
 * package name：Pointcut.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
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
 * description： 
 * creator： baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
public @interface Pointcut {

	String execution();
}
