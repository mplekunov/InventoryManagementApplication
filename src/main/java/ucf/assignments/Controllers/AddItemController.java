package ucf.assignments.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
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

    private ItemModel itemModel;

    public AddItemController(ItemModel itemModel) {
        this.itemModel = itemModel;
    }

    public void initialize() {
        Item item = new Item();



        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (itemModel.getItemObservable().stream().anyMatch((query) -> query.getSerialNumber().equals(newValue)))
                    System.out.println("Already exists");
                else
                    item.setName(newValue);
            }
        });

        serialNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> item.setSerialNumber(newValue));
        serialNumberTextField.focusedProperty().addListener((observable, unfocused, focused) -> {
            if (!focused) {
                if (serialNumberTextField.getText().isEmpty()) {
                    serialNumberTextField.requestFocus();
                    System.out.println("Error, empty string");
                }
            }
        });

        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
          if (!newValue.isEmpty())
              item.setPrice(Double.parseDouble(newValue));
          else
              item.setPrice(0d);
        });

        dateTextField.setOnMouseClicked(event -> {
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
        });

        saveBtn.setOnMouseClicked(event -> {
            itemModel.getItemObservable().add(item);
            ((Stage)mainPane.getScene().getWindow()).close();
            this.initialize();

            nameTextField.setText("");
            serialNumberTextField.setText("");
            dateTextField.setText("");
            priceTextField.setText("");
        });

        cancelBtn.setOnMouseClicked(event -> {
            ((Stage)mainPane.getScene().getWindow()).close();
            this.initialize();

            nameTextField.setText("");
            serialNumberTextField.setText("");
            dateTextField.setText("");
            priceTextField.setText("");
        });
    }
}
