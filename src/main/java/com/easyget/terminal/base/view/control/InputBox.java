package com.easyget.terminal.base.view.control;

import com.easyget.terminal.base.IConfig;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class InputBox extends HBox {

    private static String STYLE_BORDER_LIGHT = "-fx-border-color:rgb(234,111,110);";

    private static String STYLE_BORDER_DARK = "-fx-border-color:rgb(203,203,203);";

    private String icon;

    private String prompt;

    private boolean isPassword = false;

    private boolean isPhone = false;

    private final ImageView iconImg = new ImageView();

    private TextField textField;

    private ImageView btnClear;

    private EventHandler<Event> onLoseFocus;

    private EventHandler<Event> onGainFocus;

    private short inputType = InputType.AUTO_NUMBER;

    private int maxLength;

    public InputBox() {
        this.getStyleClass().add("input-box");
        this.setAlignment(Pos.CENTER);
    }

    public String getIcon() {
        return icon;
    }

    public boolean getIsNumber() {
        return this.inputType == InputType.AUTO_NUMBER ? true : false;
    }

    public boolean getIsPassword() {
        return this.isPassword;
    }

    public boolean getIsPhone() {
        return this.isPhone;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getText() {
        if (isPassword) {
            return textField.getText();
        } else {
            return ((LimitedTextField) textField).getStr();
        }
    }

    private void initBtnClear() {
        btnClear = new ImageView(IConfig.RESOURCE_BASE + "/images/text-field-clear.png");
        btnClear.setVisible(false);
        btnClear.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                textField.setText("");
                textField.requestFocus();
            }
        });
    }

    private void initTextField() {
        if (isPassword) {
            textField = new LimitedPwdField(maxLength);
        } else {
            textField = new LimitedTextField(inputType, maxLength, isPhone);
        }
        textField.setPromptText(this.prompt);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                btnClear.setVisible(textField.getText().length() != 0);
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // 输入框变红色
                    setStyle(STYLE_BORDER_LIGHT);
                    if (icon != null && !"".equals(icon)) {
                        iconImg.setImage(new Image(IConfig.RESOURCE_BASE + "/images/" + icon + ".png"));
                    }
                    if (onGainFocus != null) {
                        onGainFocus.handle(null);
                    }
                } else {
                    // 输入框变暗色
                    setStyle(STYLE_BORDER_DARK);
                    if (icon != null && !"".equals(icon)) {
                        iconImg.setImage(new Image(IConfig.RESOURCE_BASE + "/images/" + icon + "-unchecked.png"));
                    }
                    if (onLoseFocus != null) {
                        onLoseFocus.handle(null);
                    }
                }
            }
        });
        this.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                textField.requestFocus();
            }
        });
        initBtnClear();
        this.getChildren().addAll(iconImg, textField, btnClear);
    }

    public void setIcon(String icon) {
        this.icon = icon;
        iconImg.setImage(new Image(IConfig.RESOURCE_BASE + "/images/" + icon + "-unchecked.png"));
    }

    public void setIsNumber(boolean value) {
        if (value) {
            this.inputType = InputType.AUTO_NUMBER;
        } else {
            this.inputType = InputType.AUTO_MIX;
        }
    }

    public void setIsPassword(boolean value) {
        this.isPassword = value;
        initTextField();
    }

    public void setIsPhone(boolean value) {
        this.isPhone = value;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setOnGainFocus(EventHandler<Event> handler) {
        this.onGainFocus = handler;
    }

    public void setOnLoseFocus(EventHandler<Event> handler) {
        this.onLoseFocus = handler;
    }

    public void setPrompt(String text) {
        this.prompt = text;
    }

    public void setText(String text) {
        this.textField.setText(text);
    }
}