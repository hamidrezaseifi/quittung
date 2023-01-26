package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class SelectRechnungTypeDialog extends Dialog<RechnungType> {
    private static final Logger logger = LoggerFactory.getLogger(SelectRechnungTypeDialog.class);

    @FXML private Button btnSelectQuittung;

    @FXML private Button btnSelectRechnung;

    public SelectRechnungTypeDialog(Window owner) throws IOException {


        FXMLLoader loader = RechnungManagerFxApp.getSelectRechnungDialog();
		
        loader.setController(this);

        DialogPane dialogPane = loader.load();

        initModality(Modality.APPLICATION_MODAL);

        dialogPane.getStylesheets().add(RechnungManagerFxApp.getMainStyle());

        setResizable(true);
        setTitle("Exportieren in ...");
        setDialogPane(dialogPane);

        btnSelectQuittung.setOnAction(event -> {
            setResult(RechnungType.QUITTUNG);
            close();
        });

        btnSelectRechnung.setOnAction(event -> {
            setResult(RechnungType.RECHNUNG);
            close();
        });

        setResult(RechnungType.NONE);

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        setOnCloseRequest(event -> this.close());

        this.setResizable(false);
    }

    @FXML
    private void selectImage(){

    }

}
