package ucf.assignments;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;

import ucf.assignments.TreeTableFactories.DateFactory;
import ucf.assignments.TreeTableFactories.NameFactory;
import ucf.assignments.TreeTableFactories.PriceFactory;
import ucf.assignments.TreeTableFactories.SerialNumberFactory;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public void initialize() {
        itemTable.setEditable(true);

        Callback<TreeTableColumn<Item, LocalDate>, TreeTableCell<Item, LocalDate>> dateCellFactory
                = (TreeTableColumn<Item, LocalDate> param) -> new DateFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> nameCellFactory
                = (TreeTableColumn<Item, String> param) -> new NameFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> serialNumberCellFactory
                = (TreeTableColumn<Item, String> param) -> new SerialNumberFactory();

        Callback<TreeTableColumn<Item, String>, TreeTableCell<Item, String>> priceCellFactory
                = (TreeTableColumn<Item, String> param) -> new PriceFactory();

        JFXTreeTableColumn<Item, LocalDate> itemDate = new JFXTreeTableColumn<>("DATE");
        itemDate.setPrefWidth(120);
        itemDate.setStyle("-fx-alignment: center;");
//        var dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        itemDate.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue().getDate()));

        itemDate.setCellFactory(dateCellFactory);
        itemDate.setOnEditCommit(cellEditEvent ->
            cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setDate(cellEditEvent.getNewValue()));


        JFXTreeTableColumn<Item, String> itemName = new JFXTreeTableColumn<>("NAME");
        itemName.setStyle("-fx-alignment: center;");
        itemName.setPrefWidth(200);
        itemName.setResizable(false);
        itemName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));

        itemName.setCellFactory(nameCellFactory);
        itemName.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setName(cellEditEvent.getNewValue()));


        JFXTreeTableColumn<Item, String> itemPrice = new JFXTreeTableColumn<>("PRICE");
        itemPrice.setStyle("-fx-alignment: center;");
        itemPrice.setPrefWidth(100);
        itemPrice.setCellValueFactory(param -> new SimpleStringProperty(NumberFormat.getCurrencyInstance().format(param.getValue().getValue().getPrice())));

        itemPrice.setCellFactory(priceCellFactory);
        itemPrice.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setPrice(Double.parseDouble(cellEditEvent.getNewValue())));

        JFXTreeTableColumn<Item, String> itemSerialNumber = new JFXTreeTableColumn<>("SERIAL NUMBER");
        itemSerialNumber.setStyle("-fx-alignment: center;");
        itemSerialNumber.setPrefWidth(200);
        itemSerialNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getSerialNumber()));

        itemSerialNumber.setCellFactory(serialNumberCellFactory);
        itemSerialNumber.setOnEditCommit(cellEditEvent ->
                cellEditEvent.getTreeTableView().getTreeItem(cellEditEvent.getTreeTablePosition().getRow()).getValue().setSerialNumber(cellEditEvent.getNewValue()));


        TreeItem<Item> test = new RecursiveTreeItem<>(getItems(), RecursiveTreeObject::getChildren);
        itemTable.getColumns().setAll(itemDate, itemName, itemSerialNumber, itemPrice);
        itemTable.setRoot(test);
        itemTable.setShowRoot(false);
    }


    //model part

    public ObservableList<Item> getItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        items.add(new Item("Prince", "ASX33123", 210d, LocalDate.now().plusDays(1)));
        items.add(new Item("Henry", "BSX33123", 110d, LocalDate.now().plusDays(2)));
        items.add(new Item("Did", "BSX33123", 110d, LocalDate.now().plusDays(3)));
        items.add(new Item("Nothing", "BSX33123", 110d, LocalDate.now().plusDays(4)));
        items.add(new Item("Wrong", "BSX33123", 110d, LocalDate.now().plusDays(5)));

        return items;
    }
}
