package com.easyget.terminal.base.provider;

/**
 * 页面显示接口
 */
public interface PageManager {
    /**
     * 页面显示完成执行的方法
     * @return
     * @throws Exception
     */
    public void onShow();
    
    /**
     * 页面离开执行的方法
     * @return
     * @throws Exception
     */
    public void onLeave();
}
