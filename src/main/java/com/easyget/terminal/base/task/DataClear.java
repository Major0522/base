package com.easyget.terminal.base.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.SubstanceDao;
import com.easyget.terminal.base.util.ApplicationUtil;
import com.easyget.terminal.base.util.ThreadPoolManager;
import com.seagen.ecc.utils.DateUtils;

public class DataClear {
	
	private static Logger logger = Logger.getLogger(DataClear.class);

	public static void start(){
		ThreadPoolManager.getInstance().scheduleWithFixedDelay(new Runnable() {
			@Override public void run() {
				if (ApplicationUtil.isUsing()) {
					logger.debug("User is using the terminal, wait....");
					return;
				}
				
				logger.debug("系统当前处于空闲状态，准备清理历史数据");
				
				String date = DateUtils.datetimeToString(DateUtils.addDate(new Date(), -7));//保留7天的交易记录
				
				try {
					SubstanceDao.getInstance().delete(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 10, 60, TimeUnit.MINUTES);// 延迟10分钟运行，周期1小时
	}
}