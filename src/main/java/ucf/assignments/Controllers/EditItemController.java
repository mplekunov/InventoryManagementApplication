package ucf.assignments.Controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import ucf.assignments.Models.Item;

import java.text.NumberFormat;

public class EditItemController {

    @FXML
    private JFXTextField serialNumberTextField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField priceTextField;

    public EditItemController() {
    }

    public void initialize(Item item) {
        nameTextField.setText(item.getName().getValue());
        serialNumberTextField.setText(item.getSerialNumber().getValue());
        priceTextField.setText(NumberFormat.getCurrencyInstance().format(item.getPrice().getValue()));
    }

}
