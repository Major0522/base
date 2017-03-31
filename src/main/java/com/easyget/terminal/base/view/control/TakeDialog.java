package com.easyget.terminal.base.view.control;

import com.easyget.terminal.base.view.control.Dialog;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * 领取成功时的提示框
 */
public class TakeDialog extends Dialog {
	
	private Label time;
	private ImageView close;
	private SequentialTransition transition;
	
	private EventHandler<Event> onFinish;

	private static TakeDialog INSTANCE;

	public static TakeDialog getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TakeDialog();
		}
		return INSTANCE;
	}

	public TakeDialog() {
		super(450, 280);
		
		ImageView background = new ImageView(new Image("/base/images/dialog/take/dialog-background.png"));
		Label label = new Label("请取走物品，并请关闭格口");
		label.setStyle("-fx-font-size:30;-fx-text-fill:#ed7020;");
		VBox.setMargin(label, new Insets(0, 0, 30, 0));
		
		VBox box = new VBox(30, background, label);
		box.setAlignment(Pos.CENTER);
		
		close = new ImageView(new Image("/base/images/dialog/take/close-prompt.png"));
		close.setLayoutX(180);
		close.setLayoutY(95);
		
		initTransition();
		
		time = new Label();
		time.setAlignment(Pos.CENTER);
		time.setPrefWidth(40);
		time.setStyle("-fx-font-size:24;-fx-text-fill:#ffffff;");
		time.setLayoutX(405);
		time.setLayoutY(10);
		
		getRoot().getChildren().addAll(box, close, time);
	}
	
	private void initTransition(){
		// 透明度渐变（开始）
		FadeTransition fade1 = new FadeTransition();
		fade1.setDuration(Duration.millis(500));
		fade1.setAutoReverse(false);
		fade1.setFromValue(0);
		fade1.setToValue(1);
		fade1.setNode(close);
		// 透明度渐变（恢复）
		FadeTransition fade2 = new FadeTransition();
		fade2.setDuration(Duration.millis(500));
		fade2.setAutoReverse(false);
		fade2.setFromValue(1);
		fade2.setToValue(0);
		fade2.setNode(close);
        
    	// 位置渐变
		TranslateTransition translate1 = new TranslateTransition();
		translate1.setDuration(Duration.millis(2000));
		translate1.setAutoReverse(false);
		translate1.setFromX(25);
		translate1.setFromY(0);
		translate1.setToX(-25);
		translate1.setToY(0);
		translate1.setNode(close);
		
		transition = new SequentialTransition();
		transition.getChildren().addAll(fade1, translate1, fade2);
		transition.setAutoReverse(false);
		transition.setCycleCount(Timeline.INDEFINITE);
	}
	
	public void showDialog(int timeout, EventHandler<Event> onFinish){
		super.showDialog();
		this.onFinish = onFinish;
		
		time.setText(String.valueOf(timeout));
		startTimeLine(time, timeout);
		transition.play();
	}
	
	public void hide(){
		super.hide();
		
		transition.pause();
	}

	@Override
	public void overTimeHandler() {
		super.hide();
		
		transition.pause();
		
		if(onFinish != null){
			onFinish.handle(null);
		}
	}
}