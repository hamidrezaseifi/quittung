package de.seifi.rechnung_manager_app.controllers;


import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.fx_services.RechnungBindingPrintService;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class PrintRechnungDialogController implements Initializable {

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableView<RechnungItemPrintProperty> printTableView;

    @FXML private GridPane rootPane;

    @FXML private GridPane topPane;

    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungNummerTitle;

    @FXML private Label lblDatum;

    @FXML private Label lblPageIndex;

    @FXML private Label lblCustomerName;

    @FXML private Label lblStreetNo;

    @FXML private Label lblPlzCity;

    @FXML private Label lblPrintType;

    private Label lblThanks;

    @FXML private VBox commentsBox;

    @FXML private HBox endsummeBox;

    private final RechnungType rechnungType;

    private final RechnungBindingPrintService rechnungBindingService;

    private boolean forCustomer;

    private Printer selectedPrinter = RechnungManagerFxApp.appConfig.getSelectedPrinter(true);

    public PrintRechnungDialogController(RechnungType rechnungType) {
        this.rechnungType = rechnungType;
        rechnungBindingService = new RechnungBindingPrintService(rechnungType);
    }


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

    }

    public void printRechnungList(RechnungModel model, CustomerModel customerModel, boolean forCustomer){
        //Rechnung_print
        this.forCustomer = forCustomer;

        this.rechnungBindingService.setRechnungModel(model, customerModel);

        setFixedItems();

        PageLayout pageLayout = selectedPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 50,50,40,40);
        this.preparePrint(pageLayout);


    }

    private void setFixedItems() {
        if(this.rechnungType == RechnungType.RECHNUNG){
            lblRechnungNummerTitle.setText("Rechnung Nr.");
        }
        if(this.rechnungType == RechnungType.QUITTUNG){
            lblRechnungNummerTitle.setText("Quittung Nr.");
        }

        lblNetto.setText(rechnungBindingService.getNettoSumme());
        lblMvst.setText(rechnungBindingService.getMvstSumme());
        lblGesamt.setText(rechnungBindingService.getGesamtSumme());

        lblRechnungNummer.setText(rechnungBindingService.getRechnungNummer());

        lblDatum.setText(rechnungBindingService.getRechnungDatum());

        lblPrintType.setText(this.rechnungType.getTitle());

        if(this.rechnungType == RechnungType.RECHNUNG){
            lblCustomerName.setText(rechnungBindingService.getCustomerModel().getCustomerName());

            lblStreetNo.setText(rechnungBindingService.getCustomerModel().getStreetHouseNumber());

            lblPlzCity.setText(rechnungBindingService.getCustomerModel().getPlzCity());
        }

        if(this.rechnungType == RechnungType.QUITTUNG){
            lblCustomerName.setVisible(false);

            lblStreetNo.setVisible(false);

            lblPlzCity.setVisible(false);

            GridPane.setRowIndex(lblPrintType, 2);
            while(topPane.getRowConstraints().size() > 4){
                topPane.getRowConstraints().remove(topPane.getRowConstraints().size() - 1);
            }
            rootPane.getRowConstraints().get(0).setPrefHeight(220);
            rootPane.getRowConstraints().get(1).setPrefHeight(380);
        }
    }

    private void preparePrint(PageLayout pageLayout) {
    	boolean isPrinted = false;

        double w = this.rootPane.getPrefWidth();
        double h = this.rootPane.getPrefHeight();

        double pagePrintableWidth = pageLayout.getPrintableWidth();
        double pagePrintableHeight = pageLayout.getPrintableHeight();

        double scaleX = pagePrintableWidth / w;
        double scaleY = pagePrintableHeight / h;

        Scale scale = new Scale(scaleX, scaleY);

        this.rootPane.getTransforms().add(scale);
        this.printAllInctances(pageLayout);

    }

    private boolean printAllInctances(PageLayout pageLayout) {
        boolean isPrinted = false;

        if(forCustomer){
            createThanksLabel();

            if(this.printAllPages(pageLayout)){
                isPrinted = true;
            }

            forCustomer = false;
        }
        if(!forCustomer){

            removeThankLabel();

            if(this.printAllPages(pageLayout)){
                isPrinted = true;
            }

        }

    	return isPrinted;
    	
    }

    private void removeThankLabel() {
        if(lblThanks != null && commentsBox.getChildren().contains(lblThanks)){
            commentsBox.getChildren().remove(lblThanks);
            lblThanks = null;
        }
    }

    private void createThanksLabel() {
        lblThanks = new Label("Danke für Ihren Einkauf");
        lblThanks.getStyleClass().add("tank-customer-label");
        lblThanks.setAlignment(Pos.CENTER_LEFT);
        lblThanks.setPrefWidth(370);

        int indexOfEndsummeBox = commentsBox.getChildren().indexOf(endsummeBox);
        commentsBox.getChildren().add(indexOfEndsummeBox + 1, lblThanks);
    }

    protected boolean printAllPages(PageLayout pageLayout) {
        boolean printResult = true;
        for(int i=0; i< rechnungBindingService.getItemPageCount(); i++) {
            printTableView.setItems(rechnungBindingService.getRechnungPrintPageItems(i));
            String pageName = String.valueOf(i + 1);
            lblPageIndex.setText(pageName);
            PrinterJob job = PrinterJob.createPrinterJob();
            job.setPrinter(RechnungManagerFxApp.appConfig.getSelectedPrinter(true));
            printResult &= job.printPage(pageLayout, rootPane);
            job.endJob();

        }
        return printResult;

    }

}
