package com.easyget.terminal.base.util;

/**
 * 整数类型的实用工具类。
 * 
 * @author wutianbin
 */
public class IntegerUtil {
	
	/**
	 * 字符串转整数，遇到非整数时，输出默认值defaultValue。
	 * @param value 要转换的字符串。
	 * @param defaultValue 转换失败时的默认值。
	 * @return 转换后的整数，或者转换失败时返回默认值。
	 */
	public static int parseIntForDefault(String value, int defaultValue) {
		int result = defaultValue;
		
		try {
			result = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		return result;
	}
	
	/**
	 * Integer转为String，如果Integer is null，返回null.
	 * @param value
	 * @return 如果value is null，返回null，否则返回对应的string。
	 */
	public static String IntegerToString(Integer value) {
		if (value == null) {
			return null;
		}
		
		return String.valueOf(value.intValue());
	}
	
}
