/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Converters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucf.assignments.File.FileManager;
import ucf.assignments.Models.Item;

import java.beans.beancontext.BeanContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TSVConverterTest {
    private final String header = "Date\tName\tSerial Number\tPrice";
    private final List<Item> items = List.of(
            new Item("Prince", "1", 1d, null),
            new Item("Did", "2", 2d, null),
            new Item("Nothing", "3", 3d, null),
            new Item("Wrong", "4", 4d, null));
    private final String itemsTSV = "Date\tName\tSerial Number\tPrice\tPrince\t1\t1.0\tDid\t2\t2.0\tNothing\t3\t3.0\tWrong\t4\t4.0";

    private File file;

    @BeforeEach
    void setUp() {
        file = new File("Test.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        file.delete();
    }
    
    @Test
    void TestToTSV() {
        FileManager fileManager = new FileManager(file);

        TSVConverter tsvConverter = new TSVConverter(fileManager);
        tsvConverter.toTSV(items);
        
        StringBuilder actual = new StringBuilder();
        try {
            Files.readAllLines(file.toPath()).forEach(actual::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertEquals(itemsTSV, actual.toString());
    }

    @Test
    void TestFromTSV() {
        FileManager fileManager = new FileManager(file);

        TSVConverter tsvConverter = new TSVConverter(fileManager);

//        Postponed until better times
//        when(fileManager.readAll()).thenReturn();
//        tsvConverter.fromTSV();
    }
}