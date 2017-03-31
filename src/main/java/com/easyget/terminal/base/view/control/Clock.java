package com.easyget.terminal.base.view.control;

import org.apache.log4j.Logger;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Clock extends HBox {

	private static Logger logger = Logger.getLogger(Clock.class);
	
	private EventHandler<Event> onOverTime;

	private int secondNum;

	private Timeline timeline;
	
	private Label second;
	
	private String pageName;

	public Clock() {
		this.setAlignment(Pos.CENTER);
		
		second = new Label();
		second.setPrefWidth(70);
		second.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-weight:bold;-fx-font-size: 36;");
		
		Label label = new Label("倒计时：");
        label.setFont(new Font("Microsoft YaHei", 24));
        label.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        
		getChildren().addAll(label, second);
		
        startTimeLine();
	}
	
	public void pause() {
		if (timeline != null) {
			timeline.pause();
		}
	}
	
	public boolean isRunning(){
		if (timeline != null) {
			return Status.RUNNING.equals(timeline.getStatus());
		}
		return false;
	}

	public void play() {
		if (timeline != null) {
			timeline.play();
		}
	}

	public void setSeconds(int sec) {
		this.secondNum = sec;
		second.setText(Integer.toString(sec));
	}

	public int getSeconds() {
		return this.secondNum;
	}

	private void startTimeLine() {
		logger.debug("playTimeLine:" + hashCode());
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(1),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								refreshClocks(event);
							}
						}));
		timeline.play();
	}

	private void refreshClocks(ActionEvent event) {
		if (secondNum % 10 == 0) {
			logger.debug("【" + pageName + "】距离结束还剩" + secondNum + "秒,timerHash=" + hashCode());
		}
		secondNum--;
		if (secondNum >= 0) {
			second.setText(Integer.toString(secondNum));
		} else {
			// 超时后的处理
			logger.info("倒计时结束");

			if (this.onOverTime != null) {
				onOverTime.handle(event);
			}
			pause();
		}
	}
	
	public void setOnOverTime(EventHandler<Event> value){
		this.onOverTime = value;
	}
	public EventHandler<Event> getOnOverTime() {
		return this.onOverTime;
	}

	public void setPageName(String pageTitle) {
		this.pageName = pageTitle;
	}
}