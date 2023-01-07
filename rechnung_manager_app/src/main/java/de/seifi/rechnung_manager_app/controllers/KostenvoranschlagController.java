package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.repositories.KostenvoranschlagRepository;
import de.seifi.rechnung_common.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.PaymentType;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagBindingService;
import de.seifi.rechnung_manager_app.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.GeneralUtils;
import javafx.application.Platform;
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

public class KostenvoranschlagController implements Initializable, ControllerBase {

    @FXML private TableView<KostenvoranschlagItemProperty> showItemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> produktColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> originalNummerColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> teilNummerColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> markeColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, Boolean> bestelltColumn;

    @FXML private GridPane rootPane;

    @FXML private Label lblQuittung;

    @FXML private Label lblNummer;

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

    @FXML private Label lblStreet;

    @FXML private Label lblPlz;

    @FXML private Label lblCity;

    @FXML private Label lblHaus;

    @FXML private Label lblAddress2;

    @FXML private HBox nameBox;

    @FXML private VBox itemsListBox;

    @FXML private HBox toolbarBox;

    private KostenvoranschlagBindingService bindingService;

    private final KostenvoranschlagRepository kostenvoranschlagRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    public KostenvoranschlagController() {

        this.kostenvoranschlagRepository =
                RechnungManagerSpringApp.applicationContext.getBean(KostenvoranschlagRepository.class);
        this.rechnungDataHelper = RechnungManagerSpringApp.applicationContext.getBean(IRechnungDataHelper.class);

	}

    @FXML
    private void toggleBrerechnenZiel(){
        bindingService.toggleActiveBerechnenZiel();
    }

	@FXML
    private void speichern() throws IOException {
        if(!bindingService.verifySaving(true)){
            return;
        }

        if(bindingService.save()){

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

        KostenvoranschlagItemProperty item = bindingService.addNewRow();
    	btnAddItem.setDisable(true);

    	showItemsTableView.edit(-1, null);
    	
    	setItemsTableViewEditable(true);
    	int indexofItem = showItemsTableView.getItems().indexOf(item);
    	showItemsTableView.getSelectionModel().select(indexofItem);
    	showItemsTableView.requestFocus();
    	showItemsTableView.edit(indexofItem, produktColumn);
    	
    }

    @FXML
    private void deleteItem() throws IOException {

    	markSelectedItemAsDeleted();
    }

    @FXML
    private void selectCustomer() throws IOException {
    	//bindingService.test();
    	
    	SelectCustomerDialog dialog = new SelectCustomerDialog(stage, this.bindingService.getCustomerModel());

        Optional<CustomerModel> result = dialog.showAndWait();
        if(result.isPresent()){

            Platform.runLater(()->{
                CustomerModel model = result.get();
                bindingService.setCustomerModel(model);
            });
        }

    }
    
    
    private void doReloadData() {
    	
    	showItemsTableView.setItems(null);
    	bindingService.reset();
        showItemsTableView.setItems(bindingService.getKostenvoranschlagItems());
    }
    
    @FXML
    private void printRechnung() throws JRException {


    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);

        UiUtils.printKostenvoranschlagItems(Arrays.asList(bindingService.getRechnungSavingModel()));
        showItemsTableView.setEditable(true);

    }

    @FXML
    private void closeRechnung() throws IOException {
        if(bindingService.isView()){
            this.stage.close();
            return;
        }
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    private boolean canResetData() {
    	if(bindingService.isDirty()) {
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

        bindingService = new KostenvoranschlagBindingService(this.kostenvoranschlagRepository,
                                                             this.rechnungDataHelper);


        bannerPane.styleProperty().bind(bindingService.bannerBackColorProperty());

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        originalNummerColumn.widthProperty()).subtract(
                        teilNummerColumn.widthProperty()).subtract(
                        markeColumn.widthProperty()).subtract(
                            bPreisColumn.widthProperty()).subtract(
                                            bestelltColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(bindingService.getKostenvoranschlagItems());
        showItemsTableView.setUserData(bindingService);
        
        showItemsTableView.prefHeightProperty().bind(itemsListBox.heightProperty().subtract(95));

        originalNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<KostenvoranschlagItemProperty,?> tPos = event.getTablePosition();
            bindingService.setNewOriginalNummerValue(tPos.getRow(), value);
        });

        teilNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<KostenvoranschlagItemProperty,?> tPos = event.getTablePosition();
            bindingService.setNewTeilNummerValue(tPos.getRow(), value);
        });

        markeColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<KostenvoranschlagItemProperty,?> tPos = event.getTablePosition();
            bindingService.setNewMarkeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<KostenvoranschlagItemProperty,?> tPos = event.getTablePosition();
            bindingService.setNewPreisValue(tPos.getRow(), value);

            bindingService.setDirty(true);
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<KostenvoranschlagItemProperty,?> tPos = event.getTablePosition();
            bindingService.setNewProduktValue(tPos.getRow(), value);
        });

        lblNetto.valueProperty().bind(bindingService.nettoSummeProperty());
        lblMvst.valueProperty().bind(bindingService.mvstSummeProperty());
        lblGesamt.valueProperty().bind(bindingService.gesamtSummeProperty());
        
        lblNummer.textProperty().bind(bindingService.getNummerProperty());

        btnSave.disableProperty().bind(bindingService.getDisableSaveProperty());
        btnPrint.disableProperty().bind(bindingService.getDisablePrintProperty());

        lblName.textProperty().bind(bindingService.getCustomerModelProperty().getCustomerName());
        lblStreet.textProperty().bind(bindingService.getCustomerModelProperty().getStreet());
        lblPlz.textProperty().bind(bindingService.getCustomerModelProperty().getPlz());
        lblAddress2.textProperty().bind(bindingService.getCustomerModelProperty().getAddress2());
        lblCity.textProperty().bind(bindingService.getCustomerModelProperty().getCity());
        lblHaus.textProperty().bind(bindingService.getCustomerModelProperty().getHouseNumber());

        lblQuittung.setVisible(false);


    }

	@Override
	public boolean isDirty() {
		
		return bindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }

    private void markSelectedItemAsDeleted() {
    	int selectedIndx = showItemsTableView.getSelectionModel().getSelectedIndex();
    	
    	showItemsTableView.setItems(null);
        //showItemsTableView.getItems().clear();
    	bindingService.deleteItemAtIndex(selectedIndx);
    	showItemsTableView.setItems(bindingService.getKostenvoranschlagItems());
	}
    
	public void startView(KostenvoranschlagModel rechnungModel, Stage stage) {


		bindingService.startEditing(rechnungModel, null);
		
        bindingService.setIsView(true);
        this.stage = stage;
        
        addExamplarTab();
        
        toolbarBox.getChildren().remove(btnAddItem);
        toolbarBox.getChildren().remove(btnDeleteItem);
        toolbarBox.getChildren().remove(btnSave);
        toolbarBox.getChildren().remove(btnReset);
        toolbarBox.getChildren().remove(btnClose);
        
        btnEdit.setVisible(true);

		setItemsTableViewEditable(false);

        nameBox.getChildren().remove(btnSelectCsutomer);
        lblQuittung.setVisible(false);


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