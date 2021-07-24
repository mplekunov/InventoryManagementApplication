package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
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
