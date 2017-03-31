package com.easyget.terminal.base.entity;

public class SlaveCabinet {
	private Integer slaveId;
	private String slaveAddress;
	private Integer cellCount;
	private Integer state;
	private String slaveName;
	private String model;
	private String appVersion;

	/** 副柜类型：快递柜 */
	public static final int TYPE_EXPRESS = 1;
	/** 副柜类型：售货柜 */
	public static final int TYPE_GOODS = 2;
	/** 副柜类型：分发柜 */
	public static final int TYPE_DISTRIBUTE = 4;
	/** 副柜类型：广告柜 */
	public static final int TYPE_AD = 5;

	/** 副柜状态：正常 */
	public static final int STATE_NORMAL = 1;
	/** 副柜状态：失联 */
	public static final int STATE_UNUSUAL = 2;
	/**
	 * 副柜类型: 1 快递柜,2售货柜
	 */
	private Integer slaveType;

	public Integer getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(Integer slaveId) {
		this.slaveId = slaveId;
	}

	public String getSlaveAddress() {
		return slaveAddress;
	}

	public void setSlaveAddress(String slaveAddress) {
		this.slaveAddress = slaveAddress;
	}

	public Integer getCellCount() {
		return cellCount;
	}

	public void setCellCount(Integer cellCount) {
		this.cellCount = cellCount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSlaveName() {
		return slaveName;
	}

	public void setSlaveName(String slaveName) {
		this.slaveName = slaveName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((slaveId == null) ? 0 : slaveId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlaveCabinet other = (SlaveCabinet) obj;
		if (slaveId == null) {
			if (other.slaveId != null)
				return false;
		} else if (!slaveId.equals(other.slaveId))
			return false;
		return true;
	}

	public Integer getSlaveType() {
		return slaveType;
	}

	public void setSlaveType(Integer slaveType) {
		this.slaveType = slaveType;
	}

}
