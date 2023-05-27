package de.seifi.rechnung_manager_app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.fx_services.RechnungReportBindingService;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungReportItemModel;
import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IRechnungService;
import de.seifi.rechnung_manager_app.ui.IntegerTextField;
import de.seifi.rechnung_manager_app.ui.TextObserverDatePicker;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

public class RechnungReportController implements Initializable, ControllerBase {

    @FXML private TableView<RechnungReportItemModel> reportTableView;

    @FXML private TableColumn<RechnungReportItemModel, String> datumColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> nummerColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> zeitColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> paymentTypeColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> rechnungColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> produktListColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> gesamtColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> anzahlungColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> restZahlungColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> toolsColumn;

    @FXML private Label lblFilter;

    @FXML private HBox toolbarPane;

    @FXML private HBox filterPane;

    @FXML private RowConstraints filterRow;
    
    @FXML private TextObserverDatePicker dtFrom;
    
    @FXML private TextObserverDatePicker dtTo;

    @FXML private IntegerTextField txtNummer;

    @FXML private Label lblCustomer;


    private RechnungReportBindingService bindingService;

    private final IRechnungService rechnungRepository;

    private final ICustomerService customerService;

    private Stage stage;


    public RechnungReportController() {
		
    	this.rechnungRepository = RechnungManagerSpringApp.applicationContext.getBean(IRechnungService.class);
        this.customerService = RechnungManagerSpringApp.applicationContext.getBean(ICustomerService.class);

	}

    @FXML
    private void search() throws IOException {
    	
    	reportTableView.setItems(null);
        bindingService.search();
        reportTableView.setItems(bindingService.getReportItems());
    }

    private void doReloadData() {
    	
    }
    
    @FXML
    private void printRechnung() throws JRException {
    	if(bindingService.getReportItems().isEmpty()) {
    		return;
    	}

        List<RechnungModel> modelList = bindingService.getReportItems().stream().map(r -> r.getRechnungModel()).collect(Collectors.toList());
        PrintUtils.printRechnungItems(modelList, false);

    }

    @FXML
    private void closeRechnung() throws IOException {
        RechnungManagerFxApp.getMainController().showHome();
    }

    public void selectCustomer() throws IOException {
        SelectCustomerDialog dialog = new SelectCustomerDialog(stage,
                                                               this.bindingService.getSelectedCustomerModel());

        Optional<CustomerModel> result = dialog.showAndWait();
        if(result.isPresent()){

            Platform.runLater(()->{
                CustomerModel model = result.get();
                lblCustomer.setText(model.getCustomerName());
                this.bindingService.getSearchFilterProperty().customerProperty().set(model);
            });
        }

    }

    public void removeCustomer() throws IOException {
        lblCustomer.setText("");
        this.bindingService.getSearchFilterProperty().customerProperty().set(null);

    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
    	
    	RechnungManagerFxApp.setCurrentController(this);

        bindingService = new RechnungReportBindingService(
        		this.rechnungRepository,
                this.customerService);


        dtTo.valueProperty().bindBidirectional(bindingService.getSearchFilterProperty().toProperty());

        dtFrom.valueProperty().bindBidirectional(bindingService.getSearchFilterProperty().fromProperty());

        txtNummer.textProperty().bindBidirectional(bindingService.getSearchFilterProperty().nummerProperty());

        lblFilter.textProperty().bind(bindingService.getSearchFilterProperty().labelProperty());
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
                        gesamtColumn.widthProperty()).subtract(
                        anzahlungColumn.widthProperty()).subtract(
                        restZahlungColumn.widthProperty()).subtract(5));

        reportTableView.setItems(bindingService.getReportItems());
        reportTableView.setUserData(bindingService);

    }

	@Override
	public boolean isDirty() {
		
		return false;
	}

    @Override
    public String getDirtyMessage() {
        return "Die Rechnung-Daten ist geändert aber nicht gescpeichert!";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
