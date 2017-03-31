/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : abinet_terminal_plugin_base
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.embed;

/**
 * <p>
 * 嵌入式插件提供者接口
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public interface EmbedPluginProvider {
    /**
     *  获取嵌入式插件提供者bean
     * @return
     * @throws Exception
     */
    EmbedProvider getProvider() throws Exception;
}
