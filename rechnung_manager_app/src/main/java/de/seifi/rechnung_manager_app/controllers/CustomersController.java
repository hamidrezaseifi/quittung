package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.CustomerModelProperty;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.CustomerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable, ControllerBase {

    @FXML private VBox leftPane;

    @FXML private GridPane rightPane;

    @FXML private GridPane rootPane;

    @FXML private ListView<CustomerModel> lstCustomers;

    @FXML private Button btnAddCustomer;

    @FXML private Button btnDeleteCustomer;

    @FXML private Button btnReload;
    
    @FXML private TextField txtName;

    @FXML private TextField txtStreet;

    @FXML private TextField txtHouse;

    @FXML private TextField txtAddress2;

    @FXML private TextField txtPlz;

    @FXML private TextField txtCity;

    @FXML private Button btnApply;

    @FXML private Button btnCancel;


    private final ObservableList<CustomerModel> customerList;

	private CustomerModelProperty currentEditingModel = new CustomerModelProperty();
	
	private CustomerModel selectedModel = null;
	
	private int currentEditingIndex = -1;

    public CustomersController() {
        
        this.customerList = FXCollections.observableArrayList();

    }

    @FXML
    public void addCutomer(ActionEvent actionEvent) {
        this.customerList.add(new CustomerModel("neu Kunde"));
        
        this.lstCustomers.getSelectionModel().select(this.customerList.size() - 1);
        
    }

    @FXML
    public void deleteCutomer(ActionEvent actionEvent) {
        if(selectedModel != null){
        	
        	if(selectedModel.isNew()) {
        		reload(null);
        		return;
        	}
        	
        	Optional<ButtonType> result = UiUtils.showAsking("Kunde Löschen ...", 
        			String.format("Wollen Sie der Kunde '%s' löschen?", selectedModel.getCustomerName()));
        	if(result.isPresent() && result.get() == ButtonType.OK) {
        		CustomerHelper.delete(selectedModel.toEntity());
            	reload(null);
        	}
        	
        }
    }
    
    @FXML
    public void saveCutomer(ActionEvent actionEvent) {
    	CustomerModel editingModel = this.currentEditingModel.toModel();
    	editingModel.setId(this.selectedModel.getId());
    	
    	CustomerHelper.save(editingModel.toEntity());
        
    	reload(null);
    }
    
    @FXML
    public void cancelCutomer(ActionEvent actionEvent) {
    	
    	reload(null);
    }
    
    @FXML
    public void reload(ActionEvent actionEvent) {
    	CustomerHelper.reloadCustomerList();
    	
    	List<CustomerModel> allCustomerList = CustomerHelper.getCustomerList();
        this.customerList.clear();
        this.customerList.addAll(allCustomerList);
        
        this.lstCustomers.setItems(null);   
        this.lstCustomers.setItems(this.customerList);  
        this.lstCustomers.getSelectionModel().select(-1);
        this.rightPane.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    	RechnungManagerFxApp.setCurrentController(this);
    	
        leftPane.prefHeightProperty().bind(rootPane.heightProperty().subtract(20));
        rightPane.prefHeightProperty().bind(rootPane.heightProperty().subtract(20));
        rightPane.setVisible(false);
        lstCustomers.prefHeightProperty().bind(leftPane.heightProperty().subtract(100));
        
        lstCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) ->{
        	rightPane.setVisible(false);
        	if(lstCustomers.getSelectionModel().getSelectedIndex() > -1) {
        		
        		CustomerModel model = lstCustomers.getSelectionModel().getSelectedItem();
        		startEditingModel(model, lstCustomers.getSelectionModel().getSelectedIndex());
        		
        	}
        });


        reload(null);
    }

	private void startEditingModel(CustomerModel model, int index) {
		
		if(model == null) {
			return;
		}
		
		this.currentEditingModel.setModel(model);
		this.currentEditingIndex = index;
		this.selectedModel = model;

        txtName.textProperty().bindBidirectional(this.currentEditingModel.getCustomerName());
        txtStreet.textProperty().bindBidirectional(this.currentEditingModel.getStreet());
        txtPlz.textProperty().bindBidirectional(this.currentEditingModel.getPlz());
        txtAddress2.textProperty().bindBidirectional(this.currentEditingModel.getAddress2());
        txtCity.textProperty().bindBidirectional(this.currentEditingModel.getCity());
        txtHouse.textProperty().bindBidirectional(this.currentEditingModel.getHouseNumber());
        
		rightPane.setVisible(true);
	}

	@Override
	public boolean isDirty() {
		
		boolean anyNewInList = this.customerList.stream().anyMatch(c -> c.isNew());
		
		return anyNewInList;
	}

	@Override
	public String getDirtyMessage() {
		
		return "Da gibt nicht gespeicherte Änderungen bei den Kunden-Liste.";
	}
}

