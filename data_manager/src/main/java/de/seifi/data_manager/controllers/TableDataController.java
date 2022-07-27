package de.seifi.data_manager.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import de.seifi.data_manager.DataManagerFxApp;
import de.seifi.rechnung_common.config.DatabaseConfigUtils;
import de.seifi.rechnung_common.models.TableModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TableDataController implements Initializable {

	private final TableModel tableModel;
	
	@FXML private Label lblName;

	@FXML private Label lblInfo;

	@FXML private Button btnClear;

	
	private final DatabaseConfigUtils databaseConfig;
	
	public TableDataController(TableModel tableModel, DatabaseConfigUtils databaseConfig) {

		this.tableModel = tableModel;
		this.databaseConfig = databaseConfig;
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lblName.setText(tableModel.getTitle());
		
		try {
			lblInfo.setText(this.databaseConfig.getTableInfo(tableModel));
		} catch (SQLException e) {
			DataManagerFxApp.showError("Tabelle-Info extrahieren ...", "Fehler:" + e.getLocalizedMessage());
			e.printStackTrace();
		}catch (Exception e) {
			DataManagerFxApp.showError("Tabelle-Info extrahieren ...", "Fehler:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		
	}
	
	@FXML
	private void clearTable() {
		try {
			this.databaseConfig.clearTable(tableModel);
			lblInfo.setText(this.databaseConfig.getTableInfo(tableModel));
			
		} catch (SQLException e) {
			DataManagerFxApp.showError("Tabelle-Info leeren ...", "Fehler:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

}
