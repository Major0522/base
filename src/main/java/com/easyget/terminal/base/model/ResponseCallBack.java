package com.easyget.terminal.base.model;

/**
 * 响应后的结果回调接口
 * 
 * @author LB
 */
public interface ResponseCallBack {
	/**
	 * 响应成功后的结果回调
	 * 
	 * @param obj
	 *            成功信息
	 */
	public void successCallback(Object obj);

	/**
	 * 响应失败后的结果回调
	 * 
	 * @param obj
	 *            失败信息
	 */
	public void failCallback(Object obj);
}
