package com.easyget.terminal.base.view.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LimitedPwdField extends PasswordField implements InputType {

	private int maxLength;
	
	private short inputType = AUTO_MIX;
	
	public LimitedPwdField(int maxLength) {
		this.maxLength = maxLength;
		textProperty().addListener(limitLength(this));
	}

	private ChangeListener<String> limitLength(final TextField field) {
		return new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				// 当最大长度=0时，表示输入长度不受限制
				if (maxLength > 0) {
					if (newValue.length() > maxLength) {
						field.setText(oldValue);
					}
				}
			}
		};
	}

	@Override
	public short getType() {
		return this.inputType;
	}
}