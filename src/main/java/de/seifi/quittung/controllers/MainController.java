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
		
		clearChildren();
    	
    	try {
    		GridPane pange = QuittungApp.getHomePane();
    		
    		URL url = QuittungApp.loadResource("photos/auto_teil_photo_1.jpg");
    		
    		ImageView imageView = new ImageView(url.toString());
    		imageView.setFitHeight(753);
    		imageView.setFitWidth(1000);
    		pange.getChildren().add(imageView);
    		
			childBox.getChildren().add(pange);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
    @FXML
    private void showQuittung() throws IOException {
    	
    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getQuittungPane());
    }
	
    @FXML
    private void showHome() throws IOException {
    	
    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getHomePane());
    }
	
    @FXML
    private void showReport() throws IOException {
    	
    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getReportPane());
    }
	
    @FXML
    private void showAdmin() throws IOException {
    	
    	clearChildren();
    	
    	childBox.getChildren().add(QuittungApp.getAdminPane());
    }
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

    
}