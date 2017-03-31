package com.easyget.terminal.base.module;

public class DeviceConst {

	/** 返回结果数据字段的标识串 */
	public final static String RETURN_RESULT = "Result";

	/** 返回代码字段的标识串 */
	public final static String RETURN_CODE = "ReturnCode";

	/** 返回结果标志("1")：正常/正确/成功 */
	public final static String RETURN_CODE_SUCCESS = "1"; 

	/** 返回结果标志("0")：异常/错误/失败 */
	public final static String RETURN_CODE_FAILURE = "0"; 

	/** 模块名称：模块程序本身 */
	public final static String MODULE_NAME_PROGRAM = "Program";

	/** 模块名称：副柜 */
	public final static String MODULE_NAME_CABINET = "Cabinet";
	
	/** 模块名称：读卡器 */
	public final static String MODULE_NAME_IDRF_CARD = "IDRFCard";
	
	/** 模块名称：条码扫描仪 */
	public final static String MODULE_NAME_BAR_CODE = "BarCode"; 

	/** Result值(0)：格口开 */
	public final static int RESULT_CELL_STATE_OPEN = 0;
	
	/** Result值(1)：格口关 */
	public final static int RESULT_CELL_STATE_CLOSE = 1; 

	/** 功能代码(1)，获取模块控制程序版本 */
	public final static int FUN_CODE_MODULE_VERSION = 1;
	/** 功能代码(14)，获取副柜软件版本 */
	public final static int FUN_CODE_SLAVE_VERSION = 14; 

	/** 功能代码(3)，获取全部模配件模块信息 */
	public final static int FUN_CODE_MODLUE_INFO = 3;
	
	/** 功能代码(11)，打开格口 */
	public final static int FUN_CODE_CELL_OPEN = 11; 
	/** 功能代码(12)， 查询格口状态 */
	public final static int FUN_CODE_CELL_QUER = 12; 
	
	/** 功能代码(13)，上报副柜故障 */
	public final static int FUN_CODE_SLAVE_ALARM = 13;
	
	/** 功能代码(15)，副柜程序升级 */
	public final static int FUN_CODE_SLAVE_UPDATE = 15;
	/** 功能代码(16)，副柜程序重启 */
	public final static int FUN_CODE_SLAVE_RESTART = 16; 
	/** 功能代码(17)，获取副柜控制板型号 */
	public final static int FUN_CODE_SLAVE_INFO = 17;
	/** 功能代码(18)，等待格口关闭 */
	public final static int FUN_CODE_CELL_WAIT = 18; 
	
	/** 功能代码(19)，向副柜控制板发送指令 */
	public final static int FUN_CODE_SLAVE_COMMAND = 19; 

	/** 功能代码(30)，红外传感器检测到人靠近 */
	public final static int FUN_CODE_DETECT_HUMAN = 30; 
	
	/** 功能代码(31)，检测到锁的异常 */
	public final static int FUN_CODE_DETECT_LOCKS_Exception = 31;
	
	/** 功能代码(41)，扫描条码 */
	public final static int FUN_CODE_SCAN_BAR = 41; 
	/** 功能代码(49)，关闭扫描仪 */
	public final static int FUN_CODE_CLOSE_SCAN = 49; 
	/** 功能代码(61)，读卡 */
	public final static int FUN_CODE_READ_CARD = 61; 
	/** 功能代码(69)，关闭读卡器 */
	public final static int FUN_CODE_CLOSE_READ = 69; 
	/** 功能代码(105)，升级红外检测板固件 */
	public final static int FUN_CODE_INFRAREDFIRMWARE_UPGRADE = 105;
	/** 功能代码(100)，检测物件放置状态 */
	public final static int FUN_CODE_DETECTSTUFF = 100;
	/** 功能代码(101)，设置售货柜开关灯时间 */
	public final static int FUN_CODE_SWITCHLIGHTTIME = 101;
	/** 功能代码(104)，售货灯开启关闭 */
	public final static int FUN_CODE_TURNONANDOFF_LIGHT = 104;

	/** 功能代码(106)，检测副柜的红外信号 */
	public final static int FUN_CODE_CABINET_DETECTION_IR = 106; 

	/** 功能代码(107)，改变副柜的颜色 */
	public final static int FUN_CODE_CABINET_ADJUST_COLOR = 107;

}