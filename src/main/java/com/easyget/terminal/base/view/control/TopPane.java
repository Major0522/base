package com.easyget.terminal.base.view.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TopPane extends HBox {
	
	private Label home;
	private Label title;
	private Clock clock;
	
	
	public TopPane() {
		home = new Label("首页", new ImageView(new Image("/base/images/home.png")));
		home.setFont(new Font("Microsoft YaHei", 30));
		home.setStyle("-fx-font-size: 24;-fx-text-fill: #ffffff;");
		HBox.setMargin(home, new Insets(0, 0, 0, 50));
		
		title = new Label();
		title.setAlignment(Pos.CENTER);
		title.setStyle("-fx-text-fill: rgb(255, 255, 255);-fx-font-size: 24;");
		title.setPrefWidth(600);
		
		clock = new Clock();
		
		this.setAlignment(Pos.CENTER_LEFT);
		this.setPrefSize(1024, 80);
		this.setSpacing(50);
		this.getChildren().addAll(home, title, clock);
	}
	
	public Label getHomeLabel() {
		return this.home;
	}
	
	public Clock getClock(){
		return this.clock;
	}

	public void setSecondText(String secondText) {
		clock.setSeconds(Integer.parseInt(secondText));
	}
	
	public String getSecondText(){
		return String.valueOf(clock.getSeconds());
	}
	
	public void setPageTitle(String pageTitle) {
		title.setText(pageTitle);
		clock.setPageName(pageTitle);
	}
	public String getPageTitle(){
		return title.getText();
	}
	
	public void setOnExit(EventHandler<Event> onExit) {
		clock.setOnOverTime(onExit);
		home.setOnMouseClicked(onExit);
	}
	public EventHandler<Event> getOnExit(){
		return clock.getOnOverTime();
	}
	
	/**
	 * 禁用【首页】按钮，防止正在等待的任务中断
	 */
	public void setDisExit(boolean value){
		home.setDisable(value);
	}
}