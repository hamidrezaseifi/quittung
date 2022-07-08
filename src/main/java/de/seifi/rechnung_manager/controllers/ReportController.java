package de.seifi.rechnung_manager.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.RechnungManagerSpringApp;
import de.seifi.rechnung_manager.fx_services.ReportBindingService;
import de.seifi.rechnung_manager.models.ProduktModel;
import de.seifi.rechnung_manager.models.ReportItemModel;
import de.seifi.rechnung_manager.repositories.ProduktRepository;
import de.seifi.rechnung_manager.repositories.RechnungRepository;
import de.seifi.rechnung_manager.models.RechnungItemProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class ReportController implements Initializable, ControllerBse {

    @FXML private TableView<ReportItemModel> reportTableView;

    @FXML private TableColumn<ReportItemModel, String> datumColumn;

    @FXML private TableColumn<ReportItemModel, String> nummerColumn;

    @FXML private TableColumn<ReportItemModel, String> zeitColumn;

    @FXML private TableColumn<ReportItemModel, String> produktListColumn;

    @FXML private TableColumn<ReportItemModel, String> gesamtColumn;


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
    private void search() throws IOException {
        reportBindingService.search();
    	
    }

    private void doReloadData() {
    	
    }
    
    @FXML
    private void printRechnung() throws IOException {

        Pair<Parent, FXMLLoader> pair = RechnungManagerFxApp.loadFXMLLoader("rechnung_print");
        GridPane printPane = (GridPane)pair.getKey();
        FXMLLoader fxmlLoader = pair.getValue();
        PrintDialogController dialogController = fxmlLoader.<PrintDialogController>getController();
        //dialogController.printRechnungList(Arrays.asList(reportBindingService.getSavingModel()));

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

        reportBindingService = new ReportBindingService(
        		this.produktRepository,
        		this.rechnungRepository);

        produktListColumn.prefWidthProperty().bind(
                reportTableView.widthProperty().subtract(
                        datumColumn.widthProperty()).subtract(
                        nummerColumn.widthProperty()).subtract(
                        zeitColumn.widthProperty()).subtract(
                                            gesamtColumn.widthProperty()).subtract(5)
                                                                       );

        reportTableView.setItems(reportBindingService.getRechnungItems());
        reportTableView.setUserData(reportBindingService);

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
