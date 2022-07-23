package de.seifi.rechnung_manager_app.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.controllers.RechnungController;
import de.seifi.rechnung_manager_app.controllers.RechnungPrintDialogController;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import static de.seifi.rechnung_manager_app.RechnungManagerFxApp.getMainStyle;

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
			RechnungPrintDialogController dialogController = fxmlLoader.<RechnungPrintDialogController>getController();
			dialogController.printRechnungList(rechnungModelList);
		} catch (IOException e) {

		}
	}

    public static void showRechnungDialog(RechnungModel rechnungModel) {
    	try {
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = RechnungManagerFxApp.getRechnungFxmlLoader();
			RechnungController controller = new RechnungController(rechnungModel.getRechnungType());
			fxmlLoader.setController(controller);

			GridPane rechnungPane = fxmlLoader.load();
			controller.loadModel(rechnungModel, false, stage);
	        rechnungPane.setPadding(new Insets(10));
	        
	        Scene scene = new Scene(rechnungPane, 1000, 800);
			scene.getStylesheets().add(getMainStyle());

	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setScene(scene);
	        stage.setTitle(String.format("Rechnung Nummer %s Ansehen", rechnungModel.getNummer()));
	        stage.showAndWait();

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
}
