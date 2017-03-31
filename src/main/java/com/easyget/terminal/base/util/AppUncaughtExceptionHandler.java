package com.easyget.terminal.base.util;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

import com.seagen.ecc.utils.ExceptionUtils;

/**
 * 应用级的异常处理类,对程序某些未捕获的异常进行跟踪,记录,告警等操作
 */
public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private static Logger logger = Logger.getLogger(AppUncaughtExceptionHandler.class);
	private String recentMessage = "";

	public AppUncaughtExceptionHandler() {
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		String message = e.getMessage();
		if ("Too many touch points reported".equals(message)
				|| "Wrong number of touch points reported".equals(message)
				|| "Platform reported wrong touch point ID".equals(message)) {
			logger.debug("many touch points...");
			return;
		}
		logger.error("捕捉到未处理的异常,threadId=" + t.getId() + ",threadName=" + t.getName(), e);
		String stackTrace = ExceptionUtils.getStackTrace(e);
		if (!recentMessage.equals(stackTrace)) {// 上报异常,但不连续上报相同的异常
			ServiceUtil.callServiceApi("MonitorServiceApi@reportEvent", "程序异常", stackTrace);
		}
		recentMessage = stackTrace;
	}
}