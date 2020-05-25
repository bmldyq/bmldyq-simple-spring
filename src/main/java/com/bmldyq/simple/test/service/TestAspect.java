/**
 * package name：TestAspect.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.test.service;

import com.bmldyq.simple.annotation.*;

import java.lang.reflect.Method;


/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
@Aspect
@Component
public class TestAspect {
	
	@Pointcut(execution = "com.bmldyq.simple.*.service.impl..*.*(..)")
	public void pointcut() {}
	
	@BeforeRuning
	public void beforeInvoke(Object proxy, Method method, Object[] args) {
		System.out.println("我是执行前的调用................");
	}
	
	@AfterReturning
	public void afterInvoke(Object ret) {
		System.out.println("我是执行成功后调用................ret=" + ret);
	}
	
	@AfterThrowing
	public void afterInvokeThr(Throwable ab) {
		System.out.println("我在在异常出现时调用................");
	}

}
