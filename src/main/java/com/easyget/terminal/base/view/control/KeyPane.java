package com.easyget.terminal.base.view.control;


import com.easyget.terminal.base.util.AudioUtils;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class KeyPane extends VBox {
	private Label shower;

	public KeyPane() {
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(initKeyBox());
	}

	private VBox initKeyBox() {
		VBox box = new VBox(21);
		box.getChildren().addAll(createLine());

		VBox.setMargin(box, new Insets(21, 51, 21, 51));
		return box;
	}

	private HBox[] createLine() {
		HBox box1 = new HBox(51);
		HBox box2 = new HBox(51);
		HBox box3 = new HBox(51);
		HBox box4 = new HBox(51);

		box1.getChildren().addAll(createKeyButton("1"), createKeyButton("2"), createKeyButton("3"));
		box2.getChildren().addAll(createKeyButton("4"), createKeyButton("5"), createKeyButton("6"));
		box3.getChildren().addAll(createKeyButton("7"), createKeyButton("8"), createKeyButton("9"));
		box4.getChildren().addAll(createBackspaceButton(), createKeyButton("0"), createClearButton());

		return new HBox[] { box1, box2, box3, box4 };
	}

	private KeyButton createClearButton() {
		KeyButton key = new KeyButton(new Image("/base/images/button/clear.png"));
		key.setOnClicked(new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				shower.setText("");
				AudioUtils.getInstance().playKeyPanClick();
			}
		});
		return key;
	}

	private KeyButton createBackspaceButton() {
		KeyButton key = new KeyButton(new Image("/base/images/button/backspace.png"));
		key.setOnClicked(new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				String str = shower.getText();
				if(str.length() > 0){
					shower.setText(str.substring(0, str.length() - 1));
					AudioUtils.getInstance().playKeyPanClick();
				}
			}
		});
		return key;
	}

	private KeyButton createKeyButton(final String text) {
		KeyButton key = new KeyButton(text);
		key.setOnClicked(new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				String str = shower.getText();
				shower.setText(str.concat(text));
				AudioUtils.getInstance().playKeyPanClick();
			}
		});
		return key;
	}
	
	private class KeyButton extends Pane{
		
		ImageView background = new ImageView(new Image("/base/images/button/key-released.png"));
		
		private EventHandler<Event> onClicked;
		
		public KeyButton(String text) {
			this();
			
			Label label = new Label(text);
			label.setStyle("-fx-font-size: 50;-fx-text-fill: #ffffff;");
			label.setAlignment(Pos.CENTER);
			label.setPrefSize(70, 70);
			this.getChildren().addAll(background, label);
		}
		
		public KeyButton(Image image) {
			this();
			
			ImageView icon = new ImageView(image);
			icon.setLayoutX(18);
			icon.setLayoutY(18);
			
			this.getChildren().addAll(background, icon);
		}
		
		public KeyButton() {
			this.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(onClicked != null){
						onClicked.handle(event);
					}
				}
			});
			this.setOnMousePressed(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					background.setImage(new Image("/base/images/button/key-pressed.png"));
				}
			});
			this.setOnMouseReleased(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					background.setImage(new Image("/base/images/button/key-released.png"));
				}
			});
		}
		
		public void setOnClicked(EventHandler<Event> handler){
			this.onClicked = handler;
		}
	}

	public void setShower(Label label) {
		this.shower = label;
	}
}