package com.easyget.terminal.base.module;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.util.MessageHandleUtil;
import com.easyget.terminal.base.util.Options;
import com.easyget.terminal.base.util.ThreadPoolManager;
import com.seagen.ecc.ectcps.ClientConfig;
import com.seagen.ecc.ectcps.device.AttachableDevice;
import com.seagen.ecc.ectcps.device.ICommandHandler;
import com.seagen.ecc.ectcps.device.mcs.McsDevice;
import com.seagen.ecc.ectcps.handlers.HandlerProxy;
import com.seagen.ecc.ectcps.protocol.McsMessage;
import com.seagen.ecc.ectcps.protocol.Protocol;
import com.seagen.ecc.ectcps.protocol.Protocol.CommandType;
import com.seagen.ecc.ectcps.util.MessageUtils;
import com.seagen.ecc.utils.JsonUtil;

/**
 * 与模块控制系统通信的相关操作
 */
public class McsHandler implements ICommandHandler {

	private static Logger logger = Logger.getLogger(McsHandler.class);

	/**
	 * 接收到的所有待处理消息
	 */
	private Map<Long, McsMessage> cmdMaps = new ConcurrentHashMap<Long, McsMessage>();

	private McsDevice mcsDevice = null;

	private static McsHandler INSTANCE;

	public static McsHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new McsHandler();
		}
		return INSTANCE;
	}

	/**
	 * 启动 与模块控制系统通信客户端
	 */
	public void start() {
		logger.info("start MCS Connection");
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setClientName("MCS");
		clientConfig.setServerIp(Options.getString("options.mcsIp", "127.0.0.1"));
		clientConfig.setCharsetType(Protocol.CharSetType.GBK);
		clientConfig.setProtocolType(Protocol.ProtocolType.MCS);
		clientConfig.setNeedLogin(false);
		clientConfig.setServerPort(2000);
		clientConfig.setTimeout(60);
		HandlerProxy.setHandler(clientConfig.getClientName(), getInstance());

		mcsDevice = new McsDevice(clientConfig);
		mcsDevice.start();

		startClearTask();
	}

	/**
	 * 获取通信设备
	 */
	private AttachableDevice getAttachableDevice() {
		while (mcsDevice == null) {
			logger.info("模块通信未就绪...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		return mcsDevice;
	}

	/**
	 * 设备是否连接
	 */
	public boolean isConnected() {
		return getAttachableDevice().isConnected();
	}

	/**
	 * 向模块控制系统发送消息
	 */
	public boolean send(McsMessage message) {
		logger.debug("add mcsMessage,serialNumber=" + message.getSerialNumber());
		getAttachableDevice().addOutMessage(message);
		return true;
	}

	/**
	 * 发送消息并且等待回复
	 * 
	 * @param message
	 *            要发送的消息
	 * @param millis
	 *            等待时间(毫秒 ms)
	 */
	public Map<String, String> send(McsMessage message, int millis) {
		send(message);
		// 等待返回
		while (millis > 0) {
			try {
				Thread.sleep(100);
				millis -= 100;
			} catch (InterruptedException e) {
			}
			McsMessage ret = cmdMaps.get(message.getSerialNumber());
			if (ret != null) {
				return MessageUtils.paramList2Map(ret.getParamList());
			}
		}
		return null;
	}

	@Override
	public void messageReceived(String remoteAddress, Object msg) {
		// 收到消息后的处理方式
		McsMessage cmd = (McsMessage) msg;
		int cmdType = cmd.getCommandType();
		if (cmdType == CommandType.HEARTBEAT_RESP) {
			return;
		} else {
			logger.info("MCS收到消息:" + JsonUtil.ojbToJsonPrettyStr(msg));
			if (cmdType == CommandType.REQUEST_RESP) {
				cmdMaps.put(cmd.getSerialNumber(), cmd);
			} else if (cmdType == CommandType.REPORT) {
				// 分发控制层收到的消息
				MessageHandleUtil.messageHandOut(cmd);
			}
		}
	}

	@Override
	public void messageSent(String descAddress, Object msg) {
		final McsMessage cmd = (McsMessage) msg;
		if (cmd.getCommandType() != 5) {
			logger.info("MCS发送消息:" + JsonUtil.ojbToJsonPrettyStr(msg));
		}
	}

	@Override
	public void messageSendFail(String descAddress, Object msg, String error) {
	}

	/**
	 * 初始化定时清理过时消息功能
	 */
	private void startClearTask() {
		logger.info("start MCS Clear Task");
		ThreadPoolManager.getInstance().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					logger.debug("clear timeout mcsmessage start,size=" + cmdMaps.size());
					Set<Long> set = cmdMaps.keySet();
					for (Long id : set) {
						String idStr = String.valueOf(id);
						long time = Long.valueOf(idStr.substring(0, idStr.length() - 6));// 截取毫秒数（去掉后6位的纳秒数）
						if (System.currentTimeMillis() - time > 6000) {
							cmdMaps.remove(id);
						}
					}
					logger.debug("clear timeout mcsmessage end,size=" + cmdMaps.size());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 10, 10, TimeUnit.MINUTES);// 延时10分钟，周期10分钟
	}
}