/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.page;

/**
 * <p>
 * 页面插件类提供者接口
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public interface PagePluginProvider {
    /**
     *  获取页面插件提供者
     * @return
     * @throws Exception
     */
    PageProvider getProvider() throws Exception;
}
