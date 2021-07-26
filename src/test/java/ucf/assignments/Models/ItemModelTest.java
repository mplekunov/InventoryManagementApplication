/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Models;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ucf.assignments.Database.DataState;
import ucf.assignments.Database.Database;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemModelTest {
    private final List<Item> items = List.of(
            new Item("Prince", "1", 1d, null),
            new Item("Did", "2", 2d, null),
            new Item("Nothing", "3", 3d, null),
            new Item("Wrong", "4", 4d, null));
    private ItemModel itemModel = new ItemModel();

    @AfterEach
    void tearDown() {
        itemModel.resetBuffer();
    }

    @Test
    void TestResetBuffer() {
        itemModel.resetBuffer();

        itemModel.getAllItems().addAll(items);
        assertEquals(4, itemModel.getAllItems().size());
        itemModel.resetBuffer();
        assertEquals(0, itemModel.getAllItems().size());
    }

    @Test
    void TestAddItem() {
        itemModel.resetBuffer();

        itemModel.getAllItems().addAll(items);
        assertArrayEquals(items.toArray(), itemModel.getAllItems().toArray());
    }
}