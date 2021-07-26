/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import ucf.assignments.Controllers.AddItemController;
import ucf.assignments.Controllers.MainController;
import ucf.assignments.Models.ItemModel;

import java.io.IOException;
import java.util.HashMap;

public class SceneManager {
    private final HashMap<String, Scene> scenes = new HashMap<>();

    public void load() {
        ItemModel itemModel = new ItemModel();

        AddItemController addItemController = new AddItemController(itemModel);

        MainController mainController = new MainController(itemModel,this);

        Parent root;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/MainView.fxml"));
        fxmlLoader.setControllerFactory(MainController -> mainController);

        try {
            root = fxmlLoader.load();
            scenes.put("MainView", new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        fxmlLoader = new FXMLLoader(getClass().getResource("View/AddItemView.fxml"));
        fxmlLoader.setControllerFactory(AddItemController -> addItemController);

        try {
            root = fxmlLoader.load();
            scenes.put("AddItemView", new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene(String name) {
        return scenes.get(name);
    }
}
