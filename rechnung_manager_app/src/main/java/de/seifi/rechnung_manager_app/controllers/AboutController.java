package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.models.PrinterItem;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


public class AboutController implements Initializable, ControllerBase {

	public @FXML GridPane rootPane;
	private @FXML Label lblBuildVersion;
	private @FXML Label lblBuildDate;

	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	RechnungManagerFxApp.setCurrentController(this);

		lblBuildVersion.setText(RechnungManagerSpringApp.getBuildProperties().get("version"));

		long buildTime = Long.parseLong(RechnungManagerSpringApp.getBuildProperties().get("time"));

		Instant instance = java.time.Instant.ofEpochMilli(buildTime);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instance, ZoneId.systemDefault());

		lblBuildDate.setText(GeneralUtils.formatDate(localDateTime));

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