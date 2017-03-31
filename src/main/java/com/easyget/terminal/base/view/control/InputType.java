package com.easyget.terminal.base.view.control;

/**
 *
 * 文本框键盘输入方式
 */
public interface InputType {
	
	/**
	 * 固定式数字及字母键盘
	 */
	static short FIXED_MIX = 0x01;
	/**
	 * 固定式纯数字键盘
	 */
	static short FIXED_NUMBER = 0x02;
	/**
	 * 弹出式数字及字母键盘
	 */
	static short AUTO_MIX = 0x11;
	/**
	 * 弹出式纯数字键盘
	 */
	static short AUTO_NUMBER = 0x12;
	
	/**
	 * @return 输入框的键盘类型
	 */
	short getType();
}