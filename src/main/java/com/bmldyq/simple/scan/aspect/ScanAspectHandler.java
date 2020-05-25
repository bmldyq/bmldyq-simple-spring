/**
 * package name：ScanAspectHandler.java
 * description：
 * creator：baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.scan.aspect;

import com.bmldyq.simple.annotation.Aspect;
import com.bmldyq.simple.scan.AbstractScanRegisterAnnon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月20日
 * modifier： 
 * modify time:
 */
public class ScanAspectHandler extends AbstractScanRegisterAnnon {
	private Logger logger = LoggerFactory.getLogger(ScanAspectHandler.class);
	/**
	 * 包策略
	 */
	public final String packageName = "com.bmldyq.simple";
	/**
	 * 匹配类名，只有已patten结尾的类才能被扫到 "" 代表所有包
	 */
	public final String patten = "";
	@Override
	protected ClassLoader getClassLoader() {

		return ScanAspectHandler.class.getClassLoader();
	}
	@Override
	protected String getScanPackage() {

		return packageName;
	}

	@Override
	protected boolean isAddClass(Class<? extends Object> clzz) {
		boolean isExecute = false;
		if (clzz.isAnnotationPresent(Aspect.class)) {
			isExecute = true;
			if (logger.isDebugEnabled()) {
				logger.debug("ScanAspectHandler.isAddClass 扫描到Aspect容器类:{}", clzz.getName());
			}
		}
		return isExecute;

	}
	@Override
	protected boolean isAgreeCheck(String packName) {
		boolean isExecute = false;
		if (packName.endsWith(".class")) {
			isExecute = checkPatten(packName);
		}
		return isExecute;
	}

	@Override
	protected boolean checkPatten(String packName) {
		return true;
	}

}
