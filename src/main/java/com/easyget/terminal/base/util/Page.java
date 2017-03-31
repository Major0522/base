/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : 终端项目
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.util;

import java.util.List;

import com.easyget.terminal.base.provider.page.ShortCut;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * 
 * 代表显示的页面的分装bean
 * @author niujunhong
 */
public class Page {
    /** 页面 fxml loader**/
    private FXMLLoader loader;

    /** 缓存的 loader 对应的 pane **/
    private Pane pane;

    /** 是否为启动引导**/
    private boolean isBoot = false;

    /** 区域组成集合**/
    private List<Area> areaList;

    /** 快捷方式组成集合**/
    private List<ShortCut> shortCutList;

    public Page(FXMLLoader loader, List<Area> areaList, List<ShortCut> shortCutList) {
        super();
        this.loader = loader;
        this.areaList = areaList;
        this.shortCutList = shortCutList;
        this.isBoot = false;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Pane getPane() {
        return pane;
    }

    public List<ShortCut> getShortCutList() {
        return shortCutList;
    }

    public boolean isBoot() {
        return isBoot;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public void setBoot(boolean isBoot) {
        this.isBoot = isBoot;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public void setShortCutList(List<ShortCut> shortCutList) {
        this.shortCutList = shortCutList;
    }

    @Override
    public String toString() {
        final String loaderStr = loader == null ? "" : loader + "->" + loader.getLocation().toString();
        return "Page [loader=" + loaderStr + ", isBoot=" + isBoot + ", areaList=" + areaList + ", shortCutList="
                + shortCutList + "]";
    }

	public boolean is(String string) {
		return false;
	}

}
