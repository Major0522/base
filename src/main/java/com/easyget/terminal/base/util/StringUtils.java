/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import org.apache.log4j.Logger;

/**
 * 
 * String 工具类
 * @author niujunhong
 */
public class StringUtils {
    /**log4j 日志*/
    protected static final Logger logger = Logger.getLogger(StringUtils.class.getName());

    public static boolean isEmpty(String str) {
        int len = 0;
        if ((str == null) || ((len = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String trim(String str) {
        return str != null ? str.trim() : str;
    }
}