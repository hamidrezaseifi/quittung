package de.seifi.db_connection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.rechnung_common.config.DatabaseConfigUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DbConnectionController implements Initializable {
	
	@FXML private TextField txtServer;
	@FXML private TextField txtPort;
	@FXML private TextField txtDatabase;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private ComboBox<String> cmnDriver; 


	@FXML
	private void textConnectection() {
    	
    	DatabaseConfigUtils dbConfig = new DatabaseConfigUtils();
    	try {
			dbConfig.testConnection(cmnDriver.getSelectionModel().getSelectedItem(), txtServer.getText(), txtPort.getText(), txtDatabase.getText(), txtUsername.getText(), txtPassword.getText());
		} catch (SQLException e) {
			
			showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
			return;
		}
    	catch (RuntimeException e) {
			
    		showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
    		return;
		}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Test-Verbindung");
        alert.setHeaderText("Datenbank-Verbindung Test war erfolgreich. Soll die einstellung gespeichert?");
        alert.setContentText(null);
        Optional<ButtonType> res =  alert.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {
        	try {
				dbConfig.saveConnection(dbConfig.getConfigIniFile(), txtServer.getText(), txtPort.getText(), txtDatabase.getText(), txtUsername.getText(), txtPassword.getText());
			} catch (IOException e) {
				
	    		showError("Einstellung Speichern ...", "Fehler bei Speichern von den Einstellungen!:\n" + e.getLocalizedMessage());
	    		return;
				
			}
        }
        

	}

	@FXML
	private void closeForm() {
		DbConnectionFxApp.closeApp();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cmnDriver.getItems().add("org.postgresql.Driver");
		cmnDriver.getSelectionModel().select(0);
	}
	
	private void showError(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();

    }
}
