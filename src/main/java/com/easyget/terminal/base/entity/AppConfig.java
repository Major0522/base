package com.easyget.terminal.base.entity;

public class AppConfig {

	/** 参数值为告警 */
	public static final String VALUE_ALARM = "1";
	/** 参数值为正常 */
	public static final String VALUE_NOMAL = "0";
	
	/** 状态为启用 */
	public static final int STATE_USED = 1;
	
	private Long id;
	private String key;
	private String value;
	private String remark;
	private String lastUpdated;
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}