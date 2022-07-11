package de.seifi.rechnung_manager.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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


    @FXML private GridPane rootPane;

    @FXML private Label lblRechnungNummer;

    @FXML private Label lblRechnungDatum;

    @FXML private Label lblLiferdatum;

    private RechnungBindingPrintService RechnungBindingService;

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        RechnungBindingService = new RechnungBindingPrintService();

    }

    public void printRechnungList(List<RechnungModel> RechnungModelList){
        //Rechnung_print
        this.RechnungBindingService.setRechnungModelList(RechnungModelList);
        RechnungBindingService.setPrintingIndex(0);
        this.preparePrint();

    }

    private void preparePrint() {

        if(this.RechnungBindingService.hasPrintingPage()){
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                Printer printer = job.getPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

                //PageLayout pageLayout = job.getPrinter().getDefaultPageLayout();
                job.showPrintDialog(null);
                //job.showPageSetupDialog(null);
                double w = rootPane.getPrefWidth();
                double h = rootPane.getPrefHeight();

                double nWidth = rootPane.getBoundsInParent().getWidth();
                double nHeight = rootPane.getBoundsInParent().getHeight();

                double pagePrintableWidth = pageLayout.getPrintableWidth(); //this should be 8.5 inches for this page layout.
                double pagePrintableHeight = pageLayout.getPrintableHeight();// this should be 11 inches for this page layout.

                double scaleX = pagePrintableWidth / w;
                double scaleY = pagePrintableHeight / h;

                Scale scale = new Scale(scaleX, scaleY);

                rootPane.getTransforms().add(scale);
                this.RechnungBindingService.setPrintingIndex(-1);

                while (this.RechnungBindingService.increasePrintingIndex()){
                    this.startPrint(job, pageLayout);
                }


            }
        }

    }

    private void startPrint(PrinterJob job, PageLayout pageLayout) {

        
        lblNetto.setText(RechnungBindingService.getNettoSumme());
        lblMvst.setText(RechnungBindingService.getMvstSumme());
        lblGesamt.setText(RechnungBindingService.getGesamtSumme());

        lblRechnungNummer.setText(RechnungBindingService.getRechnungNummer());
        lblRechnungDatum.setText(RechnungBindingService.getRechnungDatum());
        lblLiferdatum.setText(RechnungBindingService.getLiferDatum());

        boolean success = job.printPage(pageLayout, rootPane);
        if (success) {
            job.endJob();
        }

    }

}
