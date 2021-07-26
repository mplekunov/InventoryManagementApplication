/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.ControllerStyle;

import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public final class JFXTextFieldStyle {
    private JFXTextFieldStyle() {}

    public static void setStyleOnError(JFXTextField textField, Side side, String errorMsg) {
        Node focusLine = textField.lookup(".input-focused-line");
        Node inputLine = textField.lookup(".input-line");

        focusLine.setStyle("-fx-background-color: red");
        inputLine.setStyle("-fx-background-color: red");

        ContextMenu errorPopup = new ContextMenu();
        MenuItem errorItem = new MenuItem(errorMsg);

        double offsetX = 0;
        double offsetY = 0;
        StringBuilder style = new StringBuilder("-fx-background-color: rgb(235, 233, 237); -fx-border-color: rgb(205, 203, 207);");

        if (side == Side.TOP) {
            style.append("-fx-border-radius: 8 8 8 0; -fx-background-radius: 8 8 8 0;");
            offsetX = -textField.getWidth();
            offsetY = -(textField.getHeight() + 10);
        }
        else if (side == Side.RIGHT) {
            style.append("-fx-border-radius: 8 8 8 8; -fx-background-radius: 8 8 8 8;");
            offsetX = 5;
            offsetY = 0;
        }

        errorPopup.setStyle(style.toString());
        errorItem.setStyle("-fx-text-fill: rgb(141, 114, 151); -fx-background-color: transparent;");
        errorPopup.getItems().add(errorItem);
        errorPopup.show(textField, Side.RIGHT, offsetX, offsetY);

        textField.textProperty().addListener(e -> {
            errorPopup.hide();
            focusLine.setStyle("");
            inputLine.setStyle("");
        });
    }
}
