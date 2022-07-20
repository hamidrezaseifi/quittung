package de.seifi.rechnung_manager_app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;


public class HomeController implements Initializable, ControllerBase {

    @FXML private GridPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	RechnungManagerFxApp.setCurrentController(this);

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