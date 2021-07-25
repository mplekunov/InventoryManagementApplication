package ucf.assignments.File;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File file;

    public FileManager(File file) {
        this.file = file;
    }

    public void writeAll(String text) {
        try {
            Files.writeString(Path.of(file.getPath()), text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readAll() {
        List<String> list = null;
        try {
            list = Files.readAllLines(Path.of(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

//    public void writeLine(String formattedItem) {
//        try {
//            Files.writeString(Path.of(file.getPath()), formattedItem);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
