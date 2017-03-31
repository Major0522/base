package com.easyget.terminal.base.view.control;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LimitedTextField extends TextField implements InputType {

	/**
	 * 手机号分隔符
	 */
	private final static String SEPARATOR = "-";

	private int maxLength;

	private boolean isPhone = false;

	private short inputType = AUTO_NUMBER;

	public LimitedTextField(short inputType, int maxLength, boolean isPhone) {
		this.inputType = inputType;
		this.maxLength = maxLength;
		this.isPhone = isPhone;
		
		textProperty().addListener(textChange(this));
	}

	/**
	 * 获取输入框内有效的内容（不包括分隔符，如"-"）
	 */
	public final String getTextStr() {
		return getKeyStr(this.getText());
	}

	/**
	 * 获取指定字符串中有效的部分（如，去掉分隔符“-”）
	 * @param str  指定字符串
	 * @return 有效字符串
	 */
	private String getKeyStr(String str) {
		if(str == null){
			return "";
		}
		return str.replace(SEPARATOR, "");
	}
	
	public String getStr(){
		return getKeyStr(this.getText());
	}

	private ChangeListener<String> textChange(final TextField field) {
		return new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {

				// 限制输入长度。当最大长度=0时，表示输入长度不受限制
				if (maxLength > 0) {
					if (getKeyStr(newValue).length() > maxLength) {
						field.setText(oldValue);
						return;
					}
				}

				// 输入类型是电话号码
				if (isPhone) {
					final int beginPos = field.getCaretPosition();
					String keyStr = getKeyStr(newValue);
					String addedSeparatorStr = insertSeparator(keyStr);

					if (!addedSeparatorStr.equals(keyStr)) {
						// 如果增加或者删除了间隔符，则需要移动光标
						moveCursor(beginPos, addedSeparatorStr, newValue,
								oldValue);
					}
				}
			}

			/**
			 * 添加间隔符
			 * 
			 * @param keyStr
			 *            元字符串
			 */
			private String insertSeparator(String keyStr) {
				StringBuilder s = new StringBuilder(keyStr);
				if (keyStr.length() > 7) {
					s.insert(7, SEPARATOR);
				}
				if (keyStr.length() > 3) {
					s.insert(3, SEPARATOR);
				}
				String addedSeparatorStr = s.toString();
				if(field.isFocused()){
					field.setText(addedSeparatorStr);
				}
				return addedSeparatorStr;
			}

			/**
			 * 移动光标
			 */
			private void moveCursor(final int beginPos,
					String addedSeparatorStr, String newValue, String oldValue) {
				final int insertSeparatorCursorPos = analysisCorsorPos(
						beginPos, addedSeparatorStr, newValue, oldValue);
				if (insertSeparatorCursorPos != beginPos) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							field.positionCaret(insertSeparatorCursorPos);
						}
					});
				}
			}

			/**
			 * @param beginPos
			 *            光标初始位置
			 * @return 光标应该移动的位置
			 */
			private int analysisCorsorPos(final int beginPos,
					String addedSeparatorStr, String newValue, String oldValue) {
				int newPos = beginPos;
				if (addedSeparatorStr.length() > newValue.length()) {
					// 增加了间隔符，光标需要向后移动2个字符
					if (beginPos == 3 || beginPos == 8) {
						newPos = beginPos + 2;
					}
				} 
				if (addedSeparatorStr.length() < newValue.length()) {
					// 减少了间隔符,光标向前移动2个字符
					if (beginPos == 5 || beginPos == 10) {
						newPos = beginPos - 2;
					}
				} else {
					// 没有增加也没有减少间隔符，处理光标需要越过间隔符的逻辑
					if (newValue.length() > oldValue.length()) {
						// 如果新增字符前,光标在间隔符的前边2个字符，则需向后越过间隔符
						if (beginPos == 2 || beginPos == 7) {
							newPos = beginPos + 2;
						}
					} 
					if (newValue.length() < oldValue.length()) {
						// 如果删除字符前,光标在间隔符的后边2个字符，则需向前越过间隔符
						if (beginPos == 5 || beginPos == 10) {
							newPos = beginPos - 2;
						}
					} else {
						// 不可能出现的情况，啥也不做
					}
				}
				return newPos;
			}
		};
	}

	@Override
	public short getType() {
		return this.inputType;
	}
}