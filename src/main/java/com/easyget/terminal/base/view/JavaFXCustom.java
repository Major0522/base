package com.easyget.terminal.base.view;

import com.easyget.terminal.base.model.TableStyle;
import com.seagen.ecc.utils.StringUtils;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class JavaFXCustom {

	/**
	 * 禁止表格拖动列
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void forbidTurnback(final TableView table) {
		table.getColumns().addListener(new ListChangeListener() {
			boolean isTurnback = false;
			@Override
			public void onChanged(Change change) {
				if (!isTurnback) {
					while (change.next()) {
						if (!change.wasPermutated() && !change.wasUpdated()) {
							isTurnback = true;
							table.getColumns().setAll(change.getRemoved());
						}
					}
				} else {
					isTurnback = false;
				}
			}
		});
	}
	
	/**
	 * 表格数据颜色样式渲染。需要将表格中插入的带有按钮的列名称输入作为排除项
	 * 
	 * @param table TableView对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void styleTable(TableView table){
		if(table == null || table.getColumns().isEmpty()){
			return;
		}
		
		for(int i = 0; i < table.getColumns().size(); i++){
			final TableColumn<TableStyle, String> col = (TableColumn)table.getColumns().get(i);
			String title = col.getText();
			if("操作".equals(title) || "通知".equals(title)){
				continue;
			}
			col.setCellFactory(new Callback<TableColumn<TableStyle, String>, TableCell<TableStyle, String>>() {
				
				@Override
				public TableCell<TableStyle, String> call(TableColumn<TableStyle, String> param) {
					return new TableCell<TableStyle, String>(){
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            
                            if(empty){
                            	setText(null);
                            	setGraphic(null);
                            }else{
                            	TableRow row = this.getTableRow();
                                if(row != null){
                                	TableStyle style = (TableStyle)row.getItem();
                                	if(style == null){
                                		return;
                                	}
                                	this.alignmentProperty().set(Pos.CENTER);
                                	String color = style.getColor().getValue();
                                    if(StringUtils.isNotEmpty(color)){
                                    	this.setStyle("-fx-text-fill:" + color);
                                    }
                                }
                            	setText(item);
                            }
                        }
					};
				}
			});
		}
	}
}