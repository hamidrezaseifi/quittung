package de.seifi.rechnung_common.utils;

import de.seifi.rechnung_common.controllers.ControllerBase;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;


public class UiUtils {

    private static IMainAppProvider mainAppProvider;

    public static IMainAppProvider getMainAppProvider() {
        return UiUtils.mainAppProvider;
    }

    public static void setMainAppProvider(IMainAppProvider mainAppProvider) {
        UiUtils.mainAppProvider = mainAppProvider;
    }

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
