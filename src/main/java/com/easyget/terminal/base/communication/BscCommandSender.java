package com.easyget.terminal.base.communication;

import java.util.Map;

import com.easyget.terminal.base.util.ArrayUtils;
import com.easyget.terminal.base.util.Options;
import com.seagen.ecc.common.CmdConst;
import com.seagen.ecc.ectcps.protocol.CommandMessage;
import com.seagen.ecc.ectcps.protocol.Param;
import com.seagen.ecc.ectcps.protocol.Protocol.CommandType;

/**
 * 命令发送处理器，用于往后台发送命令，主要完成命令构造并调用相关处理类来发出。
 * 
 * @author wutianbin
 *
 */
public class BscCommandSender {

	private static long defaultServiceNo = 200000;
	private static long defaultNetManagerNo = 1000;
	
	/**
	 * 根据Options的数据填充CommandHandler内的一些默认值。
	 */
	public static void fillDefaultCabinet() {
		defaultNetManagerNo = Options.getLong("ectcps.netmanagerNo", defaultNetManagerNo);
		defaultServiceNo = Options.getLong("ectcps.businessServiceNo", defaultServiceNo);		
	}
	
	/**
	 * 检测远程后台是否连接上。
	 * 
	 * @return true表示已经连接成功。
	 */
	public static boolean bscConnected() {
		return BcsHandler.getInstance().isConnected();
	}
	
	/**
	 * 发送消息并等待答复，只有发出请求（CommandType.REQUEST）时才需要等待回复。
	 * 
	 * @param serviceNo  服务编号。
	 * @param functionCode 功能编号。
	 * @param params 参数信息。
	 * @return 远程执行后的答复信息。
	 */
	public static Map<String, String> sendAndWaiting(long serviceNo, int functionCode, Param[] params) {
		return BcsHandler.getInstance().sendAndWaiting(serviceNo, functionCode, params);
	}
	
	/**
	 * 发送指定命令给远程后台服务系统。
	 * 
	 * @param command 要发送的命令。
	 */
	public static void sendNoWaiting(CommandMessage command) {
		BcsHandler.getInstance().sendNoWaiting(command);
	}

	/**
	 * 发送命令给远程后台服务系统。
	 * 
	 * @param cabinetNo			远程系统的用来识别终端的编号。
	 * @param commandType 	 命令类型。
	 * @param functionCode		功能代码。
	 * @param params				参数信息。
	 */
	public static void sendNoWaiting(long cabinetNo, int commandType, int functionCode, Param... params) {
        final CommandMessage command = new CommandMessage();

        command.setCabinetNo(cabinetNo);
        command.setCommandType(commandType);
        command.setFunctionCode(functionCode);
        command.setParamList(params);

        sendNoWaiting(command);
	}

	public static void requestToBizNoWaiting(int functionCode, Param[] params) {
		sendNoWaiting(defaultServiceNo, CommandType.REQUEST, functionCode, params);
	}
	
	public static Map<String, String> requestToBizAndWaiting(int functionCode, Param[] params) {
		return sendAndWaiting(defaultServiceNo, functionCode, params);
	}

	public static void requestToMonitorNoWaiting(int functionCode, Param[] params) {
		sendNoWaiting(defaultNetManagerNo, CommandType.REQUEST, functionCode, params);
	}
	
	public static Map<String, String> requestToMonitorAndWaiting(int functionCode, Param[] params) {
		return sendAndWaiting(defaultNetManagerNo, functionCode, params);
	}

	/**
	 * 根据源命令进行回复。
	 * 
	 * @param sourceCommand 	源命令。
	 */
	public static void reply(CommandMessage sourceCommand) {
		switch (sourceCommand.getCommandType()) {
		case CommandType.REQUEST:
			sourceCommand.setCommandType(CommandType.REQUEST_RESP);
			break;
		case CommandType.REPORT:
			sourceCommand.setCommandType(CommandType.REPORT_RESP);
			break;
		default:
			break;
		}
		
		sendNoWaiting(sourceCommand);
	}

	/**
	 * 根据源命令及源命令执行结果给远程后台系统答复。
	 * 
	 * @param sourceCommand	源命令。
	 * @param	 processResultOk	源命令的处理成功的结果，false是处理失败。
	 * 
	 */
	public static void replyForResult(CommandMessage sourceCommand, boolean processResultOk, Param... params) {
		if (processResultOk) {
			replyOk(sourceCommand, params);
		} else {
			replyFail(sourceCommand, "命令执行失败", params);
		}
    }
	
	/**
	 * 根据源命令进行回复，源命令处理已经成功。
	 * 
	 * @param sourceCommand	源命令。
	 * @param params				回复的相关参数。
	 */
	public static void replyOk(CommandMessage sourceCommand, Param... params) {
		Param[] paramList = new Param[] { new Param(CmdConst.RETURN_CODE, CmdConst.RETURN_CODE_SUCCESS) };
		
		sourceCommand.setParamList(ArrayUtils.concat(paramList, params));
		
		reply(sourceCommand);
	}

	/**
	 * 根据源命令进行回复，源命令处理失败。
	 * 
	 * @param sourceCommand	源命令。
	 * @param failDetail 			失败的详细信息。
	 * @param params				回复的相关参数。
	 */
	public static void replyFail(CommandMessage sourceCommand, String failDetail, Param... params) {
		Param[] paramList = new Param[] { new Param(CmdConst.RETURN_CODE, CmdConst.RETURN_CODE_FAILURE),
				new Param(CmdConst.RETURN_DETAIL, failDetail) };
		
		sourceCommand.setParamList(ArrayUtils.concat(paramList, params));
		
		reply(sourceCommand);
	}

	/**
	 * 发送命令到后台业务系统，cabinetNo值设置为Options内的ectcps.cabinetNo。
	 * 
	 * @param commandType 	 命令类型。
	 * @param functionCode		功能代码。
	 * @param params				参数信息。
	 */
	public static void sendToBiz(int commandType, int functionCode, Param... params) {
		sendNoWaiting(defaultServiceNo, commandType, functionCode, params);
	}

	/**
	 * 发送命令到后台监控系统，cabinetNo值设置为Options内的ectcps.netmanagerNo。
	 * 
	 * @param commandType 	 命令类型。
	 * @param functionCode		功能代码。
	 * @param params				参数信息。
	 */
	public static void sendToMonitor(int commandType, int functionCode, Param... params) {
		sendNoWaiting(defaultNetManagerNo, commandType, functionCode, params);
	}

	/**
	 * 上报命令到后台业务系统，cabinetNo值设置为Options内的ectcps.cabinetNo，commandType都设置为REPORT。
	 * 
	 * @param functionCode		功能代码。
	 * @param params				参数信息。
	 */
	public static void reportToBiz(int functionCode, Param... params) {
		sendNoWaiting(defaultServiceNo, CommandType.REPORT, functionCode, params);
	}

	/**
	 * 上报命令到后台监控系统，cabinetNo值设置为Options内的ectcps.netmanagerNo，commandType都设置为REPORT。
	 * 
	 * @param commandType 	 命令类型。
	 * @param functionCode		功能代码。
	 * @param params				参数信息。
	 */
	public static void reportToMonitor(int functionCode, Param... params) {
		sendNoWaiting(defaultNetManagerNo, CommandType.REPORT, functionCode, params);
	}
	
}
