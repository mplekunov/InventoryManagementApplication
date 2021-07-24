package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import javafx.collections.ObservableList;
import ucf.assignments.Models.Item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialNumberFactory extends JFXTreeTableCell<Item, String> {
    private JFXTextField textField;
    private final Pattern pattern;
    private final ObservableList<Item> items;
    private String currentValue;

    public SerialNumberFactory(ObservableList<Item> itemObservable) {
        pattern = Pattern.compile("^(?=([A-Z0-9]{10})(?!\\w))");
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
        Matcher matcher = pattern.matcher(newValue);

        long duplicates = 0;
        if (!currentValue.equals(newValue))
            duplicates = items.stream().filter(item -> item.getSerialNumber().getValue().equals(newValue)).count();

        if (matcher.find() && duplicates == 0)
            super.commitEdit(newValue);
        else {
            textField.requestFocus();
            System.out.println("Wrong Format");
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
