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
        SceneManager sceneManager = new SceneManager();
        sceneManager.load();

        mainWindow.setTitle("IMA");
        mainWindow.setScene(sceneManager.getScene("MainView"));
        mainWindow.show();
    }
}
