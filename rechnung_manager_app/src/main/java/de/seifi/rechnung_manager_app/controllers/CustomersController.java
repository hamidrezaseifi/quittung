package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.utils.CustomerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML private VBox leftPane;

    @FXML private GridPane rightPane;

    @FXML private GridPane rootPane;

    @FXML private ListView<CustomerModel> lstCustomers;

    @FXML private Button btnAddCustomer;

    @FXML private Button btnDeleteCustomer;

    private final ObservableList<CustomerModel> filteredCustomerList;

    public CustomersController() {
        List<CustomerModel> allCustomerList = CustomerHelper.getCustomerList();
        this.filteredCustomerList = FXCollections.observableArrayList(allCustomerList);

    }

    @FXML
    public void addCutomer(ActionEvent actionEvent) {
        this.filteredCustomerList.add(new CustomerModel("neu Kunde"));
    }

    @FXML
    public void deleteCutomer(ActionEvent actionEvent) {
        if(this.lstCustomers.getSelectionModel().getSelectedIndex() > -1){
            this.filteredCustomerList.remove(this.lstCustomers.getSelectionModel().getSelectedIndex());
            this.lstCustomers.getSelectionModel().select(-1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        leftPane.prefHeightProperty().bind(rootPane.heightProperty().subtract(20));
        rightPane.prefHeightProperty().bind(rootPane.heightProperty().subtract(20));
        lstCustomers.prefHeightProperty().bind(leftPane.heightProperty().subtract(100));

        this.lstCustomers.setItems(this.filteredCustomerList);

    }
}

