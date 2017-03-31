package com.easyget.terminal.base.view.control;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class IconButton extends Pane {

	private String STYLE_MAIN = 
			"-fx-background-radius:10;" + 
			"-fx-background-color:rgb(255,255,255);" + 
			"-fx-border-color:rgb(204,204,204);" + 
			"-fx-border-radius:8;" + 
			"-fx-border-width:2;";
    private String STYLE_NAME = "-fx-text-fill: rgb(145,145,147);-fx-font-size: 30;-fx-font-weight:bold;";
    private String STYLE_HINT = "-fx-text-fill: rgb(230, 75, 73);-fx-font-size: 16;-fx-font-weight:bold;";

    private String icon;

    private final Label name = new Label();

    private final Label hint = new Label();

    public IconButton() {
        this.setStyle(STYLE_MAIN);
        this.setFocusTraversable(false);

        name.setStyle(STYLE_NAME);
        hint.setStyle(STYLE_HINT);

        name.setAlignment(Pos.CENTER);
        hint.setAlignment(Pos.BASELINE_RIGHT);

        this.getChildren().addAll(name, hint);
    }

    public String getIcon() {
        return icon;
    }

    public ContentDisplay getPos() {
        return name.getContentDisplay();
    }

    public String getText() {
        return this.name.getText();
    }

    public void setHint(String value) {
        if (value != null && !"".equals(value)) {
            this.hint.setText(value);
            this.hint.setPrefWidth(this.getPrefWidth());
            this.hint.setLayoutY(this.getPrefHeight() * 0.8);
        }
    }

    public void setIcon(String icon) {
        name.setPrefSize(this.getPrefWidth(), this.getPrefHeight());
        if (icon != null && !"".equals(icon)) {
            name.setGraphic(new ImageView(new Image(icon)));
        }
    }

    public void setPos(ContentDisplay pos) {
        name.setContentDisplay(pos);

        if (pos == ContentDisplay.LEFT) {
            name.setGraphicTextGap(30);
        }
    }

    public void setText(String value) {
        this.name.setText(value);
        this.name.setPrefSize(this.getPrefWidth(), this.getPrefHeight());
    }
}