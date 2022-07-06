package de.seifi.quittung.controllers;

import de.seifi.quittung.QuittungApp;
import de.seifi.quittung.models.QuittungModel;
import de.seifi.quittung.services.QuittungBindingPrintService;
import de.seifi.quittung.ui.FloatGeldLabel;
import de.seifi.quittung.ui.QuittungItemProperty;
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

public class PrintDialogController implements Initializable {

    @FXML private TableView<QuittungItemProperty> printItemsTableView;

    @FXML private FloatGeldLabel lblNetto;

    @FXML private FloatGeldLabel lblMvst;

    @FXML private FloatGeldLabel lblGesamt;

    @FXML private TableColumn<QuittungItemProperty, String> produktColumn;

    @FXML private TableColumn<QuittungItemProperty, String> artikelNummerColumn;

    @FXML private TableColumn<QuittungItemProperty, Integer> mengeColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> nPreisColumn;

    @FXML private TableColumn<QuittungItemProperty, Float> gesamtColumn;

    @FXML private GridPane rootPane;

    @FXML private Label lblQuittungNummer;

    @FXML private Label lblQuittungDatum;

    @FXML private Label lblLiferdatum;

    private QuittungBindingPrintService quittungBindingService;

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        quittungBindingService = new QuittungBindingPrintService(QuittungApp.BerechnenFaktorBasis, QuittungApp.BerechnenFaktorZiel);


        produktColumn.prefWidthProperty().bind(
                printItemsTableView.widthProperty().subtract(
                        artikelNummerColumn.widthProperty()).subtract(
                        mengeColumn.widthProperty()).subtract(
                        nPreisColumn.widthProperty()).subtract(
                        gesamtColumn.widthProperty()).subtract(5)
                                              );
    }

    public void printQuittungList(List<QuittungModel> quittungModelList){
        //quittung_print
        this.quittungBindingService.setQuittungModelList(quittungModelList);
        quittungBindingService.setPrintingIndex(0);
        this.preparePrint();

    }

    private void preparePrint() {

        if(this.quittungBindingService.hasPrintingPage()){
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
                this.quittungBindingService.setPrintingIndex(-1);

                while (this.quittungBindingService.increasePrintingIndex()){
                    this.startPrint(job, pageLayout);
                }


            }
        }

    }

    private void startPrint(PrinterJob job, PageLayout pageLayout) {

        printItemsTableView.setItems(quittungBindingService.getQuittungItems());

        lblNetto.setText(quittungBindingService.getNettoSumme());
        lblMvst.setText(quittungBindingService.getMvstSumme());
        lblGesamt.setText(quittungBindingService.getGesamtSumme());

        lblQuittungNummer.setText(quittungBindingService.getQuittungNummer());
        lblQuittungDatum.setText(quittungBindingService.getQuittungDatum());
        lblLiferdatum.setText(quittungBindingService.getLiferDatum());

        boolean success = job.printPage(pageLayout, rootPane);
        if (success) {
            job.endJob();
        }

    }

}
