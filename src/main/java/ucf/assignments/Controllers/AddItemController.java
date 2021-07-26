package ucf.assignments.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import ucf.assignments.ControllerStyle.JFXTextFieldStyle;
import ucf.assignments.Converters.DateConverter;
import ucf.assignments.InputValidation.InputValidator;
import ucf.assignments.InputValidation.ValidationState;
import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;

import java.text.NumberFormat;
import java.util.regex.Pattern;

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
    private AnchorPane datePane;

    @FXML
    private AnchorPane mainPane;

    private final ItemModel itemModel;
    private Item item;

    private double xOffset = 0;
    private double yOffset = 0;

    public AddItemController(ItemModel itemModel) {
        this.itemModel = itemModel;
        item = new Item();
    }

    public void initialize() {
        mainPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainPane.setOnMouseDragged(event -> {
            mainPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            mainPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });

        mainPane.setOnMouseClicked(this::setFocusOnMainPaneWhenMouseClicked);

        nameTextField.setOnMouseClicked(e -> movePositionCaretOnClick(nameTextField));
        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                setNameTextField(item);
        });

        serialNumberTextField.setOnMouseClicked(e -> movePositionCaretOnClick(serialNumberTextField));
        serialNumberTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                setSerialNumberTextField(item);
        });

        priceTextField.setOnMouseClicked(this::setOnClickPriceTextField);
        priceTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                setPriceTextField(item);
        });

        dateTextField.setOnMouseClicked(event -> setOnClickDateTextField(event, item));

        saveBtn.setOnMouseClicked(event -> setOnClickSaveBtn(event, item));

        cancelBtn.setOnMouseClicked(this::setOnClickCancelBtn);
    }

    private void movePositionCaretOnClick(JFXTextField textField) {
            textField.positionCaret(textField.getText().length());
    }

    private void setOnClickDateTextField(MouseEvent event, Item item) {
        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            if (dateTextField.isFocused()) {
                dateTextField.setVisible(false);
                dateTextField.setManaged(false);

                JFXDatePicker datePicker = new JFXDatePicker(DateConverter.toDate(dateTextField.getText()));
                datePicker.setDefaultColor(Color.rgb(35, 25, 66));

                datePicker.setPrefWidth(dateTextField.getWidth());
                datePicker.setPrefHeight(dateTextField.getHeight());

                AnchorPane.setBottomAnchor(datePicker, AnchorPane.getBottomAnchor(dateTextField));
                AnchorPane.setLeftAnchor(datePicker, AnchorPane.getLeftAnchor(dateTextField));
                AnchorPane.setRightAnchor(datePicker, AnchorPane.getRightAnchor(dateTextField));
                AnchorPane.setTopAnchor(datePicker, AnchorPane.getTopAnchor(dateTextField));

                datePicker.setOnAction(e -> {
                    item.setDate(datePicker.getValue());

                    dateTextField.setText(DateConverter.toString(datePicker.getValue()));

                    dateTextField.setVisible(true);
                    dateTextField.setManaged(true);
                    datePane.getChildren().remove(datePicker);
                });

                datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue)
                        datePicker.fireEvent(new ActionEvent());
                });

                datePane.getChildren().add(datePicker);
            }
        }
    }

    private void setOnClickPriceTextField(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            String formattedPrice = priceTextField.getText().replace("$", "").replaceAll(",", "");
            priceTextField.setText(formattedPrice);
            movePositionCaretOnClick(priceTextField);
        }
    }

    private void setPriceTextField(Item item) {
        ValidationState state = InputValidator.validatePrice(priceTextField.getText());

        if (!priceTextField.getText().isEmpty()) {
            if (state.equals(ValidationState.INCORRECT_FORMAT)) {
                priceTextField.requestFocus();
                JFXTextFieldStyle.setStyleOnError(priceTextField, Side.TOP,"Price has incorrect format! Format Ex. (12.23, 12, 12.0)");
            } else if (state.equals(ValidationState.PASSED)){
                item.setPrice(priceTextField.getText());
                priceTextField.setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(priceTextField.getText())));
            }
        }
    }

    private void setSerialNumberTextField(Item item) {
       ValidationState state = InputValidator.validateSerialNumber(serialNumberTextField.getText(), itemModel.getAllItems());

        if (!serialNumberTextField.getText().isEmpty()) {
            if (state.equals(ValidationState.INCORRECT_FORMAT) || state.equals(ValidationState.ALREADY_EXISTS)) {
                if (state.equals(ValidationState.ALREADY_EXISTS))
                    JFXTextFieldStyle.setStyleOnError(serialNumberTextField, Side.TOP,"Serial Number already exists!");
                else
                    JFXTextFieldStyle.setStyleOnError(serialNumberTextField, Side.TOP,"Serial Number has incorrect format!");

                serialNumberTextField.requestFocus();
            } else
                item.setSerialNumber(serialNumberTextField.getText());
        }
    }

    private void setOnClickCancelBtn(MouseEvent event) {
        ((Stage)mainPane.getScene().getWindow()).close();
        cleanFields();
    }

    private void setOnClickSaveBtn(MouseEvent event, Item item) {
        ValidationState priceState = InputValidator.validatePrice(priceTextField.getText().replace("$", "").replaceAll(",", ""));
        ValidationState serialNumberState = InputValidator.validateSerialNumber(serialNumberTextField.getText(), itemModel.getAllItems());

        if (nameTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || priceTextField.getText().isEmpty()
                || !priceState.equals(ValidationState.PASSED) || !serialNumberState.equals(ValidationState.PASSED)) {
            if (nameTextField.getText().isEmpty())
                JFXTextFieldStyle.setStyleOnError(nameTextField, Side.RIGHT,"Name is empty!");

            if (serialNumberTextField.getText().isEmpty())
                JFXTextFieldStyle.setStyleOnError(serialNumberTextField, Side.RIGHT,"Serial Number is empty!");

            if (priceTextField.getText().isEmpty())
                JFXTextFieldStyle.setStyleOnError(priceTextField, Side.RIGHT,"Price is empty!");

            if (priceState.equals(ValidationState.INCORRECT_FORMAT))
                JFXTextFieldStyle.setStyleOnError(priceTextField, Side.RIGHT,"Price has incorrect format! Format Ex. (12.23, 12, 12.0)");

            if (serialNumberState.equals(ValidationState.INCORRECT_FORMAT))
                JFXTextFieldStyle.setStyleOnError(serialNumberTextField, Side.RIGHT, "Serial Number has incorrect format!");
            else if (serialNumberState.equals(ValidationState.ALREADY_EXISTS))
                JFXTextFieldStyle.setStyleOnError(serialNumberTextField, Side.RIGHT, "Serial Number already exists!");
        } else {
            itemModel.getAllItems().add(item);
            setOnClickCancelBtn(event);
        }
    }

    private void cleanFields() {
        item = new Item();
        nameTextField.setText("");
        serialNumberTextField.setText("");
        dateTextField.setText("");
        priceTextField.setText("");
    }

    private void setNameTextField(Item item) {
        if (!nameTextField.getText().isEmpty())
            item.setName(nameTextField.getText());
    }

    private void setFocusOnMainPaneWhenMouseClicked(InputEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            Node node = (Node) mouseEvent.getSource();
            node.requestFocus();
        }
    }

}
