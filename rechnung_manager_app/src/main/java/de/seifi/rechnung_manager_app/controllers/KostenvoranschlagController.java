package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.repositories.CustomerFahrzeugScheinRepository;
import de.seifi.rechnung_common.repositories.KostenvoranschlagRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagBindingService;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class KostenvoranschlagController implements Initializable, ControllerBase {

    @FXML private TableView<KostenvoranschlagItemProperty> showItemsTableView;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private Label lblFahrzeugscheinName;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> produktColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> originalNummerColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> teilNummerColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, String> markeColumn;

    @FXML private TableColumn<KostenvoranschlagItemProperty, Float> preisColumn;

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

    @FXML private Button btnViewFahrzeugschein;

    @FXML private Button btnChooseFahrzeugschein;

    @FXML private Button btnDeleteFahrzeugschein;

    @FXML private Label lblName;

    @FXML private Label lblStreet;

    @FXML private Label lblPlz;

    @FXML private Label lblCity;

    @FXML private Label lblHaus;

    @FXML private Label lblAddress2;

    @FXML private HBox nameBox;

    @FXML private VBox itemsListBox;

    @FXML private HBox toolbarBox;

    @FXML private TextField txtSchluesselNummer;

    @FXML private TextField txtFahrgestellnummer;

    @FXML private HBox fahrzeugscheinBox;

    @FXML private Label lblSchluesselNummer;

    @FXML private Label lblFahrgestellnummer;

    @FXML private Label lblFahrzeugschein;

    private KostenvoranschlagBindingService bindingService;

    private final KostenvoranschlagRepository kostenvoranschlagRepository;

    private final CustomerFahrzeugScheinRepository fahrzeugScheinRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    public KostenvoranschlagController() {

        this.kostenvoranschlagRepository =
                RechnungManagerSpringApp.applicationContext.getBean(KostenvoranschlagRepository.class);
        this.fahrzeugScheinRepository =
                RechnungManagerSpringApp.applicationContext.getBean(CustomerFahrzeugScheinRepository.class);
        this.rechnungDataHelper = RechnungManagerSpringApp.applicationContext.getBean(IRechnungDataHelper.class);

	}

	@FXML
    private void speichern() {
        if(!bindingService.verifySaving(true)){
            return;
        }

        if(bindingService.save()){

        }
    }

    @FXML
    private void reload() {

    	if(canResetData()) {
    		doReloadData();
    	}
    }

    @FXML
    private void addItem() {

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
    private void startEdit() throws IOException {
        this.stage.close();

        RechnungManagerFxApp.getMainController().startEditKostenvoranschlag(this.bindingService.getKostenvoranschlagSavingModel(),
                                                                            this.bindingService.getCustomerModel());
    }



    @FXML
    private void deleteItem() {

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

    @FXML
    private void selectFahrzeugschein() throws IOException {

        SelectCustomerFahrzeugscheinDialog dialog =
                new SelectCustomerFahrzeugscheinDialog(stage,
                                                       this.bindingService.getCustomerModel(),
                                                       this.bindingService.getSelectedFahrzeugSchein(),
                                                       this.fahrzeugScheinRepository);
        Optional<CustomerFahrzeugScheinModel> result = dialog.showAndWait();
        if(result.isPresent()){
            bindingService.setSelectedFahrzeugSchein(result.get());
        }
    }

    @FXML
    private void deleteFahrzeugschein(){

        bindingService.setSelectedFahrzeugSchein(null);
    }

    @FXML
    private void viewFahrzeugschein(){
        if(this.bindingService.selectedFahrzeugScheinProperty().get() != null){
            CustomerFahrzeugScheinModel model = this.bindingService.selectedFahrzeugScheinProperty().get();
            Image img = new Image(new ByteArrayInputStream(model.getImageBytes()));
            Dialog showImageDialog = UiUtils.createImageViewDialog(img);

            showImageDialog.showAndWait();

        }

    }
    
    
    private void doReloadData() {
    	
    	showItemsTableView.setItems(null);
    	bindingService.reset();
        showItemsTableView.setItems(bindingService.getKostenvoranschlagItems());
    }
    
    @FXML
    private void printKostenvoranschlag() throws JRException {


    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);

        PrintUtils.printKostenvoranschlagItems(Arrays.asList(bindingService.getKostenvoranschlagSavingModel()));
        showItemsTableView.setEditable(true);

    }

    @FXML
    private void closeKostenvoranschlag() throws IOException {
        if(bindingService.isView()){
            this.stage.close();
            return;
        }
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    @FXML
    private void exportKostenvoranschlag() throws IOException {
        SelectRechnungTypeDialog dialog = new SelectRechnungTypeDialog(stage);

        Optional<RechnungType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() != RechnungType.NONE){
            RechnungModel rechnungModel = this.bindingService.exportModelIn(result.get());
            closeKostenvoranschlag();
            RechnungManagerFxApp.startEditRechnung(rechnungModel, this.bindingService.getCustomerModel());

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
                                                             this.fahrzeugScheinRepository,
                                                             this.rechnungDataHelper);

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        originalNummerColumn.widthProperty()).subtract(
                        teilNummerColumn.widthProperty()).subtract(
                        markeColumn.widthProperty()).subtract(
                        preisColumn.widthProperty()).subtract(
                                            bestelltColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(bindingService.getKostenvoranschlagItems());
        showItemsTableView.setUserData(bindingService);

        showItemsTableView.prefHeightProperty().bind(itemsListBox.heightProperty().subtract(95));

        lblFahrzeugscheinName.textProperty().bind(this.bindingService.selectedFahrzeugScheinNamePropertyProperty());

        toolbarBox.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        itemsListBox.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        fahrzeugscheinBox.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        txtSchluesselNummer.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        txtFahrgestellnummer.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        lblSchluesselNummer.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        lblFahrgestellnummer.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());
        lblFahrzeugschein.visibleProperty().bind(this.bindingService.hasCustomerPropertyProperty());

        txtSchluesselNummer.textProperty().bindBidirectional(this.bindingService.schluesselNummerPropertyProperty());
        txtFahrgestellnummer.textProperty().bindBidirectional(this.bindingService.fahrgestellNummerPropertyProperty());

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

        preisColumn.setOnEditCommit(event -> {
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
    
	public void startController(KostenvoranschlagModel kostenvoranschlagModel, CustomerModel customerModel, Stage stage, boolean isView) {


		bindingService.startEditing(kostenvoranschlagModel, customerModel, isView);
		
        bindingService.setIsView(isView);
        this.stage = stage;

        btnEdit.setVisible(isView);

        if(isView){
            toolbarBox.getChildren().remove(btnAddItem);
            toolbarBox.getChildren().remove(btnDeleteItem);
            toolbarBox.getChildren().remove(btnSave);
            toolbarBox.getChildren().remove(btnReset);
            toolbarBox.getChildren().remove(btnClose);

            setItemsTableViewEditable(false);

            txtFahrgestellnummer.setDisable(isView);
            txtSchluesselNummer.setDisable(isView);
            btnChooseFahrzeugschein.setVisible(!isView);
            btnDeleteFahrzeugschein.setVisible(!isView);

            nameBox.getChildren().remove(btnSelectCsutomer);
            lblQuittung.setVisible(!isView);
        }


	}
 	private void setItemsTableViewEditable(boolean editable) {
		//showItemsTableView.getColumns().forEach(c -> c.setEditable(editable));
        showItemsTableView.setEditable(editable);
	}

}
