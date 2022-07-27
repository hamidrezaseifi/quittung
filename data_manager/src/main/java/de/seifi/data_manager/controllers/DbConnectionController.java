package de.seifi.data_manager.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.data_manager.DataManagerFxApp;
import de.seifi.rechnung_common.config.DatabaseConfigUtils;
import de.seifi.rechnung_common.models.TableModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class DbConnectionController implements Initializable {
	
	private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
	@FXML public Button btnStartBackup;
	@FXML public Button btnCancelBackup;
	@FXML public VBox processBackupBox;
	@FXML public VBox backupInfoBox;
	@FXML private TextField txtServer;
	@FXML private TextField txtPort;
	@FXML private TextField txtDatabase;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private ComboBox<String> cmnDriver;
	@FXML private Label lblPgDumpPath;
	@FXML private Label lblBackupPath;

	@FXML private VBox rechnungDataBox;
	@FXML private VBox tablesBox;

	private final DatabaseConfigUtils databaseConfig = new DatabaseConfigUtils(POSTGRESQL_DRIVER);
	
	
	@FXML
	private void textConnectection() {
    	
    	try {
    		databaseConfig.testConnection(cmnDriver.getSelectionModel().getSelectedItem(), txtServer.getText(), txtPort.getText(), txtDatabase.getText(), txtUsername.getText(), txtPassword.getText());
		} catch (SQLException e) {
			
			DataManagerFxApp.showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
			return;
		}
    	catch (RuntimeException e) {
			
    		DataManagerFxApp.showError("Test-Verbindung ...", "Ungültige Datenbank-Verbindung Einstellung!:\n" + e.getLocalizedMessage());
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
				
				DataManagerFxApp.showError("Einstellung Speichern ...", "Fehler bei Speichern von den Einstellungen!:\n" + e.getLocalizedMessage());
	    		return;
				
			}
        }
        

	}

	@FXML
	private void closeForm() {
		DataManagerFxApp.closeApp();
	}

	public void selectPgDump(ActionEvent actionEvent) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("PG_DUP Datei auswählen ...");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PG_DUMP Datei ...", "*pg_dump.exe"));
		File selectedFile = fileChooser.showOpenDialog(DataManagerFxApp.mainStage);
		if(selectedFile != null && selectedFile.exists()){
			lblPgDumpPath.setText(selectedFile.getAbsolutePath());
			DataManagerFxApp.appConfig.setPostgresDumpAppPath(lblPgDumpPath.getText());
			DataManagerFxApp.appConfig.saveConfig();

			processBackupBox.setVisible(selectedFile.exists());
		}
	}

	public void selectBackupPath(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Backup Datei auswählen ...");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Backup Datei ...", "*.zip"));
		File selectedFile = fileChooser.showSaveDialog(DataManagerFxApp.mainStage);
		if(selectedFile != null){
			lblBackupPath.setText(selectedFile.getAbsolutePath());

		}
	}

	public void startBackup(ActionEvent actionEvent) {
		btnCancelBackup.setDisable(false);
		btnStartBackup.setDisable(true);
	}

	public void cancelBackup(ActionEvent actionEvent) {
		btnCancelBackup.setDisable(true);
		btnStartBackup.setDisable(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cmnDriver.getItems().add(POSTGRESQL_DRIVER);
		cmnDriver.getSelectionModel().select(0);
		
		List<TableModel> tableList = databaseConfig.getTableModelList();
		for(TableModel tableModel: tableList) {
			try {
				
				FXMLLoader loader = DataManagerFxApp.loadFXMLLoader("table_data");
				loader.setController(new TableDataController(tableModel, databaseConfig));
				HBox tableBox = loader.load();
				tablesBox.getChildren().add(tableBox);
				
				
			} catch (IOException e) {
				DataManagerFxApp.showError("Erstellen von Tabellen-Liste ...", "Fehler: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}

		lblPgDumpPath.setText(DataManagerFxApp.appConfig.getPostgresDumpAppPath());
		File pgDumpFile = new File(DataManagerFxApp.appConfig.getPostgresDumpAppPath());
		processBackupBox.setVisible(pgDumpFile.exists());

		extractedLastBackupInfo();
	}

	private void extractedLastBackupInfo() {
		backupInfoBox.getChildren().clear();
		List<String> backupInfoList = databaseConfig.getBackupInfo();
		for(String tableBackupInfo: backupInfoList){
			Label lbl = new Label();
			lbl.setText(tableBackupInfo);
			lbl.setPrefHeight(20);

			backupInfoBox.getChildren().add(lbl);
		}
	}
}
