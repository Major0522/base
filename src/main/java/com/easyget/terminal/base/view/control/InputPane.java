package com.easyget.terminal.base.view.control;

import com.easyget.terminal.base.util.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class InputPane extends Pane{
	
	private Label prompt = new Label();
	private Label content = new Label();
	
	private int maxLength;
	
	public InputPane() {
		prompt.setStyle("-fx-font-size:30;-fx-text-fill:#d9d9d9;");
		prompt.setPrefSize(this.getPrefWidth(), 80);
		prompt.setLayoutX(40);
		
		content.setLayoutX(40);
		content.setAlignment(Pos.CENTER);
		content.setStyle("-fx-font-size:50;-fx-text-fill:rgb(60,60,60)");
		content.setPrefSize(this.getPrefWidth(), 80);
		content.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(StringUtils.isNotEmpty(newValue)){
					if(StringUtils.isEmpty(oldValue)){
						prompt.setVisible(false);
					}
					if(newValue.length() > maxLength){
						content.setText(oldValue);
					}
				}else{
					prompt.setVisible(true);
				}
			}
		});
		this.setStyle("-fx-background-color:#ffffff;-fx-background-radius:5;");
		this.getChildren().addAll(prompt, content);
	}
	
	public String getPrompt() {
		return prompt.getText();
	}

	public void setPrompt(String prompt) {
		this.prompt.setText(prompt);
	}
	
	public String getText(){
		return content.getText();
	}
	public void setText(String text){
		content.setText(text);
	}

	public Label getShower() {
		return content;
	}
	
	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}