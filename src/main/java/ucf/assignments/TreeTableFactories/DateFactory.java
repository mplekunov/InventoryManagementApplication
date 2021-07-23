package ucf.assignments.TreeTableFactories;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import ucf.assignments.Models.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateFactory extends JFXTreeTableCell<Item, LocalDate> {

    private JFXDatePicker datePicker;

    public DateFactory() {}

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        setGraphic(null);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null)
                    datePicker.setValue(getDate());

                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
            }
        }
    }

    private LocalDate getDate() {
        return getItem() == null ? LocalDate.now() : getItem();
    }

    private void createDatePicker() {
        datePicker = new JFXDatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction(e -> commitEdit(LocalDate.from(datePicker.getValue())));
    }
}




























