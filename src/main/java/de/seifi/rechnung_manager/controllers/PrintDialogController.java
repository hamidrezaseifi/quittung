package de.seifi.rechnung_manager.controllers;


import de.seifi.rechnung_manager.models.RechnungItemPrintProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.seifi.rechnung_manager.fx_services.RechnungBindingPrintService;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.ui.FloatGeldLabel;
import de.seifi.rechnung_manager.models.RechnungItemProperty;

public class PrintDialogController implements Initializable {

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableView<RechnungItemPrintProperty> printTableView;

    @FXML private GridPane rootPane;

    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungDatum;

    @FXML private Label lblLiferdatum;

    private RechnungBindingPrintService rechnungBindingService;

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        rechnungBindingService = new RechnungBindingPrintService();

    }

    public void printRechnungList(List<RechnungModel> RechnungModelList){
        //Rechnung_print
        this.rechnungBindingService.setRechnungModelList(RechnungModelList);
        rechnungBindingService.setPrintingIndex(0);
        this.preparePrint();

    }

    private void preparePrint() {

        if(this.rechnungBindingService.hasPrintingPage()){
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                Printer printer = job.getPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, 50,50,40,40);
               
                //PageLayout pageLayout = job.getPrinter().getDefaultPageLayout();
                job.showPrintDialog(null);
                //job.showPageSetupDialog(null);
                double w = rootPane.getPrefWidth();
                double h = rootPane.getPrefHeight();

                double pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 8.5 inches for this page layout.
                double pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 11 inches for this page layout.

                double scaleX = pagePrintableWidth / w;
                double scaleY = pagePrintableHeight / h;

                Scale scale = new Scale(scaleX, scaleY);

                rootPane.getTransforms().add(scale);
                this.rechnungBindingService.setPrintingIndex(-1);

                while (this.rechnungBindingService.increasePrintingIndex()){
                    this.startPrint(job, pageLayout);
                }


            }
        }

    }

    private void startPrint(PrinterJob job, PageLayout pageLayout) {

        printTableView.setItems(rechnungBindingService.getRechnungPrintItems());
        lblNetto.setText(rechnungBindingService.getNettoSumme());
        lblMvst.setText(rechnungBindingService.getMvstSumme());
        lblGesamt.setText(rechnungBindingService.getGesamtSumme());

        lblRechnungNummer.setText(rechnungBindingService.getRechnungNummer());
        lblRechnungDatum.setText(rechnungBindingService.getLiferDatum());
        lblLiferdatum.setText(rechnungBindingService.getLiferDatum());

        boolean success = job.printPage(pageLayout, rootPane);
        if (success) {
            job.endJob();
        }

    }

}
