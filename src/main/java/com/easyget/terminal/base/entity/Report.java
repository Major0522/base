package com.easyget.terminal.base.entity;

public class Report {

	private Long serialId;
	private String content;

	public Report() {
	}

	public Report(Long serialId, String content) {
		this.serialId = serialId;
		this.content = content;
	}

	public Long getSerialId() {
		return serialId;
	}

	public void setSerialId(Long serialId) {
		this.serialId = serialId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}