package de.seifi.container_manager_app.controllers;

import de.seifi.container_manager_app.ContainerManagerFxApp;
import de.seifi.rechnung_common.controllers.ControllerBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;



public class HomeController implements Initializable, ControllerBase {

    @FXML private GridPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	ContainerManagerFxApp.CURRENT_INSTANCE.setCurrentController(this);

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