package de.seifi.quittung.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.seifi.quittung.QuittungApp;
import de.seifi.quittung.ui.FloatGeldLabel;
import de.seifi.quittung.ui.FloatTextField;
import de.seifi.quittung.ui.QuittungBindingModel;
import de.seifi.quittung.ui.QuittungItemProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class QuittungController implements Initializable {

    @FXML private TableView<QuittungItemProperty> itemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<QuittungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> preisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> gesamtColumn;


    private QuittungBindingModel quittungModel;

    @FXML
    private void switchToSecondary() throws IOException {
        QuittungApp.setRoot("secondary");
    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        quittungModel = new QuittungBindingModel();

        itemsTableView.getColumns().get(0).prefWidthProperty().bind(
                itemsTableView.widthProperty().subtract(
                        itemsTableView.getColumns().get(1).widthProperty()).subtract(
                        itemsTableView.getColumns().get(2).widthProperty()).subtract(
                        itemsTableView.getColumns().get(3).widthProperty()).subtract(5)
                                                                   );

        itemsTableView.setItems(quittungModel.getQuittungItems());

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            prop.setMenge(value);
            prop.setGesamt(value * prop.getPreis());

            quittungModel.calculateQuittungSumme();
        });

        preisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            prop.setPreis(value);
            prop.setGesamt(prop.getMenge() * value);

            quittungModel.calculateQuittungSumme();
        });

        lblNetto.valueProperty().bind(quittungModel.nettoSummeProperty());
        lblMvst.valueProperty().bind(quittungModel.mvstSummeProperty());
        lblGesamt.valueProperty().bind(quittungModel.gesamtSummeProperty());

    }
}
