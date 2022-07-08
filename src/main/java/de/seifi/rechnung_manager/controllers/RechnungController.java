package de.seifi.rechnung_manager.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager.ui.FloatGeldLabel;
import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.RechnungManagerSpringApp;
import de.seifi.rechnung_manager.fx_services.RechnungBindingService;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.ui.RechnungItemProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
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
    
    @FXML private Button btnPrint;

    @FXML private GridPane bannerPane;


    private RechnungBindingService rechnungBindingService;
    
    private final ProduktRepository produktRepository;
    
    private final RechnungRepository rechnungRepository;
    
    

    public RechnungController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
    	this.produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);

	}

	@FXML
    private void speichern() throws IOException {
		rechnungBindingService.doCancelEditing();
		showItemsTableView.setEditable(false);
		TablePosition<RechnungItemProperty,?> tp = showItemsTableView.getEditingCell();
		showItemsTableView.edit(-1, null);
		
        if(rechnungBindingService.save()){

        }
        showItemsTableView.setEditable(true);
        rechnungBindingService.cancelCancelEditing();
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
    	rechnungBindingService.reset();
        showItemsTableView.setItems(rechnungBindingService.getRechnungItems());
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
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    @FXML
    private void toggleBrerechnenZiel(){
        rechnungBindingService.toggleActiveBerechnenZiel();
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

        rechnungBindingService = new RechnungBindingService(
        		this.produktRepository,
        		this.rechnungRepository);

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
    }

	@Override
	public boolean isDirty() {
		
		return rechnungBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }
    

	public List<ProduktModel> getProduktList() {
		return rechnungBindingService.getProduktList();
	}

}
