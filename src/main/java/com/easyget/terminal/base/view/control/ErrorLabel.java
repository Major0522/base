package com.easyget.terminal.base.view.control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ErrorLabel extends Label{
	
	private int secondNum;
    private Timeline clearTimer;
	
	public ErrorLabel() {
		this.setStyle("-fx-font-size:24;-fx-text-fill:#eff124;");
	}
	
	public String getErrorInfo() {
		return this.getText();
	}
	public void setErrorInfo(String errorInfo) {
		this.setText(errorInfo);
		
		autoClear();
	}
	
	private void autoClear() {
		secondNum = 3;
		if(clearTimer == null){
			clearTimer = new Timeline();
			clearTimer.setCycleCount(Timeline.INDEFINITE);
			clearTimer.getKeyFrames().add(
				new KeyFrame(Duration.seconds(1),
					new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
							secondNum--;
							if (secondNum <= 0) {
								setText("");
								clearTimer.stop();
								clearTimer = null;
							}
						}
					}));
			clearTimer.play();
		}else if(clearTimer.getStatus().equals(Status.RUNNING)){
			clearTimer.playFromStart();
		}
	}
}
