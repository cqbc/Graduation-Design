package com.cqupt.spider.pojos;

import com.cqupt.common.enums.RetCodeEnum;
import com.cqupt.common.enums.RetDescEnum;
    
/** 
 * 接口调用后的状态返回 
 * 
 */
public class RetStatus {
	private RetCodeEnum retCode;
	private RetDescEnum retDesc;
	
	@Override
	public String toString() {
		return "RetStatus [retCode=" + retCode + ", retDesc=" + retDesc + "]";
	}

	public RetStatus() {

	}

	public RetStatus(RetCodeEnum retCode, RetDescEnum retDesc) {
		this.retCode = retCode;
		this.retDesc = retDesc;
	}
	
	/**
	 * 接口调用后的是否出错
	 * 	
	 * @return RetCodeEnum 返回Ok或者Error
	 */
	public RetCodeEnum getRetCode() {
		return retCode;
	}
	/**
	 * 设置RetCodeEnum对象值，为Ok或者Error
	 */
	public void setRetCode(RetCodeEnum retCode) {
		this.retCode = retCode;
	}
	/**
	 * 接口调用后的是否成功
	 * 	
	 * @return RetCodeEnum 返回Success或者Fail
	 */
	public RetDescEnum getRetDesc() {
		return retDesc;
	}

	/**
	 * 设置RetDescEnum对象值，为Success或者Fail
	 */
	public void setRetDesc(RetDescEnum retDesc) {
		this.retDesc = retDesc;
	}
	
}
