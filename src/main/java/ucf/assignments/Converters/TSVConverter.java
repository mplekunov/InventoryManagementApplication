package ucf.assignments.Converters;

import ucf.assignments.File.FileManager;
import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatFlagsException;
import java.util.List;
import java.util.stream.Collectors;

public class TSVConverter {
    private final FileManager fileManager;
    private final String header = "Date\tName\tSerial Number\tPrice\n";

    public TSVConverter(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void toTSV(List<Item> table) {
        StringBuilder items = new StringBuilder(header);
        table.forEach(item -> {
            String formattedItem = String.format("%s\t%s\t%s\t%s\n",
                    DateConverter.toString(item.getDate().getValue()),
                    item.getName().getValue(),
                    item.getSerialNumber().getValue(),
                    item.getPrice().getValue().toString());

            items.append(formattedItem);
        });

        fileManager.writeAll(items.toString());
    }

    public List<Item> fromTSV() {
        List<String> tsvFile = fileManager.readAll();
        if (tsvFile.get(0).contains(header)) {
            return tsvFile.stream().map(line -> {
                List<String> itemProperties = Arrays.stream(line.split("\t")).collect(Collectors.toList());

                Item item = new Item();
                item.setDate(DateConverter.toDate(itemProperties.get(0)));
                item.setName(itemProperties.get(1));
                item.setSerialNumber(itemProperties.get(2));
                item.setPrice(Double.parseDouble(itemProperties.get(3)));

                return item;
            }).collect(Collectors.toList());
        } else
            throw new IllegalFormatFlagsException("Incorrect TSV File Format");
    }

}
