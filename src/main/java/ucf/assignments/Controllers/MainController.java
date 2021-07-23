package ucf.assignments.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import ucf.assignments.TreeTableFactories.DateFactory;
import ucf.assignments.TreeTableFactories.NameFactory;
import ucf.assignments.TreeTableFactories.PriceFactory;
import ucf.assignments.TreeTableFactories.SerialNumberFactory;

import java.io.File;
import java.time.LocalDate;

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
    private BorderPane mainPane;

    private SceneManager sceneManager;
    private ItemModel itemModel;
    private EditItemController editItemController;

    public MainController(ItemModel itemModel, EditItemController editItemController, SceneManager sceneManager) {
        this.itemModel = itemModel;
        this.sceneManager = sceneManager;
        this.editItemController = editItemController;
    }

    public void initialize() {
        importItemsBtn.setOnAction(this::importFile);
        exportItemsBtn.setOnAction(this::exportFile);

        itemTable.setEditable(true);

        addItemBtn.setOnAction(event -> {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Add Item");

            window.setScene(sceneManager.getScene("AddItemView"));
            window.showAndWait();
        });

        Callback<TreeTableColumn<Item, LocalDate>, TreeTableCell<Item, LocalDate>> dateCellFactory
                = (TreeTableColumn<Item, LocalDate> param) -> new DateFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> nameCellFactory
                = (TreeTableColumn<Item, String> param) -> new NameFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> serialNumberCellFactory
                = (TreeTableColumn<Item, String> param) -> new SerialNumberFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> priceCellFactory
                = (TreeTableColumn<Item, String> param) -> new PriceFactory();

        itemTable.setRowFactory((Callback<TreeTableView, TreeTableRow>) param -> {
            final TreeTableRow<Item> row = new JFXTreeTableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            rowMenu.setOnShown(event -> rowMenu.show(row, Side.RIGHT, -row.getWidth(), row.getHeight()));

            MenuItem editItem = new MenuItem("Edit");
            MenuItem removeItem = new MenuItem("Delete");

            editItem.setOnAction(event -> {
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edit");

                window.setScene(sceneManager.getScene("EditItemView"));
                editItemController.initialize(row.getItem());
                window.showAndWait();
            });

            removeItem.setOnAction(event -> {
                itemModel.getItemObservable().remove(row.getItem());
                itemTable.getSelectionModel().clearSelection();
            });

            rowMenu.getItems().add(editItem);
            rowMenu.getItems().add(removeItem);

            row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty()))
                    .then(rowMenu)
                    .otherwise((ContextMenu)null));
            return row;
        });

        JFXTreeTableColumn<Item, LocalDate> itemDate = new JFXTreeTableColumn<>("DATE");
        itemDate.setPrefWidth(120);
        itemDate.setStyle("-fx-alignment: center;");

        itemDate.setCellValueFactory(param -> param.getValue().getValue().getDate());

        itemDate.setCellFactory(dateCellFactory);
        itemDate.setOnEditCommit(cellEditEvent ->
            cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setDate(cellEditEvent.getNewValue()));


        JFXTreeTableColumn<Item, String> itemName = new JFXTreeTableColumn<>("NAME");
        itemName.setStyle("-fx-alignment: center;");
        itemName.setPrefWidth(200);
        itemName.setResizable(false);
        itemName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName().getValue()));

        itemName.setCellFactory(nameCellFactory);
        itemName.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setName(cellEditEvent.getNewValue()));


        JFXTreeTableColumn<Item, String> itemPrice = new JFXTreeTableColumn<>("PRICE");
        itemPrice.setStyle("-fx-alignment: center;");
        itemPrice.setPrefWidth(100);
        itemPrice.setCellValueFactory(param -> new SimpleStringProperty(Double.toString(param.getValue().getValue().getPrice().getValue())));

        itemPrice.setCellFactory(priceCellFactory);
        itemPrice.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setPrice(Double.parseDouble(cellEditEvent.getNewValue())));

        JFXTreeTableColumn<Item, String> itemSerialNumber = new JFXTreeTableColumn<>("SERIAL NUMBER");
        itemSerialNumber.setStyle("-fx-alignment: center;");
        itemSerialNumber.setPrefWidth(200);
        //
        itemSerialNumber.setCellValueFactory(param -> param.getValue().getValue().getSerialNumber());

        itemSerialNumber.setCellFactory(serialNumberCellFactory);
        itemSerialNumber.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setSerialNumber(cellEditEvent.getNewValue()));


        TreeItem<Item> test = new RecursiveTreeItem<>(itemModel.getItemObservable(), RecursiveTreeObject::getChildren);
        itemTable.getColumns().setAll(itemDate, itemName, itemSerialNumber, itemPrice);
        itemTable.setRoot(test);
        itemTable.setShowRoot(false);
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
