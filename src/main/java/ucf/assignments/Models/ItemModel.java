package ucf.assignments.Models;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import ucf.assignments.Database.DataState;
import ucf.assignments.Database.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemModel {
    private final HashMap<Item, DataState> itemCollection = new HashMap<>();

    private final ObservableList<Item> itemObservable;
    private final Database database = new Database();

    public ItemModel() {
        itemObservable = FXCollections.observableList(
                new ArrayList<>(),
                (Item item) -> new Observable[] {
                        item.getName(),
                        item.getSerialNumber(),
                        item.getPrice(),
                        item.getDate()
                });

        initCollections();

        itemObservable.addListener(this::addListChangeListener);
    }

    public ObservableList<Item> getItemObservable() {
        return itemObservable;
    }

    private void addListChangeListener(ListChangeListener.Change<? extends Item> listener) {
        while (listener.next()) {
            if (listener.wasAdded()) {
                for (int i = listener.getFrom(); i < listener.getTo(); i++) {
                    SimpleStringProperty serialNumber = itemObservable.get(i).getSerialNumber();
                    boolean isFound = itemCollection.keySet().stream().anyMatch(obj -> obj.getSerialNumber().equals(serialNumber));

                    if (!isFound)
                        itemCollection.put(itemObservable.get(i), DataState.Added);
                    else throw new NullPointerException();
                }
            } else if (listener.wasUpdated()) {
                for (int i = listener.getFrom(); i < listener.getTo(); i++)
                    itemCollection.put(itemObservable.get(i), DataState.Updated);
            } else {
                for (Item removed : listener.getRemoved())
                    itemCollection.put(removed, DataState.Removed);
            }
        }
    }

    private void initCollections() {
        database.getItems().forEach(item -> {
            itemCollection.put(item, DataState.Cached);
            itemObservable.add(item);
        });
    }

    public void upload() {
        itemCollection.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Cached))
                .forEach(entry -> {
                    if (entry.getValue().equals(DataState.Added))
                        database.insertItems(entry.getKey());
                    else if (entry.getValue().equals(DataState.Updated))
                        database.updateItems(entry.getKey());
                    else
                        database.deleteItem(entry.getKey());
                });

        itemCollection.values().removeIf(dataState -> dataState.equals(DataState.Removed));
        itemCollection.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.Cached))
                .forEach(entry -> entry.setValue(DataState.Cached));
    }
}
