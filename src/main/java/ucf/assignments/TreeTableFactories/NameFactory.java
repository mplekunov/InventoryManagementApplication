package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TreeTableCell;
import ucf.assignments.Item;

public class NameFactory extends TreeTableCell<Item, String> {
    private JFXTextField textField;

    public NameFactory() {}

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

        setText(textField.getText());
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

    private String getName() {return getItem() == null ? "" : getItem(); }

    private void createTextField() {
        textField = new JFXTextField(getText());
        textField.setMinWidth(this.getWidth());
        textField.setOnAction(e -> commitEdit(textField.getText()));
    }
}
