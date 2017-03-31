package com.easyget.terminal.base.view.control;

import javafx.scene.control.ProgressIndicator;

public class Progress extends Dialog {
	
	private ProgressIndicator progress;
	
	private static Progress INSTANCE;
	public static Progress getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new Progress();
		}
		return INSTANCE;
	}
	
	public Progress(){
		super(200, 200, "rgba(0,0,0,0)");
	}

	public void start() {
		if(INSTANCE.isShowing()){
			// 防止多个操作引起重复显示等待圈
			return;
		}
		
		progress = new ProgressIndicator();
		progress.setPrefSize(200, 200);
		
		getRoot().getChildren().add(progress);
		
		INSTANCE.showDialog();
	}
	public void stop() {
		if (INSTANCE != null) {
			getRoot().getChildren().clear();
			progress = null;
			INSTANCE.hide();
		}
	}

	@Override
	public void overTimeHandler() {
	}
}