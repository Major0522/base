/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider;

import java.util.Map;

/**
 * 插件初始化配置接口
 */
public interface PluginManager {
    /**
     * 插件初始化调用的方法，主要作用是给插件传递初始化参数
     * @return
     * @throws Exception
     */
    public void initCall(Map<String, String> pluginConfig);
}
