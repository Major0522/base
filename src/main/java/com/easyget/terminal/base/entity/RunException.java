package com.easyget.terminal.base.entity;

/**
 * 系统运行异常数据库日志
 */
public class RunException {

	private Long serialId;

	private String content;

	public RunException(Long serialId, String content) {
		this.serialId = serialId;
		this.content = content;
	}

	public RunException(String string) {
		this.content = string;

	}

	public String getContent() {
		return content;
	}

	public Long getSerialId() {
		return serialId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSerialId(Long serialId) {
		this.serialId = serialId;
	}

}
