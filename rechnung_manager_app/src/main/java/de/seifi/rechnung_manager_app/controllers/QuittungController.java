package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.data_service.IRechnungDataHelper;
import de.seifi.rechnung_manager_app.fx_services.QuittungBindingService;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.models.QuittungItemProperty;
import de.seifi.rechnung_manager_app.models.QuittungModel;
import de.seifi.rechnung_manager_app.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.repositories.QuittungRepository;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.ui.UiUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class QuittungController implements Initializable, ControllerBse {

    @FXML private TableView<QuittungItemProperty> showItemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<QuittungItemProperty, String> produktColumn;

    @FXML private TableColumn<QuittungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<QuittungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;

    @FXML private Label lblQuittungNummer;

    @FXML private Label lblQuittungDatum;

    @FXML private Label lblLiferdatum;

    @FXML private Button btnSave;

    @FXML private Button btnReset;

    @FXML private Button btnClose;

    @FXML private Button btnPrint;

    @FXML private GridPane bannerPane;

    @FXML private Label lblStatusChange;

    private QuittungBindingService quittungBindingService;

    private final QuittungRepository quittungRepository;

    private final IRechnungDataHelper rechnungDataHelper;

    private Stage stage;

    public QuittungController() {
		
    	this.quittungRepository = RechnungManagerSpringApp.applicationContext.getBean(QuittungRepository.class);
        this.rechnungDataHelper = RechnungManagerSpringApp.applicationContext.getBean(IRechnungDataHelper.class);

	}

	@FXML
    private void speichern() throws IOException {
		showItemsTableView.setEditable(false);
		showItemsTableView.edit(-1, null);
		showItemsTableView.setDisable(true);
		
		showItemsTableView.setItems(null);
		
        if(quittungBindingService.save()){

        }
		
		showItemsTableView.setItems(quittungBindingService.getQuittungItems());
        showItemsTableView.setEditable(true);
        showItemsTableView.setDisable(false);
    }

    @FXML
    private void reload() throws IOException {
        //RechnungApp.setRoot("secondary");
    	
    	if(canResetData()) {
    		doReloadData();
	        
    	}
    }

    private void doReloadData() {
    	showItemsTableView.setItems(null);
    	quittungBindingService.reset();
        showItemsTableView.setItems(quittungBindingService.getQuittungItems());
    }
    
    @FXML
    private void printQuittung() throws IOException {

    	showItemsTableView.setEditable(false);
    	showItemsTableView.edit(-1, null);

        UiUtils.printQuittungItems(Arrays.asList(quittungBindingService.getSavingModel()));


    }

    @FXML
    private void closeQuittung() throws IOException {
        if(quittungBindingService.isView()){
            this.stage.close();
            return;
        }
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    @FXML
    private void toggleBrerechnenZiel(){
        quittungBindingService.toggleActiveBerechnenZiel();
    }

    private boolean canResetData() {
    	if(quittungBindingService.isDirty()) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	    alert.setTitle("Geändrte daten löschen ...");
    	    alert.setHeaderText("Die Daten von Quittung ist geändert. Soll die Anderungen gelöscht werden?");
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

        quittungBindingService = new QuittungBindingService(this.quittungRepository, this.rechnungDataHelper);

        bannerPane.styleProperty().bind(quittungBindingService.bannerBackColorProperty());

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(quittungBindingService.getQuittungItems());
        showItemsTableView.setUserData(quittungBindingService);

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            TablePosition<QuittungItemProperty,?> tPos = event.getTablePosition();
            quittungBindingService.setNewMengeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<QuittungItemProperty,?> tPos = event.getTablePosition();
            quittungBindingService.setNewBrutoPreisValue(tPos.getRow(), value);
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<QuittungItemProperty,?> tPos = event.getTablePosition();
            quittungBindingService.setNewProduktValue(tPos.getRow(), value);
        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<QuittungItemProperty,?> tPos = event.getTablePosition();
            quittungBindingService.setNewArtikelNummerValue(tPos.getRow(), value);

        });

        lblNetto.valueProperty().bind(quittungBindingService.nettoSummeProperty());
        lblMvst.valueProperty().bind(quittungBindingService.mvstSummeProperty());
        lblGesamt.valueProperty().bind(quittungBindingService.gesamtSummeProperty());
        
        lblQuittungNummer.textProperty().bind(quittungBindingService.getQuittungNummerProperty());
        lblQuittungDatum.textProperty().bind(quittungBindingService.getQuittungDatumProperty());
        lblLiferdatum.textProperty().bind(quittungBindingService.getLiferDatumProperty());

        btnSave.disableProperty().bind(quittungBindingService.getDisableSaveProperty());
        btnPrint.disableProperty().bind(quittungBindingService.getDisablePrintProperty());
    }

	@Override
	public boolean isDirty() {
		
		return quittungBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Quittung-Daten ist geändert aber nicht gescpeichert!";
    }

	public void loadModel(QuittungModel quittungModel, boolean editable, Stage stage) {
		showItemsTableView.setEditable(editable);
		quittungBindingService.setQuittungModel(quittungModel);
		
		showItemsTableView.getColumns().forEach(c -> c.setEditable(editable));
        quittungBindingService.setIsView(!editable);
        this.stage = stage;

		if(!editable) {
			HBox hbox = (HBox)lblStatusChange.getParent();
			hbox.getChildren().remove(lblStatusChange);
			hbox.setVisible(false);
			
			btnSave.setVisible(false);
			btnReset.setVisible(false);

		}
	}

}
