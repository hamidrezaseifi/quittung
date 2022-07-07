package de.seifi.rechnung_manager.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class MainController implements Initializable {
	
	@FXML private GridPane childBox;

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
    private void showQuittung() throws IOException {
    	
    	if(RechnungManagerFxApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(RechnungManagerFxApp.getQuittungPane());
    }
	
    @FXML
    public void showHome() throws IOException {
    	
    	if(RechnungManagerFxApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(RechnungManagerFxApp.getHomePane());
    }
	
    @FXML
    private void showReport() throws IOException {
    	
    	if(RechnungManagerFxApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(RechnungManagerFxApp.getReportPane());
    }
	
    @FXML
    private void showAdmin() throws IOException {
    	
    	if(RechnungManagerFxApp.isCurrentControllerDirty()) {
    		return;
    	}
	
    	clearChildren();
    	
    	childBox.getChildren().add(RechnungManagerFxApp.getAdminPane());
    }
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

    
}