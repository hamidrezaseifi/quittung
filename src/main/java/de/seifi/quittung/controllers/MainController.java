package de.seifi.quittung.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.quittung.QuittungApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class MainController implements Initializable {
	
	@FXML private GridPane childBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		QuittungApp.setMainController(this);
		try {
			showHome();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
    @FXML
    private void showQuittung() throws IOException {
    	
    	if(QuittungApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getQuittungPane());
    }
	
    @FXML
    public void showHome() throws IOException {
    	
    	if(QuittungApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getHomePane());
    }
	
    @FXML
    private void showReport() throws IOException {
    	
    	if(QuittungApp.isCurrentControllerDirty()) {
    		return;
    	}

    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getReportPane());
    }
	
    @FXML
    private void showAdmin() throws IOException {
    	
    	if(QuittungApp.isCurrentControllerDirty()) {
    		return;
    	}
	
    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getAdminPane());
    }
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

    
}