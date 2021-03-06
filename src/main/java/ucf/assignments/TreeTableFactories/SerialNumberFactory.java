/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import ucf.assignments.ControllerStyle.JFXTextFieldStyle;
import ucf.assignments.InputValidation.InputValidator;
import ucf.assignments.InputValidation.ValidationState;
import ucf.assignments.Models.Item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialNumberFactory extends JFXTreeTableCell<Item, String> {
    private JFXTextField textField;
    private final ObservableList<Item> items;
    private String currentValue;

    public SerialNumberFactory(ObservableList<Item> itemObservable) {
        items = itemObservable;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            currentValue = getItem();

            createTextField();
            setText(null);

            setGraphic(textField);
            textField.requestFocus();
            textField.positionCaret(getSerialNumber().length());
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
                setText(getSerialNumber());
                setGraphic(null);
            }
        }
    }

    @Override
    public void commitEdit(String newValue) {
        ValidationState state = InputValidator.validateSerialNumber(newValue, items);

        long duplicates = 0;
        if (!currentValue.equals(newValue))
            duplicates = items.stream().filter(item -> item.getSerialNumber().getValue().equals(newValue)).count();

        if (state.equals(ValidationState.PASSED) && duplicates == 0)
            super.commitEdit(newValue);
        else {
            textField.requestFocus();
            if (duplicates > 0)
                JFXTextFieldStyle.setStyleOnError(textField, Side.TOP, "Serial Number already exists!");
            else if (state.equals(ValidationState.INCORRECT_FORMAT))
                JFXTextFieldStyle.setStyleOnError(textField, Side.TOP, "Serial Number has incorrect format!");
        }
    }

    private String getSerialNumber() {
        return getItem() == null ? "" : getItem();
    }

    private void createTextField() {
        textField = new JFXTextField(getSerialNumber());
        textField.setMinWidth(this.getWidth());

        textField.setOnAction(e -> commitEdit(textField.getText()));

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                cancelEdit();
            }
        });
    }
}
