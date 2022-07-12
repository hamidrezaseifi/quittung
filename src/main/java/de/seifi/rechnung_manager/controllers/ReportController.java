package de.seifi.rechnung_manager.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.RechnungManagerSpringApp;
import de.seifi.rechnung_manager.fx_services.ReportBindingService;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.models.ReportItemModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.ui.FloatTextField;
import de.seifi.rechnung_manager.ui.IntegerTextField;
import de.seifi.rechnung_manager.ui.TextObserverDatePicker;
import de.seifi.rechnung_manager.ui.UiUtils;
import de.seifi.rechnung_manager.models.RechnungItemProperty;
import de.seifi.rechnung_manager.models.RechnungModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ReportController implements Initializable, ControllerBse {

    @FXML private TableView<ReportItemModel> reportTableView;

    @FXML private TableColumn<ReportItemModel, String> datumColumn;

    @FXML private TableColumn<ReportItemModel, String> nummerColumn;

    @FXML private TableColumn<ReportItemModel, String> zeitColumn;

    @FXML private TableColumn<ReportItemModel, String> produktListColumn;

    @FXML private TableColumn<ReportItemModel, String> gesamtColumn;
    
    @FXML private TableColumn<ReportItemModel, String> toolsColumn;


    @FXML private GridPane rootPane;

    @FXML private GridPane bannerPane;
    
    @FXML private TextObserverDatePicker dtFrom;
    
    @FXML private TextObserverDatePicker dtTo;


    private ReportBindingService reportBindingService;
    
    private final ProduktRepository produktRepository;
   
    private final RechnungRepository rechnungRepository;
    
    

    public ReportController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(RechnungRepository.class);
    	this.produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);

	}

    @FXML
    private void search() throws IOException {
    	
    	reportTableView.setItems(null);
        reportBindingService.search(dtFrom.getValue(), dtTo.getValue());
        reportTableView.setItems(reportBindingService.getReportItems());
    }

    private void doReloadData() {
    	
    }
    
    @FXML
    private void printRechnung() throws IOException {
    	if(reportBindingService.getReportItems().isEmpty()) {
    		return;
    	}
        List<RechnungModel> modelList = reportBindingService.getReportItems().stream().map(r -> r.getRechnungModel()).collect(Collectors.toList());
        UiUtils.printRechnungItems(modelList);

    }

    @FXML
    private void closeRechnung() throws IOException {
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
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
    	
    	dtTo.setValue(LocalDate.now());
    	
    	dtFrom.setValue(LocalDate.now().minusMonths(2));
    	
        reportBindingService = new ReportBindingService(
        		this.produktRepository,
        		this.rechnungRepository);

        produktListColumn.prefWidthProperty().bind(
                reportTableView.widthProperty().subtract(
                        datumColumn.widthProperty()).subtract(
                        nummerColumn.widthProperty()).subtract(
                                zeitColumn.widthProperty()).subtract(
                                		toolsColumn.widthProperty()).subtract(
                                            gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        reportTableView.setItems(reportBindingService.getReportItems());
        reportTableView.setUserData(reportBindingService);

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
