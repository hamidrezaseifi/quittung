package de.seifi.rechnung_manager_app.ui;

import java.io.IOException;
import java.util.List;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.controllers.PrintQuittungDialogController;
import de.seifi.rechnung_manager_app.controllers.PrintRechnungDialogController;
import de.seifi.rechnung_manager_app.controllers.RechnungController;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import static de.seifi.rechnung_manager_app.RechnungManagerFxApp.getMainStyle;

public class UiUtils {

    public static void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }

	public static void printRechnungItems(List<RechnungModel> rechnungModelList,
										  Window window) {
		try {
			PrinterJob job = PrinterJob.createPrinterJob();
			if (job != null && job.showPrintDialog(window)){
				boolean goforward = job.showPageSetupDialog(window);

				FXMLLoader rechnungPrintLoader = RechnungManagerFxApp.getRechnungPrintFxmlLoader();
				PrintRechnungDialogController rechnungPringController = new PrintRechnungDialogController();
				rechnungPrintLoader.setController(rechnungPringController);
				GridPane rechnungPrintPane = rechnungPrintLoader.load();

				FXMLLoader quittungPrintLoader = RechnungManagerFxApp.getQuittungPrintFxmlLoader();
				PrintQuittungDialogController quittungPringController = new PrintQuittungDialogController();
				quittungPrintLoader.setController(quittungPringController);
				GridPane quittungPrintPane = quittungPrintLoader.load();

				for(RechnungModel model: rechnungModelList){
					if(model.getRechnungType() == RechnungType.RECHNUNG){
						rechnungPringController.printRechnungList(model, job);
					}
					if(model.getRechnungType() == RechnungType.QUITTUNG){
						quittungPringController.printRechnungList(model, job);
					}
				}
			}


		} catch (IOException e) {
			e.printStackTrace();
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
