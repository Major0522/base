/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.util.Map;

import javafx.fxml.FXMLLoader;

/**
 * <p>
 * 插件显示在页面上的区域
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public class Area {
    /**插入父级页面**/
    private FXMLLoader loader;

    /**插入父级页面位置x**/
    private double layoutX;

    /**插入父级页面位置y**/
    private double layoutY;

    /**初始化插件的时候的配置参数map**/
    private Map<String, String> pluginConfig;

    public Area(FXMLLoader loader, double layoutX, double layoutY, Map<String, String> pluginConfig) {
        super();
        this.loader = loader;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.pluginConfig = pluginConfig;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Map<String, String> getPluginConfig() {
        return pluginConfig;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public void setPluginConfig(Map<String, String> pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    @Override
    public String toString() {
        return "Area [loader=" + loader + ", layoutX=" + layoutX + ", layoutY=" + layoutY + ", pluginInitConfig="
                + pluginConfig + "]";
    }

}
