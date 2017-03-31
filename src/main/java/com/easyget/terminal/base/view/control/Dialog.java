package com.easyget.terminal.base.view.control;

import com.easyget.terminal.base.util.ApplicationUtil;
import com.easyget.terminal.base.util.ContextUtil;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public abstract class Dialog extends Stage  {
	
	public static final int pageH = 768;
	public static final int pageW = 1024;
	
	private Pane root;

	/**
	 * Need a parameter, the value of StageStyle 
	 * @param style StageStyle value
	 */
	public Dialog(int width, int height, String color){
		root = new Pane();
		root.setLayoutX((pageW - width) / 2);
		root.setLayoutY((pageH - height) / 2);
		root.setStyle("-fx-background-color:" + color + ";-fx-background-radius:8;");
		
		Pane background = new Pane();
		background.setPrefSize(pageW, pageH);
		background.getStyleClass().add("background");
		background.getChildren().add(root);
		
		Scene scene = new Scene(background, Color.TRANSPARENT);
		scene.getStylesheets().add("/base/css/modal-dialog.css");
		if (!ApplicationUtil.isDebug()) {
			scene.setCursor(Cursor.NONE);
		}
		
		this.setX(0);
		this.setY(0);
		this.initStyle(StageStyle.TRANSPARENT);
		this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(ContextUtil.getPrimaryStage());
		this.setScene(scene);
	}
	
	public Dialog(int width, int height){
		this(width, height, "rgb(255,255,255)");
	}

	/**
	 * StageStyle is The default value : StageStyle.UNDECORATED
	 */
	
	public Pane getRoot(){
		return this.root;
	}
	
	public void showDialog(){
		super.show();
	}
	
	public void hide(){
		super.hide();
		
		stopTimeLine();
	}
	
	
	private int secondNum;
	private Timeline timeline;
	/**
	 * 启动对话框倒计时计时器   （特别注意：如果启动计时器，需要在关闭/退出/取消等Button操作时执行 stopTimeLine()）
	 * @param timer 显示Label
	 * @param seconds 超时时间（秒）
	 */
	public void startTimeLine(final Label timer, int seconds) {
		secondNum = seconds;
		
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(
			new KeyFrame(Duration.seconds(1),
				new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						secondNum--;
						if (secondNum > 0) {
							if(timer != null){
								timer.setText(Integer.toString(secondNum));
							}
						} else {
							overTimeHandler();
							stopTimeLine();
						}		
					}
				}));
		timeline.play();
	}
	
	/**
	 * 停止话框倒计时计时器
	 */
	public void stopTimeLine(){
		if(timeline != null){
			timeline.stop();
			timeline = null;
		}
	}
	
	public abstract void overTimeHandler();
}