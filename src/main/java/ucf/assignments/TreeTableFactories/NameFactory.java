/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.event.Event;
import javafx.geometry.Side;
import javafx.scene.control.*;
import ucf.assignments.ControllerStyle.JFXTextFieldStyle;
import ucf.assignments.Models.Item;

public class NameFactory extends JFXTreeTableCell<Item, String> {
    private JFXTextField textField;

    public NameFactory() {}

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            createTextField();
            setText(null);

            setGraphic(textField);
            textField.requestFocus();
            textField.positionCaret(getName().length());
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null)
                    textField.setText(getText());

                setText(null);
                setGraphic(textField);
            } else {
                setText(getName());
                setGraphic(null);
            }
        }
    }

    @Override
    public void commitEdit(String newValue) {
        if (!newValue.isEmpty())
            super.commitEdit(newValue);
        else {
            textField.requestFocus();
            JFXTextFieldStyle.setStyleOnError(textField, Side.TOP,"Name is empty!");
        }
    }

    private String getName() {return getItem() == null ? "" : getItem(); }

    private void createTextField() {
        textField = new JFXTextField(getItem());
        textField.setMinWidth(this.getWidth());

        textField.setOnAction(e -> commitEdit(textField.getText()));

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue)
                cancelEdit();
        });
    }
}
