package com.easyget.terminal.base.model;

public enum Color {

	Red("RED"), Green("GREEN"), Gray("DARKGRAY"), Orange("ORANGE");

	private String value;

	private Color(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}