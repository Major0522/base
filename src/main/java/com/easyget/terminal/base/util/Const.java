package com.easyget.terminal.base.util;

public interface Const {
	
	public static final int BUSSINESS_TYPE_EXPRESS = 1;// 业务类型：快递
	public static final int BUSSINESS_TYPE_SALE = 2;// 业务类型：销售
	public static final int BUSSINESS_TYPE_ACTIVITY = 3;// 业务类型：分发

	public static final int MEMBER_TYPE_PUT = 1;// 会员身份：快递员（包含放件、收件权限）
	public static final int MEMBER_TYPE_TAKE = 2;// 会员身份：普通会员（包含收件、购物权限）
	public static final int MEMBER_TYPE_SALE = 5;// 会员身份：售货员（包含商品管理权限）
	public static final int MEMBER_TYPE_ATTACH = 9;// 会员身份：样品专员（包含样品管理权限）

	public static final int MEMBER_STATE_ACTIVA = 1;// 会员状态：正常
	public static final int MEMBER_STATE_FREEZE = 2;// 会员状态：冻结
	public static final int MEMBER_STATE_CANCEL = 3;// 会员状态：注销
	public static final int MEMBER_STATE_UNACTI = 4;// 会员状态：未激活

	public static final int LOGIN_MODE_NO_PWD = 1;// 会员登录方式：账号+密码
	public static final int LOGIN_MODE_TEL_PWD = 2;// 会员登录方式：手机号+密码
	public static final int LOGIN_MODE_CARD_PWD = 4;// 会员登录方式：会员卡+密码
	public static final int LOGIN_MODE_CARD = 8;// 会员登录方式：会员卡

	public static final int LOGIN_FAILURE = 0;// 登录失败
	public static final int LOGIN_SUCCESS = 1;// 登录成功

	public static final int LOGIN_READ_CARD_SUCCESS = 2;// 读卡成功

	public static final int SLAVE_TYPE_EXPRESS = 1;// 副柜类型：快递柜
	public static final int SLAVE_TYPE_GOODS = 2;// 副柜类型：售货柜
	public static final int SLAVE_TYPE_MASTER = 3;// 副柜类型：快递柜主柜
	public static final int SLAVE_TYPE_DISTRIBUTE = 4;// 副柜类型：分发柜

	public static final int SLAVE_STATE_NORMAL = 1;// 副柜状态：正常
	public static final int SLAVE_STATE_UNUSUAL = 2;// 副柜状态：故障
	public static final int SLAVE_STATE_DISABLE = 3;// 副柜状态：禁用
	
	public static final int GOODS_STATE_SELLING = 1; 	//商品状态：已补货，可销售
	public static final int GOODS_STATE_SOLD = 2; 		//商品状态：已下架，待回收
	public static final int GOODS_STATE_DISABLE = 3; 	//商品状态：禁止销售
	
	public static final int CELL_SWITCH_OFF = 1;// 格口开关状态：关
	public static final int CELL_SWITCH_ON = 2;// 格口开关状态：开

	public static final int CELL_ACTION_PUT = 1;// 格口操作行为：放件
	public static final int CELL_ACTION_TAKE = 2;// 格口操作行为：取件
	public static final int CELL_ACTION_CANCEL = 3;// 格口操作行为：退件
	public static final int CELL_ACTION_ADD = 4;// 格口操作行为：上架
	public static final int CELL_ACTION_BUY = 5;// 格口操作行为：购买
	public static final int CELL_ACTION_REMOVE = 6;// 格口操作行为：下架

	public static final int CELL_STATE_UNUSE = 1;// 格口状态：未使用
	public static final int CELL_STATE_USING = 2;// 格口状态：使用中
	public static final int CELL_STATE_ERROR = 3;// 格口状态：故障
	public static final int CELL_STATE_DISABLED = 4;// 格口状态：禁用
	public static final int CELL_STATE_RESERVED = 5;// 格口状态：预约

	public static final int TRADING_STATE_SAVING  = 1;// 交易状态：在柜
	public static final int TRADING_STATE_OVERDUE = 2;// 交易状态：逾期/失效
	public static final int TRADING_STATE_CANCEL  = 3;// 交易状态：退回
	public static final int TRADING_STATE_TAKEOFF = 4;// 交易状态：已取

	public static final int ACTION_FAILURE = 0;// 操作失败
	public static final int ACTION_SUCCESS = 1;// 操作成功

	public static final int PAY_TYPE_ALIPAY = 1; // 支付方式：支付宝支付
	public static final int PAY_TYPE_WECHAT = 2; // 支付方式：微信支付
	public static final int PAY_TYPE_MEMBER = 3; // 支付方式：member pay

//	public final static int ALARM_STATE_NOMAL = 0; // 格口告警状态：正常
//	public final static int ALARM_STATE_GET_CLOSE_ERROR = 1; // 格口告警状态：取件关闭失败
//	public final static int ALARM_STATE_GET_OPEN_ERROR = 2; // 格口告警状态：取件打开失败
//	public final static int ALARM_STATE_PUT_CLOSE_ERROR = 3; // 格口告警状态：放件关闭失败
//	public final static int ALARM_STATE_PUT_OPEN_ERROR = 4; // 格口告警状态：放件打开失败
//	public final static int ALARM_STATE_CANCEL_OPEN_ERROR = 5; // 格口告警状态：退件时打开格口失败
//	public final static int ALARM_STATE_CANCEL_CLOSE_ERROR = 6; // 格口告警状态：退件时关闭格口失败
//	public final static int ALARM_STATE_ADD_OPEN_ERROR = 7; // 格口告警状态：上架时打开格口失败
//	public final static int ALARM_STATE_ADD_CLOSE_ERROR = 8; // 格口告警状态：上架时关闭格口失败
//	public final static int ALARM_STATE_ADD_DETECT_ERROR = 9; // 格口告警状态：上架时检测货物故障
//	public final static int ALARM_STATE_ADD_NO_GOODS = 10; // 格口告警状态：上架时没有放置货物
//	public final static int ALARM_STATE_BUY_OPEN_ERROR = 11; // 格口告警状态：购买时打开格口失败
//	public final static int ALARM_STATE_BUY_CLOSE_ERROR = 12; // 格口告警状态：购买时关闭格口失败
//	public final static int ALARM_STATE_BUY_DETECT_ERROR = 13; // 格口告警状态：购买时检测货物故障
//	public final static int ALARM_STATE_BUY_KEEP_GOODS = 14; // 格口告警状态：购买时没有取走货物
//	public final static int ALARM_STATE_REMOVE_OPEN_ERROR = 15; // 格口告警状态：下架时打开格口失败
//	public final static int ALARM_STATE_REMOVE_CLOSE_ERROR = 16; // 格口告警状态：下架时关闭格口失败
//	public final static int ALARM_STATE_REMOVE_DETECT_ERROR = 17; // 格口告警状态：下架时检测货物故障
//	public final static int ALARM_STATE_REMOVE_KEEP_GOODS = 18; // 格口告警状态：下架时没有取走货物
//	public final static int ALARM_STATE_OPEN_EXCEPTION_ERROR = 19; //格口被异常打开
	
	public final static long DEFAULT_BIZ_SERVICE_NO = 99998;
	
	/** 充值窗口的FXML文件名 */
	public static final String FXML_RECHARGE = "/member/fxml/Recharge.fxml";
	
	/** 取件页面的FXML文件 */
	public static final String FXML_TAKE = "/base/fxml/TakePane.fxml";
	
	/** 取件页面的JSON配置文件 */
	public static final String JSON_TAKE = "/base/TakeProvider.json";
	
}