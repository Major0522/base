/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.net.URL;

import javafx.fxml.FXMLLoader;

/**
 * 
 * fxml 文件工具类
 * @author niujunhong
 */
public class FxmlUtil {

	public static FXMLLoader getFxmlLoader(String fxmlFileName) throws Exception {
        String path = FxmlUtil.class.getResource(fxmlFileName).toString();
        path = java.net.URLDecoder.decode(path, "UTF-8");
        return new FXMLLoader(new URL(path));
    }
	
    public static String getKey(FXMLLoader xmlLoader) {
        final URL url = xmlLoader.getLocation();
        final String protocol = url.getProtocol();
        final String path = url.getFile();

        String key = null;
        if (protocol.equals("jar")) {
            final String flag = ".jar!";
            final int indexOf = path.indexOf(flag);
            key = path.substring(indexOf + flag.length());
        } else {
            final String flag = "/classes";
            final int indexOf = path.indexOf(flag);
            key = path.substring(indexOf + flag.length());
        }
        return key;
    }

    private FxmlUtil() {
    }
}
