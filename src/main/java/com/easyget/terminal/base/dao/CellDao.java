package com.easyget.terminal.base.dao;

import java.util.ArrayList;
import java.util.List;

import com.easyget.terminal.base.entity.CabinetCell;
import com.easyget.terminal.base.model.AlarmState;
import com.easyget.terminal.base.provider.BaseDao;
import com.easyget.terminal.base.util.ArrayUtils;
import com.easyget.terminal.base.util.Const;

public class CellDao extends BaseDao<CabinetCell> {

	private static CellDao INSTANCE;

	public static CellDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CellDao();
		}

		return INSTANCE;
	}

	public List<CabinetCell> list() {
		return super.list("SELECT * FROM tb_cabinet_cell");
	}

	public List<CabinetCell> list(int slaveId, int state, int alarm) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE slaveId=? AND cellState=? AND alarmState=?";
		return super.list(sql, slaveId, state, alarm);
	}

	public List<CabinetCell> listUsableCell(int slaveType) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE cellState=? AND alarmState=? AND "
				+ "slaveId in(SELECT slaveId FROM tb_slave_cabinet WHERE state=? and slaveType=?)";
		Object[] params = { Const.CELL_STATE_UNUSE, AlarmState.Nomal.getValue(), Const.SLAVE_STATE_NORMAL, slaveType };
		return super.list(sql, params);
	}

	public List<CabinetCell> listUsableCellByType(Integer cellType, int slaveType) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE "
				+ "cellState=? AND alarmState=? AND cellType=? AND slaveId in"
				+ "(SELECT slaveId FROM tb_slave_cabinet WHERE state=? and slaveType=?)";
		Object[] params = { Const.CELL_STATE_UNUSE, AlarmState.Nomal.getValue(), cellType, Const.SLAVE_STATE_NORMAL,
				slaveType };
		return super.list(sql, params);
	}

	public CabinetCell getBySlaveCell(Integer slaveId, Integer cellId) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE slaveId=? AND cellId=?";
		Object[] params = { slaveId, cellId };
		return super.get(sql, params);
	}

	public List<CabinetCell> getBySlaveCell(Integer slaveId, Integer... cellIds) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE slaveId=? AND cellId in(" + ArrayUtils.convert(cellIds, ",")
				+ ")";
		Object[] params = { slaveId };
		return super.list(sql, params);
	}

	public List<CabinetCell> listOpenCell() {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE switchState=?";
		return super.list(sql, Const.CELL_SWITCH_ON);
	}

	public List<CabinetCell> listCellBySlave(Integer slaveId) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE slaveId=? order by cellId asc";
		return super.list(sql, slaveId);
	}

	public void delete(Integer slaveId, Integer cellId) {
		String sql = "delete from tb_cabinet_cell WHERE slaveId=? AND cellId=?";

		super.update(sql, slaveId, cellId);

	}

	public void delete(Integer slaveId) {
		String sql = "delete from tb_cabinet_cell WHERE slaveId=? ";
		super.update(sql, slaveId);
	}

	public void add(CabinetCell cell) {
		String sql = "insert into tb_cabinet_cell "
				+ "(slaveId,cellId,cellType,cellState,switchState,alarmState,usingCount) " + " values(?,?,?,?,?,?,?)";
		super.update(sql, cell.getSlaveId(), cell.getCellId(), cell.getCellType(), cell.getCellState(),
				cell.getSwitchState(), cell.getAlarmState(), cell.getUsingCount());

	}

	public boolean saves(List<CabinetCell> cells) {
		List<String> sqls = new ArrayList<String>();
		for (CabinetCell cell : cells) {
			String sql = String.format(
					"insert into tb_cabinet_cell "
							+ "(slaveId,cellId,cellType,cellState,switchState,alarmState,usingCount) "
							+ " values(%s,%s,%s,%s,%s,%s,%s);",
					cell.getSlaveId(), cell.getCellId(), cell.getCellType(), cell.getCellState(), cell.getSwitchState(),
					cell.getAlarmState(), cell.getUsingCount());
			sqls.add(sql);
		}
		return super.execSqlList(sqls);
	}

	public void update(CabinetCell cell) {
		String sql = "UPDATE tb_cabinet_cell SET " + " cellType=?,cellState=?,switchState=?,alarmState=?,usingCount=? "
				+ " WHERE slaveId=? AND cellId=?";
		super.update(sql, cell.getCellType(), cell.getCellState(), cell.getSwitchState(), cell.getAlarmState(),
				cell.getUsingCount(), cell.getSlaveId(), cell.getCellId());
	}

	/**
	 * 获取有相同告警状态的格口
	 */
	public List<CabinetCell> listByAlarm(AlarmState alarmState) {
		String params = alarmState.strValue();
		return super.list("SELECT * FROM tb_cabinet_cell WHERE alarmState in (?)", params);
	}

	public List<CabinetCell> listByAlarms(String alarms) {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE alarmState in (" + alarms + ")";
		return super.list(sql);
	}

	public boolean isExistAlarm() {
		String sql = "SELECT * FROM tb_cabinet_cell WHERE  alarmState <> ? ";
		List<CabinetCell> cells = this.list(sql, AlarmState.Nomal.getValue());
		if (cells == null || cells.isEmpty()) {
			return false;
		}
		return true;
	}

	public void updateState(Integer cellId, Integer slaveId, int cellState) {
		String sql = "UPDATE tb_cabinet_cell SET cellState=? WHERE slaveId=? AND cellId=?";
		super.update(sql, cellState, slaveId, cellId);
	}

	public void updateAlarm(Integer slaveId, Integer cellId, String alarmState) {
		String sql = "UPDATE tb_cabinet_cell SET alarmState=? WHERE slaveId=? AND cellId=?";
		super.update(sql, alarmState, slaveId, cellId);
	}

	public CabinetCell get(int slaveId, int cellId) {
		return this.get("SELECT * FROM tb_cabinet_cell WHERE slaveId=? AND cellId=?", slaveId, cellId);
	}

	public void updateSwitchState(int slaveId, int cellId, int switchState) {
		String sql = "UPDATE tb_cabinet_cell SET switchState=? WHERE slaveId=? AND cellId=?";
		super.update(sql, switchState, slaveId, cellId);
	}
}