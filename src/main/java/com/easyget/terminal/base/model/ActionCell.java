package com.easyget.terminal.base.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.text.Font;

/*再次打开格口按钮等界面显示**/
public class ActionCell<S, T> extends TableCell<S, T> {

    private final Button button;

    private ObservableValue<T> ov;

    private String actionName;

    private String displayName;

    public ActionCell(String actionName) {
        this(actionName, null);
    }

    public ActionCell(String actionName, String displayName) {
        this.button = new Button(actionName);

        this.button.setFocusTraversable(false);
        this.button.setFont(new Font("Microsoft YaHei", 24));
        this.button.setAlignment(Pos.CENTER);
        this.button.getStyleClass().add("action-button");

        this.setGraphic(button);

        this.actionName = actionName;
        this.displayName = displayName;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            this.setGraphic(null);
        } else {
            this.setGraphic(button);

            if (ov instanceof BooleanProperty) {
                button.disableProperty().unbindBidirectional((BooleanProperty) ov);
            }

            ov = getTableColumn().getCellObservableValue(getIndex());

            if (ov instanceof BooleanProperty) {
                button.disableProperty().bindBidirectional((BooleanProperty) ov);

                if ((Boolean) item) {
                    button.setText(this.displayName);
                } else {
                    button.setText(this.actionName);
                }
            }
        }
    }
}