package de.seifi.rechnung_manager_app.ui;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_app.controllers.PrintRechnungDialogController;
import de.seifi.rechnung_manager_app.controllers.RechnungController;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
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

public class UiUtils {

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

	public static void printRechnungItems(List<RechnungModel> rechnungModelList,
										  Window window) {
		
		CustomerAdapter customerAdapter = new CustomerAdapter(); 
		
		try {
			
			PrintRechnungDialogController rechnungPringController = getPrintController(RechnungType.RECHNUNG);
			
			PrintRechnungDialogController quittungPringController = getPrintController(RechnungType.QUITTUNG);
			
			for(RechnungModel model: rechnungModelList){
				PrinterJob job = PrinterJob.createPrinterJob();
				if (job != null){
					// && job.showPrintDialog(window)
					//boolean goforward = job.showPageSetupDialog(window);

					if(model.getRechnungType() == RechnungType.RECHNUNG){

						Optional<CustomerModel> customerEntityOptional = RechnungManagerSpringApp.getCustomerService().getById(model.getCustomerId());
						if(customerEntityOptional.isEmpty()){
							throw new RuntimeException("Der Kunde von der Rechnung nicht gefunden!");
						}
						rechnungPringController.printRechnungList(model, customerEntityOptional.get(), job);
					}
					if(model.getRechnungType() == RechnungType.QUITTUNG){

						quittungPringController.printRechnungList(model, null, job);
					}

					job.endJob();
				}
			}



		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static PrintRechnungDialogController getPrintController(RechnungType rechnungType) throws IOException {
		FXMLLoader rechnungPrintLoader = RechnungManagerFxApp.getQuittungPrintFxmlLoader();
		PrintRechnungDialogController rechnungPringController = new PrintRechnungDialogController(rechnungType);
		rechnungPrintLoader.setController(rechnungPringController);
		GridPane rechnungPrintPane = rechnungPrintLoader.load();
		rechnungPrintPane.getStylesheets().add(RechnungManagerFxApp.getPrintStyle());

		return rechnungPringController;
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
			scene.getStylesheets().add(RechnungManagerFxApp.getMainStyle());

	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setScene(scene);
	        stage.setTitle(String.format("Rechnung Nummer %s Ansehen", rechnungModel.getNummer()));
	        stage.showAndWait();

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
}
