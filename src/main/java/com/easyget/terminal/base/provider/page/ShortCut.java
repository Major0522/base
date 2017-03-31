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
 * 快捷方式bean
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public class ShortCut {
    /**快捷方式放在哪个页面上fxml 路径**/
    private String parent;

    /**快捷方式位置x**/
    private double layoutX;

    /**快捷方式位置y**/
    private double layoutY;

    /**快捷方式高度**/
    private double prefHeight;

    /**快捷方式宽度**/
    private double prefWidth;

    /**快捷方式图标**/
    private String icon;

    /**快捷方式直接的fxml 路径**/
    private String self;

    public ShortCut() {
        super();
    }

    public ShortCut(String parent, double layoutX, double layoutY, double prefHeight, double prefWidth, String icon,
            String self) {
        super();
        this.parent = parent;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.prefHeight = prefHeight;
        this.prefWidth = prefWidth;
        this.icon = icon;
        this.self = self;
    }

    public String getIcon() {
        return icon;
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

    public double getPrefHeight() {
        return prefHeight;
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public String getSelf() {
        return self;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
    }

    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return "ShortCut [parent=" + parent + ", layoutX=" + layoutX + ", layoutY=" + layoutY + ", prefHeight="
                + prefHeight + ", prefWidth=" + prefWidth + ", icon=" + icon + ", self=" + self + "]";
    }

}
