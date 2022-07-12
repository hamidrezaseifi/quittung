package de.seifi.rechnung_manager.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.controllers.PrintDialogController;
import de.seifi.rechnung_manager.models.RechnungModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class UiUtils {

    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }
    
    public static void printRechnungItems(List<RechnungModel> rechnungModelList) {
    	try {
			Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("rechnung_print");
			GridPane printPane = (GridPane)pair.getKey();
	        FXMLLoader fxmlLoader = pair.getValue();
	        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
	        dialogController.printRechnungList(rechnungModelList);
		} catch (IOException e) {
			
		}
    }
}
