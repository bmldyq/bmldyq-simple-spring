/**
 * package name：SysService.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.test.service;


import com.bmldyq.simple.annotation.Component;

/**

 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
@Component(beanType = Component.BEAN_TYPE.prototype)
public class SysService {
	
	
	public String getSysdate() {
		
		return "20200519";
	}

}
