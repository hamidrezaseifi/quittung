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

import de.seifi.rechnung_manager.RechnungManagerFxApp;
import de.seifi.rechnung_manager.fx_services.RechnungBindingPrintService;
import de.seifi.rechnung_manager.models.RechnungModel;
import de.seifi.rechnung_manager.ui.FloatGeldLabel;

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

    public void printRechnungList(List<RechnungModel> rechnungModelList){
        //Rechnung_print
        this.rechnungBindingService.setRechnungModelList(rechnungModelList);
        rechnungBindingService.setPrintingIndex(0);
        PrinterJob job = PrinterJob.createPrinterJob();
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
    	
        if(this.rechnungBindingService.hasPrintingPage()){
            
            double w = rootPane.getPrefWidth();
            double h = rootPane.getPrefHeight();

            double pagePrintableWidth = pageLayout.getPrintableWidth(); 
            double pagePrintableHeight = pageLayout.getPrintableHeight();

            double scaleX = pagePrintableWidth / w;
            double scaleY = pagePrintableHeight / h;

            Scale scale = new Scale(scaleX, scaleY);

            rootPane.getTransforms().add(scale);
            if(this.startPrintItemPage(job, pageLayout)) {
            	isPrinted = true;
            }

            while (this.rechnungBindingService.increasePrintingIndex()){
            	if(this.startPrintItemPage(job, pageLayout)) {
                	isPrinted = true;
                }
            }


        }


    	if (isPrinted) {
            job.endJob();
        }
    	
    }

    private boolean startPrintItemPage(PrinterJob job, PageLayout pageLayout) {
    	
    	boolean isPrinted = false;
    	if(rechnungBindingService.hasMorePrintingPage()) {
    		if(this.startPrint(job, pageLayout)){
    			isPrinted = true;
    		}
    		
    		while(rechnungBindingService.increateIfMorePrintingPage()) {
    			this.startPrint(job, pageLayout);
    		}
    	}
        
    	return isPrinted;
    	
    }

    private boolean startPrint(PrinterJob job, PageLayout pageLayout) {
    	
    	if(rechnungBindingService.hasMorePrintingPage()) {
    		printTableView.setItems(rechnungBindingService.getRechnungPrintItems());
            lblNetto.setText(rechnungBindingService.getNettoSumme());
            lblMvst.setText(rechnungBindingService.getMvstSumme());
            lblGesamt.setText(rechnungBindingService.getGesamtSumme());

            lblRechnungNummer.setText(rechnungBindingService.getRechnungNummer());
            lblRechnungDatum.setText(rechnungBindingService.getLiferDatum());
            lblLiferdatum.setText(rechnungBindingService.getLiferDatum());

            boolean success = job.printPage(pageLayout, rootPane);
            
            return success;
    	}
        return false;

    }

}
