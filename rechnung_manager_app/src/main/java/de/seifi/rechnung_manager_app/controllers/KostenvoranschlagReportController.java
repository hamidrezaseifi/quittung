package de.seifi.rechnung_manager_app.controllers;

import de.seifi.rechnung_common.repositories.CustomerRepository;
import de.seifi.rechnung_common.repositories.ProduktRepository;
import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.fx_services.KostenvoranschlagReportBindingService;
import de.seifi.rechnung_manager_app.models.*;
import de.seifi.rechnung_manager_app.services.ICustomerService;
import de.seifi.rechnung_manager_app.services.IKostenvoranschlagService;
import de.seifi.rechnung_manager_app.ui.IntegerTextField;
import de.seifi.rechnung_manager_app.ui.TextObserverDatePicker;
import de.seifi.rechnung_manager_app.utils.PrintUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class KostenvoranschlagReportController implements Initializable, ControllerBase {

    @FXML
    private TableView<KostenvoranschlagReportItemModel> reportTableView;

    @FXML private TableColumn<RechnungReportItemModel, String> datumColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> nummerColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> customerColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> statusColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> produktListColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> gesamtColumn;

    @FXML private TableColumn<RechnungReportItemModel, String> toolsColumn;

    @FXML private Label lblFilter;

    @FXML private HBox toolbarPane;

    @FXML private HBox filterPane;

    @FXML private RowConstraints filterRow;

    @FXML private TextObserverDatePicker dtFrom;

    @FXML private TextObserverDatePicker dtTo;

    @FXML private IntegerTextField txtNummer;

    @FXML private Label lblCustomer;

    private final IKostenvoranschlagService kostenvoranschlagService;

    private final ICustomerService customerService;

    private Stage stage;

    private KostenvoranschlagReportBindingService bindingService;

    public KostenvoranschlagReportController() {

        this.kostenvoranschlagService = RechnungManagerSpringApp.applicationContext.getBean(IKostenvoranschlagService.class);;
        this.customerService = RechnungManagerSpringApp.applicationContext.getBean(ICustomerService.class);
    }

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {

        RechnungManagerFxApp.setCurrentController(this);

        bindingService = new KostenvoranschlagReportBindingService(kostenvoranschlagService,
                                                                   customerService);
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
                        customerColumn.widthProperty()).subtract(
                        statusColumn.widthProperty()).subtract(
                        toolsColumn.widthProperty()).subtract(
                        gesamtColumn.widthProperty()).subtract(5)
                                                  );

        reportTableView.setItems(bindingService.getReportItems());
        reportTableView.setUserData(bindingService);
    }

    @FXML
    private void search() throws IOException {

        reportTableView.setItems(null);
        bindingService.search();
        reportTableView.setItems(bindingService.getReportItems());
    }

    @FXML
    private void printRechnung() throws JRException {
        if(bindingService.getReportItems().isEmpty()) {
            return;
        }

        List<KostenvoranschlagModel> modelList = bindingService.getReportItems().stream().map(r -> r.getKostenvoranschlagModel()).collect(
                Collectors.toList());
        PrintUtils.printKostenvoranschlagItems(modelList);

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
    public boolean isDirty() {
        return false;
    }

    @Override
    public String getDirtyMessage() {
        return null;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
