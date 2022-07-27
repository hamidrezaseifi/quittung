package de.seifi.db_connection.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.db_connection.DbConnectionFxApp;
import de.seifi.rechnung_common.config.DatabaseConfigUtils;
import de.seifi.rechnung_common.models.TableModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DbConnectionController implements Initializable {
	
	private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
	@FXML private TextField txtServer;
	@FXML private TextField txtPort;
	@FXML private TextField txtDatabase;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private ComboBox<String> cmnDriver; 
	
	@FXML private VBox rechnungDataBox;
	@FXML private VBox tablesBox;

	private final DatabaseConfigUtils databaseConfig = new DatabaseConfigUtils(POSTGRESQL_DRIVER);
	
	
	@FXML
	private void textConnectection() {
    	
    	try {
    		databaseConfig.testConnection(cmnDriver.getSelectionModel().getSelectedItem(), txtServer.getText(), txtPort.getText(), txtDatabase.getText(), txtUsername.getText(), txtPassword.getText());
		} catch (SQLException e) {
			
			DbConnectionFxApp.showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
			return;
		}
    	catch (RuntimeException e) {
			
    		DbConnectionFxApp.showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
    		return;
		}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Test-Verbindung");
        alert.setHeaderText("Datenbank-Verbindung Test war erfolgreich. Soll die einstellung gespeichert?");
        alert.setContentText(null);
        Optional<ButtonType> res =  alert.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {
        	try {
        		databaseConfig.saveConnection(databaseConfig.getConfigIniFile(), txtServer.getText(), txtPort.getText(), txtDatabase.getText(), txtUsername.getText(), txtPassword.getText());
			} catch (IOException e) {
				
				DbConnectionFxApp.showError("Einstellung Speichern ...", "Fehler bei Speichern von den Einstellungen!:\n" + e.getLocalizedMessage());
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
		
		cmnDriver.getItems().add(POSTGRESQL_DRIVER);
		cmnDriver.getSelectionModel().select(0);
		
		List<TableModel> tableList = databaseConfig.getTableModelList();
		for(TableModel tableModel: tableList) {
			try {
				
				FXMLLoader loader = DbConnectionFxApp.loadFXMLLoader("table_data");
				loader.setController(new TableDataController(tableModel, databaseConfig));
				HBox tableBox = loader.load();
				tablesBox.getChildren().add(tableBox);
				
				
			} catch (IOException e) {
				DbConnectionFxApp.showError("Erstellen von Tabellen-Liste ...", "Fehler: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}
	
	
}
