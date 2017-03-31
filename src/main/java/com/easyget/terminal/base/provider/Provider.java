/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.easyget.terminal.base.util.FileUtil;
import com.easyget.terminal.base.util.StringUtils;

/**
 * <p>
 * 插件提供者接口
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public abstract class Provider {
	
	/**
	 * Provider初始化的处理。
	 * 
	 * @throws Exception
	 */
    public abstract void init() throws Exception;
    

	protected <T> List<T> createListFormJson(String jsonFileName, Class<T> clazz) throws Exception {
		if (jsonFileName == null) {
			return null;
		}
		
		final String providerJsonConfig = FileUtil.getFromClassPath(jsonFileName);
		if (StringUtils.isEmpty(providerJsonConfig)) {
			throw new Exception("must set " + jsonFileName);
		}
		
		return JSON.parseArray(providerJsonConfig, clazz);
	}
    
}
