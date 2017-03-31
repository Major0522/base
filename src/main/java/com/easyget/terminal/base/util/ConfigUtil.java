package com.easyget.terminal.base.util;

import java.util.List;

import com.easyget.terminal.base.dao.AppConfigDao;
import com.easyget.terminal.base.entity.AppConfig;
import com.seagen.ecc.utils.StringUtils;

public class ConfigUtil {
	
	public static void init(String version) {
		Options.set("ectbs.version", version);// 当前程序版本
		
		Options.set("java.home", System.getProperties().get("java.home"));
		
		Options.set("ectbs.os.name", "Windows");// 操作系统信息
		Options.set("ectbs.os.version", System.getProperty("os.version"));
		
		Options.set("file.database", "d:/ecc/ectbs.db");
		Options.set("file.module", "C:/ecc/ModuleControl/ModuleControl.exe");
		
		Options.set("file.timesynctool", "C:/ecc/ModuleControl/BypassUac.exe");//增加时间同步选项
		

		Options.set("update.biz.startFilePath", "C:/ecc/Terminal/bin/startup.bat");
		Options.set("update.biz.process", "javaw.exe");
		Options.set("update.biz.srcDirectory", "C:/ecc/Terminal");
		Options.set("update.mnt.startFilePath", "C:/ecc/ModuleControl/ModuleControl.exe");
		Options.set("update.mnt.process", "ModuleControl.exe");
		Options.set("update.mnt.srcDirectory", "C:/ecc/ModuleControl");
		Options.set("update.slave.startFilePath", "placeholder");
		Options.set("update.slave.process", "placeholder");
		Options.set("update.slave.srcDirectory", "placeholder");
		Options.set("dir.updateDir", "D:/ecc/ecc_update");
		Options.set("dir.screenSaver", "D:/ecc/ad/screen/");
		Options.set("dir.homeAd", "D:/ecc/ad/home/");
		// daemon 
		Options.set("daemon.0.startFilePath", "C:/ecc/Terminal/bin/startup.bat");
		Options.set("daemon.0.process", "javaw.exe");
		Options.set("daemon.1.startFilePath", "C:/ecc/ModuleControl/ModuleControl.exe");
		Options.set("daemon.1.process", "ModuleControl.exe");
		//增加日志  选项 
		Options.set("dir.logUpload.biz", "D:/ecc_log");
		Options.set("dir.logUpload.mnt", "D:/ecc_mlog");
		Options.set("dir.awardPhoto", "D:/ecc/award_photo/");// 样品图片路径
		Options.set("dir.awardAudio", "D:/ecc/award_audio/");// 样品声音路径
		
		// startup
		Options.set("startup.0", "C:/ecc/ModuleControl/ModuleControl.exe");
		Options.set("startup.1", "C:/ecc/Terminal/bin/startup.bat");
	}
	
	/**
	 * 加载数据库定义的配置信息
	 */
	public static void readConfigFromDB(){
		List<AppConfig> list = AppConfigDao.getInstance().list();
		for (AppConfig ac : list) {
			if (ac.getKey() != null && !StringUtils.isEmpty(ac.getValue())) {
				Options.set(ac.getKey(), ac.getValue());
			}
		}
	}
}