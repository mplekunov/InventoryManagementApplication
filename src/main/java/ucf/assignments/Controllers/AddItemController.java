package ucf.assignments.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;

import java.time.LocalDate;

public class AddItemController {
    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField serialNumberTextField;

    @FXML
    private JFXTextField dateTextField;

    @FXML
    private JFXTextField priceTextField;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private AnchorPane Test;

    @FXML
    private AnchorPane mainPane;

    private final ItemModel itemModel;

    public AddItemController(ItemModel itemModel) {
        this.itemModel = itemModel;
    }

    public void initialize() {
        Item item = new Item();

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> nameTextProperty(item, newValue));
        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> emptyTextPropertyChecker(newValue));

        serialNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> item.setSerialNumber(newValue));
        serialNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> emptyTextPropertyChecker(newValue));

        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> priceTextProperty(item, newValue));

        dateTextField.setOnMousePressed(event -> setOnClickDateTextField(event, item));

        saveBtn.setOnMouseClicked(event -> setOnClickSaveBtn(event, item));

        cancelBtn.setOnMouseClicked(event -> setOnClickCancelBtn(event, item));
    }

    private void setOnClickCancelBtn(MouseEvent event, Item item) {
        ((Stage)mainPane.getScene().getWindow()).close();
        this.initialize();
        cleanFields();
    }

    private void setOnClickSaveBtn(MouseEvent event, Item item) {
        itemModel.getItemObservable().add(item);
        setOnClickCancelBtn(event, item);
    }

    private void cleanFields() {
        nameTextField.setText("");
        serialNumberTextField.setText("");
        dateTextField.setText("");
        priceTextField.setText("");
    }

    private void nameTextProperty(Item item, String newValue) {
        if (!newValue.isEmpty()) {
            if (itemModel.getItemObservable().stream().anyMatch((query) -> query.getSerialNumber().getValue().equals(newValue)))
                System.out.println("Already exists");
            else
                item.setName(newValue);
        }
    }

    private void serialNumberTextProperty(Item item, String newValue) {

    }

    private void emptyTextPropertyChecker(boolean isFocused) {
        if (!isFocused) {
            if (serialNumberTextField.getText().isEmpty()) {
                serialNumberTextField.requestFocus();
                System.out.println("Error, empty string");
            }
        }
    }

    private void priceTextProperty(Item item, String newValue) {
        if (!newValue.isEmpty())
            item.setPrice(Double.parseDouble(newValue));
        else
            item.setPrice(0d);
    }

    private void setOnClickDateTextField(MouseEvent event, Item item) {
        dateTextField.setVisible(false);
        dateTextField.setManaged(false);

        JFXDatePicker datePicker = new JFXDatePicker(LocalDate.now());
        datePicker.setMinWidth(dateTextField.getWidth());
        datePicker.setOnAction(e -> {
            item.setDate(datePicker.getValue());

            dateTextField.setVisible(true);
            dateTextField.setManaged(true);
            Test.getChildren().remove(datePicker);
        });

        Test.getChildren().add(datePicker);
    }
}
