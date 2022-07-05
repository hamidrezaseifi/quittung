package de.seifi.quittung.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.quittung.QuittungApp;

public class HomeController implements Initializable, ControllerBse {

    @FXML private GridPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	QuittungApp.setCurrentController(this);

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