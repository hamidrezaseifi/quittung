package de.seifi.rechnung_manager_app.controllers;


import javafx.print.*;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

import java.util.List;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;

public abstract class PrintDialogController<T> {


    protected abstract void setModelList(List<T> modelList);

    protected abstract void setPrintingIndex(int index);

    protected abstract boolean hasPrintingPage();

    protected abstract boolean increasePrintingIndex();

    protected abstract boolean hasMorePrintingPage();

    protected abstract boolean increateIfMorePrintingPage();

    protected abstract GridPane getRootPane();
    
    protected abstract boolean startPrint(PrinterJob job, PageLayout pageLayout);
   
    public void printRechnungList(List<T> modelList){
        //Rechnung_print
        this.setModelList(modelList);
        this.setPrintingIndex(0);
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
    	
        if(this.hasPrintingPage()){
            
            double w = getRootPane().getPrefWidth();
            double h = getRootPane().getPrefHeight();

            double pagePrintableWidth = pageLayout.getPrintableWidth(); 
            double pagePrintableHeight = pageLayout.getPrintableHeight();

            double scaleX = pagePrintableWidth / w;
            double scaleY = pagePrintableHeight / h;

            Scale scale = new Scale(scaleX, scaleY);

            getRootPane().getTransforms().add(scale);
            if(this.startPrintItemPage(job, pageLayout)) {
            	isPrinted = true;
            }

            while (this.increasePrintingIndex()){
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
    	if(this.hasMorePrintingPage()) {
    		if(this.startPrint(job, pageLayout)){
    			isPrinted = true;
    		}
    		
    		while(this.increateIfMorePrintingPage()) {
    			this.startPrint(job, pageLayout);
    		}
    	}
        
    	return isPrinted;
    	
    }

}
