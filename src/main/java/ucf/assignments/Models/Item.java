package ucf.assignments.Models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.Objects;

public class Item extends RecursiveTreeObject<Item> {
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty serialNumber;
    private final SimpleStringProperty name;

    private int id;

    public Item() {
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        serialNumber = new SimpleStringProperty();
        date = new SimpleObjectProperty<>();
    }

    public Item(String name, String serialNumber, Double price, LocalDate date) {
        this.name = new SimpleStringProperty(name);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.price = new SimpleDoubleProperty(price);
        this.date = new SimpleObjectProperty<>(date);
    }

    public SimpleDoubleProperty getPrice() {
        return price;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public SimpleObjectProperty<LocalDate> getDate() {
        return date;
    }

    public SimpleStringProperty getSerialNumber() {
        return serialNumber;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(Double price) {
        this.price.set(price);
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber.setValue(serialNumber);
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return serialNumber.equals(item.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }
}
