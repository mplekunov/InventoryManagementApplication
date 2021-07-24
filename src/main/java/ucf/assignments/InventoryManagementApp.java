package ucf.assignments;

import javafx.application.Application;
import javafx.stage.Stage;

public class InventoryManagementApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainWindow) {
        SceneManager sceneManager = new SceneManager();
        sceneManager.load();

        mainWindow.setTitle("IMA");
        mainWindow.setScene(sceneManager.getScene("MainView"));
        mainWindow.show();
    }
}
