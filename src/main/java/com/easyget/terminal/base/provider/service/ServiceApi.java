/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.service;

import java.lang.reflect.Method;

/**
 * <p>
 * service 插件ServiceApi bean
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public class ServiceApi {
    /** api 方法**/
    private Method method;

    /** api class**/
    private Class<?> cls;

    public ServiceApi(Method method, Class<?> cls) {
        super();
        this.method = method;
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Method getMethod() {
        return method;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ServiceApi [method=" + method + ", cls=" + cls + "]";
    }

}
