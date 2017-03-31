package com.easyget.terminal.base.model;

public class Result {

	private boolean state = false;
	private Object value;

	public boolean isSuccess() {
		return state;
	}

	public void setSuccess(boolean state) {
		this.state = state;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}