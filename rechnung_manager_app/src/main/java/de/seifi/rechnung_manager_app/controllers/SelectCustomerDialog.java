package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.CustomerModelProperty;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class SelectCustomerDialog extends Dialog<CustomerModel> {

    @FXML private GridPane rootPane;

    @FXML TableView<CustomerModel> twCustomers;

    @FXML private TextField txtName;

    @FXML private TextField txtStreet;

    @FXML private TextField txtHouse;

    @FXML private TextField txtAddress2;

    @FXML private TextField txtPlz;

    @FXML private TextField txtCity;


    @FXML private ButtonType okButtonType;

    @FXML private TextField txtFilter;

    private Button okButton;

    @FXML private TabPane tabPane;

    private CustomerModelProperty customerModelProperty;

    private final List<CustomerModel> allCustomerList;

    private final ObservableList<CustomerModel> customerList;

    private final FilteredList<CustomerModel> filteredList;

    public SelectCustomerDialog(Window owner,
                                CustomerModel inCustomerModel) throws IOException {

        this.allCustomerList = RechnungManagerSpringApp.getCustomerService().getCustomerList();

        FXMLLoader loader = RechnungManagerFxApp.getSelectCustomerDialog();
		
        loader.setController(this);

        DialogPane dialogPane = loader.load();

        this.okButton = (Button)dialogPane.lookupButton(okButtonType);
        this.okButton.addEventFilter(ActionEvent.ACTION,
                             event -> {
                                 if(tabPane.getSelectionModel().getSelectedIndex() == 0 && customerModelProperty.isInvalid()){
                                     event.consume();
                                 }
                                 if(tabPane.getSelectionModel().getSelectedIndex() == 1 && twCustomers.getSelectionModel().getSelectedItem() == null){
                                     event.consume();
                                 }
                             });

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setResizable(true);
        setTitle("Kunde auswählen ...");
        setDialogPane(dialogPane);

        customerModelProperty = new CustomerModelProperty();

        customerList = FXCollections.observableArrayList();
        customerList.addAll(this.allCustomerList);

        filteredList = new FilteredList<>(this.customerList, p -> true);


        setResultConverter(buttonType -> {
            if( buttonType.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                return null;
            }
            if(tabPane.getSelectionModel().getSelectedIndex() == 0){
                return customerModelProperty.toModel();
            }
            if(tabPane.getSelectionModel().getSelectedIndex() == 1){
                return twCustomers.getSelectionModel().getSelectedItem();
            }
            return null;
		});

        txtName.textProperty().bindBidirectional(customerModelProperty.getCustomerName());
        txtStreet.textProperty().bindBidirectional(customerModelProperty.getStreet());
        txtPlz.textProperty().bindBidirectional(customerModelProperty.getPlz());
        txtAddress2.textProperty().bindBidirectional(customerModelProperty.getAddress2());
        txtCity.textProperty().bindBidirectional(customerModelProperty.getCity());
        txtHouse.textProperty().bindBidirectional(customerModelProperty.getHouseNumber());

        twCustomers.setRowFactory( tv -> {
            TableRow<CustomerModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    setResult(row.getItem());
                    close();
                }
            });
            return row ;
        });

        twCustomers.setItems(filteredList);

        txtName.requestFocus();

        txtFilter.textProperty().addListener((obs, oldValue, newValue) -> {
            final String selected = txtFilter.getText();

            Platform.runLater(() -> {

                filteredList.setPredicate(item -> {
                    if(newValue == null) {
                        return false;
                    }
                    if (item.getCustomerName().toUpperCase().contains(newValue.toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
        });

        validateModel();

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            validateModel();
        });

        twCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            validateModel();
        });

        if(inCustomerModel.isNew() == false){
            for(int i = 0; i < customerList.size(); i++){
                if(customerList.get(i).getId() == inCustomerModel.getId()){
                    twCustomers.getSelectionModel().select(i);
                    break;
                }
            }
            tabPane.getSelectionModel().select(1);
        }

        if(inCustomerModel.isNew()){
            customerModelProperty.setModel(inCustomerModel);
            tabPane.getSelectionModel().select(0);
        }

        txtName.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });
        txtStreet.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });
        txtPlz.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });
        txtAddress2.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });
        txtCity.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });
        txtHouse.textProperty().addListener((obs, oldValue, newValue) -> {
            validateModel();
        });


    }

    private void validateModel(){
        if(tabPane.getSelectionModel().getSelectedIndex() == 0 && customerModelProperty.isInvalid()){
            this.okButton.setDisable(true);
            return;
        }
        if(tabPane.getSelectionModel().getSelectedIndex() == 1 && twCustomers.getSelectionModel().getSelectedItem() == null){
            this.okButton.setDisable(true);
            return;
        }
        this.okButton.setDisable(false);

    }
}
