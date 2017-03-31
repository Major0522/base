package com.easyget.terminal.base.view.control;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PageBox extends HBox {
	
	private EventHandler<Event> onLastAction;
	private EventHandler<Event> onNextAction;
	
	private final Label btnNext = new Label();
	private final Label btnLast = new Label();
	private final Label pageInfo = new Label();
	
	/**
	 * 总页数
	 */
	private int pageNum = 1;
	/**
	 * 当前页数
	 */
	private int index = 1;
	
	public PageBox(){
		this.btnLast.setText("上一页");
		this.btnLast.setStyle("-fx-font-size:24;-fx-text-fill:#ed7020;-fx-font-weight:bold;");
		this.btnLast.setDisable(true);
		this.btnLast.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				index--;
				btnLast.setDisable(index <= 1 ? true : false);
				btnNext.setDisable(index >= pageNum ? true : false);

				refreshPageInfo();
				
				if(onLastAction != null){
					onLastAction.handle(event);
				}
			}
		});
		
		this.btnNext.setText("下一页");
		this.btnNext.setStyle("-fx-font-size:24;-fx-text-fill:#ed7020;-fx-font-weight:bold;");
		this.btnNext.setDisable(true);
		this.btnNext.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				index++;
				
				btnLast.setDisable(index <= 1 ? true : false);
				btnNext.setDisable(index >= pageNum ? true : false);

				refreshPageInfo();

				if(onNextAction != null){
					onNextAction.handle(event);
				}
			}
		});
		pageInfo.setAlignment(Pos.CENTER);
		pageInfo.setMinWidth(100);
		pageInfo.setStyle("-fx-font-size: 24;-fx-text-fill: rgb(60, 60, 60);");
		pageInfo.setText(index + "/" + pageNum);
		
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.getStyleClass().add("page-box");
		this.getChildren().addAll(btnLast, pageInfo, btnNext);
	}
	
	private void refreshPageInfo(){
		pageInfo.setText(index + "/" + pageNum);
	}

	public EventHandler<Event> getOnNextAction() {
		return onNextAction;
	}
	public void setOnNextAction(EventHandler<Event> handler) {
		this.onNextAction = handler;
	}
	public EventHandler<Event> getOnLastAction() {
		return onLastAction;
	}
	public void setOnLastAction(EventHandler<Event> handler) {
		this.onLastAction = handler;
	}
	public void setPageNum(int num){
		this.pageNum = num;
		this.btnNext.setDisable(num > 1 ? false : true);
		this.index = 1;
		refreshPageInfo();
	}
	public void setIndex(int index){
		this.index = index;
	}
	public int getIndex(){
		return this.index;
	}
}