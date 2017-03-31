package com.easyget.terminal.base.view.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SelectDialog extends Dialog {
	
	private Button btnFirst;
	private Button btnSecond;
	
	private EventHandler<Event> onFirstHandler;
	private EventHandler<Event> onSecondHandler;

	private Label labelInfo;
	private Label labelMsg;
	
	private static SelectDialog INSTANCE;
	public static SelectDialog getInstance(){
		if(INSTANCE == null){
			INSTANCE = new SelectDialog();
		}
		return INSTANCE;
	}
	
	public SelectDialog() {
		super(500,300);
		getRoot().getChildren().add(createPane());
	}
	
	private VBox createPane() {
		labelMsg = new Label();
		labelMsg.getStyleClass().add("label-content");
		labelMsg.setAlignment(Pos.CENTER);
		labelMsg.setWrapText(true);

		labelInfo = new Label();
		labelInfo.getStyleClass().add("label-info");
		labelInfo.setAlignment(Pos.CENTER);
		labelInfo.setWrapText(true);
		labelInfo.setPrefSize(400, 95);
		
		VBox box = new VBox(30);
		box.setAlignment(Pos.CENTER);
		box.getStyleClass().add("dialog");
		box.getChildren().addAll(labelInfo, labelMsg, createBtnBox());
		
		return box;
	}

	private HBox createBtnBox() {
		btnFirst = new Button();
		btnFirst.setPrefSize(180, 70);
		btnFirst.getStyleClass().add("button-main");
		btnFirst.setOnMouseClicked(new EventHandler<Event>() {
			@Override public void handle(Event event) {
				hide();
				if(onFirstHandler != null){
					onFirstHandler.handle(event);
				}
			}
		});
		
		btnSecond = new Button();
		btnSecond.setPrefSize(180, 70);
		btnSecond.getStyleClass().add("button-sub");
		btnSecond.setOnMouseClicked(new EventHandler<Event>() {
			@Override public void handle(Event event) {
				hide();
				stopTimeLine();
				if(onSecondHandler != null){
					onSecondHandler.handle(event);
				}
			}
		});
		
		HBox box = new HBox(40);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(btnSecond, btnFirst);
		VBox.setMargin(box, new Insets(20, 50, 20, 50));
		return box;
	}

	public void show(String info, String message,String first, String second, EventHandler<Event> onFirstHandler, EventHandler<Event> onSecondHandler){
		super.showDialog();
		
		this.labelInfo.setText(info);
		this.labelMsg.setText(message);
		
		this.btnFirst.setText(first);
		this.btnSecond.setText(second);
		
		this.onFirstHandler = onFirstHandler;
		this.onSecondHandler = onSecondHandler;
	}
	
	@Override
	public void overTimeHandler() {
		this.hide();
	}
}