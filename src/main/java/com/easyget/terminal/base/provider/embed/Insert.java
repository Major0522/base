/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : abinet_terminal_plugin_base
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.embed;

import java.util.Map;

/**
 * <p>
 * 嵌入式插件插入到哪个页面哪个位置由此类设置
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public class Insert {
    /**插入父级页面**/
    private String parent;

    /**插入父级页面位置x**/
    private double layoutX;

    /**插入父级页面位置y**/
    private double layoutY;
    
    /**插入父级页面层级**/
    private int level = Integer.MAX_VALUE;

    /**初始化插件的时候的配置参数map**/
    private Map<String, String> pluginInitConfig;

    public Insert() {
        super();
    }

    public Insert(String parent, double layoutX, double layoutY, Map<String, String> pluginInitConfig) {
        super();
        this.parent = parent;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.pluginInitConfig = pluginInitConfig;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public String getParent() {
        return parent;
    }

    public Map<String, String> getPluginInitConfig() {
        return pluginInitConfig;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setPluginInitConfig(Map<String, String> pluginInitConfig) {
        this.pluginInitConfig = pluginInitConfig;
    }

    @Override
    public String toString() {
        return "Insert [parent=" + parent + ", layoutX=" + layoutX + ", layoutY=" + layoutY + ", pluginInitConfig="
                + pluginInitConfig + "]";
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
