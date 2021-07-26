package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.geometry.Side;
import ucf.assignments.ControllerStyle.JFXTextFieldStyle;
import ucf.assignments.InputValidation.InputValidator;
import ucf.assignments.InputValidation.ValidationState;
import ucf.assignments.Models.Item;

import java.text.NumberFormat;

public class PriceFactory extends JFXTreeTableCell<Item, String> {
    private JFXTextField textField;

    public PriceFactory() {}

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();

            createTextField();
            setText(null);

            setGraphic(textField);
            textField.requestFocus();
            textField.positionCaret(getPrice().length());
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(getPrice())));
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

                setGraphic(null);
                setGraphic(textField);
            } else {
                setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(getPrice())));
                setGraphic(null);
            }
        }
    }

    @Override
    public void commitEdit(String newValue) {
        ValidationState state = InputValidator.validatePrice(newValue);

        if (state.equals(ValidationState.INCORRECT_FORMAT)) {
            JFXTextFieldStyle.setStyleOnError(textField, Side.TOP, "Price has incorrect format! Format Ex. (12.23, 12, 12.0)");
            textField.requestFocus();
        }
        else
            super.commitEdit(newValue);
    }

    private String getPrice() {
        return getItem() == null || getItem().isEmpty() ? "0" : getItem();
    }

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
