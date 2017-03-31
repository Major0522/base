/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * service 插件服务调用方法异步还是同步注解
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface ServiceCall {
	
	/**
	 * 是否是异步处理。
	 * 
	 * @return	true 异步处理，false 同步处理。
	 */
    boolean isAsync() default false;
    
}
