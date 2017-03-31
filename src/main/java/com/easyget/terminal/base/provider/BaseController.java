package com.easyget.terminal.base.provider;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.easyget.terminal.base.util.PageUtil;
import com.easyget.terminal.base.view.control.Progress;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * fxml 文件所有Controller的基类
 */
public abstract class BaseController implements Initializable, PageManager, PluginManager {

	public static void gotoHome() {
		PageUtil.jumpToPage("/home/fxml/Home.fxml");
	}
	
	@FXML
	public void onExitAction(Event event) {
		gotoHome();
	}

	@Override
	public void onShow() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@Override
	public void onLeave(){
		Progress.getInstance().stop();
	}

	@Override
	public void initCall(Map<String, String> pluginInitConfig) {

	}
}