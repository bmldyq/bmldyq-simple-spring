/**
 * package name：TestServiceImpl.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.test.service.impl;


import com.bmldyq.simple.annotation.AutoWired;
import com.bmldyq.simple.annotation.Component;
import com.bmldyq.simple.test.service.SysSerial;
import com.bmldyq.simple.test.service.SysService;
import com.bmldyq.simple.test.service.TestService;

/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
@Component(beanType = Component.BEAN_TYPE.prototype)
public class TestServiceImpl implements TestService {

	@AutoWired
	private SysService SysService;
	
	@AutoWired
	private SysSerial sysSerial;
	
	@AutoWired(value = "abcdefg")
	private SysSerial sysSerial2;
	
	@Override
	public String getMsg(String s) {
		System.out.println(sysSerial);
		System.out.println(sysSerial2);
		return "cuuent date is "+SysService.getSysdate()+" @Component is ok!";
	}

}
