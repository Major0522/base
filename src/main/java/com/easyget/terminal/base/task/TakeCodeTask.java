package com.easyget.terminal.base.task;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.dao.DynamicPwdDao;
import com.easyget.terminal.base.dao.SubstanceDao;
import com.easyget.terminal.base.entity.DynamicPassword;
import com.easyget.terminal.base.util.ApplicationUtil;
import com.easyget.terminal.base.util.SecurityUtils;
import com.easyget.terminal.base.util.ThreadPoolManager;

public class TakeCodeTask {

	/**
	 * 数据库生成提取码的最大数量
	 */
	public static final int MAX_NUM = 200;

	private static Logger logger = Logger.getLogger(TakeCodeTask.class);
	
	public static void start(){
		// 延迟1分钟运行，周期10分钟
		ThreadPoolManager.getInstance().scheduleWithFixedDelay(task(), 1, 10, TimeUnit.MINUTES);
	}
	
	private static Runnable task() {
		return new Runnable() {

			@Override
			public void run() {
				if (ApplicationUtil.isUsing()) {
					logger.debug("User is using the terminal, wait....");
					return;
				}

				int count = DynamicPwdDao.getInstance().getDynamicPasswordCount();
				logger.debug("数据库中取件码个数为：" + count);
				if (count < MAX_NUM) {
					int num = MAX_NUM - count;
					logger.debug("即将生成" + num + "个取件码");
					for (int i = 0; i < num; i++) {
						String password = createDynPwd();
						DynamicPwdDao.getInstance().add(password);
					}
					logger.debug("取件码生成完毕");
				}
			}
		};
	}
	
	public static String getDynPwd(){
		DynamicPassword pwd = DynamicPwdDao.getInstance().get();
		if (pwd != null) {
			DynamicPwdDao.getInstance().delete(pwd);
			return pwd.getDynamicPassword();
		} else {
			return TakeCodeTask.createDynPwd();
		}
	}
	
	public static String createDynPwd() {
		String pwd;
		boolean flag ;
		do {
			flag = false;
			pwd = SecurityUtils.creatRandomCode(6, false);

			for(int i = 0; i < 2; i++){
				String str = pwd.substring(i, i + 5);
				if((SubstanceDao.getInstance().getCountLikePwd(str) != null) ||
						(DynamicPwdDao.getInstance().getCountLikePwd(str) != null)){
					flag = true;
					break;
				}
			}
		} while (flag);
		return pwd;
	}
}