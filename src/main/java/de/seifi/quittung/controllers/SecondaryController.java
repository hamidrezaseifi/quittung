package de.seifi.quittung.controllers;

import java.io.IOException;

import de.seifi.quittung.QuittungApp;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        QuittungApp.setRoot("primary");
    }
}