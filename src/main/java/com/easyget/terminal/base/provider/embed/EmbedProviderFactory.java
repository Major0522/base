package com.easyget.terminal.base.provider.embed;

import com.easyget.terminal.base.util.FxmlUtil;

import javafx.fxml.FXMLLoader;

/**
 * EmbedProvider的工厂类。
 * 
 * @author wutianbin
 *
 */
public class EmbedProviderFactory {
	
	/**
	 * 根据FXML和对应的配置文件生成默认的EmbedProvider。
	 * <p> 
	 * 生成的EmbedProvider的init()为空方法，即没有任何处理语句，如果需要有相关的处理，则不能使用本方法。
	 * </p>
	 * @param fxmlName	对应的FXML文件的路径及名称。
	 * @param insertPosJson	嵌入的位置信息的JSON配置文件路径及名称。
	 * @return	对应的插件EmbedProvider。
	 * @throws Exception 
	 */
	public static EmbedProvider newEmbedProvider(String fxmlName, String insertPosJson) throws Exception {
        final FXMLLoader fxmlLoader = FxmlUtil.getFxmlLoader(fxmlName);
        
        // 根据配置把页面嵌入到对应的地方
        return new EmbedProvider(fxmlLoader, insertPosJson) {
            @Override
            public void init() throws Exception {

            }
        };
        
	}
	
}
