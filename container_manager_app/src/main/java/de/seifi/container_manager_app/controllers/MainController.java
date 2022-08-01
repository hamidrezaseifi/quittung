package de.seifi.container_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.container_manager_app.ContainerManagerFxApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;


public class MainController implements Initializable {
	
	@FXML private GridPane childBox;

	@FXML private Button btnShowModule;


	private ContextMenu contextMenu = new ContextMenu();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ContainerManagerFxApp.setMainController(this);

		MenuItem menuItem1 = new MenuItem("menu item 1");
		MenuItem menuItem2 = new MenuItem("menu item 2");
		MenuItem menuItem3 = new MenuItem("menu item 3");

		// add menu items to menu
		contextMenu.getItems().add(menuItem1);
		contextMenu.getItems().add(menuItem2);
		contextMenu.getItems().add(menuItem3);

		try {
			showHome();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@FXML
	public void showHome() throws IOException {

		if(ContainerManagerFxApp.cannCurrentControllerClosed()) {
			clearChildren();

			childBox.getChildren().add(ContainerManagerFxApp.CURRENT_INSTANCE.getHomePane());
		}

	}

	@FXML
	public void showModule() throws IOException {

		contextMenu.show(btnShowModule, Side.RIGHT, -40, 0);

	}
	
    @FXML
    private void showAdmin() throws IOException {
    	
    	if(ContainerManagerFxApp.cannCurrentControllerClosed()) {
    		clearChildren();
        	
        	childBox.getChildren().add(ContainerManagerFxApp.CURRENT_INSTANCE.getAdminPane());
    	}
    	
    }
    
    private void clearChildren() {
    	while(childBox.getChildren().isEmpty() == false) {
    		childBox.getChildren().remove(0);	
    	}
    }

    
}