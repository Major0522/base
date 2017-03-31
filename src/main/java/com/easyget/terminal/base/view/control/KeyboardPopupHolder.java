package com.easyget.terminal.base.view.control;

import org.comtel.javafx.control.KeyBoardPopup;
import org.comtel.javafx.factory.KeyBoardPopupFactory;
import org.comtel.javafx.factory.KeyBoardType;

import com.easyget.terminal.base.util.ContextUtil;
import com.easyget.terminal.base.util.CursorUtils;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 */
public class KeyboardPopupHolder {

	/**
	 * 混合键盘（大写字母 +数字）
	 */
	private static KeyBoardPopup mixKeyBoard;
	/**
	 * 数字键盘
	 */
	private static KeyBoardPopup numKeyBoard;

	private static String style = KeyboardPopupHolder.class.getResource("/base/css/KeyboardButtonStyle.css").toExternalForm();
	
	public static void init(final Stage stage) {
		numKeyBoard = KeyBoardPopupFactory.create(KeyBoardType.ONLY_NUMBERS, 1.8);
		initKeyBoard(numKeyBoard, stage);

		mixKeyBoard = KeyBoardPopupFactory.create(KeyBoardType.ONLY_LETTERS_AND_NUMBERS, 1.6);
		initKeyBoard(mixKeyBoard, stage);
	}

	public static void show(){
		Scene scene = ContextUtil.getPrimaryStage().getScene();
		scene.focusOwnerProperty().addListener(listener);
		
		if(!scene.getStylesheets().contains(style)){
			scene.getStylesheets().add(style);
		}
	}
	
	public static void hide(){
		ContextUtil.getPrimaryStage().getScene().focusOwnerProperty().removeListener(listener);
	}
	
	private static ChangeListener<Node> listener = new ChangeListener<Node>() {
		@Override
		public void changed(ObservableValue<? extends Node> value, Node n1, Node n2) {
			if (n2 instanceof TextField) {
				initCurrentTextNode((TextField) n2);
				setPopupVisible(true, (TextField) n2);
			} else {
				setPopupVisible(false, null);
			}
		}
	};

	/**
	 * 给stage中的scene加入软键盘
	 */
	private static void initKeyBoard(KeyBoardPopup keyBoard, final Stage stage) {
		keyBoard.setAutoHide(true); // 点击非键盘区域自动隐藏键盘
		initKeyBoardColseHandler(keyBoard, stage);
		keyBoard.show(stage);
		setPopupVisible(false, null);
	}

	private static void initKeyBoardColseHandler(KeyBoardPopup keyBoardPopup, final Stage stage) {
		keyBoardPopup.getKeyBoard().setOnKeyboardCloseButton(
				new EventHandler<Event>() {
					public void handle(Event event) {
						setPopupVisible(false, null);
						CursorUtils.moveFocusToNextText(stage.getScene().getRoot());
						
					}
				});
	}
	
	/**
	 * 根据TextField内容限制选择键盘类型
	 */
	private static KeyBoardPopup chooseKeyBoard(final TextField textNode) {
		KeyBoardPopup keyBoard = mixKeyBoard;
		
		if (textNode instanceof InputType) {
			InputType type = (InputType) textNode;
			if (type.getType() == InputType.AUTO_MIX) {
				// 显示数字及字母键盘
				keyBoard = mixKeyBoard;
			}
			if (type.getType() == InputType.AUTO_NUMBER) {
				// 显示纯数字键盘
				keyBoard = numKeyBoard;
			}
		}
		return keyBoard;
	}

	/**
	 * 在TextField处显示键盘
	 */
	private static void showKeyBoard(final TextField textNode) {
		final KeyBoardPopup keyBoardPopup = chooseKeyBoard(textNode);
		if(null == keyBoardPopup){
			return;
		}
		
		Platform.runLater(new Runnable() {
			private Animation fadeAnimation;

			@Override
			public void run() {
				Rectangle2D textNodeBounds = new Rectangle2D(textNode
						.getLocalToSceneTransform().getTx(), textNode
						.getLocalToSceneTransform().getTy(), textNode
						.getWidth(), textNode.getHeight());

				Rectangle2D screenBounds = Screen.getPrimary().getBounds();

				keyBoardPopup
						.setX((1024 - keyBoardPopup
								.getWidth()) / 2);

				if (textNodeBounds.getMaxY() + keyBoardPopup.getHeight() > screenBounds
						.getMaxY()) {
					keyBoardPopup.setY(textNodeBounds.getMinY()
							- keyBoardPopup.getHeight() - 5);
				} else {
					keyBoardPopup.setY(textNodeBounds.getMaxY() + 20);
				}

				if (fadeAnimation != null) {
					fadeAnimation.stop();
				}
				keyBoardPopup.getKeyBoard().setOpacity(0.0);
				FadeTransition fade = new FadeTransition(Duration.seconds(.5),
						keyBoardPopup.getKeyBoard());
				fade.setToValue(1.0);
				fade.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						fadeAnimation = null;
					}
				});

				ScaleTransition scale = new ScaleTransition(Duration
						.seconds(.5), keyBoardPopup.getKeyBoard());
				scale.setToX(1);
				scale.setToY(1);

				ParallelTransition tx = new ParallelTransition(fade, scale);
				fadeAnimation = tx;
				tx.play();
				keyBoardPopup.show(keyBoardPopup.getOwnerWindow());
			}
		});
	}

	/**
	 * 隐藏键盘
	 */
	private static void hideKeyBoard() {
		final boolean is_OLAN_KeyBoardShowing = (null != mixKeyBoard)
				&& mixKeyBoard.isShowing();
		final boolean isON_KeyBoardShowing = (null != numKeyBoard)
				&& numKeyBoard.isShowing();

		if (!is_OLAN_KeyBoardShowing && !isON_KeyBoardShowing) {
			// 没有键盘弹出，直接返回，提升性能和稳定性
			return;
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (is_OLAN_KeyBoardShowing) {
					mixKeyBoard.hide();
				}
				if (isON_KeyBoardShowing) {
					numKeyBoard.hide();
				}
			}
		});
	}

	/**
	 * 设置键盘的可见属性
	 * @param visible
	 * @param textNode
	 */
	private static void setPopupVisible(final boolean visible,
			final TextField textNode) {
		if (visible) {
			showKeyBoard(textNode);
		} else {
			hideKeyBoard();
		}

	}

	/**
	 * 给文本输入框添加click显示键盘事件，防止关闭键盘后显示不出来键盘。
	 * 
	 * @param textNode
	 */
	private static void initCurrentTextNode(final TextField textNode) {
		if (textNode.getOnMouseClicked() == null) {
			textNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (!textNode.isFocused()) {
						textNode.requestFocus();
					}
					setPopupVisible(true, textNode);

				}
			});
			textNode.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					if (!newValue) {
						// 当输入框失去焦点时，隐藏键盘
						setPopupVisible(false, textNode);
					}
				}
			});
		}
	}

}
