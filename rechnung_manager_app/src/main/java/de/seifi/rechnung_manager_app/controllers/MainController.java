package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
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
        	
        	childBox.getChildren().add(RechnungManagerFxApp.getQuittungPane());
    	}

	}

	@FXML
	private void showRechnung() throws IOException {

		logger.debug("Show Rechnung");

		if(RechnungManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(RechnungManagerFxApp.getRechnungPane());
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
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

    
}