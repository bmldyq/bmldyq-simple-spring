/**
 * package name：SysConfiguration.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.test.service;

import com.bmldyq.simple.annotation.Bean;
import com.bmldyq.simple.annotation.Component;
import com.bmldyq.simple.annotation.Property;

@Component
public class SysConfiguration {

	/**
	 * 
	 * method name：sysSerial
	 * description：注册一个新的Bean
	 * @return
	 * return_type：SysSerial
	 * creator：baiml
	 * create time：2020年5月19日
	 * modifier： 
	 * modify time：
	 */
	@Bean(name = "sysSerial", propertys = { @Property(name = "txDate", value = "20200519"),
			@Property(name = "cardNo", value = "6231236547890000"), @Property(name = "txAmt", value = "98.50"),
			@Property(name = "sn", value = "100000001")})
	public SysSerial sysSerial() {

		System.out.println("我是新注册的Bean，启动初始化需要调用,我带了初始化参数");
		return new SysSerial();
	}
	@Bean(name = "abcdefg")
	public SysSerial sysSerial2() {
		System.out.println("我是新注册的Bean，启动初始化需要调用,我不带初始化参数");
		SysSerial serial = new SysSerial();
		serial.setCardNo("82323232323");
		serial.setSn(200001);
		return serial;
	}
	

}
