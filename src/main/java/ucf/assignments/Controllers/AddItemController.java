package ucf.assignments.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import ucf.assignments.Converters.DateConverter;
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
    private final Pattern pattern;
    private final Pattern pricePattern;
    private Item item;

    private double xOffset = 0;
    private double yOffset = 0;

    public AddItemController(ItemModel itemModel) {
        this.itemModel = itemModel;
        pattern = Pattern.compile("^(?:([A-Z0-9]{10})(?!\\w))");
        pricePattern = Pattern.compile("^(?=.)(([0-9]*)(\\.([0-9]{1,2}))?)$");
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

        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                setNameTextField(item);
        });

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

    private void setOnClickDateTextField(MouseEvent event, Item item) {
        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            if (dateTextField.isFocused()) {
                dateTextField.setVisible(false);
                dateTextField.setManaged(false);

                JFXDatePicker datePicker = new JFXDatePicker(DateConverter.toDate(dateTextField.getText()));

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
            priceTextField.positionCaret(formattedPrice.length());
        }
    }

    private void setPriceTextField(Item item) {
        boolean isMatched = pricePattern.matcher(priceTextField.getText()).find();

        if (!isMatched) {
            priceTextField.requestFocus();
            System.out.println("Price Incorrect Format");
        } else {
            item.setPrice(priceTextField.getText());
            priceTextField.setText(NumberFormat.getCurrencyInstance().format(Double.parseDouble(priceTextField.getText())));
        }
    }

    private void setSerialNumberTextField(Item item) {
        boolean isMatched = pattern.matcher(serialNumberTextField.getText()).find();
        boolean isFound = itemModel.getAllItems().stream().anyMatch(obj -> obj.getSerialNumber().getValue().equals(serialNumberTextField.getText()));
        boolean isEmpty = serialNumberTextField.getText().isEmpty();

        if (isEmpty || isFound || !isMatched) {
            if (isEmpty)
                System.out.println("Field is Empty");
            else if (isFound)
                System.out.println("Already Exists");
            else
                System.out.println("Incorrect Format");

            serialNumberTextField.requestFocus();
        } else
            item.setSerialNumber(serialNumberTextField.getText());
    }

    private void setOnClickCancelBtn(MouseEvent event) {
        ((Stage)mainPane.getScene().getWindow()).close();
        cleanFields();
    }

    private void setOnClickSaveBtn(MouseEvent event, Item item) {
        if (nameTextField.getText().isEmpty() || serialNumberTextField.getText().isEmpty() || priceTextField.getText().isEmpty()) {
            if (nameTextField.getText().isEmpty())
                System.out.println("Name Field is Empty");

            if (serialNumberTextField.getText().isEmpty())
                System.out.println("Serial Number Field is Empty");

            if (priceTextField.getText().isEmpty())
                System.out.println("Price Field is Empty");

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
        if (nameTextField.getText().isEmpty()) {
            System.out.println("Error, empty string");
            nameTextField.requestFocus();
        } else
            item.setName(nameTextField.getText());
    }

    private void setFocusOnMainPaneWhenMouseClicked(InputEvent mouseEvent) {
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            Node node = (Node) mouseEvent.getSource();
            node.requestFocus();
        }
    }

}
