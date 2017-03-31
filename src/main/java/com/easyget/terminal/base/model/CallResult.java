package com.easyget.terminal.base.model;

/**
 * <p>
 * 服务api调用返回结果
 * </p>
 */
public class CallResult {
	public static final Integer STATUS_ERROR = 0;

	public static final Integer STATUS_SUCCESS = 1;

	/** 调用状态 **/
	private Integer status = STATUS_ERROR;

	/** 调用出错的时候，抛出的异常 **/
	private Throwable exception;

	/** 返回值 **/
	private Object data;

	/** 返回值 class 字符串 **/
	private String dataCls;

	public CallResult() {
	}

	public Object getData() {
		return data;
	}

	public String getDataCls() {
		return dataCls;
	}

	public Throwable getException() {
		return exception;
	}

	public int getStatus() {
		return status;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setDataCls(String dataCls) {
		this.dataCls = dataCls;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CallResult [status=" + status + ", exception=" + exception + ", data=" + data + ", dataCls=" + dataCls
				+ "]";
	}
}
