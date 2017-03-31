package com.easyget.terminal.base.module;

import java.util.Map;

import org.apache.log4j.Logger;

import com.seagen.ecc.ectcps.protocol.McsMessage;
import com.seagen.ecc.ectcps.protocol.Param;
import com.seagen.ecc.ectcps.protocol.Protocol.CommandType;

/**
 * 模块功能业务处理
 */
public class ModuleHandler {

	private static Logger logger = Logger.getLogger(ModuleHandler.class);

	public static synchronized boolean openCell(Integer slaveId, Integer cellId) {
		logger.info("打开格口（" + slaveId + "," + cellId + ")");
		if (!McsHandler.getInstance().isConnected()) {
			return false;
		}
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setParamList(new Param[] { new Param("slaveAddress", slaveId.toString()),
				new Param("cellAddress", cellId.toString()) });
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CELL_OPEN);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 5000);

		if (paramMap != null && isSuccess(paramMap)) {
			String state = paramMap.get(DeviceConst.RETURN_RESULT);
			return state.equals(DeviceConst.RESULT_CELL_STATE_OPEN + "");
		}
		return false;
	}

	public static boolean waitCellClose(Integer slaveId, Integer cellId, int waitCellTime) {
		logger.info("监听格口（" + slaveId + "," + cellId + ")关闭");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setParamList(new Param[] { new Param("waitTimeout", waitCellTime + ""),
				new Param("slaveAddress", slaveId.toString()), new Param("cellAddress", cellId.toString()) });
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CELL_WAIT);

		// 等待时间补偿5s，防止业务层超时而控制层还未超时的极端情况
		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, waitCellTime * 1000);
		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT).equals("" + DeviceConst.RESULT_CELL_STATE_CLOSE);
		}
		return false;
	}

	public static String queryCellState(Integer slaveId, Integer cellId) {
		logger.info("查询格口（" + slaveId + "," + cellId + ")开关状态");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setParamList(new Param[] { new Param("slaveAddress", slaveId.toString()),
				new Param("cellAddress", cellId.toString()) });
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CELL_QUER);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 5000);
		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT);
		}
		return null;
	}

	public static boolean isOnlineSlave(int slaveId) {
		logger.info("查询副柜（" + slaveId + ")连接状态");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setParamList(new Param[] { new Param("slaveAddress", slaveId + ""), new Param("cellAddress", "1") });
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CELL_QUER);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 5000);
		if (paramMap != null && isSuccess(paramMap)) {
			String state = paramMap.get(DeviceConst.RETURN_RESULT);
			if ("1".equals(state) || "0".equals(state)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSuccess(Map<String, String> params) {
		if (params != null) {
			return params.get(DeviceConst.RETURN_CODE).equals(DeviceConst.RETURN_CODE_SUCCESS);
		}
		return false;
	}

	public static String queryModulesInfo() {
		logger.info("查询模块信息");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_PROGRAM);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_MODLUE_INFO);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 5000);
		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT);
		}
		return null;
	}

	public static String readCard() {
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_IDRF_CARD);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_READ_CARD);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 60000);

		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT);
		}
		return null;
	}

	public static void stopReadCard() {
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_IDRF_CARD);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CLOSE_READ);

		McsHandler.getInstance().send(cmd);
	}

	public static String scanBarCode() {
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_BAR_CODE);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_SCAN_BAR);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 60000);

		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT);
		}
		return null;
	}

	public static void stopScanBarCode() {
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_BAR_CODE);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_CLOSE_SCAN);

		McsHandler.getInstance().send(cmd);
	}

	public static String querySlaveInfo(int functionCode) {
		logger.info("查询副柜硬件信息");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(functionCode);

		Map<String, String> paramMap = McsHandler.getInstance().send(cmd, 5000);
		if (paramMap != null && isSuccess(paramMap)) {
			return paramMap.get(DeviceConst.RETURN_RESULT);
		}
		return null;
	}

	public static void restartSlave(String slaveAdress) {
		logger.info("重启副柜【" + slaveAdress + "】");
		McsMessage cmd = new McsMessage(CommandType.REQUEST);
		cmd.setModuleName(DeviceConst.MODULE_NAME_CABINET);
		cmd.setFunctionCode(DeviceConst.FUN_CODE_SLAVE_RESTART);
		cmd.setParamList(new Param[] { new Param("slaveAddress", slaveAdress + "") });

		McsHandler.getInstance().send(cmd);
	}
}