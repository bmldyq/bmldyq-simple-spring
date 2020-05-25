/**
 * package name：ScanIocHandler.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.scan.ioc;

import com.bmldyq.simple.annotation.Component;
import com.bmldyq.simple.scan.AbstractScanRegisterAnnon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public class ScanIocHandler extends AbstractScanRegisterAnnon {
	private Logger logger = LoggerFactory.getLogger(ScanIocHandler.class);
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
		return ScanIocHandler.class.getClassLoader();
	}

	@Override
	protected String getScanPackage() {
		return packageName;
	}

	@Override
	protected boolean isAddClass(Class<? extends Object> clzz) {
		boolean isExecute = false;
		if (clzz.isAnnotationPresent(Component.class)) {
			isExecute = true;
			if (logger.isDebugEnabled()) {
				logger.debug("ScanIocHandler.isAddClass 扫描到Component容器类:{}", clzz.getName());
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
