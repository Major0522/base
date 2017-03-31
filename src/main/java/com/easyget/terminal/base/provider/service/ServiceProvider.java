/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.service;

import com.easyget.terminal.base.provider.Provider;

/**
 * <p>
 * 服务插件提供者bean
 * </p> 
 */
public abstract class ServiceProvider<T> extends Provider implements Comparable<ServiceProvider<T>> {
    /**服务排序值**/
    private Integer order;

    /**
     * 构建服务插件提供者。
     * 
     * @param order	ServiceProvider在队列中的排序号，处理按顺序执行的情况。
     */
    public ServiceProvider(Integer order) {
        super();
        
        if (order == null) {
            order = 0;
        }
        
        this.order = order;
    }

    @Override
    public int compareTo(ServiceProvider<T> o) {
        return this.order.compareTo(o.order);
    }

    /**
     * 服务插件所执行的处理方法。
     */
    @ServiceCall
    public abstract void doCall();

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ServiceProvider [order=" + order + "]";
    }
}
