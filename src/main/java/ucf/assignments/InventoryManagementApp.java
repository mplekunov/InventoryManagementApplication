package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class InventoryManagementApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainWindow) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainView.fxml")));

            Scene mainScene = new Scene(root);

            mainWindow.setTitle("To Do List");
            mainWindow.setScene(mainScene);
            mainWindow.show();
            mainWindow.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
