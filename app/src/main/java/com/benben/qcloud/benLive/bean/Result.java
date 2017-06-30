package com.benben.qcloud.benLive.bean;

import java.io.Serializable;

public class Result<T> implements Serializable {
	private int code = -1;
//	private boolean retMsg;
	private T data;
	public Result() {
	}
	public Result(int retCode,/* boolean retMsg,*/ T retData) {
		super();
		this.code = retCode;
//		this.retMsg = retMsg;
		this.data = retData;
	}
	public int getRetCode() {
		return code;
	}
	public void setRetCode(int retCode) {
		this.code = retCode;
	}
	/*public boolean isRetMsg() {
		return retMsg;
	}
	public void setRetMsg(boolean retMsg) {
		this.retMsg = retMsg;
	}*/
	public T getRetData() {
		return data;
	}
	public void setRetData(T retData) {
		this.data = retData;
	}
	@Override
	public String toString() {
		return "Result [retCode=" + code /*+ ", retMsg=" + retMsg*/ + ", retData=" + data + "]";
	}
}
