package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.PrinterItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminController implements Initializable, ControllerBase {

	@FXML private ComboBox<PrinterItem> cmbPrinter;
	@FXML private GridPane rootPane;

	private ObservableList<PrinterItem> printerItemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	RechnungManagerFxApp.setCurrentController(this);

		cmbPrinter.setItems(printerItemList);
		PrinterItem defaultPrinterItem = null;
		String selectedPrinter = RechnungManagerFxApp.appConfig.getSelectedPrinterName();
		if(selectedPrinter.isEmpty()){
			selectedPrinter = Printer.getDefaultPrinter().getName();
		}
		for(Printer p: Printer.getAllPrinters()){
			PrinterItem printerItem = new PrinterItem(p);
			printerItemList.add(printerItem);
			if(p.getName().equals(selectedPrinter)){
				defaultPrinterItem = printerItem;
				cmbPrinter.getSelectionModel().select(defaultPrinterItem);
			}
		}

		if(defaultPrinterItem != null){
			cmbPrinter.getSelectionModel().select(defaultPrinterItem);
		}

		cmbPrinter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			RechnungManagerFxApp.appConfig.setSelectedPrinterName(newVal.getPrinter().getName());
			try {
				RechnungManagerFxApp.appConfig.saveConfig();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

    }

	@Override
	public boolean isDirty() {
		
		return false;
	}

	@Override
	public String getDirtyMessage() {
		return "Was bei Home ist geändert!";
	}
}