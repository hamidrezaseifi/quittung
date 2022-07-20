package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.data_service.impl.RechnungDataHelper;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.repositories.RechnungRepository;
import de.seifi.rechnung_manager_app.models.RechnungItemProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class RechnungController implements Initializable, ControllerBse {

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
    
    @FXML private Label lblRechnungNummer;
    
    @FXML private Label lblRechnungDatum;
    
    @FXML private Label lblLiferdatum;
    
    @FXML private Button btnSave;
    
    @FXML private Button btnReset;

    @FXML private Button btnClose;
    
    @FXML private Button btnPrint;

    @FXML private GridPane bannerPane;

    @FXML private TextField txtName;

    @FXML private TextField txtStreet;

    @FXML private TextField txtPlz;
    
    @FXML private TextField txtCity;
    
    @FXML private TextField txtHaus;
    
    @FXML private TextField txtAddress2;

    private RechnungBindingService rechnungBindingService;

    private final RechnungRepository rechnungRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    public RechnungController() {

        this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
        this.rechnungDataHelper = RechnungManagerSpringApp.applicationContext.getBean(IRechnungDataHelper.class);

	}

	@FXML
    private void speichern() throws IOException {
		showItemsTableView.setEditable(false);
		showItemsTableView.edit(-1, null);
		showItemsTableView.setDisable(true);
		
		showItemsTableView.setItems(null);
		
        if(rechnungBindingService.save()){

        }
		
		showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
        showItemsTableView.setEditable(true);
        showItemsTableView.setDisable(false);
    }

    @FXML
    private void reload() throws IOException {

    	if(canResetData()) {
    		doReloadData();
    	}
    }

    private void doReloadData() {
    	showItemsTableView.setItems(null);
    	rechnungBindingService.reset();
        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());

        txtPlz.setText("");
        txtName.setText("");
        txtStreet.setText("");
        txtAddress2.setText("");
        txtCity.setText("");
        txtHaus.setText("");
        
        txtName.setFocusTraversable(true);
    }
    
    @FXML
    private void printRechnung() throws IOException {

    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);
    	
        Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("rechnung_print");
        GridPane printPane = (GridPane)pair.getKey();
        FXMLLoader fxmlLoader = pair.getValue();
        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
        dialogController.printRechnungList(Arrays.asList(rechnungBindingService.getSavingModel()));
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

        rechnungBindingService = new RechnungBindingService(this.rechnungRepository, this.rechnungDataHelper);

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

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewMengeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            rechnungBindingService.setNewBrutoPreisValue(tPos.getRow(), value);
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
        lblRechnungDatum.textProperty().bind(rechnungBindingService.getRechnungDatumProperty());
        lblLiferdatum.textProperty().bind(rechnungBindingService.getLiferDatumProperty());

        btnSave.disableProperty().bind(rechnungBindingService.getDisableSaveProperty());
        btnPrint.disableProperty().bind(rechnungBindingService.getDisablePrintProperty());

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setCustomerNameValue(newValue);
        });

        txtStreet.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setStreetValue(newValue);
        });

        txtPlz.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setPlzValue(newValue);
        });

        txtAddress2.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setAddress2Value(newValue);
        });

        txtCity.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setCityValue(newValue);
        });

        txtHaus.textProperty().addListener((observable, oldValue, newValue) -> {
            rechnungBindingService.setHausValue(newValue);
        });
    }

	@Override
	public boolean isDirty() {
		
		return rechnungBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }

	public void loadModel(RechnungModel rechnungModel, boolean editable, Stage stage) {
		showItemsTableView.setEditable(editable);
		rechnungBindingService.setRechnungModel(rechnungModel);
		
		showItemsTableView.getColumns().forEach(c -> c.setEditable(editable));
        rechnungBindingService.setIsView(!editable);
        this.stage = stage;

		if(!editable) {

			btnSave.setVisible(false);
			btnReset.setVisible(false);
			
			txtName.setEditable(false);
			txtStreet.setEditable(false);
			txtPlz.setEditable(false);
			txtAddress2.setEditable(false);
			txtCity.setEditable(false);
			txtHaus.setEditable(false);
			
			txtName.setText(rechnungModel.getCustomerName());
			txtStreet.setText(rechnungModel.getStreet());
			txtPlz.setText(rechnungModel.getPlz());
			txtAddress2.setText(rechnungModel.getAddress2());
			txtCity.setText(rechnungModel.getCity());
			txtHaus.setText(rechnungModel.getHouseNumber());
			
		}
	}

}
