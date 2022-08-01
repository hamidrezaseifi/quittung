package de.seifi.container_manager_app.ui;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CommonUiUtils {

    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showAsking(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        return alert.showAndWait();
    }

}
