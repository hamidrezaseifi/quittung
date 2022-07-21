package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Pair;

public class SelectCustomerDialog extends Dialog<CustomerModel> {
	
	public SelectCustomerDialog(Window owner) throws IOException {
		
		Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.getSelectCustomerDialog();
		
        FXMLLoader loader = pair.getValue();
        loader.setController(this);

        DialogPane dialogPane = (DialogPane)pair.getKey();
        //dialogPane.lookupButton(connectButtonType).addEventFilter(ActionEvent.ANY, this::onConnect);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setResizable(true);
        setTitle("Kunde auswählen ...");
        setDialogPane(dialogPane);

        
		setResultConverter(buttonType -> {
			return new CustomerModel();
		});
	}

}
