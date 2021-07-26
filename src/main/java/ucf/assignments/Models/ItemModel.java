/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Models;

import javafx.beans.Observable;
import javafx.collections.*;
import ucf.assignments.Database.DataState;
import ucf.assignments.Database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        download();

        itemObservable.addListener(this::addListChangeListener);
    }

    public ObservableList<Item> getAllItems() {
        return itemObservable;
    }

    private void addListChangeListener(ListChangeListener.Change<? extends Item> listener) {
        while (listener.next()) {
            if (listener.wasAdded()) {
                for (int i = listener.getFrom(); i < listener.getTo(); i++) {
                    Item item = itemObservable.get(i);
                    boolean isFound = itemCollection.keySet().stream().anyMatch(obj -> obj.equals(item));

                    if (!isFound)
                        itemCollection.put(item, DataState.ADDED);
                    else {
                        Map.Entry<Item, DataState> duplicateEntry = itemCollection.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(item))
                                .findFirst().get();

                        DataState value = duplicateEntry.getValue();
                        Item key = duplicateEntry.getKey();

                        if (value == DataState.REMOVED_CACHED || value == DataState.REMOVED_UNCACHED) {
                            key.setDate(item.getDate().getValue());
                            key.setSerialNumber(item.getSerialNumber().getValue());
                            key.setName(item.getName().getValue());
                            key.setPrice(item.getPrice().getValue());
                            if (value == DataState.REMOVED_CACHED)
                                itemCollection.put(key, DataState.UPDATED);
                            else
                                itemCollection.put(key, DataState.ADDED);
                        } else
                            throw new NullPointerException();
                    }
                }
            } else if (listener.wasUpdated()) {
                for (int i = listener.getFrom(); i < listener.getTo(); i++)
                    if (itemCollection.get(itemObservable.get(i)) != DataState.ADDED)
                        itemCollection.put(itemObservable.get(i), DataState.UPDATED);
            } else {
                for (Item removed : listener.getRemoved())
                    if (itemCollection.get(removed) == DataState.CACHED)
                        itemCollection.put(removed, DataState.REMOVED_CACHED);
                    else
                        itemCollection.put(removed, DataState.REMOVED_UNCACHED);
            }
        }
    }

    private void download() {
        database.getItems().forEach(item -> itemCollection.put(item, DataState.CACHED));
        itemObservable.addAll(itemCollection.keySet());
    }

    public void resetBuffer() {
        itemObservable.clear();
        itemCollection.entrySet().forEach(entry -> entry.setValue(DataState.REMOVED_CACHED));
        upload();
    }

    public void upload() {
        itemCollection.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.CACHED))
                .forEach(entry -> {
                    if (entry.getValue().equals(DataState.ADDED))
                        database.insertItem(entry.getKey());
                    else if (entry.getValue().equals(DataState.UPDATED))
                        database.updateItem(entry.getKey());
                    else if (entry.getValue().equals(DataState.REMOVED_CACHED))
                        database.deleteItem(entry.getKey());
                });

        itemCollection.values().removeIf(dataState -> dataState.equals(DataState.REMOVED_CACHED) || dataState.equals(DataState.REMOVED_UNCACHED));
        itemCollection.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(DataState.CACHED))
                .forEach(entry -> entry.setValue(DataState.CACHED));
    }
}
