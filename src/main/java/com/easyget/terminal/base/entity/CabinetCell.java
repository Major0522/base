package com.easyget.terminal.base.entity;

/**
 * 快递柜格口信息
 */
public class CabinetCell {

    private Integer slaveId;// 所属副柜Id

    private Integer cellId; // 格口在副柜中的ID（等同于物理地址）

    private Integer cellType; // 格口类型：1小，2中，3大

    private Integer cellState; // 使用状态：1 未使用，2 使用中

    private Integer switchState; // 开关状态：1关闭，2打开

    private String alarmState; // 告警状态，存储字符型的状态，如果同时存在多个状态时，使用逗号分隔。

    private Integer usingCount; // 使用次数

    public CabinetCell() {
    }

    public CabinetCell(Integer slaveId, Integer cellId) {
        this();
        this.slaveId = slaveId;
        this.cellId = cellId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CabinetCell other = (CabinetCell) obj;
        if (cellId == null) {
            if (other.cellId != null) {
                return false;
            }
        } else if (!cellId.equals(other.cellId)) {
            return false;
        }
        if (slaveId == null) {
            if (other.slaveId != null) {
                return false;
            }
        } else if (!slaveId.equals(other.slaveId)) {
            return false;
        }
        return true;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public Integer getCellId() {
        return cellId;
    }

    public Integer getCellState() {
        return cellState;
    }

    public Integer getCellType() {
        return cellType;
    }

    public Integer getSlaveId() {
        return slaveId;
    }

    public Integer getSwitchState() {
        return switchState;
    }

    public Integer getUsingCount() {
        return usingCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
        result = prime * result + ((slaveId == null) ? 0 : slaveId.hashCode());
        return result;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public void setCellState(Integer cellState) {
        this.cellState = cellState;
    }

    public void setCellType(Integer cellType) {
        this.cellType = cellType;
    }

    public void setSlaveId(Integer slaveId) {
        this.slaveId = slaveId;
    }

    public void setSwitchState(Integer switchState) {
        this.switchState = switchState;
    }

    public void setUsingCount(Integer usingCount) {
        this.usingCount = usingCount;
    }

}