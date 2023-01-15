package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.fx_services.ReportBindingService;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.ProduktModel;
import de.seifi.rechnung_manager_app.models.ReportItemModel;
import de.seifi.rechnung_manager_app.services.IRechnungService;
import de.seifi.rechnung_manager_app.ui.IntegerTextField;
import de.seifi.rechnung_manager_app.ui.TextObserverDatePicker;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

public class ReportController implements Initializable, ControllerBase {

    @FXML private TableView<ReportItemModel> reportTableView;

    @FXML private TableColumn<ReportItemModel, String> datumColumn;

    @FXML private TableColumn<ReportItemModel, String> nummerColumn;

    @FXML private TableColumn<ReportItemModel, String> zeitColumn;

    @FXML private TableColumn<ReportItemModel, String> paymentTypeColumn;

    @FXML private TableColumn<ReportItemModel, String> rechnungColumn;

    @FXML private TableColumn<ReportItemModel, String> produktListColumn;

    @FXML private TableColumn<ReportItemModel, String> gesamtColumn;
    
    @FXML private TableColumn<ReportItemModel, String> toolsColumn;

    @FXML private GridPane rootPane;

    @FXML private GridPane bannerPane;

    @FXML private Label lblFilter;

    @FXML private HBox toolbarPane;

    @FXML private HBox filterPane;

    @FXML private RowConstraints filterRow;
    
    @FXML private TextObserverDatePicker dtFrom;
    
    @FXML private TextObserverDatePicker dtTo;

    @FXML private IntegerTextField txtNummer;

    @FXML private Label lblCustomer;


    private ReportBindingService reportBindingService;
    
    private final ProduktRepository produktRepository;
   
    private final IRechnungService rechnungRepository;

    private final CustomerRepository customerRepository;

    private Stage stage;


    public ReportController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(IRechnungService.class);
    	this.produktRepository = RechnungManagerSpringApp.applicationContext.getBean(ProduktRepository.class);
        this.customerRepository = RechnungManagerSpringApp.applicationContext.getBean(CustomerRepository.class);

	}

    @FXML
    private void search() throws IOException {
    	
    	reportTableView.setItems(null);
        reportBindingService.search();
        reportTableView.setItems(reportBindingService.getReportItems());
    }

    private void doReloadData() {
    	
    }
    
    @FXML
    private void printRechnung() throws JRException {
    	if(reportBindingService.getReportItems().isEmpty()) {
    		return;
    	}

        List<RechnungModel> modelList = reportBindingService.getReportItems().stream().map(r -> r.getRechnungModel()).collect(Collectors.toList());
        PrintUtils.printRechnungItems(modelList, false);

    }

    @FXML
    private void closeRechnung() throws IOException {
    	if(canResetData()) {
    		doReloadData();
	        RechnungManagerFxApp.getMainController().showHome();
    	}
    	
    }

    public void selectCustomer() throws IOException {
        SelectCustomerDialog dialog = new SelectCustomerDialog(stage,
                                                               this.reportBindingService.getSelectedCustomerModel());

        Optional<CustomerModel> result = dialog.showAndWait();
        if(result.isPresent()){

            Platform.runLater(()->{
                CustomerModel model = result.get();
                lblCustomer.setText(model.getCustomerName());
                this.reportBindingService.getSearchFilterProperty().customerProperty().set(model);
            });
        }

    }

    public void removeCustomer() throws IOException {
        lblCustomer.setText("");
        this.reportBindingService.getSearchFilterProperty().customerProperty().set(null);

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
        		this.rechnungRepository,
                this.customerRepository);


        dtTo.valueProperty().bindBidirectional(reportBindingService.getSearchFilterProperty().toProperty());

        dtFrom.valueProperty().bindBidirectional(reportBindingService.getSearchFilterProperty().fromProperty());

        txtNummer.textProperty().bindBidirectional(reportBindingService.getSearchFilterProperty().nummerProperty());

        lblFilter.textProperty().bind(reportBindingService.getSearchFilterProperty().labelProperty());
        lblFilter.prefWidthProperty().bind(toolbarPane.widthProperty().subtract(370));
        filterRow.setPrefHeight(filterPane.isVisible()? 40: 0);
        lblFilter.setOnMouseClicked(mouseEvent -> {
            filterPane.setVisible(!filterPane.isVisible());
            filterRow.setPrefHeight(filterPane.isVisible()? 40: 0);
        });

        produktListColumn.prefWidthProperty().bind(
                reportTableView.widthProperty().subtract(
                        datumColumn.widthProperty()).subtract(
                            nummerColumn.widthProperty()).subtract(
                                zeitColumn.widthProperty()).subtract(
                                    paymentTypeColumn.widthProperty()).subtract(
                                        rechnungColumn.widthProperty()).subtract(
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
