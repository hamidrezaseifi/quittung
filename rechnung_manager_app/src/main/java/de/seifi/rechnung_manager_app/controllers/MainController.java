package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.util.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainController implements Initializable {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@FXML private GridPane childBox;
	
	private static GridPane reportPane = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		RechnungManagerFxApp.setMainController(this);
		try {
			showHome();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@FXML
	private void showQuitting() throws IOException {
		logger.debug("Show Quitting");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
    		Pair<GridPane, RechnungController> pair = RechnungManagerFxApp.getRechnungPane(RechnungType.QUITTUNG);
        	childBox.getChildren().add(pair.getKey());
    	}

	}

	@FXML
	private void showRechnung() throws IOException {

		logger.debug("Show Rechnung");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
    		Pair<GridPane, RechnungController> pair = RechnungManagerFxApp.getRechnungPane(RechnungType.RECHNUNG);
        	childBox.getChildren().add(pair.getKey());
    	}

	}

	@FXML
	private void showKostenvoranschlag() throws IOException {
		logger.debug("Show Quitting");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			Pair<GridPane, KostenvoranschlagController> pair = RechnungManagerFxApp.getKostenvoranschlag();
			childBox.getChildren().add(pair.getKey());
		}

	}

    @FXML
    public void showHome() throws IOException {
    	
		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(RechnungManagerFxApp.getHomePane());
    	}
    	
    }

	@FXML
	private void showReport() throws IOException {

		logger.debug("Show Report");

		if(reportPane == null) {
			reportPane = RechnungManagerFxApp.getReportPane();
		}
		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(reportPane);
    	}

	}

	@FXML
	private void showCustomers() throws IOException {

		logger.debug("Show Customers");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(RechnungManagerFxApp.getCustomersPane());
    	}

	}

	@FXML
	private void showAdmin() throws IOException {

		logger.debug("Show Admin");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			childBox.getChildren().add(RechnungManagerFxApp.getAdminPane());
		}

	}

	@FXML
	private void showAbout() throws IOException {

		logger.debug("Show About");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			childBox.getChildren().add(RechnungManagerFxApp.getAboutPane());
		}

	}
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

	public void startEditRechnung(RechnungModel rechnungModel, CustomerModel customerModel) throws IOException {
		logger.debug(String.format("Start editing Rechnung Type '%s' id: '%s'", rechnungModel.getRechnungType(), rechnungModel.getId()));

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
    		Pair<GridPane, RechnungController> pair = RechnungManagerFxApp.getRechnungPane(rechnungModel.getRechnungType());
    		RechnungController controller = pair.getValue();
    		controller.startEdit(rechnungModel, customerModel);
        	childBox.getChildren().add(pair.getKey());
    	}
		
	}

    
}