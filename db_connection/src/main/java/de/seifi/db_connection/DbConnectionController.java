package de.seifi.db_connection;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class DbConnectionController implements Initializable {
	
	@FXML private TextField txtServer;
	@FXML private TextField txtPort;
	@FXML private TextField txtDatabase;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;


	@FXML
	private void textConnectection() {
		
	}

	@FXML
	private void closeForm() {
		DbConnectionFxApp.closeApp();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	

}
