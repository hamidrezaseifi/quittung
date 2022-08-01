package de.seifi.container_manager_app.controllers;

import de.seifi.container_manager_app.ContainerManagerFxApp;
import de.seifi.container_manager_app.models.PrinterItem;
import de.seifi.rechnung_common.controllers.ControllerBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;


public class AdminController implements Initializable, ControllerBase {

	@FXML private ComboBox<PrinterItem> cmbPrinter;
	@FXML private GridPane rootPane;

	private ObservableList<PrinterItem> printerItemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	ContainerManagerFxApp.CURRENT_INSTANCE.setCurrentController(this);

		cmbPrinter.setItems(printerItemList);
		PrinterItem defaultPrinterItem = null;
		for(Printer p: Printer.getAllPrinters()){
			PrinterItem printerItem = new PrinterItem(p);
			printerItemList.add(printerItem);
			if(p == Printer.getDefaultPrinter()){
				defaultPrinterItem = printerItem;
			}
		}

		//cmbPrinter.prefWidthProperty().bind(rootPane.widthProperty().subtract(130));
		cmbPrinter.getSelectionModel().select(defaultPrinterItem);
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