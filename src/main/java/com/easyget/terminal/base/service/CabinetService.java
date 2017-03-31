package com.easyget.terminal.base.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.communication.BcsHandler;
import com.easyget.terminal.base.dao.CellDao;
import com.easyget.terminal.base.dao.SlaveCabinetDao;
import com.easyget.terminal.base.entity.CabinetCell;
import com.easyget.terminal.base.entity.SlaveCabinet;
import com.easyget.terminal.base.model.AlarmState;
import com.easyget.terminal.base.model.CellModel;
import com.easyget.terminal.base.module.ModuleHandler;
import com.easyget.terminal.base.util.Const;
import com.easyget.terminal.base.util.Options;
import com.easyget.terminal.base.util.ServiceUtil;
import com.seagen.ecc.common.CmdConst;
import com.seagen.ecc.ectcps.protocol.CommandMessage;
import com.seagen.ecc.ectcps.protocol.Param;
import com.seagen.ecc.ectcps.protocol.Protocol.CommandType;
import com.seagen.ecc.utils.DateUtils;
import com.seagen.ecc.utils.StringUtils;

public class CabinetService {

	private static Logger logger = Logger.getLogger(CabinetService.class);
	
	private static CabinetService INSTANCE;
	public static CabinetService getInstance(){
		if(INSTANCE == null){
			INSTANCE = new CabinetService();
		}
		return INSTANCE;
	}
	
	public boolean isClose(CabinetCell cell) {
		String state = ModuleHandler.queryCellState(cell.getSlaveId(), cell.getCellId());
		if(state.equals(Const.CELL_SWITCH_OFF + "")){
			cell.setSwitchState(Const.CELL_SWITCH_OFF);
			CellDao.getInstance().updateSwitchState(cell.getSlaveId(), cell.getCellId(), Const.CELL_SWITCH_OFF);
			return true;
		}else{
			return false;
		}
	}

	public boolean openCell(int slaveId, int cellId, int action) {
		if (ModuleHandler.openCell(slaveId, cellId)) {
			// 打开成功时的处理
			handleForOpenOK(slaveId, cellId);
			return true;
		} else {
			// 打开失败时的处理
			handleForOpenError(slaveId, cellId, action);
			return false;
		}
	}

	private void handleForOpenError(int slaveId, int cellId, int action) {
		// 打开异常时的处理
		AlarmState alarmState = AlarmState.ErrorForUnknown;
		
		switch (action) {
		case Const.CELL_ACTION_TAKE:
			alarmState = AlarmState.ErrorForTakeOpen;
			break;
		case Const.CELL_ACTION_ADD:
			alarmState = AlarmState.ErrorForAddOpen;
			break;
		case Const.CELL_ACTION_REMOVE:
			alarmState = AlarmState.ErrorForRemoveOpen;
			break;
		case Const.CELL_ACTION_PUT:
			alarmState = AlarmState.ErrorForPutOpen;
			break;
		default:
			break;
		}

		CabinetCell cell = CellDao.getInstance().getBySlaveCell(slaveId, cellId);
		
		String strOldAlarmState = cell.getAlarmState();
		saveAlarmAndReport(slaveId, cellId, alarmState, strOldAlarmState);
	}

	private void saveAlarmAndReport(int slaveId, int cellId, AlarmState alarmState, String strOldAlarmState) {
		String strCurrentAlarm = alarmState.strValue();
		
		if (AlarmState.Nomal.strValue().equals(strOldAlarmState)) {
			CellDao.getInstance().updateAlarm(slaveId, cellId, strCurrentAlarm);
			ServiceUtil.callServiceApi("MonitorServiceApi@reportCellAlarm", slaveId, cellId, alarmState);
		} else {
			// 处理同时存在多个告警的情况
			String[] alarmList = strOldAlarmState.split(",");
			
			boolean isExit = false;
			for(String as : alarmList){
				if(as.equals(alarmState + "")){
					isExit = true;   // 已经存在当前告警，将不再进行存储处理
					break;
				}
			}
			
			if(!isExit){
				// 不包含当前的告警时，把当前告警存储起来
				CellDao.getInstance().updateAlarm(slaveId, cellId, strCurrentAlarm + "," + strOldAlarmState);
				ServiceUtil.callServiceApi("MonitorServiceApi@reportCellAlarm", slaveId, cellId, alarmState);
			}
		}
	}

	private void handleForOpenOK(int slaveId, int cellId) {
		CabinetCell cell = CellDao.getInstance().getBySlaveCell(slaveId, cellId);
		// 更改格口状态信息，并持久化
		cell.setUsingCount(cell.getUsingCount() + 1);
		cell.setSwitchState(Const.CELL_SWITCH_ON);

		// 打开格口清除所有的告警（取件）
		if (!AlarmState.Nomal.strValue().equals(cell.getAlarmState())) {
			String existAlarm = cell.getAlarmState();
			cell.setAlarmState(AlarmState.Nomal.strValue());
			
			ServiceUtil.callServiceApi("MonitorServiceApi@reportCellRecovery", slaveId, cellId, existAlarm);
		}
		
		CellDao.getInstance().update(cell);
	}
	
	public boolean listenClosed(int slaveId, int cellId, int action) {
		int overTime = 60; // 默认等待60秒，如果需要可配置，取配置的值
		AlarmState alarmState = AlarmState.Nomal;

		switch (action) {
		case Const.CELL_ACTION_TAKE:
			overTime = Options.getInt("config.timeout.take", 60);
			alarmState = AlarmState.ErrorForTakeClose;
			break;
		case Const.CELL_ACTION_PUT:
			overTime = Options.getInt("config.timeout.push", 180);
			alarmState = AlarmState.ErrorForPutClose;
			break;
		default:
			break;
		}

		// 等待超时时间补偿3秒
		boolean flag = ModuleHandler.waitCellClose(slaveId, cellId, overTime + 3);
		
		// 获取数据库最新数据，避免操作老数据导致错误
		CabinetCell cell = CellDao.getInstance().getBySlaveCell(slaveId, cellId);
		
		if (flag) {
			// 更改格口状态信息，并持久化
			cell.setSwitchState(Const.CELL_SWITCH_OFF);
			CellDao.getInstance().update(cell);
			return true;
		} else {
			String strOldAlarmState = cell.getAlarmState();
			saveAlarmAndReport(slaveId, cellId, alarmState, strOldAlarmState);
			return false;
		}
	}

	public List<CabinetCell> listBySlaveCell(Integer slaveId, Integer ... cellIds) {
		return CellDao.getInstance().getBySlaveCell(slaveId, cellIds);
	}

	public List<CellModel> listCellModel(int slaveId) {
		List<CellModel> ret = new ArrayList<>();
		
		List<CabinetCell> cells = CellDao.getInstance().listCellBySlave(slaveId);
		if(StringUtils.isNotEmpty(cells)){
			for(CabinetCell cell : cells){
				CellModel cm = new CellModel();
				cm.setCellId(cell.getCellId());
				
				int state = cell.getCellState();
				if(state == Const.CELL_STATE_USING){
					cm.setUsing(true);
				}else if(state == Const.CELL_STATE_DISABLED){
					cm.setUsable(false);
				} 
				
				String alarmState = cell.getAlarmState();
				if(!AlarmState.Nomal.strValue().equals(alarmState)){
					cm.setWarning(true);
				}
				
				ret.add(cm);
			}
		}
		
		return ret;
	}
	
	public List<SlaveCabinet> listUsableDistribute(){
		return SlaveCabinetDao.getInstance().list(SlaveCabinet.STATE_NORMAL,SlaveCabinet.TYPE_DISTRIBUTE );
	}
	
	public void setCellState(int slaveId, int cellId, boolean flag){
		logger.info("切换格口[" + slaveId + "," + cellId + "]状态为：" + (flag ? "启用" : "禁用"));

		CellDao.getInstance().updateAlarm(slaveId, cellId, AlarmState.Nomal.strValue());
		int cellState = 0;
		if(flag){
			CellDao.getInstance().updateState(cellId, slaveId, Const.CELL_STATE_UNUSE);
			cellState = Const.CELL_STATE_UNUSE;
		}else{
			CellDao.getInstance().updateState(cellId, slaveId, Const.CELL_STATE_DISABLED);	
			cellState = Const.CELL_STATE_DISABLED;
		}
		syncCellState(slaveId, cellId, cellState, AlarmState.Nomal.strValue());
	}
	
	/**
	 * 同步告警格口状态
	 */
	public void syncCellState(int slaveId, int cellId, int state, String alarmState) {
		CommandMessage returnMessage = new CommandMessage();
		returnMessage.setCabinetNo(Options.getLong("ectcps.netmanagerNo"));
		returnMessage.setCommandType(CommandType.REPORT);
		returnMessage.setFunctionCode(CmdConst.FUN_CODE_SYN_CELL_STATE);
		returnMessage.setSerialNumber(DateUtils.getSerialNo());
		returnMessage.setParamList(new Param[]{
				new Param("slaveId", slaveId + ""),
				new Param("cellId", cellId + ""),
				new Param("cellState", state + ""),
				new Param("alarmState", alarmState)
		});
		BcsHandler.getInstance().sendNoWaiting(returnMessage);

	}
	/**
	 * 上/下架时，打开/关闭格口失败告警
	 * */
	public void updateCellAlarm(CabinetCell cc, AlarmState alarmType){
		String alarmStr = "0".equals(cc.getAlarmState()) == true?(alarmType.strValue()):(cc.getAlarmState()+","+alarmType.strValue());
		CellDao.getInstance().updateAlarm(cc.getSlaveId(), cc.getCellId(), alarmStr);
		ServiceUtil.callServiceApi("MonitorServiceApi@reportCellAlarm", cc.getSlaveId(),cc.getCellId(), alarmType);
	}
	
	/**
	 * 调用API恢复格口告警
	 * */
	public void restoreCellAlarm(CabinetCell cell){
		ServiceUtil.callServiceApi("MonitorServiceApi@reportCellRecovery",
				cell.getSlaveId(), cell.getCellId(), cell.getAlarmState());
	}

	public boolean existDistributeSlave() {
		List<SlaveCabinet> slaves = SlaveCabinetDao.getInstance().queryByType(SlaveCabinet.TYPE_DISTRIBUTE);
		if(slaves != null && !slaves.isEmpty()){
			return true;
		}
		return false;
	}

	public boolean checkSlave() {
		List<SlaveCabinet> slaves = SlaveCabinetDao.getInstance().queryAll();
		
		for (SlaveCabinet slave : slaves) {
			if (ModuleHandler.isOnlineSlave(slave.getSlaveId())) {
				SlaveCabinetDao.getInstance().updateState(slave.getSlaveId(), Const.SLAVE_STATE_NORMAL);
			}else{
				SlaveCabinetDao.getInstance().updateState(slave.getSlaveId(), Const.SLAVE_STATE_UNUSUAL);
			}
		}
		return true;
	}
}