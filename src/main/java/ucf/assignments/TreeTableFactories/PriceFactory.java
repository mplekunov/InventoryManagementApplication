package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TreeTableCell;
import ucf.assignments.Item;

import java.text.NumberFormat;

public class PriceFactory extends TreeTableCell<Item, String> {
    private JFXTextField textField;

    public PriceFactory() {}

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(textField.getText())));
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
                setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(getSerialNumber())));
                setGraphic(null);
            }
        }
    }

    private String getSerialNumber() {
        return getItem() == null ? "" : getItem().replaceAll("\\$", "");
    }

    private void createTextField() {
        textField = new JFXTextField(getText());
        textField.setMinWidth(this.getWidth());
        textField.setOnAction(e -> commitEdit(textField.getText()));
    }
}
