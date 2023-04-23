package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.PaymentType;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.ui.FloatTextField;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class RechnungController implements Initializable, ControllerBase {

    @FXML private TableView<RechnungItemProperty> showItemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<RechnungItemProperty, String> produktColumn;

    @FXML private TableColumn<RechnungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<RechnungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;

    @FXML private Label lblQuittung;
    
    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungNummerTitle;

    @FXML private Label lblDatum;

    @FXML private Button btnSave;

    @FXML private Button btnReset;

    @FXML private Button btnClose;

    @FXML private Button btnSelectCsutomer;

    @FXML private Button btnPrint;

    @FXML private Button btnAddItem;

    @FXML private Button btnDeleteItem;
    
    @FXML private Button btnEdit;

    @FXML private GridPane bannerPane;

    @FXML private Label lblName;

    @FXML private Label lblCalcFactor;

    @FXML private Label lblStreet;

    @FXML private Label lblPlz;

    @FXML private Label lblCity;

    @FXML private Label lblHaus;

    @FXML private Label lblAddress2;
    
    @FXML private Label lblExemplar;

    @FXML private HBox toggleStatusBox;

    @FXML private HBox toggleButtonBox;

    @FXML private Button btnToggleBrerechnenZiel;

    @FXML private HBox nameBox;
    
    @FXML private VBox itemsListBox;
    
    @FXML private HBox toolbarBox;

    @FXML private ComboBox<PaymentType> cmbPaymentType;

    @FXML private ToggleGroup tgCalcGroup;

    @FXML private FloatTextField txtAnzahlung;

    @FXML private FloatGeldLabel lblRest;

    private RechnungBindingService rechnungBindingService;

    private final RechnungRepository rechnungRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    private final RechnungType rechnungType;

    public RechnungController(RechnungType rechnungType) {

        this.rechnungType = rechnungType;
        this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
        this.rechnungDataHelper = RechnungManagerSpringApp.applicationContext.getBean(IRechnungDataHelper.class);

	}

    @FXML
    private void toggleBrerechnenZiel(){
        rechnungBindingService.toggleActiveBerechnenZiel();
    }

	@FXML
    private void speichern() throws IOException {
        if(!rechnungBindingService.verifySaving(true)){
            return;
        }

        if(rechnungBindingService.save()){

        }
    }

    @FXML
    private void reload() throws IOException {

    	if(canResetData()) {
    		doReloadData();
    	}
    }

    @FXML
    private void addItem() throws IOException {

    	RechnungItemProperty item = rechnungBindingService.addNewRow();
    	btnAddItem.setDisable(true);

    	showItemsTableView.edit(-1, null);
    	
    	setItemsTableViewEditable(true);
    	int indexofItem = showItemsTableView.getItems().indexOf(item);
    	showItemsTableView.getSelectionModel().select(indexofItem);
    	showItemsTableView.requestFocus();
    	showItemsTableView.edit(indexofItem, mengeColumn);
    	
    }
    
    @FXML
    private void startEdit() throws IOException {
    	this.stage.close();
    	
    	RechnungManagerFxApp.startEditRechnung(rechnungBindingService.getRechnungSavingModel(), rechnungBindingService.getCustomerModel());
    }

    @FXML
    private void deleteItem() throws IOException {

    	markSelectedItemAsDeleted();
    }

    @FXML
    private void selectCustomer() throws IOException {
    	//rechnungBindingService.test();
    	
    	SelectCustomerDialog dialog = new SelectCustomerDialog(stage, this.rechnungBindingService.getCustomerModel());

        Optional<CustomerModel> result = dialog.showAndWait();
        if(result.isPresent()){

            Platform.runLater(()->{
                CustomerModel model = result.get();
                rechnungBindingService.setCustomerModel(model);
            });
        }

    }
    
    
    private void doReloadData() {
    	
    	showItemsTableView.setItems(null);
    	rechnungBindingService.reset();
        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
    }
    
    @FXML
    private void printRechnung() throws JRException {


    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);

        PrintUtils.printRechnungItems(Arrays.asList(rechnungBindingService.getRechnungSavingModel()), true);
        showItemsTableView.setEditable(true);

    }

    @FXML
    private void closeRechnung() throws IOException {
        if(rechnungBindingService.isView()){
            this.stage.close();
            return;
        }
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    private boolean canResetData() {
    	if(rechnungBindingService.isDirty()) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	    alert.setTitle("Geändrte daten löschen ...");
    	    alert.setHeaderText("Die Daten von Rechnung ist geändert. Soll die Anderungen gelöscht werden?");
    	    alert.setContentText(null);
    	    Optional<ButtonType> res = alert.showAndWait();
    	    if(res.isPresent() && res.get() == ButtonType.OK) {
    	    	return true;
    	    }
    	    return false;
    		
    	}
    	return true;
    }
    
    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
    	
    	RechnungManagerFxApp.setCurrentController(this);

        rechnungBindingService = new RechnungBindingService(this.rechnungType,
                                                            this.rechnungRepository,
                                                            this.rechnungDataHelper);


        bannerPane.styleProperty().bind(rechnungBindingService.bannerBackColorProperty());

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
        showItemsTableView.setUserData(rechnungBindingService);
        
        showItemsTableView.prefHeightProperty().bind(itemsListBox.heightProperty().subtract(95));

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewMengeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewBrutoPreisValue(tPos.getRow(), value);
            rechnungBindingService.setVisbleToggleStatusBox(false);
            
            if(rechnungBindingService.isEditingMode()) {
            	showItemsTableView.edit(-1, null);

            	btnAddItem.setDisable(false);
            	setItemsTableViewEditable(false);
            }

            rechnungBindingService.setDirty(true);
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewProduktValue(tPos.getRow(), value);
        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewArtikelNummerValue(tPos.getRow(), value);

        });

        lblNetto.valueProperty().bind(rechnungBindingService.nettoSummeProperty());
        lblMvst.valueProperty().bind(rechnungBindingService.mvstSummeProperty());
        lblGesamt.valueProperty().bind(rechnungBindingService.gesamtSummeProperty());
        
        lblRechnungNummer.textProperty().bind(rechnungBindingService.getRechnungNummerProperty());

        lblDatum.setText(GeneralUtils.formatDate(LocalDate.now()));

        if(this.rechnungType == RechnungType.RECHNUNG){
            lblRechnungNummerTitle.setText("Rechnung Nr.");
        }
        if(this.rechnungType == RechnungType.QUITTUNG){
            lblRechnungNummerTitle.setText("Quittung Nr.");
        }

        btnSave.disableProperty().bind(rechnungBindingService.getDisableSaveProperty());
        btnPrint.disableProperty().bind(rechnungBindingService.getDisablePrintProperty());
        toggleStatusBox.visibleProperty().bind(rechnungBindingService.getVisbleToggleStatusBox());

        tgCalcGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            String value = newValue.getUserData().toString();
            rechnungBindingService.setCalcFactor(Float.parseFloat(value));

            lblCalcFactor.setText("Druck-Scale: " + value);
            toggleButtonBox.setVisible(false);
        });

        selectCalcFactorButton();

        cmbPaymentType.getItems().addAll(PaymentType.values());
        cmbPaymentType.valueProperty().bindBidirectional(rechnungBindingService.getPaymentTypeProperty());
        cmbPaymentType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            rechnungBindingService.calculateButtons();
        });

        txtAnzahlung.textProperty().bindBidirectional(rechnungBindingService.getAntahlungProperty());
        lblRest.valueProperty().bind(rechnungBindingService.getRemainingProperty());

        KeyCombination kc = new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
        if(this.rechnungType == RechnungType.QUITTUNG){
        	
        	for(Node child: bannerPane.getChildren()) {
        		if(GridPane.getColumnIndex(child) == 1 || GridPane.getColumnIndex(child) == 2) {
        			child.setVisible(false);
        		}
        		
        	}
            
        	lblQuittung.setVisible(true);

            btnToggleBrerechnenZiel = new Button("");
            btnToggleBrerechnenZiel.setPrefWidth(1);
            btnToggleBrerechnenZiel.setPrefHeight(1);
            btnToggleBrerechnenZiel.setPadding(new Insets(0));
            btnToggleBrerechnenZiel.setOnAction(e -> {
                rechnungBindingService.toggleActiveBerechnenZiel();
                showItemsTableView.requestFocus();
                toggleButtonBox.setVisible(!toggleButtonBox.isVisible());
                selectCalcFactorButton();
            });

            toggleStatusBox.getChildren().add(btnToggleBrerechnenZiel);

            RechnungManagerFxApp.mainScene.addMnemonic(new Mnemonic(btnToggleBrerechnenZiel, kc));
        }

        if(this.rechnungType == RechnungType.RECHNUNG){

            lblName.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getCustomerName());
            lblStreet.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getStreet());
            lblPlz.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getPlz());
            lblAddress2.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getAddress2());
            lblCity.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getCity());
            lblHaus.textProperty().bind(rechnungBindingService.getCustomerModelProperty().getHouseNumber());

            lblQuittung.setVisible(false);

            if(btnToggleBrerechnenZiel != null){
                toggleStatusBox.getChildren().remove(btnToggleBrerechnenZiel);
            }

            RechnungManagerFxApp.mainScene.getMnemonics().remove(kc);

        }

    }

    private void selectCalcFactorButton() {
        for(Toggle toggle: tgCalcGroup.getToggles()){
            String value = toggle.getUserData().toString();
            if(Float.parseFloat(value) == rechnungBindingService.getCalcFactor()){
                tgCalcGroup.selectToggle(toggle);
                break;
            }
        }
    }

    @Override
	public boolean isDirty() {
		
		return rechnungBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }

    private void markSelectedItemAsDeleted() {
    	int selectedIndx = showItemsTableView.getSelectionModel().getSelectedIndex();
    	
    	showItemsTableView.setItems(null);
        //showItemsTableView.getItems().clear();
    	rechnungBindingService.deleteItemAtIndex(selectedIndx);
    	showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
	}
    
	public void startView(RechnungModel rechnungModel, Stage stage) {


		rechnungBindingService.startEditing(rechnungModel, null);
		
        rechnungBindingService.setIsView(true);
        this.stage = stage;

        toggleStatusBox.getChildren().remove(btnToggleBrerechnenZiel);

        lblExemplar.setText("original");
		if(rechnungModel.hasReference()) {
			lblExemplar.setText("überarbeitet: ver " + rechnungModel.getRechnungVersion());
		}
		
		if(this.rechnungType == RechnungType.RECHNUNG){
            lblQuittung.setVisible(false);
        }

        if(this.rechnungType == RechnungType.QUITTUNG){
        	lblQuittung.setVisible(true);

        }
        
        addExamplarTab();
        
        toolbarBox.getChildren().remove(btnAddItem);
        toolbarBox.getChildren().remove(btnDeleteItem);
        toolbarBox.getChildren().remove(btnSave);
        toolbarBox.getChildren().remove(btnReset);
        toolbarBox.getChildren().remove(btnClose);
        
        btnEdit.setVisible(true);

        cmbPaymentType.setDisable(true);

        txtAnzahlung.setDisable(true);

		setItemsTableViewEditable(false);

        if(this.rechnungType == RechnungType.RECHNUNG){
            
            nameBox.getChildren().remove(btnSelectCsutomer);
            lblQuittung.setVisible(false);
        }

        if(this.rechnungType == RechnungType.QUITTUNG){
        	lblQuittung.setVisible(true);
        	for(Node child: bannerPane.getChildren()) {
        		if(lblQuittung == child) {
        			continue;
        		}
        		if(GridPane.getColumnIndex(child) == 1 || GridPane.getColumnIndex(child) == 2) {
        			child.setVisible(false);
        		}
        		
        	}

        }

	}
    
	public void startEdit(RechnungModel rechnungModel, CustomerModel customerModel) {


		rechnungBindingService.startEditing(rechnungModel, customerModel);
		
        rechnungBindingService.setIsView(false);

        lblExemplar.setText("original");
		if(rechnungModel.hasReference()) {
            lblExemplar.setText("überarbeitet: ver " + rechnungModel.getRechnungVersion());
		}
		
		if(this.rechnungType == RechnungType.RECHNUNG){
            lblQuittung.setVisible(false);
        }

        if(this.rechnungType == RechnungType.QUITTUNG){
        	lblQuittung.setVisible(true);

        }
        
        addExamplarTab();
        
        btnAddItem.setVisible(true);
        btnDeleteItem.setVisible(true);
        btnEdit.setVisible(false);
        
		setItemsTableViewEditable(false);
        showItemsTableView.setOnKeyPressed( new EventHandler<KeyEvent>()
        	{
	          @Override
	          public void handle( final KeyEvent keyEvent )
	          {

	              if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
	              {
	            	  markSelectedItemAsDeleted();
	              }
	
	          }

	        } 
        );
        
        if(this.rechnungType == RechnungType.RECHNUNG){
            nameBox.getChildren().remove(btnSelectCsutomer);
            lblQuittung.setVisible(false);
        }

        if(this.rechnungType == RechnungType.QUITTUNG){
        	lblQuittung.setVisible(true);
        	for(Node child: bannerPane.getChildren()) {
        		if(lblQuittung == child) {
        			continue;
        		}
        		if(GridPane.getColumnIndex(child) == 1 || GridPane.getColumnIndex(child) == 2) {
        			child.setVisible(false);
        		}
        		
        	}

        }

	}

	private void setItemsTableViewEditable(boolean editable) {
		//showItemsTableView.getColumns().forEach(c -> c.setEditable(editable));
        showItemsTableView.setEditable(editable);
	}

	private void addExamplarTab() {
		TabPane tbItemsList = new TabPane();
        GridPane.setColumnIndex(tbItemsList, 0);
        GridPane.setRowIndex(tbItemsList, 2);
        rootPane.getChildren().remove(itemsListBox);
        rootPane.getChildren().add(tbItemsList);
        
        Tab tbOriginal = new Tab("Original");
        tbOriginal.setContent(itemsListBox);
        tbItemsList.getTabs().add(tbOriginal);
	}

}
