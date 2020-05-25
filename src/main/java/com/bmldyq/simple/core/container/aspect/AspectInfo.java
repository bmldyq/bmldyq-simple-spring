/**
 * package name：AspectInfo.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.core.container.aspect;

import com.bmldyq.simple.annotation.Pointcut;

import java.lang.reflect.Method;


/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
public class AspectInfo {
	
	private Class<? extends Object> aspectClzz;
	
	private Object trueProxy;
	
	private Pointcut pointcut;
	
	private Method beforeMethod;
	
	private Method afterMethod;
	
	private Method afterTrxMethod;

	public Pointcut getPointcut() {
		return pointcut;
	}

	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
	}

	public Method getBeforeMethod() {
		return beforeMethod;
	}

	public void setBeforeMethod(Method beforeMethod) {
		this.beforeMethod = beforeMethod;
	}

	public Method getAfterMethod() {
		return afterMethod;
	}

	public void setAfterMethod(Method afterMethod) {
		this.afterMethod = afterMethod;
	}

	public Method getAfterTrxMethod() {
		return afterTrxMethod;
	}

	public void setAfterTrxMethod(Method afterTrxMethod) {
		this.afterTrxMethod = afterTrxMethod;
	}

	public Class<? extends Object> getAspectClzz() {
		return aspectClzz;
	}

	public void setAspectClzz(Class<? extends Object> aspectClzz) {
		this.aspectClzz = aspectClzz;
	}

	@Override
	public String toString() {
		return "AspectInfo [aspectClzz=" + aspectClzz + ", pointcut=" + pointcut + ", beforeMethod=" + beforeMethod
				+ ", afterMethod=" + afterMethod + ", afterTrxMethod=" + afterTrxMethod + "]";
	}

	public Object getTrueProxy() {
		return trueProxy;
	}

	public void setTrueProxy(Object trueProxy) {
		this.trueProxy = trueProxy;
	}
	
	
	

}
