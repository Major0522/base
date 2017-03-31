package com.easyget.terminal.base.view.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 消息框：提示、告警、错误（异常）
 * @author Major
 */
public class MessageDialog extends Dialog {
	
	private EventHandler<Event> onConfirm;

	private ImageView icon = new ImageView();
	private Label labelType = new Label();
	private Label labelMsg = new Label();
	
	
	private static MessageDialog INSTANCE;
	public static MessageDialog getInstance(){
		if(INSTANCE == null){
			INSTANCE = new MessageDialog();
		}
		return INSTANCE;
	}
	
	public MessageDialog() {
		super(500, 310);
		getRoot().getChildren().add(createPane());
	}
	
	private VBox createPane() {
		labelMsg.getStyleClass().add("label-content");
		labelMsg.setAlignment(Pos.CENTER);
		labelMsg.setWrapText(true);
		labelMsg.setPrefSize(400, 95);
		
		VBox pane = new VBox(20);
		pane.setAlignment(Pos.CENTER);
		pane.getStyleClass().add("dialog");
		pane.getChildren().addAll(createTypeBox(), labelMsg, createBtnBox());
		
		return pane;
	}

	private HBox createBtnBox() {
		Button btnConfirm = new Button("确    定");
		btnConfirm.setPrefSize(180, 70);
		btnConfirm.getStyleClass().add("button-sub");
		btnConfirm.setOnMouseClicked(new EventHandler<Event>() {
			@Override public void handle(Event event) {
				hide();
				stopTimeLine();
				if(onConfirm != null){
					onConfirm.handle(event);
				}
			}
		});
		
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(btnConfirm);
		VBox.setMargin(box, new Insets(0, 160, 20, 160));
		return box;
	}

	private HBox createTypeBox() {
		labelType.getStyleClass().add("label-info");
		labelType.setGraphicTextGap(30);
		
		HBox box = new HBox();
		box.setSpacing(30);
		box.getChildren().addAll(icon, labelType);
		VBox.setMargin(box, new Insets(20, 0, 0, 20));
		return box;
	}
	
	public void show(MessageType type, String message, EventHandler<Event> onConfirm){
		super.showDialog();
		
		this.onConfirm = onConfirm;
		this.labelMsg.setText(message);
		
		startTimeLine(null, 10);
		
		String iconURL = null;
		String typeName = null;
		if(type == MessageType.INFO){
			iconURL = "/base/images/info.png";
			typeName = "提示：";
		}
		if(type == MessageType.WARNING){
			typeName = "警告：";
			iconURL = "/base/images/warning.png";
		}
		if(type == MessageType.ERROR){
			iconURL = "/base/images/error.png";
			typeName = "错误：";
		}
		labelType.setGraphic(new ImageView(iconURL));
		labelType.setText(typeName);
	}
	
	@Override
	public void overTimeHandler() {
		this.hide();
		
		if(onConfirm != null){
			onConfirm.handle(null);
		}
	}
	
	public enum MessageType{
		INFO,		//提示
		WARNING,	//警告
		ERROR;		//错误
	}
}