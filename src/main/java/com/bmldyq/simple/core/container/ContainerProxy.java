/**
 * package name：ContainerProxy.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.core.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class ContainerProxy implements InvocationHandler {

   /**
    * 执行前调用的方法
    */
	private Method beforeMethod;
    /**
     * 正确执行成功调用的方法
     */
	private Method afterMethod;
    /**
     * 执行过程中出现异常的方法
     */
	private Method afterTrxMethod;
    /**
     * 切面对象
     */
	private Object aspectObject;
	/**
	 * 被代理的真实对象
	 */
	private Object object;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?>[] interfaces = proxy.getClass().getInterfaces();// 主调用的接口
		String methodName = method.getName();// 调用的方法名称
		Class<? extends Object> clzz = interfaces[0];
		System.out.println("ContainerProxy.invoke ----------调用接口："+clzz.getName());
		System.out.println("ContainerProxy.invoke ----------调用方法："+methodName);
		System.out.println("ContainerProxy.invoke ----------参数列表："+args);
		System.out.println(proxy.getClass().getName());
		Object result = null;
		try {
			// 获取切入点的对象
			// 切入点开始调用
			if (beforeMethod != null) {
				beforeMethod.invoke(aspectObject, new Object[] { proxy, method, args });
			}
			result = method.invoke(object, args);
			if (afterMethod != null) {
				afterMethod.invoke(aspectObject, new Object[] { result });
			}
		} catch (Throwable ab) {
			ab.fillInStackTrace();
			if (afterTrxMethod != null) {
				afterTrxMethod.invoke(aspectObject, new Object[] { ab });
			}
			throw ab;
		}
		return result;
	}

	public void setBeforeMethod(Method beforeMethod) {
		this.beforeMethod = beforeMethod;
	}

	public void setAfterMethod(Method afterMethod) {
		this.afterMethod = afterMethod;
	}

	public void setAfterTrxMethod(Method afterTrxMethod) {
		this.afterTrxMethod = afterTrxMethod;
	}

	public void setAspectObject(Object aspectObject) {
		this.aspectObject = aspectObject;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

}
