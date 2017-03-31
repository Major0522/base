/*
 * 深圳壹号柜科技股份有限公司源代码，版权归深圳壹号柜科技股份有限公司所有。
 * 
 * 项目名称 : abinet_terminal_plugin_base
 * 创建日期 : 2016年3月25日
 * 修改历史 : 
 *     1. [2016年3月25日]创建文件 by niujunhong
 */
package com.easyget.terminal.base.provider.embed;

import java.util.List;

import com.easyget.terminal.base.provider.Provider;

import javafx.fxml.FXMLLoader;

/**
 * <p>
 * 嵌入式插件提供者bean
 * </p> 
 * 
 * 创建日期 : 2016年3月25日
 * @author niujunhong
 */
public abstract class EmbedProvider extends Provider {
    /**插件fxml loader**/
    private FXMLLoader fxmlLoader;

    /**插入到目标页面的插入点集合**/
    private List<Insert> insertList;

    public EmbedProvider(FXMLLoader mainLoader, String JsonFileName) throws Exception {
        super();

        if (mainLoader == null) {
            throw new Exception("main Loader is empty!");
        }
        
        this.fxmlLoader = mainLoader;
        
        this.insertList = createListFormJson(JsonFileName, Insert.class);
    }

    public List<Insert> getInsertList() {
        return insertList;
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public void setInsertList(List<Insert> insertList) {
        this.insertList = insertList;
    }

    @Override
    public String toString() {
        final String loaderStr = fxmlLoader == null ? "" : fxmlLoader + "->" + fxmlLoader.getLocation().toString();
        return "EmbedProvider [mainLoader=" + loaderStr + ", insertList=" + insertList + "]";
    }

}
