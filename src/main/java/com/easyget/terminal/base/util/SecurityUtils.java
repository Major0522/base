package com.easyget.terminal.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户账户生成和判断工具
 */
public class SecurityUtils {
	
	public static String dataId(){
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	/**
	 * 判断是否手机号
	 */
	public static boolean isMobileNo(String key) {
		Pattern pattern = Pattern.compile("^[1][3-8]{1}[0-9]{9}$");
		if(key == null || "".equals(key)){
			return false;
		}
		return pattern.matcher(key).matches();
	}

	/**
	 * 判断是否卡号
	 */
	public static boolean isCardNo(String key) {
		Pattern pattern = Pattern.compile("\\d{6}$");
		if(key == null || "".equals(key)){
			return false;
		}
		Matcher m = pattern.matcher(key);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否账户号
	 */
	public static boolean isAccNo(String key) {
		Pattern pattern = Pattern.compile("\\d{8}$");
		if(key == null || "".equals(key)){
			return false;
		}
		Matcher m = pattern.matcher(key);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 创建指定长度的随机字符串
	 * @param len 长度
	 * @param hasLetter 是否包括大写字母（true包括 ,false不包括）
	 * @return 随机字符串
	 */
	public static String creatRandomCode(int len, boolean hasLetter) {
		StringBuilder result = new StringBuilder();
		Random random = new Random();
		
		if(hasLetter){
			for (int i = 0; i < len; i++) {
				int ran = random.nextInt(2);
				
				switch (ran) {
					case 0:
						char ch;
						do {
							ch = (char)(65 + random.nextInt(26));
						} while (ch == 'O' || ch == 'I');	//排除'O'和'I'，以免与'0'和'1'混淆
						result.append(String.valueOf(ch));
//						result.append(String.valueOf((char)(65 + random.nextInt(26))));
						break;
					case 1:
						int num;
						do {
							num = random.nextInt(10);
						} while (num == 0 || num == 1);	//排除'0'和'1'，以免与'O'和'I'混淆
						result.append(String.valueOf(num));
//						result.append(String.valueOf(random.nextInt(10)));
						break;
				}
			}
		}else{
			for (int i = 0; i < len; i++) {
				result.append(String.valueOf(random.nextInt(10)));
			}
		}
		return result.toString();
	}
}