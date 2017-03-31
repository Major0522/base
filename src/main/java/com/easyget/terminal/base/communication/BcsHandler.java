package com.easyget.terminal.base.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.ReportDao;
import com.easyget.terminal.base.entity.Report;
import com.easyget.terminal.base.util.ApplicationUtil;
import com.easyget.terminal.base.util.Const;
import com.easyget.terminal.base.util.MessageHandleUtil;
import com.easyget.terminal.base.util.Options;
import com.easyget.terminal.base.util.ThreadPoolManager;
import com.seagen.ecc.common.CmdConst;
import com.seagen.ecc.ectcps.ClientConfig;
import com.seagen.ecc.ectcps.device.ICommandHandler;
import com.seagen.ecc.ectcps.device.bcs.BcsDevice;
import com.seagen.ecc.ectcps.handlers.HandlerProxy;
import com.seagen.ecc.ectcps.protocol.CommandMessage;
import com.seagen.ecc.ectcps.protocol.Param;
import com.seagen.ecc.ectcps.protocol.Protocol;
import com.seagen.ecc.ectcps.protocol.Protocol.CommandType;
import com.seagen.ecc.ectcps.util.MessageUtils;
import com.seagen.ecc.utils.JsonUtil;
import com.seagen.ecc.utils.NetUtils;

/**
 * 与后台服务器通信的相关操作
 * 
 */
public class BcsHandler implements ICommandHandler {

	private static Logger logger = Logger.getLogger(BcsHandler.class);

	/**
	 * 用于消息暂存
	 */
	private Map<Long, CommandMessage> cmdMaps = new ConcurrentHashMap<Long, CommandMessage>();

	/**
	 * 向后台请求的超时时间（单位：毫秒）
	 */
	private static final int TIME_OUT = 5000;

	private BcsDevice bcsDevice = null;

	private static BcsHandler INSTANCE;

	public static BcsHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BcsHandler();
		}

		return INSTANCE;
	}

	/**
	 * 启动与后台通信系统通信客户端
	 */
	public void start() {
		// 启动连接处理
		startConnection();

		// 启动上报任务
		startReportTask();

		// 启动清除任务
		startClearTask();
	}

	/**
	 * 发送消息并等待答复，只有发出请求（CommandType.REQUEST）时才需要等待回复。
	 * 
	 * @param serviceNo  后台服务器。
	 * @param functionCode 功能编号。
	 * @param params 参数信息。
	 * @return 远程执行后的答复信息。
	 */
	public Map<String, String> sendAndWaiting(long serviceNo, int functionCode, Param[] params) {
		if (bcsDevice == null) {
			return null;
		}

		CommandMessage message = new CommandMessage(serviceNo, CommandType.REQUEST, functionCode);
		message.setParamList(params);

		// 发出请求
		bcsDevice.addOutMessage(message);

		// 等待返回
		CommandMessage result = null;

		int timeout = TIME_OUT;

		while (timeout > 0) {
			result = cmdMaps.get(message.getSerialNumber());

			if (result != null) {
				break;
			}

			try {
				Thread.sleep(100);
				timeout -= 100;
			} catch (InterruptedException e) {
			}
		}

		if (result != null) {
			return MessageUtils.paramList2Map(result.getParamList());
		} else if (timeout <= 0) {
			Map<String, String> ret = new HashMap<String, String>();

			ret.put(CmdConst.RETURN_CODE, CmdConst.RETURN_CODE_FAILURE);
			ret.put(CmdConst.RETURN_DETAIL, "请求超时，请重试！");

			return ret;
		}

		return null;
	}

	/**
	 * 发送消息并等待答复，只有发出请求（CommandType.REQUEST）时才需要等待回复。
	 * 
	 * @param functionCode  功能编号。
	 * @param params 参数信息。
	 * @return 远程执行后的答复信息。
	 */
	public Map<String, String> sendAndWaiting(int functionCode, Param[] params) {
		if (!isConnected()) {
			Map<String, String> ret = new HashMap<String, String>();

			ret.put(CmdConst.RETURN_CODE, CmdConst.RETURN_CODE_FAILURE);
			ret.put(CmdConst.RETURN_DETAIL, "网络连接失败，请稍后重试！");

			return ret;
		}

		return sendAndWaiting(Options.getLong("ectcps.businessServiceNo", Const.DEFAULT_BIZ_SERVICE_NO), 
				functionCode, params);
	}

	/**
	 * 发送消息，不等待答复。
	 */
	public void sendNoWaiting(CommandMessage message) {
		if (message.getCommandType() == CommandType.REPORT) {
			ReportDao.getInstance().add(new Report(message.getSerialNumber(), JsonUtil.ojbToJsonStr(message)));
		}

		if (isConnected()) {
			bcsDevice.addOutMessage(message);
		}
	}

	/**
	 * 设备是否连接
	 */
	public boolean isConnected() {
		if (bcsDevice == null) {
			return false;
		}

		return bcsDevice.isConnected();
	}

	@Override
	public void messageReceived(String remoteAddress, Object msg) {
		CommandMessage message = (CommandMessage) msg;

		if (message.getCommandType() == CommandType.HEARTBEAT_RESP) {
			return;
		}

		logger.info("BCS收到消息:" + JsonUtil.ojbToJsonPrettyStr(msg));

		int cmdType = message.getCommandType(); 
		Map<String, String> params = MessageUtils.paramList2Map(message.getParamList());
		
		if (CmdConst.RETURN_CODE_SUCCESS.equals(params.get(CmdConst.RETURN_CODE))) {
			ReportDao.getInstance().remove(message.getSerialNumber());
		}
		
		if (cmdType == CommandType.REQUEST_RESP) {
			cmdMaps.put(message.getSerialNumber(), message);
		} else {
			MessageHandleUtil.messageHandOut(message);
		}
	}

	@Override
	public void messageSent(String descAddress, Object msg) {
		CommandMessage message = (CommandMessage) msg;
		
		if (message.getCommandType() != 5) {
			logger.info("BCS发送消息:" + JsonUtil.ojbToJsonPrettyStr(msg));
		}
	}

	@Override
	public void messageSendFail(String descAddress, Object msg, String error) {
		logger.info("BCS发送失败(" + error + "):" + JsonUtil.ojbToJsonPrettyStr(msg));
	}

	private void startConnection() {
		logger.info("start BCS Connection");
		
		ClientConfig clientConfig = new ClientConfig();
		
		clientConfig.setNeedLogin(true);
		clientConfig.setClientName("test");
		clientConfig.setUserId(ApplicationUtil.getCabinetNo());
		clientConfig.setPasswd("password");
		clientConfig.setTimeout(60);
		clientConfig.setServerPort(Options.getInt("ectcps.tcpServerPort", 18090));
		clientConfig.setServerIp(Options.getString("ectcps.tcpServerIp", "test.easyget.com"));
		clientConfig.setProtocolType(Protocol.ProtocolType.COMMAND_SSL_SIMPLE);
		
		try {
			clientConfig.setIdentity(NetUtils.getMachineCode());
		} catch (Exception e) {
			logger.error("获取物理地址失败", e);
		}
		
		HandlerProxy.setHandler(clientConfig.getClientName(), getInstance());
		
		bcsDevice = new BcsDevice(clientConfig);
		
		bcsDevice.start();
	}

	private void startReportTask() {
		logger.info("start BCS Report Task");
		
		ThreadPoolManager.getInstance().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				List<Report> reports = ReportDao.getInstance().listAll();
		
				if (reports == null || reports.isEmpty()) {
					return;
				}
				
				logger.debug("开始向Server上报消息");
				
				for (Report report : reports) {
					CommandMessage message = JsonUtil.jsonStrToObj(report.getContent(), CommandMessage.class);
					bcsDevice.addOutMessage(message);
					
					// 休整2秒，避免发送过快导致服务器没出理完报错
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, 1, 1, TimeUnit.MINUTES);
	}

	/**
	 * 初始化定时清理过时消息功能
	 */
	private void startClearTask() {
		logger.info("start BCS Clear Task");

		ThreadPoolManager.getInstance().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					Set<Long> set = cmdMaps.keySet();
		
					for (Long id : set) {
						String idStr = String.valueOf(id);
						long time = Long.valueOf(idStr.substring(0, idStr.length() - 6));// 截取毫秒数（去掉后6位的纳秒数）
					
						if (System.currentTimeMillis() - time > 6000) {
							cmdMaps.remove(id);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 10, 10, TimeUnit.MINUTES);// 延时10分钟，周期10分钟
	}
	
}