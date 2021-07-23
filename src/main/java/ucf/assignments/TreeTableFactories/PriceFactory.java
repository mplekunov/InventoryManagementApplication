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
        return getItem() == null ? "" : getItem();
    }

    private void createTextField() {
        textField = new JFXTextField(getText().replaceAll("\\$", ""));
        textField.setMinWidth(this.getWidth());
        textField.setOnAction(e -> commitEdit(textField.getText()));
    }
}
