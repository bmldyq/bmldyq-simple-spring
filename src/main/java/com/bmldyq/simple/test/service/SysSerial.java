/**
 * package name：SysSerial.java
 * description：
 * creator：baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
package com.bmldyq.simple.test.service;

import java.math.BigDecimal;

/**
 *
 * description： 
 * creator： baiml
 * create time：2020年5月19日
 * modifier： 
 * modify time:
 */
public class SysSerial {
	
	private String txDate;
	private String cardNo;
	private BigDecimal txAmt;
	private Integer sn;
	public String getTxDate() {
		return txDate;
	}
	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public BigDecimal getTxAmt() {
		return txAmt;
	}
	public void setTxAmt(BigDecimal txAmt) {
		this.txAmt = txAmt;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	@Override
	public String toString() {
		return "SysSerial [txDate=" + txDate + ", cardNo=" + cardNo + ", txAmt=" + txAmt + ", sn=" + sn + "]";
	}
	
	
	

}
