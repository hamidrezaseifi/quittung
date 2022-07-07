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
import de.seifi.rechnung_manager.fx_services.QuittungBindingService;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.ui.QuittungItemProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
    
    @FXML private Button btnPrint;


    private QuittungBindingService quittungBindingService;
    
    private final ProduktRepository produktRepository;
    
    private final RechnungRepository rechnungRepository;
    
    

    public QuittungController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
    	this.produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);

	}

	@FXML
    private void speichern() throws IOException {
        //QuittungApp.setRoot("secondary");
        if(quittungBindingService.save()){

        }
    }

    @FXML
    private void reload() throws IOException {
        //QuittungApp.setRoot("secondary");
    	
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

        Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("quittung_print");
        GridPane printPane = (GridPane)pair.getKey();
        FXMLLoader fxmlLoader = pair.getValue();
        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
        dialogController.printQuittungList(Arrays.asList(quittungBindingService.getSavingModel()));

    }

    @FXML
    private void closeQuittung() throws IOException {
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
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

        quittungBindingService = new QuittungBindingService(
        		RechnungManagerFxApp.BerechnenFaktorBasis, 
        		RechnungManagerFxApp.BerechnenFaktorZiel,
        		this.produktRepository,
        		this.rechnungRepository);

        produktColumn.prefWidthProperty().bind(
                showItemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        showItemsTableView.setItems(quittungBindingService.getQuittungItems());

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            quittungBindingService.setNewMengeValue(event.getTablePosition().getRow(), value);
            
            /*QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getMenge() != value) {
            	prop.setMenge(value);

                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);
            }*/
            
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            quittungBindingService.setNewBrutoPreisValue(event.getTablePosition().getRow(), value);

            /*QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            
            if(prop.getBrutoPreis() != value) {
            	
            	prop.setBrutoPreis(value);
                prop.setPreis(quittungModel.calculateNettoPreis(value));
                
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);
            }*/
            
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            quittungBindingService.setNewProduktValue(event.getTablePosition().getRow(), value);

            /*QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getProdukt() != value) {
                prop.setProdukt(value);
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);

            }*/

        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            quittungBindingService.setNewArtikelNummerValue(event.getTablePosition().getRow(), value);

            /*QuittungItemProperty prop = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if(prop.getArtikelNummer() != value) {
                prop.setArtikelNummer(value);
                quittungModel.calculateQuittungSumme();
                quittungModel.setDirty(true);

            }*/

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
    

	public List<ProduktModel> getProduktList() {
		return quittungBindingService.getProduktList();
	}

}
