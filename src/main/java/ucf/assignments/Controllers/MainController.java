package ucf.assignments.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;
import ucf.assignments.SceneManager;
import ucf.assignments.TreeTableFactories.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class MainController {

    @FXML
    private JFXTreeTableView itemTable;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXButton addItemBtn;

    @FXML
    private JFXButton importItemsBtn;

    @FXML
    private JFXButton exportItemsBtn;

    @FXML
    private JFXButton exitBtn;

    @FXML
    private BorderPane mainPane;

    private final SceneManager sceneManager;
    private final ItemModel itemModel;
    private final EditItemController editItemController;
    private final Collection<JFXTreeTableColumn<ucf.assignments.Models.Item, ?>> columns = new ArrayList<>();


    public MainController(ItemModel itemModel, EditItemController editItemController, SceneManager sceneManager) {
        this.itemModel = itemModel;
        this.sceneManager = sceneManager;
        this.editItemController = editItemController;
    }


    //DatePicker throws NullPointerException when the value is null
    public void initialize() {
        exitBtn.setOnAction(this::exitProgram);

        importItemsBtn.setOnAction(this::importFile);

        exportItemsBtn.setOnAction(this::exportFile);

        addItemBtn.setOnAction(this::setOnActionAddItemBtn);

        Callback<TreeTableColumn<Item, LocalDate>, TreeTableCell<Item, LocalDate>> dateFactory = param -> new DateFactory();
        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> nameFactory = param -> new NameFactory();
        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> priceFactory = param -> new PriceFactory();
        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> serialNumberFactory = param -> new SerialNumberFactory();

        JFXTreeTableColumn<Item, LocalDate> itemDate = initTreeTableColumn("Date", 120, "-fx-alignment: center;", dateFactory);
        JFXTreeTableColumn<Item, String> itemName = initTreeTableColumn("Name", 200, "-fx-alignment: center;", nameFactory);
        JFXTreeTableColumn<Item, String> itemPrice = initTreeTableColumn("Price", 100, "-fx-alignment: center", priceFactory);
        JFXTreeTableColumn<Item, String> itemSerialNumber = initTreeTableColumn("Serial Number", 200, "-fx-alignment: center", serialNumberFactory);

        itemDate.setCellValueFactory(param -> param.getValue().getValue().getDate());
        itemName.setCellValueFactory(param -> param.getValue().getValue().getName());
        itemSerialNumber.setCellValueFactory(param -> param.getValue().getValue().getSerialNumber());
        itemPrice.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPrice().getValue().toString()));

        itemDate.setOnEditCommit(cellEditEvent ->
            cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setDate(cellEditEvent.getNewValue()));

        itemName.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setName(cellEditEvent.getNewValue()));

        itemPrice.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setPrice(Double.parseDouble(cellEditEvent.getNewValue())));

        itemSerialNumber.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setSerialNumber(cellEditEvent.getNewValue()));

        initItemTable();
    }

    private void initItemTable() {
        TreeItem<Item> items = new RecursiveTreeItem<>(itemModel.getItemObservable(), RecursiveTreeObject::getChildren);

        itemTable.setRoot(items);
        itemTable.setShowRoot(false);
        itemTable.setEditable(true);
        itemTable.getColumns().setAll(columns);

        itemTable.setRowFactory((Callback<TreeTableView<ucf.assignments.Models.Item>, TreeTableRow<ucf.assignments.Models.Item>>) param -> {
            final TreeTableRow<ucf.assignments.Models.Item> row = new JFXTreeTableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            rowMenu.setOnShown(event -> rowMenu.show(row, Side.RIGHT, -row.getWidth(), row.getHeight()));

            MenuItem editItem = new MenuItem("Edit");
            MenuItem removeItem = new MenuItem("Delete");

            editItem.setOnAction(actionEvent -> setOnActionEditItemBtn(actionEvent, row));

            removeItem.setOnAction(actionEvent -> setOnActionRemoveItemBtn(actionEvent, row));

            rowMenu.getItems().add(editItem);
            rowMenu.getItems().add(removeItem);

            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
                    .then(rowMenu)
                    .otherwise((ContextMenu)null));

            return row;
        });
    }

    private <T> JFXTreeTableColumn<Item, T> initTreeTableColumn(String name, int width, String style, Callback<TreeTableColumn<Item, T>, TreeTableCell<Item, T>> factory) {
        JFXTreeTableColumn<Item, T> column = new JFXTreeTableColumn<>(name);
        column.setPrefWidth(width);
        column.setStyle(style);

        column.setCellFactory(factory);

        columns.add(column);

        return column;
    }

    private void setOnActionRemoveItemBtn(ActionEvent actionEvent, TreeTableRow<ucf.assignments.Models.Item> row) {
        itemModel.getItemObservable().remove(row.getItem());
        itemTable.getSelectionModel().clearSelection();
    }

    private void setOnActionEditItemBtn(ActionEvent actionEvent, TreeTableRow<ucf.assignments.Models.Item> row) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit");

        window.setScene(sceneManager.getScene("EditItemView"));
        editItemController.initialize(row.getItem());
        window.showAndWait();
    }

    private void setOnActionAddItemBtn(ActionEvent event) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Item");

        window.setScene(sceneManager.getScene("AddItemView"));
        window.showAndWait();
    }

    private void exitProgram(ActionEvent actionEvent) {
        itemModel.upload();

        Platform.exit();
        System.exit(0);
    }


    private void exportFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        String appDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        if (appDir.endsWith(".jar"))
            appDir = new File(appDir).getParent();

        fileChooser.setInitialDirectory(new File(appDir));

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tab Separated Value", "*.tsv"));

//        toDoListModel.upload(database);

        if (!itemModel.getItemObservable().isEmpty()) {
            File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

            if (file != null) {
                if (!file.getName().contains("."))
                    file = new File(file.getPath() + ".sqlite");

//                try {
////                    Files.copy(Paths.get(database.getFilePath()), Path.of(file.getPath()), REPLACE_EXISTING);
//                } catch (java.io.IOException e) {
//                    e.printStackTrace();
//                }

            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Action is not valid");
                errorAlert.setContentText("You have no items for export");
                errorAlert.showAndWait();
            }
        }
    }

    private void importFile(ActionEvent actionEvent) {
        //upload model to database

        FileChooser fileChooser = new FileChooser();

        String appDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        if (appDir.endsWith(".jar"))
            appDir = new File(appDir).getParent();

        fileChooser.setInitialDirectory(new File(appDir));

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Tab Separated Value", "*.tsv"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
//            database.setConnection(selectedFile.getAbsolutePath());

        }
    }
}
