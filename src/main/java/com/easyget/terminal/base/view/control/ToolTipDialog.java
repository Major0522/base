package com.easyget.terminal.base.view.control;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToolTipDialog extends Dialog {
	
	private Label labelType;
	
	private static ToolTipDialog INSTANCE;
	public static ToolTipDialog getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ToolTipDialog();
		}
		return INSTANCE;
	}
	
	public ToolTipDialog() {
		super(300,120);
		getRoot().getChildren().add(createPane());
	}
	
	private VBox createPane() {
		VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		pane.setPrefSize(300, 120);
		pane.setSpacing(0);
		pane.setStyle("-fx-background-color:rgba(0,0,0,0.9);-fx-background-radius:5;");
		pane.getChildren().addAll(createHead());
		
		return pane;
	}
	private VBox createHead(){
		VBox box = new VBox();
		box.setPrefSize(300, 70);
		box.setAlignment(Pos.CENTER);
		box.setSpacing(0);
		box.getChildren().addAll(createType());
		return box;
	}
	
	private HBox createType() {
		labelType = new Label();
		labelType.setStyle("-fx-font-size:30;-fx-text-fill:#ffffff;-fx-background-color:rgba(255,255,255,0);");
		labelType.setAlignment(Pos.CENTER);
		labelType.setWrapText(true);
		labelType.setPrefSize(300, 30);

		HBox box = new HBox();
		box.setPrefSize(300, 30);
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(labelType);
		return box;
	}
	
	public void show(String info){
		super.showDialog();
		labelType.setText(info);
		
		startTimeLine(null, 1);
	}
	
	@Override
	public void overTimeHandler() {
		this.hide();
	}
}