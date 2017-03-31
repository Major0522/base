package com.easyget.terminal.base.entity;

/**
 * 实物类
 */
public class Substance {

	private String substanceId;// 实物Id
	private Integer slaveId;
	private Integer cellId;
	private Integer state;// in/out
	private Integer type;// express、award、sale
	private String inputTime;
	private String dynamicPwd;

	public String getSubstanceId() {
		return substanceId;
	}

	public void setSubstanceId(String substanceId) {
		this.substanceId = substanceId;
	}

	public Integer getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(Integer slaveId) {
		this.slaveId = slaveId;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDynamicPwd() {
		return dynamicPwd;
	}

	public void setDynamicPwd(String dynamicPwd) {
		this.dynamicPwd = dynamicPwd;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
}