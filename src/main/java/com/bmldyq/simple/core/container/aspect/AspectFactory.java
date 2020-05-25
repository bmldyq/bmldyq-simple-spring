/**
 * package name：AspectFactory.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.core.container.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import com.bmldyq.simple.annotation.AfterReturning;
import com.bmldyq.simple.annotation.AfterThrowing;
import com.bmldyq.simple.annotation.BeforeRuning;
import com.bmldyq.simple.annotation.Pointcut;
import com.bmldyq.simple.scan.AbstractScanRegisterAnnon;
import com.bmldyq.simple.scan.aspect.ScanAspectHandler;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;


/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
public class AspectFactory {
	
	
	protected   List<AspectInfo> getAspectInfo() throws Exception {
		AbstractScanRegisterAnnon scan = new ScanAspectHandler();
		List<AspectInfo> aspectInfoList = null;
		List<Class<? extends Object>> list = scan.scanRegisterClass();
		
		if(list != null && list.size() > 0 ) {
			aspectInfoList = Lists.newCopyOnWriteArrayList();
			/**
			 * 1.找到poincut
			 */
			for(Class<? extends Object> clzz : list ) {
				AspectInfo aspectInfo = findMethods(clzz);
				aspectInfoList.add(aspectInfo);
			}
		}
		return aspectInfoList;
	}
	
	private AspectInfo findMethods(Class<? extends Object> clzz) throws Exception {
		Method methods [] = clzz.getDeclaredMethods();
		Pointcut pointcut = null;
		Method beforeMethod = null;
		Method afterMethod = null;
		Method afterExMethod = null;
		for(Method method : methods) {
			pointcut = findPointcut(method, pointcut);
			beforeMethod = findMethod(method, beforeMethod, BeforeRuning.class);
			afterMethod = findMethod(method, afterMethod, AfterReturning.class);
			afterExMethod = findMethod(method, afterExMethod, AfterThrowing.class);
		}
		AspectInfo aspectInfo = new AspectInfo();
		pointcutChk(aspectInfo, pointcut);
		aspectInfo.setAfterMethod(afterMethod);
		aspectInfo.setAfterTrxMethod(afterExMethod);
		aspectInfo.setBeforeMethod(beforeMethod);
		aspectInfo.setAspectClzz(clzz);
		return aspectInfo;
	}
	
	private void pointcutChk(AspectInfo aspectInfo,Pointcut pointcut ) throws Exception {
		if(pointcut == null) {
			throw new Exception("pointcut 未找到，请确认！");
		}
		if(StringUtils.isEmpty(pointcut.execution())) {
			throw new Exception("pointcut 未配置execution,请确认！");
		}
		aspectInfo.setPointcut(pointcut);
	}
	
	private Pointcut findPointcut(Method method,Pointcut pointcut) throws Exception {
		if(pointcut == null ) {
			if(method.isAnnotationPresent(Pointcut.class)) {
				pointcut = method.getAnnotation(Pointcut.class);
			}
		}
		return pointcut;
	}
	
	private Method findMethod(Method method,Method destMethod,Class<? extends Annotation> annotationClzz) throws Exception {
		if(destMethod == null ) {
			if(method.isAnnotationPresent(annotationClzz)) {
				destMethod = method;
			}
		}
		return destMethod;
	}
}
