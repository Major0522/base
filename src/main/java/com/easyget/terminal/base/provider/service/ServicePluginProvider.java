/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.service;

/**
 * <p>
 * 服务插件类提供者接口
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public interface ServicePluginProvider<T> {
    /**
     *  获取服务插件提供者
     * @return
     * @throws Exception
     */
    ServiceProvider<T> getProvider() throws Exception;
}
