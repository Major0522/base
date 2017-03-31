package com.easyget.terminal.base.provider.page;

import java.util.ArrayList;
import java.util.List;

import com.easyget.terminal.base.provider.Provider;

import javafx.fxml.FXMLLoader;

/**
 * 页面插件提供者bean
 */
public abstract class PageProvider extends Provider {
	/** 是否为系统系统提供者 **/
	private boolean isBoot = false;

	/** 插件主fxml loader **/
	private FXMLLoader mainLoader;

	/** 插件所有fxml loader **/
	private List<FXMLLoader> exportLoaderList;

	/** 插件的快捷方式列表 **/
	private List<ShortCut> shortCutList;

	/**
	 * 构建PageProvider页面插件对象。
	 * 
	 * @param mainLoader	插件主FXMLLoader。
	 * @param isBoot	是否为系统提供者。
	 * @param shortCutJsonFileName	插件的快捷方式列表的配置文件名称。
	 * @param fxmlLoaders	所有以mainLoader为入口的其他FXMLLoader。 
	 * @throws Exception
	 */
	public PageProvider(FXMLLoader mainLoader, boolean isBoot,
			String shortCutJsonFileName, FXMLLoader... fxmlLoaders) throws Exception {
		super();

		if (mainLoader == null) {
			throw new Exception("main Loader is empty!");
		}
		
		final List<FXMLLoader> exportLoaderList = new ArrayList<FXMLLoader>();
		if (fxmlLoaders != null) {
			for (FXMLLoader loader: fxmlLoaders) {
				exportLoaderList.add(loader);
			}
		}

		this.mainLoader = mainLoader;
		
		this.exportLoaderList = exportLoaderList;
		this.exportLoaderList.add(mainLoader);

		this.isBoot = isBoot;
		this.shortCutList = createListFormJson(shortCutJsonFileName, ShortCut.class);
	}

	public List<FXMLLoader> getExportLoaderList() {
		return exportLoaderList;
	}

	public FXMLLoader getMainLoader() {
		return mainLoader;
	}

	public List<ShortCut> getShortCutList() {
		return shortCutList;
	}

	public boolean isBoot() {
		return isBoot;
	}

	public void setBoot(boolean isBoot) {
		this.isBoot = isBoot;
	}

	public void setExportLoaderList(List<FXMLLoader> exportLoaderList) {
		this.exportLoaderList = exportLoaderList;
	}

	public void setMainLoader(FXMLLoader mainLoader) {
		this.mainLoader = mainLoader;
	}

	public void setShortCutList(List<ShortCut> shortCutList) {
		this.shortCutList = shortCutList;
	}

	@Override
	public String toString() {
		final String loaderStr = mainLoader == null ? "" : mainLoader + "->" + mainLoader.getLocation().toString();
		return "PageProvider [isBoot=" + isBoot + ", mainLoader=" + loaderStr + ", exportLoaderList=" + exportLoaderList
				+ ", shortCutList=" + shortCutList + "]";
	}

}
