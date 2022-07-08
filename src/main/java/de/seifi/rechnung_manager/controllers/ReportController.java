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
import de.seifi.rechnung_manager.fx_services.ReportBindingService;
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

public class ReportController implements Initializable, ControllerBse {

    @FXML private TableView<RechnungItemProperty> reportTableView;

    @FXML private TableColumn<RechnungItemProperty, String> produktColumn;

    @FXML private TableColumn<RechnungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<RechnungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> bPreisColumn;

    @FXML private TableColumn<RechnungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;
    
    @FXML private Button btnPrint;

    @FXML private GridPane bannerPane;


    private ReportBindingService reportBindingService;
    
    private final ProduktRepository produktRepository;
   
    private final RechnungRepository rechnungRepository;
    
    

    public ReportController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
    	this.produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);

	}

    @FXML
    private void reload() throws IOException {
     	
    	
    }

    private void doReloadData() {
    	
    }
    
    @FXML
    private void printRechnung() throws IOException {

        Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("rechnung_print");
        GridPane printPane = (GridPane)pair.getKey();
        FXMLLoader fxmlLoader = pair.getValue();
        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
        dialogController.printRechnungList(Arrays.asList(reportBindingService.getSavingModel()));

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
        reportBindingService.toggleActiveBerechnenZiel();
    }

    private boolean canResetData() {
    	if(reportBindingService.isDirty()) {
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

        reportBindingService = new ReportBindingService(
        		this.produktRepository,
        		this.rechnungRepository);

        bannerPane.styleProperty().bind(reportBindingService.bannerBackColorProperty());

        produktColumn.prefWidthProperty().bind(
                reportTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                            mengeColumn.widthProperty()).subtract(
                                    bPreisColumn.widthProperty()).subtract(
                                            nPreisColumn.widthProperty()).subtract(
                                                    gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        reportTableView.setItems(reportBindingService.getRechnungItems());
        reportTableView.setUserData(reportBindingService);

        mengeColumn.setOnEditCommit(event -> {
            final Integer value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            reportBindingService.setNewMengeValue(tPos.getRow(), value);
        });

        bPreisColumn.setOnEditCommit(event -> {
            final Float value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            reportBindingService.setNewBrutoPreisValue(tPos.getRow(), value);
        });

        produktColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            reportBindingService.setNewProduktValue(tPos.getRow(), value);
        });

        artikelNummerColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            TablePosition<RechnungItemProperty,?> tPos = event.getTablePosition();
            reportBindingService.setNewArtikelNummerValue(tPos.getRow(), value);

        });


        btnPrint.disableProperty().bind(reportBindingService.getDisablePrintProperty());
    }

	@Override
	public boolean isDirty() {
		
		return reportBindingService.isDirty();
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }
    

	public List<ProduktModel> getProduktList() {
		return reportBindingService.getProduktList();
	}

}
