package com.easyget.terminal.base.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.entity.Substance;
import com.easyget.terminal.base.model.Result;
import com.easyget.terminal.base.module.McsHandler;
import com.easyget.terminal.base.provider.BaseController;
import com.easyget.terminal.base.service.SubstanceService;
import com.easyget.terminal.base.task.TakeSubstanceTask;
import com.easyget.terminal.base.view.control.ErrorLabel;
import com.easyget.terminal.base.view.control.InputPane;
import com.easyget.terminal.base.view.control.KeyPane;
import com.easyget.terminal.base.view.control.MessageDialog;
import com.easyget.terminal.base.view.control.MessageDialog.MessageType;
import com.easyget.terminal.base.view.control.Progress;

import javafx.event.Event;
import javafx.fxml.FXML;

/**
 * 取样品Controller类
 */
public class TakeController extends BaseController {

	private static Logger logger = Logger.getLogger(TakeController.class);

	@FXML
	private ErrorLabel errorLabel;
	@FXML
	private InputPane takeCode;
	@FXML
	private KeyPane keyPane;

	@FXML
	public void btnTakeEvent() {
		String takeCodeStr = takeCode.getText();
		
		logger.info("领取样品，提取码：" + takeCodeStr);

		if (!isConnect()) {
			return;
		}

		Progress.getInstance().start();
		
		takeCode.setText("");

		Result result = SubstanceService.getInstance().validateTakeCode(takeCodeStr);
		
		if (result.isSuccess()) {
			Substance substance = (Substance) result.getValue();
			TakeSubstanceTask.take(substance);
		} else {
			Progress.getInstance().stop();
			errorLabel.setErrorInfo((String) result.getValue());
		}
	}

	private boolean isConnect() {
		if (McsHandler.getInstance().isConnected()) {
			return true;
		} else {
			MessageDialog.getInstance().show(MessageType.WARNING, "系统正在初始化，请稍后再试！", null);
			return false;
		}
	}

	@FXML
	public void onExitAction(Event event) {
		gotoHome();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		keyPane.setShower(takeCode.getShower());
	}

	@Override
	public void initCall(Map<String, String> pluginInitConfig) {
		takeCode.setText("");
	}

	@Override
	public void onLeave() {
		super.onLeave();
	}
	
}