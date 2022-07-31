package de.seifi.rechnung_manager_app.controllers;


import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.fx_services.RechnungBindingPrintService;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.RechnungItemPrintProperty;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.ui.FloatGeldLabel;
import de.seifi.rechnung_manager_app.services.GeneralUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PrintRechnungDialogController implements Initializable {

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableView<RechnungItemPrintProperty> printTableView;

    @FXML private GridPane rootPane;

    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungDatum;

    @FXML private Label lblLiferdatum;

    @FXML private Label lblDatum;

    @FXML private Label lblCustomerName;

    @FXML private Label lblStreetNo;

    @FXML private Label lblPlzCity;

    @FXML private Label lblPrintType;

    private final RechnungType rechnungType;

    private final RechnungBindingPrintService rechnungBindingService;

    public PrintRechnungDialogController(RechnungType rechnungType) {
        this.rechnungType = rechnungType;
        rechnungBindingService = new RechnungBindingPrintService();
    }


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {


    }

    public void printRechnungList(RechnungModel model, CustomerModel customerModel, PrinterJob job){
        //Rechnung_print

        this.rechnungBindingService.setRechnungModel(model, customerModel);

        if (job != null) {
            Printer printer = job.getPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 50,50,40,40);
            job.showPrintDialog(RechnungManagerFxApp.getWindow());
            job.showPageSetupDialog(RechnungManagerFxApp.getWindow());
        	this.preparePrint(job, pageLayout);
        }

    }

    private void preparePrint(PrinterJob job, PageLayout pageLayout) {
    	boolean isPrinted = false;

        double w = this.rootPane.getPrefWidth();
        double h = this.rootPane.getPrefHeight();

        double pagePrintableWidth = pageLayout.getPrintableWidth();
        double pagePrintableHeight = pageLayout.getPrintableHeight();

        double scaleX = pagePrintableWidth / w;
        double scaleY = pagePrintableHeight / h;

        Scale scale = new Scale(scaleX, scaleY);

        this.rootPane.getTransforms().add(scale);
        if(this.startPrintItemPage(job, pageLayout)) {
            isPrinted = true;
        }

        if(this.startPrintItemPage(job, pageLayout)) {
            isPrinted = true;
        }

    	if (isPrinted) {
            job.endJob();
        }
    	
    }

    private boolean startPrintItemPage(PrinterJob job, PageLayout pageLayout) {
    	
    	boolean isPrinted = false;
    	if(this.rechnungBindingService.hasMorePrintingPage()) {
    		if(this.startPrint(job, pageLayout)){
    			isPrinted = true;
    		}
    		
    		while(this.rechnungBindingService.increateIfMorePrintingPage()) {
    			this.startPrint(job, pageLayout);
    		}
    	}
        
    	return isPrinted;
    	
    }

    protected boolean startPrint(PrinterJob job, PageLayout pageLayout) {

        if(rechnungBindingService.hasMorePrintingPage()) {
            printTableView.setItems(rechnungBindingService.getRechnungPrintItems());
            lblNetto.setText(rechnungBindingService.getNettoSumme());
            lblMvst.setText(rechnungBindingService.getMvstSumme());
            lblGesamt.setText(rechnungBindingService.getGesamtSumme());

            lblRechnungNummer.setText(rechnungBindingService.getRechnungNummer());
            lblRechnungDatum.setText(rechnungBindingService.getRechnungDatum());
            lblLiferdatum.setText(rechnungBindingService.getLiferDatum());

            lblDatum.setText(GeneralUtils.formatDate(LocalDate.now()));

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
            }


            job.printPage(pageLayout, rootPane);

            return true;
        }
        return false;

    }

}
