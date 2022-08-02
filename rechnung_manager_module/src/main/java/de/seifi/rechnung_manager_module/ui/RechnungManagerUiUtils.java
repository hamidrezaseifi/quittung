package de.seifi.rechnung_manager_module.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import de.seifi.rechnung_common.utils.UiUtils;
import de.seifi.rechnung_manager_module.ModuleRootClass;
import de.seifi.rechnung_manager_module.adapter.CustomerAdapter;
import de.seifi.rechnung_manager_module.controllers.PrintRechnungDialogController;
import de.seifi.rechnung_manager_module.controllers.RechnungController;
import de.seifi.rechnung_manager_module.enums.RechnungType;
import de.seifi.rechnung_manager_module.models.CustomerModel;
import de.seifi.rechnung_manager_module.models.RechnungModel;
import de.seifi.rechnung_manager_module.services.RechnungManagerServiceProvider;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RechnungManagerUiUtils {


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

						Optional<CustomerModel> customerEntityOptional = RechnungManagerServiceProvider
								.getCustomerService().getById(model.getCustomerId());
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
		//FXMLLoader rechnungPrintLoader = getFXMLLoader("rechnung_print");
		FXMLLoader rechnungPrintLoader = ModuleRootClass.loadFxmlLoader("rechnung_print");
		PrintRechnungDialogController rechnungPringController = new PrintRechnungDialogController(rechnungType);
		rechnungPrintLoader.setController(rechnungPringController);
		GridPane rechnungPrintPane = rechnungPrintLoader.load();
		rechnungPrintPane.getStylesheets().add(ModuleRootClass.loadStyleResource("print_styles"));

		return rechnungPringController;
	}

    public static void showRechnungDialog(RechnungModel rechnungModel) {
    	try {
			Stage stage = new Stage();
			//FXMLLoader fxmlLoader = getFXMLLoader("rechnung_base");
			FXMLLoader fxmlLoader = ModuleRootClass.loadFxmlLoader("rechnung_base");
			RechnungController controller = new RechnungController(rechnungModel.getRechnungType());
			fxmlLoader.setController(controller);

			GridPane rechnungPane = fxmlLoader.load();
			controller.loadModel(rechnungModel, false, stage);
	        rechnungPane.setPadding(new Insets(10));
	        
	        Scene scene = new Scene(rechnungPane, 1000, 800);
			scene.getStylesheets().add(ModuleRootClass.loadStyleResource("styles"));

	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setScene(scene);
	        stage.setTitle(String.format("Rechnung Nummer %s Ansehen", rechnungModel.getNummer()));
	        stage.showAndWait();

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
